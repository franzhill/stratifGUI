package main.chargement_couches.swing_worker;


import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelCharg;
import main.common._excp.ExecutionException;
import main.common.swing_worker.ASwingWorker;
import main.common.tool._excp.TableExtractionException;
import main.common.tool.bat.PlaceHolderReplacer;
import main.common.tool.bat.TemplateProcessor;
import main.common.tool.sql.TableExtractor;
import main.common.tool.swingWorker.LogMessage;
import org.apache.logging.log4j.Level;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;


/**
 * From the thread this worker will be executing in, we'll be generating the bat scripts.
 * We'll be doing it only for the "couche Foncier" scripts, which takes time. Generating the scripts for the other "couches"
 * is quick so it can be done in the Event Dispatch Thread
 *
 * The parametrized <Integer, LogMessage> is for <result of execution ot this worker, type of information that the worker will use to inform (update) the application with its progress>
 */
public class SwingWorkerGenerateScripts extends ASwingWorker<ModelCharg>
{
  protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Folder to generate scripts to.
   */
  private File folder;

  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  /**
   *
   * @param gui
   * @param model
   * @param actionButton
   * @param progressBar pass null if no progressBar to manage
   * @param folder see class
   */
  public SwingWorkerGenerateScripts(Gui gui, ModelCharg model, JButton actionButton, JProgressBar progressBar, File folder)
  {
    super(gui, model, actionButton, progressBar, null);
    this.folder = folder;
  }


  @Override
  protected Integer doInBackground_() throws Exception
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de la génération des scripts..."));
    setProgress(1);

    try
    {
      // More work was done
      publish(new LogMessage(Level.INFO, "Génération des scripts en cours..."));
      setProgress(0);
      generateScriptsFoncier();
    }
    catch (Exception e)
    { e.printStackTrace(); // TODO handle
    }

    // Complete
    publish(new LogMessage(Level.INFO, "Fin de la génération des scripts."));

    return 1;
  }


  /**
   * TODO refactor
   * @throws ExecutionException
   */
  private void generateScriptsFoncier() throws ExecutionException
  {
    // 1. For each file (.sql dump for departement) provided by the user,
    //   1.1 Generate the dump extract (for the relevant table) part and store it in a .sql file (1)
    //   1.2 Generate the reduce table commands, in a .sql file (2)
    //   1.3 Generate the .bat file that plays (1) and (2)

    // Preparation for 1.2 and 1.3 - Involves processing a template
    File template12 = new File ("resources/reduce_table.foncier.ftl.sql") ; // TODO  put in conf file ? / function
    File template13 = new File ("resources/chargement_couche.foncier.ftl.bat") ; // TODO  put in conf file ? / function

    tmplproc.addData("model", model);

    // Do 1.1 through 1.3
    int nb_files_done=0;
    for (FileDep fd : model.depFiles)
    { setProgressFiles(nb_files_done++, model.depFiles.size());
      logger.debug("Processing file : " + fd.toString());

      File sqlFile11  = new File(model.workFolder.getAbsolutePath() + File.separator + String.format(       "dump_table_%s_%s_%s.sql", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file)
      File sqlFile12  = new File(model.workFolder.getAbsolutePath() + File.separator + String.format(     "reduce_table_%s_%s_%s.sql", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file)
      File batFile13  = new File(model.workFolder.getAbsolutePath() + File.separator + String.format("chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file


      gui.loggerGui.info("Traitement du fichier : {} ... ", fd.toString());
      if (fd.departement.isEmpty())
      { throw new ExecutionException(String.format("Département manquant pour le fichier [%s].", fd.file.getAbsolutePath()));
      }

      // 1.1
      String tableName = model.couche.schemaTableSource;
             tableName = new PlaceHolderReplacer(tableName).addDep(fd.departement.toLowerCase()).replace(); // toLowerCase for departements like 2A and 2B, inside the dump it's 2a and 2b
      // Original table name in dump file is something like :  ff_d16_2017.d16_2017_pnb10_parcelle
      // => replace the schema so we have something like ff.d16_2017_pnb10_parcelle
      String tableNameNew = "ff." + tableName.split("\\.")[1];  // TODO brittle. Extract, put in conf file
      // Store new table name in model, will be of use when generating the "reducing" sql command

      fd.schemaTable = tableNameNew;
      fd.table       = tableNameNew.split("\\.")[1];  // (remove the schema part)
      // Where we're going to output the extract, and store it in the model, same as above : will be of use when generating the .bat
      fd.sqlFile1 = sqlFile11;
      fd.sqlFile2 = sqlFile12;

      // Finally do the extract
      TableExtractor tableExtractor = new TableExtractor(fd.file, tableName, tableNameNew);
      try
      { tableExtractor.extract(fd.sqlFile1);
      }
      catch (TableExtractionException e)
      { throw new ExecutionException(String.format("Erreur lors de la génération du sql de restore pour le fichier [%s].", fd.file.getAbsolutePath()), e);
      }

      // 1.2 and 1.3 - Involves processing a template
      tmplproc.addData("fd", fd);
      try
      { tmplproc.process(template12, fd.sqlFile2);
        tmplproc.process(template13, batFile13);
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = [%s], département = [%s].", model.couche.type, fd.departement), e);
      }

      // 2. That should be it then ;o)
    }
  }

}
package main.chargement_couches.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelLoad;
import main.common.controller.AController;
import main.common.tool.MaskPlaceHolderReplacer;
import main.common.tool.TableExtractor;
import main.common._excp.ExecutionException;
import main.common.tool.TemplateProcessor;
import main.common.tool._excp.TableExtractionException;
import org.apache.commons.io.FileUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ControllerGenerateScripts extends AController
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerGenerateScripts(Gui gui, ModelLoad model)
  {   super(gui, model);
  }


  /**
   * Update parts of the model
   *  - where user might have modified input
   *  - that directly impact the present action
   */
  private void updateModel_()
  { try
    {
      updateModelDb();
      model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
      model.setTempFolderPath           (gui.txtTempDir          .getText());
      model.couche.schemaTableSource =   gui.txtSchemaTableSource.getText() ;
    }
    catch (NullPointerException e)
    { gui.showMessageError("Il manque probablement des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
    }
  }

  /**
   * TODO Refactor refactor refactor
   * TODO use SwingWorker - otherwise repainting the GUi happens only at the end of the execution of this
   *      so messages in the GUI log pane appear only at the end ...
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    gui.loggerGui.info("Génération des scripts... Veuillez patienter...");
    gui.loggerGui.info("...");

    updateModel_();

    // Save configs (they might have been edited by user)
    // TODO for the time being we won't be saving the config
    //gui.saveUserConfigDisplay();

    // Perform some checks : have all details been provided?
    if (model.isIncomplete())
    {
      gui.showMessageError("Il manque des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
      return;
    }

    // Do
    try
    { do_();
      gui.loggerGui.info("Tous les scripts ont été générés.");
    }
    catch (ExecutionException e1)
    { gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
      return;
    }
  }


  protected void preDoChecks()
  {

  }


  @Override
  protected void do_() throws ExecutionException
  { emptyWorkDir();
    if (gui.rdoCoucheFoncier.isSelected())
    { generateScriptsFoncier();
    }
    else
    { generateScripts();
    }
  }


  private void emptyWorkDir() throws ExecutionException
  {
    if (gui.chbEmptyWorkDirFirst.isSelected())
    { try
    { FileUtils.cleanDirectory(new File(model.getTempFolderPath()));
    }
    catch (IOException e)
    {  throw new ExecutionException(String.format("Impossible de vider le répertoire des scripts (%s). Un fichier est peut-être verrouillé.", model.getTempFolderPath()), e);
    }
    }
  }

  /**
   * TODO refactor
   * @throws ExecutionException
   */
  private void generateScriptsFoncier()  throws ExecutionException
  {
    // 1. For each file (.sql dump for departement) provided by the user,
    //   1.1 Generate the dump extract (for the relevant table) part and store it in a .sql file (1)
    //   1.2 Generate the reduce table commands, in a .sql file (2)
    //   1.3 Generate the .bat file that plays (1) and (2)

    // Preparation for 1.2 and 1.3 - Involves processing a template
    File template12 = new File (String.format("resources/reduce_table.foncier.ftl.sql"     )) ; // TODO  put in conf file ? / function
    File template13 = new File (String.format("resources/chargement_couche.foncier.ftl.bat")) ; // TODO  put in conf file ? / function

    tmplproc.addData("model", model);

    // Do 1.1 through 1.3
    for (FileDep fd : model.depFiles)
    { logger.debug("Processing file : " + fd.toString());

      File sqlFile11  = new File(model.getTempFolderPath() + File.separator + String.format(       "dump_table_%s_%s_%s.sql", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file)
      File sqlFile12  = new File(model.getTempFolderPath() + File.separator + String.format(     "reduce_table_%s_%s_%s.sql", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file)
      File batFile13  = new File(model.getTempFolderPath() + File.separator + String.format("chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file


      gui.loggerGui.info("Traitement du fichier : {} ... ", fd.toString());
      if (fd.departement.isEmpty())
      { throw new ExecutionException(String.format("Département manquant pour le fichier {%s}.", fd.file.getAbsolutePath()));
      }

      // 1.1
      String tableName = model.couche.schemaTableSource;
             tableName = new MaskPlaceHolderReplacer(tableName).addDep(fd.departement).replace();
      // Original table name is something like :  ff_d16_2017.d16_2017_pnb10_parcelle
      // => replace the schema
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
      { throw new ExecutionException(String.format("Erreur lors de la génération du sql de restore pour le fichier {%s}.", fd.file.getAbsolutePath()), e);
      }

      // 1.2 and 1.3 - Involves processing a template
      tmplproc.addData("fd", fd);
      try
      { tmplproc.process(template12, fd.sqlFile2);
        tmplproc.process(template13, batFile13);
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = {%s}, département = {%s}.", model.couche.type, fd.departement), e);
      }

      // 2. That should be it then ;o)
    }
  }



  /**
   * Generate bat scripts for all files provided by user
   * @throws ExecutionException
   */
  private void generateScripts() throws ExecutionException
  {
    // Use the template processor to generate the bat file that will load the "couche" in DB

    File template = new File (String.format("resources/chargement_couche.%s.ftl.bat", model.couche.type.toLowerCase())) ; // TODO  put in conf file ? / function
    tmplproc.addData("model", model);

    // Process each file
    for (FileDep fd : model.depFiles) // TODo rename FileDep in DepFile or depFiles in fileDeps
    {
      logger.debug("Processing file : " + fd.toString());
      if (fd.departement.isEmpty())
      { throw new ExecutionException(String.format("Département manquant pour le fichier {%s}.", fd.file.getAbsolutePath()));
      }

      // Provide templating engine with remaining data for interpolation
      tmplproc.addData("fd", fd);

      File   outputFile     = new File(String.format(model.getTempFolderPath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement));  // TODO put in conf file

      try
      { tmplproc.process(template, outputFile);
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = {%s}, département = {%s}.", model.couche.type, fd.departement), e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
  }




















  /**
   * TODO refactor
   * @deprecated
   */
  private void generateScriptsOLD() throws ExecutionException
  {
    // Use Freemarker to generate the bat file that will load the "couche" in DB
    Configuration cfg = new Configuration();   // Freemarker configuration object

    //Load template
    Template template;
    String template_path  = String.format("resources/chargement_couche.%s.ftl.bat", model.couche.type.toLowerCase()) ; // TODO  put in conf file ? / function

    try
    { template = cfg.getTemplate(template_path);
    }
    catch (IOException e)
    { e.printStackTrace(); // TODO handle
      throw new ExecutionException(String.format("Impossible de charger le template pour fabriquer le script bat de chargement, {%s}.", template_path));
    }

    // Provide templating engine with data for interpolation
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("model", model);

    // Process each file
    for (FileDep fd : model.depFiles) // TODo rename FileDep in DepFile or depFiles in fileDeps
    {
      logger.debug("Processing file : " + fd.toString());
      if (fd.departement.isEmpty())
      { throw new ExecutionException(String.format("Département manquant pour le fichier {%s}.", fd.file.getAbsolutePath()));
      }

      // Provide templating engine with remaining data for interpolation
      data.put("fd"   , fd);

      // Interpolate and output file
      Writer filewriter;
      String outputFilepath = String.format(model.getTempFolderPath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement); // TODO put in conf file
      File   outputFile     = new File(outputFilepath);
      logger.debug("outputFilepath=" + outputFilepath);
      try
      { filewriter = new FileWriter(outputFile);
      }
      catch (IOException e)
      { throw new ExecutionException(String.format("Erreur d'accès pour écriture au fichier bat {%s}.", outputFilepath), e);
      }
      try
      { template.process(data, filewriter);
        filewriter.flush();
        filewriter.close();
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = {%s}, département = {%s}.", model.couche.type, fd.departement), e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }

  }

}

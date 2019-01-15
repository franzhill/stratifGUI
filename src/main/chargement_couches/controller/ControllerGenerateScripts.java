package main.chargement_couches.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.swing_worker.SwingWorkerGenerateScripts;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.exec.outputHandler.OutputHandlerNull;
import main.common._excp.ExecutionException;
import main.common.tool.bat.TemplateProcessor;
import org.apache.commons.io.FileUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * Geenrate all scripts, as per inputs provided by user through the GUI
 */
public class ControllerGenerateScripts extends AControllerCharg
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerGenerateScripts(Gui gui, ModelCharg model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel__()
  { try
    {
      //updateModelDb(); // Done in parent functions
      model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
      model.setTempFolderPath           (gui.txtTempDir          .getText());
      //model.couche.schemaTableSource =   gui.txtSchemaTableSource.getText() ;
    }
    catch (NullPointerException e)
    { gui.showMessageError("Il manque probablement des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
    }
  }

  /**
   * TODO Refactor refactor refactor
   * TODO use SwingWorker - otherwise repainting the GUi happens only at the end of the execution of this
   *      so messages in the GUI log pane appear only at the end ...
   */
  @Override
  public void doo()
  {
    gui.loggerGui.info("Génération des scripts... Veuillez patienter...");
    gui.loggerGui.info("...");

    // Save configs (they might have been edited by user)
    // TODO for the time being we won't be saving the config
    //gui.saveUserConfigDisplay();

    try
    { emptyWorkDir();
      if (gui.rdoCoucheFoncier.isSelected())
      { generateScriptsFoncier();
      }
      else
      { generateScripts();
      }
      gui.loggerGui.info("Tous les scripts ont été générés.");
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
    }
  }


  protected boolean preDoChecks()
  {
    // Perform some checks : have all details been provided?
    if (model.isIncomplete())
    {
      gui.showMessageError("Il manque des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
      return false;
    }
    /*else if (model.depFiles.isEmpty())
    {
      gui.showMessageError("Aucun fichier sélectionné !");
      return false;
    }*/
    else return true;
  }



  private void emptyWorkDir() throws ExecutionException
  {
    if (gui.chbEmptyWorkDirFirst.isSelected())
    { try
      { FileUtils.cleanDirectory(new File(model.getTempFolderPath()));
      }
      catch (Exception e)
      {  throw new ExecutionException(String.format("Impossible de vider le répertoire des scripts (%s). Existe-t-il bien ? Un fichier y est peut-être verrouillé ?", model.getTempFolderPath()), e);
      }
    }
  }


  /**
   * This operation might be long so we'll be doing it in a swing worker
   */
  private void generateScriptsFoncier()
  {
    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    //IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    IOutputHandler ouh = new OutputHandlerNull();  // silent
    SwingWorkerGenerateScripts gssw = new SwingWorkerGenerateScripts(gui, model, new File(model.getTempFolderPath()));
    gssw.addPropertyChangeListener(gssw);
    gssw.execute();

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

package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.swing_worker.SwingWorkerGenerateScripts;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.exec.outputHandler.OutputHandlerNull;
import main.common._excp.ExecutionException;
import main.common.tool.bat.TemplateProcessor;
import org.apache.commons.io.FileUtils;

import java.io.File;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
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
  {  //updateModelDb(); // Done in parent functions
      model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
      model.setTempFolderPath           (gui.txtTempDir          .getText());
  }


  /**
   */
  @Override
  protected void doo_()
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
        gui.loggerGui.info("Génération des scripts teminée.");
      }
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
    }
  }


  @Override
  protected void preDoChecks() throws Exception
  {
    // Create workdir if not exists
    model.workFolder.mkdir();

    // Perform some checks : have all details been provided?
    if (model.isIncomplete())
    {
      throw new ExecutionException("Il manque des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
    }
    /*else if (model.depFiles.isEmpty())
    {
      gui.showMessageError("Aucun fichier sélectionné !");
      return false;
    }*/
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
    SwingWorkerGenerateScripts gssw = new SwingWorkerGenerateScripts(gui, model, gui.buttGenerateScripts, gui.progbCouche, model.workFolder);
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
      { throw new ExecutionException(String.format("Département manquant pour le fichier [%s].", fd.file.getAbsolutePath()));
      }

      // Provide templating engine with remaining data for interpolation
      tmplproc.addData("fd", fd);

      File   outputFile     = new File(String.format(model.workFolder.getAbsolutePath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement));  // TODO put in conf file

      try
      { tmplproc.process(template, outputFile);
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = [%s], département = [%s].", model.couche.type, fd.departement), e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
  }


  @Override
  protected boolean shouldEmptyWorkDirectory()
  { return  gui.chbEmptyWorkDirFirst.isSelected();
  }



}

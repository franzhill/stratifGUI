package main.java.b.backup.controller;

import main.java.Gui;
import main.java.a.chargement_couches.controller.AControllerCharg;
import main.java.a.chargement_couches.model.FileDep;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.a.chargement_couches.model.ModelCouche;
import main.java.a.chargement_couches.swing_worker.SwingWorkerGenerateScripts;
import main.java.b.backup.model.ModelBckp;
import main.java.common._excp.ExecutionException;
import main.java.common.controller.AController;
import main.java.common.tool.bat.PlaceHolderReplacer;
import main.java.common.tool.bat.TemplateProcessor;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.common.tool.exec.outputHandler.OutputHandlerGui;

import java.io.File;
import java.util.Arrays;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
 */
public class ControllerGenerateScripts extends AController<ModelBckp>
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerGenerateScripts(Gui gui, ModelBckp model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel_()
  { // TODO WBN put all this in AController.updateModelConfig() and have it called by  AController.updateModel()
    model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
    model.setTempFolderPath           (gui.txtTempDir          .getText());
    model.setNbThreads                (gui.txtNbThreads        .getText());

    model.name      =               gui.txtBckpName       .getText();
    model.parentDir = new File(     gui.txtBckpParentDir  .getText());
    model.schemas   = Arrays.asList(gui.txtaStratDepSelect.getText().split("\\s*[,\\s]{1}\\s*"));  // split on , or space - and trim leading and trailing white spaces
  }


  /**
   */
  @Override
  protected void doo_()
  {
    gui.loggerGui.info("Génération des scripts... Veuillez patienter...");
    gui.loggerGui.info("...");

    try
    { emptyWorkDir();
      File template   = new File ("resources/backup_db.ftl.bat") ; // TODO WBN extract constant in conf file ? / function
      File outputFile = new File(model.workFolder.getAbsolutePath() + File.separator + "sauvegarde_db.bat");  // TODO put in conf file
      tmplproc.addData("model", model);
      try
      { tmplproc.process(template, outputFile);
      }
      catch (Exception e)
      { throw new ExecutionException("Erreur lors de la fabrication du script bat sauvegarde à partir du template.", e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors de la génération du script de sauvegarde. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
    }
  }


  @Override
  protected void preDoChecks() throws Exception
  {
    // Create workdir if not exists
    model.workFolder.mkdir();

    // Perform some checks : have all details been provided?
    if (model.isIncomplete())
    { throw new ExecutionException("Il manque des paramètres. Vérifier que tous les éléments nécessaires ont été indiqués.");
    }
  }


  @Override
  protected boolean shouldEmptyWorkDirectory()
  { return  gui.chbBckpEmptyWorkDirFirst.isSelected();
  }





// TODO






}

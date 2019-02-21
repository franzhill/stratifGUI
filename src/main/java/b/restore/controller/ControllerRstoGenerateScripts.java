package main.java.b.restore.controller;

import main.java.Gui;
import main.java.b.backup.model.ModelBckp;
import main.java.b.restore.model.ModelRsto;
import main.java.common._excp.ExecutionException;
import main.java.common.controller.AController;
import main.java.common.tool.bat.TemplateProcessor;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
 */
public class ControllerRstoGenerateScripts extends AController<ModelRsto>
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerRstoGenerateScripts(Gui gui, ModelRsto model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel_()
  { // TODO WBN put all this in AController.updateModelConfig() and have it called by  AController.updateModel()
    model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
    model.setTempFolderPath           (gui.txtTempDir          .getText());
    model.setNbThreads                (gui.txtNbThreads        .getText());
    model.bckpFolder   = new File     (gui.txtRstoBckpFolder   .getText());
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
      { throw new ExecutionException("Erreur lors de la fabrication du script bat de restauration à partir du template.", e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors de la génération du script de restauration. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
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


}

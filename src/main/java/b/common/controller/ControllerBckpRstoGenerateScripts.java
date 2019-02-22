package main.java.b.common.controller;

import main.java.Gui;
import main.java.b.backup.model.ModelBckp;
import main.java.b.common.model.ModelBckpRsto;
import main.java.common._excp.ExecutionException;
import main.java.common.controller.AController;
import main.java.common.tool.bat.TemplateProcessor;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
 */
public abstract class ControllerBckpRstoGenerateScripts<M extends ModelBckpRsto> extends AController<M>
{
  /**
   * Used to process (interpolate) the script templates
   */
  protected TemplateProcessor tmplproc = new TemplateProcessor();

  /**
   * Freemarker template used to generate the script(s)
   */
  protected File template;

  /**
   * Name to give to the generated script(s)
   */
  protected String outputFileName;


  public ControllerBckpRstoGenerateScripts(Gui gui, M model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel_()
  { // TODO WBN put all this in AController.updateModelConfig() and have it called by  AController.updateModel()
    model.setPostgresqlBinPath     (gui.txtPostgresqlBinDir .getText());
    model.setTempFolderPath        (gui.txtTempDir          .getText());
    model.setNbThreads             (gui.txtNbThreads        .getText());

    updateModel__();
  }


  abstract protected void updateModel__();


  /**
   */
  @Override
  protected void doo_()
  {
    gui.loggerGui.info("Génération des scripts... Veuillez patienter...");
    gui.loggerGui.info("...");

    try
    { emptyWorkDir();

      File outputFile = new File(model.workFolder.getAbsolutePath() + File.separator + outputFileName);
      tmplproc.addData("model", model);
      try
      { tmplproc.process(template, outputFile);
      }
      catch (Exception e)
      { throw new ExecutionException("Erreur lors de la fabrication du script .bat à partir du template.", e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors de la génération du script .bat. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
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

package main.java.b.backup.controller;

import main.java.Gui;
import main.java.a.chargement_couches.controller.AControllerCharg;
import main.java.a.chargement_couches.model.FileDep;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.a.chargement_couches.model.ModelCouche;
import main.java.a.chargement_couches.swing_worker.SwingWorkerGenerateScripts;
import main.java.b.backup.model.ModelBckp;
import main.java.b.common.controller.ControllerBckpRstoGenerateScripts;
import main.java.common._excp.ExecutionException;
import main.java.common.controller.AController;
import main.java.common.tool.bat.PlaceHolderReplacer;
import main.java.common.tool.bat.TemplateProcessor;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.common.tool.exec.outputHandler.OutputHandlerGui;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
 */
public class ControllerBckpGenerateScripts extends ControllerBckpRstoGenerateScripts<ModelBckp>
{

  public ControllerBckpGenerateScripts(Gui gui, ModelBckp model)
  { super(gui, model);
    template = new File ("resources/backup_db.ftl.bat") ; // TODO WBN put in conf file
    outputFileName =  "backup_db.bat";                          // TODO WBN put in conf file
  }


  @Override
  protected void updateModel__()
  {
    model.name      =               gui.txtBckpName         .getText();
    model.parentDir = new File     (gui.txtBckpParentDir    .getText());

    model.schemas   = gui.txtBckpListSchemas.getText().trim().isEmpty()
                          ? Collections.emptyList()   // !! split on an empty string returns a non empty list
                          : Arrays.asList(gui.txtBckpListSchemas.getText().split("\\s*[,\\s]{1}\\s*"));  // split on , or space - and trim leading and trailing white spaces

    logger.debug("model.schemas =" + Arrays.toString(model.schemas.toArray()));
    logger.debug("model.schemas.size =" + model.schemas.size());
  }


}

package main.java.b.restore.controller;

import main.java.Gui;
import main.java.b.backup.model.ModelBckp;
import main.java.b.common.controller.ControllerBckpRstoGenerateScripts;
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
public class ControllerRstoGenerateScripts extends ControllerBckpRstoGenerateScripts<ModelRsto>
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerRstoGenerateScripts(Gui gui, ModelRsto model)
  { super(gui, model);
    template = new File ("resources/restore_db.ftl.bat") ; // TODO WBN put in conf file
    outputFileName =  "restore_db.bat";                          // TODO WBN put in conf file
  }


  @Override
  protected void updateModel__()
  {
    model.bckpFolder   = new File     (gui.txtRstoBckpFolder   .getText());
  }


}

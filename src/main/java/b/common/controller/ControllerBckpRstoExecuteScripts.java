package main.java.b.common.controller;

import main.java.Gui;
import main.java.b.backup.model.ModelBckp;
import main.java.b.backup.swing_worker.SwingWorkerBckpExecuteScripts;
import main.java.common.controller.AController;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.common.tool.exec.outputHandler.OutputHandlerGui;


/**
 * Execute all scripts that have been pre-generated and placed in the script folder
 */
public class ControllerBckpRstoExecuteScripts extends AController<ModelBckp>
{
  /**
   * public for convenience
   */
  public SwingWorkerBckpExecuteScripts esw;


  public ControllerBckpRstoExecuteScripts(Gui gui, ModelBckp model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel_()
  {
    model.setTempFolderPath   (gui.txtTempDir.getText());
  }


  @Override
  protected void preDoChecks()
  {
    // Create workdir if not exists
    model.workFolder.mkdir();
  }


  @Override
  protected void doo_()
  {
    gui.loggerGui.info("Exécution du script... Veuillez patienter...");
    gui.loggerGui.info("...");

    // We'll be using a Swing worker to avoid blocking the thread in which actionPerformed() is executed.
    // This thread is the one in which the refreshing of the GUI happens. So blocking it is not too good...

    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    //IOutputHandler ouh = new OutputHandlerNull();  // silent

    esw = new SwingWorkerBckpExecuteScripts(gui, model, gui.buttExecuteScripts, gui.progbCouche, /*gui.buttCancelExecuteScripts*/ null, model.workFolder, ouh);

    esw.execute();

    // gui.loggerGui.info("Fin execution script.");  // No point in indicating completion here, this will be hit before action even
                                                     // starts, asynchronously, in the swingworker.
  }


}

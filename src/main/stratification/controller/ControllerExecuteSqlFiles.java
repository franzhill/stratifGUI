package main.stratification.controller;

import main.Gui;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.exec.outputHandler.OutputHandlerGui;
import main.stratification.model.ModelStrat;
import main.stratification.swing_worker.SwingWorkerExecuteSqlFiles;


/**
 * Execute all sql scripts
 */
public class ControllerExecuteSqlFiles extends AControllerStrat
{
  public ControllerExecuteSqlFiles(Gui gui, ModelStrat model)
  {   super(gui, model);
  }


  @Override
  public void doo()
  {
/*     for (File f : model.sqlFiles)
    {
      gui.loggerGui.debug("Traitement du fichier : {}", f.getAbsolutePath());
    }
*/
    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    //IOutputHandler ouh = new OutputHandlerNull();  // silent

    SwingWorkerExecuteSqlFiles esw = new SwingWorkerExecuteSqlFiles(gui, model, gui.buttStratExecute, null, ouh, model.getNbThreads());
    esw.execute();
  }


  @Override
  protected void updateModel__()
  {
    model.setTempFolderPath   (gui.txtTempDir  .getText());
    model.setNbThreads        (gui.txtNbThreads.getText());
  }


}

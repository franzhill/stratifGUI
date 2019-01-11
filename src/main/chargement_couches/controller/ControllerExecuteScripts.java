package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.chargement_couches.swingWorker.SwingWorkerExecuteScripts;
import main.chargement_couches.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.common.controller.AController;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.exec.outputHandler.OutputHandlerGui;
import main.common._excp.DirException;
import main.common.tool.exec.outputHandler.OutputHandlerNull;

import java.awt.event.ActionEvent;
import java.io.File;


/**
 * Execute all scripts that have been pre-generated and placed in the script folder
 */
public class ControllerExecuteScripts extends AController
{
  public ControllerExecuteScripts(Gui gui, ModelLoad model)
  {   super(gui, model);
  }

  /**
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {

    model.setTempFolderPath   (gui.txtTempDir.getText());
    model.setNbThreads        (gui.txtNbThreads.getText());

    //executeBats();  // Old method - New one below leverages a SwingWorker

    // We'll be using a Swing worker to avoid blocking the thread in which actionPerformed() is executed.
    // This thread is the one in which the refreshing of the GUI happens. So blocking it is not too good...

    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    //IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    IOutputHandler ouh = new OutputHandlerNull();  // silent

    SwingWorkerExecuteScripts esw = new SwingWorkerExecuteScripts(gui, model, new File(model.getTempFolderPath()), ouh, model.getNbThreads());

    esw.execute();

    // gui.loggerGui.info("Tous les scripts ont été exécutés.");  // Do not indicate termination here, cause tasks are run asynchronously (threads)
                                                                  // so we might very likely go through here before the tasks are finished
  }


  /**
   * @deprecated
   */
  private void executeBats()
  {
    //BatFolderExecutor bfe = new BatFolderExecutor(model.getTempFolderPath(), new OutputHandlerGui(this.gui));
    // Experimental :
    int nbThreads = Integer.parseInt(this.gui.userConfig.getProp("couches.max_db_conn", "1"));
    MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(new File(model.getTempFolderPath()),  Gui.loggerGui, new OutputHandlerGui(this.gui), nbThreads);
    try
    { bfe.execute();  // TODO try executing in thread
    }
    catch (DirException e)
    { e.printStackTrace(); // TODO handle
    }
  }

}

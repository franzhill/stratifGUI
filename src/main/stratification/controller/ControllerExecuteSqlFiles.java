package main.stratification.controller;

import main.Gui;
import main.chargement_couches.controller.AControllerCharg;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.swingWorker.SwingWorkerExecuteScripts;
import main.chargement_couches.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.common._excp.DirException;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.exec.outputHandler.OutputHandlerGui;
import main.common.tool.exec.outputHandler.OutputHandlerNull;
import main.stratification.model.ModelStrat;
import main.stratification.swingWorker.SwingWorkerExecuteSqlFiles;

import java.awt.event.ActionEvent;
import java.io.File;


/**
 * Execute all sql scripts
 */
public class ControllerExecuteSqlFiles extends AControllerStrat
{
  public ControllerExecuteSqlFiles(Gui gui, ModelStrat model)
  {   super(gui, model);
  }

  /**
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    updateModel(); // TODO move in parent

    for (File f : model.sqlFiles)
    {
      gui.loggerGui.debug("Traitement du fichier : {}", f.getAbsolutePath());
    }

    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    //IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    IOutputHandler ouh = new OutputHandlerNull();  // silent

    //SwingWorkerExecuteSqlFiles esw = new SwingWorkerExecuteSqlFiles(gui, model, new File(model.getTempFolderPath()), ouh, model.getNbThreads());

    //esw.execute();

  }

  @Override
  protected void updateModel_()
  {
    model.depPlaceholder     = gui.txtStratDepPlacheholder.getText();
    model.setTempFolderPath   (gui.txtTempDir.getText());
    model.setNbThreads        (gui.txtNbThreads.getText());
  }


}

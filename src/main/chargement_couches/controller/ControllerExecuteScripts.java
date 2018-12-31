package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.chargement_couches.tool.MultiThreadedBatFolderExecutor;
import main.chargement_couches.worker.ExecuteScriptsWorker;
import main.common.controller.AController;
import main.common.tool.outputHandler.OutputHandlerGui;
import main.common._excp.DirException;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

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

      //executeBats();

      ExecuteScriptsWorker esw = new ExecuteScriptsWorker(new File(model.getTempFolderPath()), Gui.loggerGui, new OutputHandlerGui(gui));
      esw.execute();


      // TODO since commands are launched in subprocesses there is no callback to this main thread when
      // they finish so we don't know when to tell the user everything has been completed
      // => find a way
       gui.loggerGui.info("Tous les scripts ont été exécutés.");
    }



    private void executeBats()
    {
      //BatFolderExecutor bfe = new BatFolderExecutor(model.getTempFolderPath(), new OutputHandlerGui(this.gui));
      // Experimental :
      int nbThreads = Integer.parseInt(this.gui.userConfig.getProp("couches.max_db_conn", "1"));
      MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(new File(model.getTempFolderPath()), new OutputHandlerGui(this.gui), nbThreads);
      try
      { bfe.execute();  // TODO try executing in thread
      }
      catch (DirException e)
      { e.printStackTrace(); // TODO handle
      }
    }

}

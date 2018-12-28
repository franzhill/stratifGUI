package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.common.BatFolderExecutor;
import main.common.controller.AController;
import main.common.tool.streamGobblerOutputhandler.StreamGobblerOutputhandlerGui;
import main.common._excp.DirException;

import java.awt.event.ActionEvent;

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

      executeBats();

      // TODO since commands are launched in subprocesses there is not callback to this main thread when
      // they finish so we don't know when to tell the user everything has been completed
      // => find a way
      // gui.loggerGui.info("Tous les scripts ont été exécutés.");
    }



    private void executeBats()
    {
      BatFolderExecutor bfe = new BatFolderExecutor(model.getTempFolderPath(), new StreamGobblerOutputhandlerGui(this.gui));
      try {
        bfe.execute();  // TODO try executing in thread
      } catch (DirException e) {
        e.printStackTrace(); // TODO handle
      }
    }

}

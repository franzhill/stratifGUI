package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.common.BatFolderExecutor;
import main.common.controller.AController;
import main.common.tool.streamGobblerOutputhandler.StreamGobblerOutputhandlerGui;
import main.ex.DirException;

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
    }



    private void executeBats()
    {
      BatFolderExecutor bfe = new BatFolderExecutor(model.getTempFolderPath(), new StreamGobblerOutputhandlerGui(this.gui));
      try {
        bfe.execute();
      } catch (DirException e) {
        e.printStackTrace(); // TODO handle
      }
    }

}

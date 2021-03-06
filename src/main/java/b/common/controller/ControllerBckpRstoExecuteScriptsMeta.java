package main.java.b.common.controller;

import main.java.Gui;
import main.java.b.backup.controller.ControllerBckpExecuteScripts;
import main.java.b.backup.controller.ControllerBckpGenerateScripts;
import main.java.b.backup.model.ModelBckp;
import main.java.b.restore.controller.ControllerRstoExecuteScripts;
import main.java.b.restore.controller.ControllerRstoGenerateScripts;
import main.java.b.restore.model.ModelRsto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Lets us dynamically "assign" a controller to the execute button on the GUI
 * by acting as a proxy and delegating to the real target controller
 * depending on an input selection made by the user in the GUI.
 */
public class ControllerBckpRstoExecuteScriptsMeta implements ActionListener
{
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected final Gui gui;
  protected ControllerBckpExecuteScripts controllerBckp;
  protected ControllerRstoExecuteScripts controllerRsto;


  public ControllerBckpRstoExecuteScriptsMeta(Gui gui, ModelBckp modelBckp, ModelRsto modelRsto)
  {
    this.gui = gui;
    this.controllerBckp = new ControllerBckpExecuteScripts(gui, modelBckp);
    this.controllerRsto = new ControllerRstoExecuteScripts(gui, modelRsto);
  }


  @Override
  public final void actionPerformed(ActionEvent e)
  {
    // If backp selected, delegate to that controller
    if (gui.rdoBckp.isSelected())
    { controllerBckp.actionPerformed(e);
    }

    // If restore selected, delegate to that controller
    if (gui.rdoRsto.isSelected())
    { controllerRsto.actionPerformed(e);
    }
  }
}


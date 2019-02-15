package main.java.common.controller;

import main.java.Gui;
import main.java.chargement_couches.model.ModelCharg;
import main.java.common.tool.exec.SysCommand;
import main.java.common.tool.exec.outputHandler.OutputHandlerGui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerTest extends AController
{
    /**
     * Effing boilerplate code ...
     */
    public ControllerTest(Gui gui)
    {   super(gui, new ModelCharg());  // just a dummy Model, won't be used anyway
    }

    @Override
    protected void doo_()
    {
        gui.loggerGui.info("logging this following press of button");

        // Test
        SysCommand cmd = new SysCommand("dir", new ArrayList<String>(Arrays.asList(".")),  new OutputHandlerGui(this.gui));
        cmd.execute();
    }


    @Override
    protected void updateModel_()
    {   // Nothing
    }
}

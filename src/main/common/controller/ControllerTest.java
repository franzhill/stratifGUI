package main.common.controller;

import main.Gui;
import main.common.tool.exec.SysCommand;
import main.common.tool.exec.outputHandler.OutputHandlerGui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerTest extends AController
{
    /**
     * Effing boilerplate code ...
     */
    public ControllerTest(Gui gui)
    {   super(gui);
    }

    @Override
    public void actionPerformed(ActionEvent e)
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

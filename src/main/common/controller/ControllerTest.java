package main.common.controller;

import main.Gui;
import main.common.SysCommand;
import main.common.controller.AController;

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
        SysCommand cmd = new SysCommand("dir", new ArrayList<String>(Arrays.asList(".")));
        cmd.execute();
    }
}

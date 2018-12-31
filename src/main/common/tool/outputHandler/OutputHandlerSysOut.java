package main.common.tool.outputHandler;

import main.Gui;

/**
 * @author fhill
 */
public class OutputHandlerSysOut implements IOutputHandler
{
    private Gui gui;

    public OutputHandlerSysOut(Gui gui)
    {   this.gui = gui;
    }

    @Override
    public void handleOutput(String message, String type)
    {
        System.out.println(type + ">" + message);
    }
}

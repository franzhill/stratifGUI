package main.common.tool.outputHandler;

import main.Gui;

/**
 * @author fhill
 */
public class OutputHandlerSysOut implements IOutputHandler
{

    @Override
    public void handleOutput(String message, String type)
    {
        System.out.println(type + ">" + message);
    }
}

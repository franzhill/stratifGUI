package main.common.tool.exec.outputHandler;

import main.Gui;

/**
 * Describe how to handle output such as to have it processed by a GUI
 * @author fhill
 */
public class OutputHandlerGui implements IOutputHandler
{
    private Gui gui;

    public OutputHandlerGui(Gui gui)
    {   this.gui = gui;
    }

    @Override
    public void handleOutput(String message, String type)
    {
        //gui.txtaLog.append(type + ">" + message);
        gui.loggerGui2.info(type + "> " + message);
        // Also output on Sys out (for debug)
        System.out.println(type + "> " + message);

        // TODO : also output in log (or handle that in the config of loggerGui
    }
}

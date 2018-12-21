package main.common.tool.streamGobblerOutputhandler;

import main.Gui;
import main.common.tool.streamGobblerOutputhandler.IStreamGobblerOutputhandler;

public class StreamGobblerOutputhandlerGui implements IStreamGobblerOutputhandler
{
    private Gui gui;

    public StreamGobblerOutputhandlerGui(Gui gui)
    {   this.gui = gui;
    }

    @Override
    public void handleOutput(String message, String type)
    {
        //gui.txtaLog.append(type + ">" + message);
        gui.loggerGui.info(type + "> " + message);
        // Also output on Sys out (for debug)
        System.out.println(type + "> " + message);

        // TODO : also output in log (or handle that in the config of loggerGui
    }
}

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
      System.out.println("Calling  gui.loggerGui.info(type " + ">" + message + ")");
        gui.loggerGui.info(type + ">" + message);    // TODO the messages are only flushed on what seems to be the end of the thread in which the system call is made (??)
        // Also output on Sys out (for debug)
        System.out.println(type + ">" + message);

        // TODO : also output in log (or handle that in the config of loggerGui
    }
}

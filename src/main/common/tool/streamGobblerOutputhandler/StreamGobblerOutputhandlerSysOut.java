package main.common.tool.streamGobblerOutputhandler;

import main.Gui;
import main.common.tool.streamGobblerOutputhandler.IStreamGobblerOutputhandler;

public class StreamGobblerOutputhandlerSysOut implements IStreamGobblerOutputhandler
{
    private Gui gui;

    public StreamGobblerOutputhandlerSysOut(Gui gui)
    {   this.gui = gui;
    }

    @Override
    public void handleOutput(String message, String type)
    {
        System.out.println(type + ">" + message);
    }
}

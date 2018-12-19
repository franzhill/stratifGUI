package main.gui;


import main.Gui;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Outpustream which is bound to a GUI
 * Will redirect (write) its content to a (to be specified) function of the GUI
 * (that will e.g. display it in a text area)
 * @author fhill
 */
public class GuiLogOutputStream extends OutputStream
{
    private Gui gui;

    public GuiLogOutputStream(Gui gui)
    {   this.gui = gui;
    }

    @Override
    public void write(int b) throws IOException
    {
        // redirect data to the GUI
        gui.logInGui(String.valueOf((char)b));
    }
}

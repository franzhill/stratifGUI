package main.gui;


import main.Gui;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Outpustream which is bound to a GUI
 * Will redirect (write) its content to a (to be specified) function of the GUI
 * (that will e.g. display it in a text area)
 * @author fhill
 */
public class GuiLogOutputStream extends OutputStream
{
    private Gui gui;

    /** The internal memory for the written bytes. */
    private String mem;

    public GuiLogOutputStream(Gui gui)
    {   this.gui = gui;
    }

/*
    @Override
    public void write(int b) throws IOException
    {
        // redirect data to the GUI
        gui.logInGui(String.valueOf((char)b));
    }
*/

    @Override
    public void write (int b)
    {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (b & 0xff);

        // TODO to be able to convert to UTF8 we need to accumulate the bytes (b) into an array until we hit a "\n"
        //      only then can we convert to string
        mem = mem + new String(bytes, Charset.forName("UTF-8"));

        //flush every so often
        // e.g. when detecting an \n
        if (mem.endsWith ("\n"))
        {
            // But don't remove it
            //mem = mem.substring (0, mem.length () - 1);
            flush ();
        }
    }

    /**
     * Flushes the output stream.
     */
    public void flush ()
    {
        // redirect data to the GUI
        gui.logInGui(mem);//, true);
        mem = "";
    }
}

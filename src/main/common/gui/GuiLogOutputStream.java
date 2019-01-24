package main.common.gui;


import main.Gui;
import org.apache.commons.lang3.ArrayUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Outpustream which is bound to a Gui
 *
 * Will redirect (write) its content to a (to be specified) function of the GUI
 * (that will e.g. display it in a text area)
 *
 * TODO remove direct dependency to Gui, and use an interface
 *
 * @author fhill
 */
public class GuiLogOutputStream extends OutputStream
{
  private Gui gui;

  /**
   * Name of logger which will be using this outputsream
   */
  private String loggerName;

  /** The internal memory for the written bytes. */
  private String mem;

  /** The internal memory for the written bytes. */
  List<Byte> lBytes = new ArrayList<>();


  public GuiLogOutputStream(Gui gui, String loggerName)
  { this.gui = gui;
    this.loggerName = loggerName;
  }

/*
    @Override
    public void write(int b) throws IOException
    {
        // redirect data to the GUI
        gui.logInGui(String.valueOf((char)b));
    }
*/

/*
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
*/



  // TODO could probably be optimized working with a byte[] instead of a List<Byte>
  public void write (int b)
  {
    lBytes.add((byte) (b & 0xff));  // Auto-boxing

    if (b == 0xA) // End of line   //if (new String(bytees).equals("\n"))
    { // Flush
      // i.e. redirect data to the GUI

      // All the following to convert List<Byte> to byte[] ...  so we can easily build a String to support encoding
      Byte[] aBytes = new Byte[lBytes.size()];
      lBytes.toArray(aBytes);
      byte[] abytes =  ArrayUtils.toPrimitive(aBytes);

      // Now we can log a String to GUI
      gui.logInGui(  new String( abytes ), loggerName );   //, true);
      lBytes.clear();
    }
  }






//  /**
//   * Flushes the output stream.
//   */
//  public void flush ()
//  {
//    // redirect data to the GUI
//    gui.logInGui(mem);//, true);
//    mem = "";
//  }
}

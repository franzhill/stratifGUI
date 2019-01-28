package main.common.tool.exec.outputHandler;

import main.Gui;
import main.common.tool.exec.StreamGobbler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Describe how to handle output such as to have it processed by a GUI
 * @author fhill
 */
public class OutputHandlerGui implements IOutputHandler
{
  private Gui gui;

  /**
   * The previous message logged
   * Key = origin
   * Value = message
   */
  private HashMap<String, String> prevMsg = new HashMap<String, String>();

  /**
   * Have we started showing an "idem" message (instead of lots of identical lines)
   */
  private boolean idemStarted = false;

  public OutputHandlerGui(Gui gui)
  {   this.gui = gui;
  }


  @Override
  public void handleOutput(String message, String type, @Nullable String origin)
  {
    origin   = (origin != null) ? origin : "";
    message  = origin + " : " + type + "> " + message;

    switch (type)
    { case StreamGobbler.TYPE_OUT :  if (! message.equals(prevMsg.get(origin)))
                                     {  gui.loggerGui2.info (message);
                                        idemStarted = false;
                                     }
                                     else
                                     { if (! idemStarted)
                                       {  gui.loggerGui2.info (origin + " : " + "...idem...");
                                          idemStarted = true;
                                       }
                                     }
                                     prevMsg.put(origin, message);
                                     break;
      case StreamGobbler.TYPE_ERR :  gui.loggerGui2.error(message);  break;
      default                     :  throw new RuntimeException("Programmatic error, should never happen.");
    }

    // Also output on Sys out (for debug)
    // NO - slows things down a lot
    //if (! message.equals(prevMsg)) {System.out.println(type + "> " + message);}

  }
}

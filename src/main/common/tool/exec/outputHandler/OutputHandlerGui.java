package main.common.tool.exec.outputHandler;

import main.Gui;
import main.common.tool.exec.StreamGobbler;

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
    switch (type)
    { case StreamGobbler.TYPE_OUT :  gui.loggerGui2.info (type + "> " + message); break;
      case StreamGobbler.TYPE_ERR :  gui.loggerGui2.error(type + "> " + message); break;
      default                     :  throw new RuntimeException("Programmatic error, should not happen.");
    }

    // Also output on Sys out (for debug)
    System.out.println(type + "> " + message);

    // TODO : also output in log (or handle that in the config of loggerGui
  }
}

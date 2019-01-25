package main.common.tool.exec.outputHandler;

import main.common.tool.exec.StreamGobbler;
import org.jetbrains.annotations.Nullable;

/**
 * @author fhill
 */
public class OutputHandlerSysOut implements IOutputHandler
{

  @Override
  public void handleOutput(String message, String type, @Nullable String origin)
  {
    origin = (origin != null) ? origin : "";
    System.out.println(origin + " : " + type + "> " + message);
  }
}
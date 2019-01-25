package main.common.tool.exec.outputHandler;

import org.jetbrains.annotations.Nullable;

/**
 * Describe how to handle the output (messages) from threads, syscommands etc.
 * @author fhill
 */
public interface IOutputHandler
{
  /**
   * Describe how to handle the output (messages) from threads, syscommands etc.
   * @param message
   * @param type    a qualification of the message (ex: ERROR, INFO etc.)
   * @param origin  some information on the origin or the output, for example could be a syscommand from which the
   *                message we want to handle originates.
   *
   */
  void handleOutput(String message, String type, @Nullable String origin);


  //void handleOutput(String message);

}

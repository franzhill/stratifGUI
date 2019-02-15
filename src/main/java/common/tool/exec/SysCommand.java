package main.java.common.tool.exec;

import main.java.common.tool.exec.outputHandler.IOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author fhill
 */
public class SysCommand
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String       command;

  private List<String> args   = new ArrayList<String>();

  private Process proc;


  /**
   * Will handle the output from the system command calls (e.g. write messages in the GUI)
   */
  private IOutputHandler outputHandler;

  /**
   * @param command will be executed by cmd.exe
   * @param args list of arguments to pass to the command
   * @param outputHandler will handle the output from the system command calls (e.g. write messages in the GUI)
   */
  public SysCommand(String command, List<String> args, IOutputHandler outputHandler)
  { this(command, outputHandler);
    this.args    = args;
  }


  /**
   * For command with no params
   * See other constructor
   * @param command
   * @param outputHandler
   */
  public SysCommand(String command, IOutputHandler outputHandler)
  {
    this.command       = command;
    this.outputHandler = outputHandler;
  }

  /**
   *
   * @return the exit value of the command
   */
  public int execute()
  { logger.debug("");
    String homeDirectory = System.getProperty("user.home");
    int exitVal = 0;
    try
    { // Execute sys command in its own process
      proc = Runtime.getRuntime().exec("cmd.exe /C " + command);
      // /C = carries out the command specified by string and then terminates

      // Catch error messages and handle them, with a gobbler
      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), StreamGobbler.TYPE_ERR, outputHandler, this);
      // Catch regular output messages and handle, with a gobbler
      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream() /* sic, not getOutputStream() */, StreamGobbler.TYPE_OUT, outputHandler, this);
      // Launch the gobblers concurrently
      //# Executors.newSingleThreadExecutor().submit(errorGobbler);
      //# Executors.newSingleThreadExecutor().submit(outputGobbler);
      errorGobbler.start();
      outputGobbler.start();
      // Sys command exit status
      try
      { //# //Test destroy proc ...
        //# wait(10000); // milliseconds
        //# logger.debug("Destroying proc ...");
        //# proc.destroy(); */
        exitVal = proc.waitFor();
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt(); // see https://stackoverflow.com/questions/1087475/when-does-javas-thread-sleep-throw-interruptedexception
      }

    }
    catch (IOException e)
    {   e.printStackTrace();
      exitVal=-1;
    }
    logger.debug("System call command exited with value = " + exitVal);
    return exitVal;
  }


  public void cancel()
  {
    proc.destroy();
    // May not work as we'd want (i.e. kill the cmd.exe launched)
    // See https://stackoverflow.com/questions/19726804/how-to-kill-a-process-in-java-process-destroy
  }


  /*
      @Override
      public String toString()
      {   return "cmd.exe /C " + command;  // TODO clean up
      }
  */
  @Override
  public String toString()
  {   return command + " " + args.toString();
  }


  public String toStringShort()
  {   return new File(command).getName();
  }


}

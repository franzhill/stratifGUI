package main.common.tool.exec;

import main.common.tool.exec.outputHandler.IOutputHandler;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Used to catch output of executed system commands (see class SysCommand)
 *
 * @courtesy https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
 */
public class StreamGobbler extends Thread
{
  private InputStream    is;
  private String         type;
  private IOutputHandler outputHandler;
  private SysCommand     command;

  //@MagicConstant(stringValues = {TYPE_OUT, TYPE_ERR})  // annotation that works with IntelliJ -- can't seem to make it work properrly though...
  // We could use an enum -- but we'll keep it simple, POC style
  public static final String TYPE_OUT = "STDO";
  public static final String TYPE_ERR = "STDE";

  /**
   * @param is
   * @param type           one of StreamGobbler.TYPE_OUT or StreamGobbler.TYPE_ERR
   * @param outputHandler  will handle i.e. define what to actually do with the data from the inputstream
   * @param command        the command for which this gobbler is used.
   */
  public StreamGobbler(InputStream is, String type, IOutputHandler outputHandler, SysCommand command)
  {
    this.is   = is;
    this.type = type;
    this.outputHandler = outputHandler;
    this.command = command;
  }


  public void run()
  {
    try
    { InputStreamReader isr = new InputStreamReader(is);
      BufferedReader    br  = new BufferedReader(isr);
      String line = null;
      while ( (line = br.readLine()) != null)
      { outputHandler.handleOutput(line, type, command.toStringShort());
      }
    }
    catch (IOException ioe)
    {  ioe.printStackTrace();
    }
  }


}

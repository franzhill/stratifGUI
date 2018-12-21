package main.common.tool;

import main.common.tool.streamGobbler.StreamGobbler;
import main.common.tool.streamGobblerOutputhandler.IStreamGobblerOutputhandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SysCommand
{
    private Logger logger = LoggerFactory.getLogger(SysCommand.class);

    protected String       command;
    protected List<String> args   = new ArrayList<String>();

    /**
     * Will handle the output from the system command calls (e.g. write messages in the GUI)
     */
    private IStreamGobblerOutputhandler outputHandler;

    /**
     * @param command will be executed by cmd.exe
     * @param args list of arguments to pass to the command
     * @param outputHandler will handle the output from the system command calls (e.g. write messages in the GUI)
     */
    public SysCommand(String command, List<String> args, IStreamGobblerOutputhandler outputHandler)
    {   this(command, outputHandler);
        this.args    = args;
    }


    /**
     * For command with no params
     * See other constructor
     * @param command
     * @param outputHandler
     */
    public SysCommand(String command, IStreamGobblerOutputhandler outputHandler)
    {
        this.command       = command;
        this.outputHandler = outputHandler;
    }

    /**
     *
     * @return the exit value of the command
     */
    public int execute()
    {
        String homeDirectory = System.getProperty("user.home");
        Process proc;
        int exitVal = 0;
        try
        {   // Execute sys command in its own process
            proc = Runtime.getRuntime().exec("cmd.exe /C " + command);
            // /C      Carries out the command specified by string and then terminates

            // Catch error messages and handle them in the gobbler
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), StreamGobbler.TYPE_ERR, outputHandler);
            // Catch regular output messages and handle them in the gobbler
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream() /* sic, not getOutputStream() */, StreamGobbler.TYPE_OUT, outputHandler);
            // Launch the gobblers
            Executors.newSingleThreadExecutor().submit(errorGobbler);
            Executors.newSingleThreadExecutor().submit(outputGobbler);
            //errorGobbler.start();
            //outputGobbler.start();
            // Sys command exit status
            /*
            try
            {   exitVal = proc.waitFor();
            }
            catch (InterruptedException e)
            {   e.printStackTrace();
                exitVal=-1;
            }*/

        }
        catch (IOException e)
        {   e.printStackTrace();
            exitVal=-1;
        }
        logger.debug("System call command exited with value = " + exitVal);
        return exitVal;
    }
}

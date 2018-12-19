package main.common;

import main.common.StreamGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysCommand
{
    private Logger logger = LoggerFactory.getLogger(SysCommand.class);

    protected String       command;
    protected List<String> args   = new ArrayList<String>();

    /**
     *
     * @param command will be executed by cmd.exe
     * @param args list of arguments to pass to the command
     */
    public SysCommand(String command, List<String> args)
    {
        this.command = command;
        this.args    = args;
    }


    public SysCommand(String command)
    {
        this.command = command;
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
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), StreamGobbler.TYPE_ERR);
            // Catch regular output messages and handle them in the gobbler
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream() /* sic, not getOutputStream() */, StreamGobbler.TYPE_OUT);
            // Launch the gobblers
            errorGobbler.start();
            outputGobbler.start();
            // Sys command exit status
            try
            {   exitVal = proc.waitFor();

            }
            catch (InterruptedException e)
            {   e.printStackTrace();
                exitVal=-1;
            }

        }
        catch (IOException e)
        {   e.printStackTrace();
            exitVal=-1;
        }
        logger.debug("System call command exited with value = " + exitVal);
        return exitVal;
    }
}

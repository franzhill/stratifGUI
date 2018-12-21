package main.common.tool.streamGobbler;

import main.common.tool.streamGobblerOutputhandler.IStreamGobblerOutputhandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Used to catch output of executed system commannds (see class SysCommand)
 *
 * @courtesy https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
 */
public class StreamGobbler extends Thread
{
    private InputStream is;
    private String type;

    //@MagicConstant(stringValues = {TYPE_OUT, TYPE_ERR})  // annotation that works with IntelliJ
    public static final String TYPE_OUT = "STDO";
    public static final String TYPE_ERR = "STDE";

    protected IStreamGobblerOutputhandler outputHandler;

    /**
     *
     * @param is
     * @param type one of StreamGobbler.TYPE_OUT or StreamGobbler.TYPE_ERR
     */
    public StreamGobbler(InputStream is, String type, IStreamGobblerOutputhandler outputHandler)
    {
        this.is   = is;
        this.type = type;
        this.outputHandler = outputHandler;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader    br  = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null)
            {
                outputHandler.handleOutput(line, type);
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }


}

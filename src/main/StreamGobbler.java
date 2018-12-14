package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Used to catch output of executed system commannds (see class SysCommand)
 *
 * @courtesy https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
 */
class StreamGobbler extends Thread
{
    InputStream is;
    String type;

    //@MagicConstant(stringValues = {TYPE_OUT, TYPE_ERR})  // annotation that works with IntelliJ
    public static final String TYPE_OUT = "OUT";
    public static final String TYPE_ERR = "ERR";


    /**
     *
     * @param is
     * @param type one of StreamGobbler.TYPE_OUT or StreamGobbler.TYPE_ERR
     */
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null)
            {
                System.out.println(type + ">" + line);
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}

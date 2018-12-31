package main.common.tool.outputHandler;

/**
 * Describe how to handle the output (messages) from threads, syscommands etc.
 * @author fhill
 */
public interface IOutputHandler
{
    /**
     * Describe how to handle the output (messages) from threads, syscommands etc.
     * @param message
     * @param type
     */
    void handleOutput(String message, String type);


    //void handleOutput(String message);

}

package main.common.tool.exec.outputHandler;

/**
 * @author fhill
 */
public class OutputHandlerNull implements IOutputHandler
{

    @Override
    public void handleOutput(String message, String type)
    {
        // Do nothing
    }
}

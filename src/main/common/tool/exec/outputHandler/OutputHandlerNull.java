package main.common.tool.exec.outputHandler;

import org.jetbrains.annotations.Nullable;

/**
 * @author fhill
 */
public class OutputHandlerNull implements IOutputHandler
{

    @Override
    public void handleOutput(String message, String type, @Nullable String origin)
    {
        // Do nothing
    }
}

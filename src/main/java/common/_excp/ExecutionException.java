package main.java.common._excp;

/**
 * There was a problem while executing a script or something
 */
public class ExecutionException extends Exception
{
    public ExecutionException(String message, Throwable cause)
    { super(message, cause);
    }

    public ExecutionException(String message)
    { super(message);
    }

}

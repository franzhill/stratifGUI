package main.common._excp;

public class ExecutionException extends Exception
{
    public ExecutionException(String message, Throwable cause)
    { super(message, cause);
    }

    public ExecutionException(String message)
    { super(message);
    }

}

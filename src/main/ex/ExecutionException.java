package main.ex;

public class ExecutionException extends Exception
{
    public ExecutionException(String message, Throwable cause)
    { super(message, cause);
    }

    public ExecutionException(String message)
    { super(message);
    }

}

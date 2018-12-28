package main.ex;

public class DepExtractionException extends Exception
{
    public DepExtractionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DepExtractionException(String message)
    {
        super(message);
    }

}

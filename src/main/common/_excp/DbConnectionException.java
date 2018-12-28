package main.ex;

public class DbConnectionException extends Exception
{
    public DbConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

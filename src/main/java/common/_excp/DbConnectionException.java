package main.java.common._excp;

public class DbConnectionException extends Exception
{
    public DbConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

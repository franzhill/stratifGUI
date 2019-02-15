package main.java.common._excp;

/**
 * There was a problem accessing the configuration
 */
public class ConfigAccessException extends Exception
{
  public ConfigAccessException(String message, Throwable cause)
  {
    super(message, cause);
  }
}

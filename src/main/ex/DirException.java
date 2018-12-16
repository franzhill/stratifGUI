package main.ex;

/**
 * There was a problem with a provided directory path
 * - dir was not found
 * - it's a file, not a directory...
 * - I/O error ...
 */
public class DirException extends Exception
{
  public DirException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public DirException(String message)
  {
    super(message);
  }

}


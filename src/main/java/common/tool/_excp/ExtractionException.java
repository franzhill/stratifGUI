package main.java.common.tool._excp;

public class ExtractionException extends Exception
{
  public ExtractionException(String message, Throwable cause)
  { super(message, cause);
  }

  public ExtractionException(String message)
  { super(message);
  }

}

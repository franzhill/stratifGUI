package main.common._excp;

/**
 * Top level exception containing a message to be displayed to user of the GUI
 */
public class UserLevelException extends Exception
{
    public UserLevelException(String message, Throwable cause)
    { super(message, cause);
    }

    public UserLevelException(String message)
    { super(message);
    }

}

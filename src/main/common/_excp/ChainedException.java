package main.common._excp;

import main.utils.MyExceptionUtils;

public class ChainedException extends Exception
{
  public String getStrackTrace()
  {
    return MyExceptionUtils.getStackMessages(this);
  }
}

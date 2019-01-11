package main.z_deprecated;

import main.utils.MyExceptionUtils;

public class ChainedException extends Exception
{
  public String getStrackTrace()
  {
    return MyExceptionUtils.getStackMessages(this);
  }
}

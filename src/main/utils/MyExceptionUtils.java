package main.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fhill
 */
public class MyExceptionUtils
{
  /**
   * Return messages of all chained cause exceptions of t, in a stack fashion
   * @param t
   * @return
   */
  public static String getStackMessages(Throwable t)
  {
    List<String> stack = new ArrayList<String>();
    while (t != null)
    {   stack.add(t.getClass().getName() + " : " + t.getMessage());
      t = t.getCause();
    }

    return StringUtils.join(stack, "\n");
  }
}

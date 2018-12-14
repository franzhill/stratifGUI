package main.common;

/**
 * @author fhill
 */
public class StringUtils
{
  /**
   * Can't believe I actually have to write a function for this...
   */
  public static boolean isNullOrEmpty(String str)
  {
    return (str == null || str.isEmpty());
  }
}

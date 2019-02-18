package main.java.utils;

import main.java.common._excp.DepExtractionException;

/**
 * @author fhill
 */
public class MyStringUtils
{
  /**
   * Can't believe I actually have to write a function for this...
   */
  public static boolean isNullOrEmpty(String str)
  {
    return (str == null || str.isEmpty());
  }


  /**
   *
   * @param dep département on 2 ou 3 chars (e.g. 31, 031, 975...)
   * @return the département on 3 chars (a padding with 0 is added if nnecessary)
   */
  public static String normalizeDep3Chars(String dep) throws DepExtractionException
  {
    boolean error = false;
    if ( dep.length() != 2 && dep.length() != 3)
    { error = true;
    }
    //# Don't do this, some départements have letters in them
    //#try
    //#{ Integer.parseInt(dep);
    //#}
    //#catch (NumberFormatException e)
    //#{ error = true;
    //#}
    if (error) { throw new DepExtractionException(String.format("Le département fourni [%s] n'est pas au format attendu (2 ou 3 caractères).", dep)); }

    return ( dep.length() == 2) ? "0" + dep : dep;

  }




}

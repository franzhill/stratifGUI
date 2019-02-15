package main.java.common.tool.bat;


import main.java.common.tool.sql.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces occurrences of placeholders like
 *   %DEP%
 *   %DEPxx%
 *   %ANNEE%
 *   ...
 * with their real values in a given string containing these placeholders
 * @author fhill
 */
public class PlaceHolderReplacer
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * The string with placeholders to be replaced
   */
  private String target;

  /**
   * Departement. On 3 characters
   */
  private String dep = "INCONNU";

  /**
   * Year. On 4 characters
   */
  private String year = "INCONNU";


  public PlaceHolderReplacer(String target)
  { this.target = target;
  }

  /**
   * Add the real value of a departement.
   * @param dep
   * @return
   */
  public PlaceHolderReplacer addDep(String dep)
  { this.dep = dep;
    return this;
  }


  /**
   * Add the real value of a year.
   * @param year
   * @return
   */
  public PlaceHolderReplacer addYear(String year)
  { this.year = year;
    return this;
  }


  public String replace()
  {
    return target.replace("%DEP%", dep).replace("%DEPxx%", dep.substring(1)).replace("%ANNEE%", year);
  }

}

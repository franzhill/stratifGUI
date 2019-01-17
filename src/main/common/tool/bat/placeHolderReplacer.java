package main.common.tool.bat;


import main.common.tool.sql.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces occurrences of placeholders like
 *   %DEP%
 *   %DEPxx%
 *   %ANNEE%
 *   ...
 * with their real values in a given mask (a string containing these placeholders)
 * @author fhill
 */
public class MaskPlaceHolderReplacer
{
  private static Logger logger = LoggerFactory.getLogger(Extractor.class);

  /**
   * The mask with placeholders to be replaced
   */
  private String mask;

  /**
   * Departement. On 3 characters
   */
  private String dep = "INCONNU";

  /**
   * Year. On 4 characters
   */
  private String year = "INCONNU";


  public MaskPlaceHolderReplacer(String mask)
  { this.mask = mask;
  }

  /**
   * Add the real value of a departement.
   * @param dep
   * @return
   */
  public MaskPlaceHolderReplacer addDep(String dep)
  { this.dep = dep;
    return this;
  }


  /**
   * Add the real value of a year.
   * @param year
   * @return
   */
  public MaskPlaceHolderReplacer addYear(String year)
  { this.year = year;
    return this;
  }


  public String replace()
  {
    return mask.replace("%DEP%", dep).replace("%DEPxx%", dep.substring(1)).replace("%ANNEE%", year);
  }

}

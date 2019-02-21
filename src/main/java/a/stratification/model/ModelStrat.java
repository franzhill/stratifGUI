package main.java.a.stratification.model;

import main.java.common.model.AModel;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fhill
 */
public class ModelStrat extends AModel
{
  public List<File> sqlFiles = new LinkedList<File>();

  /**
   * List of all possible départements.
   * Will be read from a user conf file
   */
  public List<String> allDeps = new ArrayList<>();

/* This is now provided by user conf file
  public static final List<String> allDeps = new ArrayList<>(Arrays.asList
    (       "001", "002", "003", "004", "005", "006", "007", "008", "009",
     "010", "011", "012", "013", "014", "015", "016", "017", "018", "019",
     "02A", "02B",
            "021", "022", "023", "024", "025", "026", "027", "028", "029",
     "030", "031", "032", "033", "034", "035", "036", "037", "038", "039",
     "040", "041", "042", "043", "044", "045", "046", "047", "048", "049",
     "050", "051", "052", "053", "054", "055", "056", "057", "058", "059",
     "060", "061", "062", "063", "064", "065", "066", "067", "068", "069",
     "070", "071", "072", "073", "074", "075", "076", "077", "078", "079",
     "080", "081", "082", "083", "084", "085", "086", "087", "088", "089",
     "090", "091", "092", "093", "094", "095",
     "971", "972", "973", "974", "975", "976"
    ));
*/

  /**
   * Process all départements or only a selection?
   * If only a selection, that selection is held in selDeps.
   */
  public boolean processAllDeps = true;

  /**
   * List of selected departements
   * Ex :
   * 032, 033, 975, 2B ...
   */
  public List<String> selDeps = new ArrayList<>();

  /**
   * The placeholder (pattern) in sql files to be replaced
   * by the actual département.
   * Usually something like #DEPT3  or $DEPTxx  ...
   */
  public String depPlaceholder;


  protected boolean isIncomplete_()
  {
    return  false; // TODO
  }

  @Override
  public void finalize()
  {
    workFolder = new File(getTempFolderPath() + File.separator + "STRATIF");
    if (processAllDeps)
    { selDeps = allDeps;
    }
  }

}

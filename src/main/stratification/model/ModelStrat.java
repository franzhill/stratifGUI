package main.stratification.model;

import lombok.Getter;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelCouche;
import main.common.model.AModel;
import main.common.model.ModelDb;
import main.utils.MyStringUtils;

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


  /**
   * Finalise : do all transformations and stuff before final use (i.e. injection in template)
   */

}

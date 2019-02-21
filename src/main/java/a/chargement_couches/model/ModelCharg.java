package main.java.chargement_couches.model;

import lombok.Getter;
import main.java.common.model.AModel;
import main.java.common.tool.bat.PlaceHolderReplacer;
import main.java.utils.MyStringUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fhill
 */
public class ModelCharg extends AModel
{
  @Getter public ModelCouche couche;

  /**
   * Parent folders in which to look for files to be loaded
   * Public cause heck we're not sending a rocket in space so I'm not going to be using all that getters and setters crap
   */
  public List<File> parents = new LinkedList<File>();

  /**
   * Files to be loaded in the DB
   * Store all depFiles matching all criteria - candidates for loading in DB
   * // TODo rename FileDep in DepFile or depFiles in fileDeps
   */
  public List<FileDep> depFiles = new LinkedList<FileDep>();


  /**
   * Model is incomplete if final action (here, generate of execute scripts) cannot take place
   * @return
   */
  public boolean isIncomplete_()
  {
    return  depFiles.isEmpty() ||
            couche == null     ||
            MyStringUtils.isNullOrEmpty(postgresqlBinPath) ||
            MyStringUtils.isNullOrEmpty(tempFolderPath);
  }


  @Override
  public void finalize()
  {
    workFolder = new File(tempFolderPath + File.separator + "CHARG_COUCHE");

  }


}

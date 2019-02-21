package main.java.b.backup.model;

import lombok.Getter;
import main.java.common.model.AModel;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fhill
 */
public class ModelBckp extends AModel
{
  /**
   * Name of backup folder
   */
  @Getter public String name;

  /**
   * Dir where to place the backup folder
   */
  @Getter public File parentDir;

  /**
   * List of schemas to backup
   */
  @Getter public List<String> schemas = new ArrayList<String>();


  @Override
  public void finalize()
  {
    workFolder = new File(tempFolderPath + File.separator + "SAUVG");  // TODO WBN extract constant
  }


  @Override
  protected boolean isIncomplete_()
  {
    return name.isEmpty() || parentDir == null;
  }
}
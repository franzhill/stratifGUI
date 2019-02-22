package main.java.b.restore.model;

import lombok.Getter;
import main.java.b.common.model.ModelBckpRsto;
import main.java.common.model.AModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fhill
 */
public class ModelRsto extends ModelBckpRsto
{
  /**
   * Backup folder (parent_dir/backup_name)
   */
  @Getter public File bckpFolder;

  /**
   * List of schemas to backup
   */
  @Getter public List<String> schemas = new ArrayList<String>();


  @Override
  public void finalize()
  {
    workFolder = new File(tempFolderPath + File.separator + "RESTO");  // TODO WBN extract constant
  }


  @Override
  protected boolean isIncomplete_()
  {
    return bckpFolder == null;
  }
}
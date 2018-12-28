package main.chargement_couches.model;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Association of a file and a departement
 */
public class FileDep
{
  // The getters are for use by Freemarker

  @Getter public String departement;   // TODO rename departement to dep ?
  public File   file;

  /**
   * In some cases (e.g. couche Foncier) we may want to associate a DB table name to the departement
   * Here in the format schema.table
   */
  @Getter public String schemaTable;

  /**
   * Same as schemaTable, but without the schema. part, i.e. just the table name
   */
  @Getter public String table;

  /**
   * In some cases (e.g. couche Foncier) we may need to specify an intermediary sql file
   * for use in the processing of the current FileDep
   */
  @Getter public File sqlFile1;
  @Getter public File sqlFile2;



  /**
   * @used_by Freemarker
   * (unfortunately Freemarker doesn't seem to be able to access properties directly, only through getters)
   * @return
   */
  /* This has been "Lomboked" ;o)
  public String getDepartement()
  { return departement;
  }*/

  /**
   * @used_by Freemarker
   * @return
   */
  public String getAbsPath()
  { return file.getAbsolutePath();
  }

  /**
   * @used_by Freemarker
   * @return file name without extension
   */
  public String getName()
  { return FilenameUtils.getBaseName(file.getName());
  }



  public FileDep(String dep, File f)
  { departement = dep;
    file = f;
  }


  /**
   * Enhances a list of files to a list of FileWithDeps by adding a given departement
   *
   * @param dep département
   * @param lf list of files with departement
   * @return
   */
  public static List<FileDep> addDep(String dep, List<File> lf )
  {
    List<FileDep> ret = new ArrayList<FileDep>();
    for (File f : lf)
    {
      ret.add(new FileDep(dep, f));
    }
    return ret;
  }


  @Override
  public String toString()
  {
    return String.format("[%s, %s]", departement, file.getAbsolutePath());
  }
}

package main.java.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fhill
 */
public class MyFileUtils
{
  public static boolean isShapefile(File f)
  {
    return (FilenameUtils.getExtension(f.getName()).toUpperCase().equals("SHP"));
  }

  public static boolean isBatfile(File f)
  {
    return (FilenameUtils.getExtension(f.getName()).toUpperCase().equals("BAT"));
  }



  /**
   * Same as String.split, though
   *  - returning an empty list if the searched string is empty
   *  - does some triming
   *
   * @param searchIn
   * @param regex
   * @return
   */
  public static List<String> split(String searchIn, String regex)
  {
    if (searchIn.isEmpty())  return new ArrayList<String>();  //
    return Arrays.asList(StringUtils.stripAll(searchIn.split(regex)));
  }


  public static boolean isRarFile(File f)
  {
    return true; // TODO
  }



  public static void replace(File f, String search, String replace, File dest) throws Exception
  {
    replace(f, search, replace, dest, "UTF-8");
  }


  /**
   * Pattern replacement in a file.
   * Loads file in mem so use only for small files.
   * @param src
   * @param search
   * @param replace
   * @param encoding if null defaults to "UTF-8"
   * @param dest if null, do the replacement directly in the src file. If provided, leave src untouched
   *             and put result in dest (create if necessary).
   * @throws Exception a wrapped IOException
   */
  public static void replace(File src, String search, String replace, File dest, String encoding) throws Exception
  { if (encoding == null) encoding = "UTF-8";
    try
    { String content = FileUtils.readFileToString(src, encoding);
      content = content.replace(search, replace);
      if (dest == null)
      { // Write back to src :
        FileUtils.writeStringToFile(src, content, encoding);
      }
      else
      { FileUtils.writeStringToFile(dest, content, encoding);  // creates dest if not exists
      }
    }
    catch (IOException e)
    { throw new Exception(String.format("Erreur dans le remplacement dans le fichier [%s] de [%s] par [%s].",  src.getAbsolutePath(), search, replace), e);
    }
  }


  /**
   * Wrapper around FileUtils.moveToDirectory
   * with the added value that abstract file f will hold the new path
   * @param destDir
   * @param createDestDir
   */
  public static File moveToDirectory(File f, File destDir, boolean createDestDir) throws IOException
  {
    FileUtils.moveToDirectory(f, destDir, true);
    // cant seem to be able to modify the actual file in place ... renameTo does not seem to work ... so returning new file
    // Anyway appartently File is immutable ...
    return new File(destDir.getAbsolutePath() + File.separator + f.getName());
  }


}

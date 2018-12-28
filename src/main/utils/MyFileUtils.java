package main.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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



  public static void replace(File f, String search, String replace) throws Exception
  {
    replace(f, search, replace, "UTF-8");
  }


  /**
   * In place replacement in a file.
   * Loads file in mem so use only for small files.
   * @param f
   * @param search
   * @param replace
   * @param encoding if null defaults to "UTF-8"
   * @throws Exception a wrapped IOException
   */
  public static void replace(File f, String search, String replace, String encoding) throws Exception
  { if (encoding == null) encoding = "UTF-8";
    try
    { String content = FileUtils.readFileToString(f, encoding);
      content = content.replace(search, replace);
      FileUtils.writeStringToFile(f, content, encoding);
    }
    catch (IOException e)
    { throw new Exception(String.format("Erreur dans le remplacement dans le fichier [%s] de [%s] par [%s].",  f.getAbsolutePath(), search, replace), e);
    }
  }
}

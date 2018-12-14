package main.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFileUtils
{
    public static boolean isShapefile(File f)
    {
        return (FilenameUtils.getExtension(f.getName()).toUpperCase().equals("SHP"));
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

}

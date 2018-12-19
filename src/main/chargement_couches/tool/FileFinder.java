package main.chargement_couches.tool;

import main.chargement_couches.model.FileDep;
import main.ex.DepExtractionException;
import main.utils.MyFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FileFinder
{
  protected Logger logger = LoggerFactory.getLogger(FileFinder.class);

  private List<File>   rootFolders;
  private List<String> fileExtensions;
  private List<String> fileDetectionPatterns;
  /**
   * One and only one of {depDetectionPattern, dep} has to be provided
   */
  private String     depDetectionPattern = "";

  /**
   * One and only one of {depDetectionPattern, dep} has to be provided
   */
  private String     dep = "";

  private List<FileDep> foundFiles = new ArrayList<FileDep>();


  /**
   * Find files by calling find() on it
   *
   * @param rootFolders  search for files under these folders.
   * @param fileExtensions files must have one of the provided extensions seperated by ",". Empty for no filtering.
   * @param fileDetectionPattern regular expressions, seperated by ",". Files (paths) must validate any of these. Empty for no filtering.
   * @param departement  departement number, or detection pattern (regexep).
   *                     Leave empty if no departement is to be detected.
   * @param isDepPattern is the previous paramter a pattern?
   */
  public FileFinder(List<File> rootFolders, String fileExtensions, String fileDetectionPattern, String departement, boolean isDepPattern)
  {
    this.rootFolders            = rootFolders;
    this.fileExtensions         = MyFileUtils.split(fileExtensions.toUpperCase(),",");
    this.fileDetectionPatterns = MyFileUtils.split(fileDetectionPattern.toUpperCase(),",");;

    if (isDepPattern) {this.depDetectionPattern = departement;} else {this.dep = departement;}
  }

  /**
   * Extract the "departement"
   * @param f
   * @return empty if pattern provided was empty
   * @throws DepExtractionException
   */
  public String extractDepartement(File f) throws DepExtractionException
  {
    if (! dep.isEmpty())
    {
      return dep;
    }
    else if (depDetectionPattern.isEmpty())
    {
      //logger.debug("depDetectionPattern provided is empty, not assigning a dep to files...");
      return "";
    }
    else
    {
      String pattern = depDetectionPattern;
      String searchIn = f.getAbsolutePath();

      Pattern p; Matcher m;
      try
      { p = Pattern.compile(pattern);
      }catch (PatternSyntaxException e)
      { throw new DepExtractionException(String.format("Erreur de syntaxe dans le pattern {%s}", pattern));
      }
      m = p.matcher(searchIn);


      if (m.find())  // found a match for departement
      {
        String depar = m.group(1);  // we're only looking for one group
        logger.debug(String.format("Trouvé le département : {%s} dans le fichier {%s}", depar, searchIn));
        return depar;
      }
      // if we don't find a match, fail
      else
      { logger.debug("could not detect departement, throwing exception...");
        throw new DepExtractionException(String.format("Impossible de trouver un département dans le le fichier {%s} en utilsant le pattern {%s}", searchIn, pattern));
      }
    }
  }


  public List<FileDep> find() throws DepExtractionException
  {
    for(File rootFolder : rootFolders)
    {
      logger.debug("Processing folder : " + rootFolder.getAbsolutePath());

      // Extract the departement from the file path
      String dep_ = extractDepartement(rootFolder);
      logger.debug("dep_= "+ dep_);

      // All files at any level in current root folder
      List<File> allFiles = (List<File>) FileUtils.listFiles(rootFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

      // Find all depFiles corresponding to the file detection patterns given by user
      if (!fileDetectionPatterns.isEmpty())
      {
        for (File ff : allFiles)
        {
          for (String patt : fileDetectionPatterns)
          {   // TODO only search on the part of the file name that is under the "root" dir !
            Pattern p = Pattern.compile(patt);
            Matcher m = p.matcher(FilenameUtils.separatorsToUnix(ff.getAbsolutePath()));

            logger.debug(String.format("Looking for pattern {%s} in file {%s}", patt, ff));
            if (m.find())  // Retain file
            {
                logger.debug(String.format("Retaining file : {%s}", ff));
                foundFiles.add(new FileDep(dep_, ff));
            }
          }
        }
      }
      else  // File detection pattern empty, select all files
      { logger.debug("File detection pattern empty, select all files...");
        foundFiles.addAll(FileDep.addDep(dep_, allFiles));
        logger.debug("Found " + foundFiles.size() + " files.");
      }
    }

    // Filter again to retain only files with correct extensions
    logger.debug("fileExtensions=" + Arrays.toString(fileExtensions.toArray()));
    logger.debug("fileExtensions size =" + fileExtensions.size());
    if (! this.fileExtensions.isEmpty())
    { List<FileDep> foundFilesWithCorrectExtensions = new ArrayList<>();
      for (FileDep fd : foundFiles)
      { if (this.fileExtensions.contains(FilenameUtils.getExtension(fd.file.getName()).toUpperCase()))  // check extension
        { foundFilesWithCorrectExtensions.add(fd);
        }
      }
      foundFiles = foundFilesWithCorrectExtensions;
    }


    return foundFiles;
  }


}

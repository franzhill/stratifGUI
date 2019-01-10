package main.common.tool;

import main.common.tool._excp.ExtractionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Extract part(s) of a file
 * @author jbringuier / fhill
 */
public class Extractor
{
  private static Logger logger = LoggerFactory.getLogger(Extractor.class);

  private File inputFile;
  private File outputFile;
  /**
   * Default
   */
  private String encoding = "UTF-8";
  private final String lineSeparator = System.getProperty("line.separator");

  public Extractor(File input, File output) {
    this(input,output,null);
  }

  public Extractor(File input, File output, String encoding)
  {
    this.inputFile = input;
    this.outputFile = output;
    if(encoding!=null){this.encoding =encoding;}
  }

  /**
   * Does not append to output files (overwrites)
   * See other function extract
   */
  public void extract(String startPattern, String endPattern) throws IOException, ExtractionException
  {
    extract(startPattern, endPattern, startPattern, endPattern, false);
  }

  /**
   * Extract everything from the Extractor input file in between patterns, output in Extractor output file
   * Extracts only one occurrence of {startpattern - stuff_to_extract - endpattern }
   * @param startPattern
   * @param endPattern
   * @param startPatternReplacement replace startPattern with this when outputting to file - null if no replace desired
   * @param endPatternReplacement   replace endPattern   with this when outputting to file - null if no replace desired
   * @param append append to output file?
   * @throws IOException
   * @throws ExtractionException startPattern or endPattern or both not found
   */
  public void extract(String startPattern, String endPattern, String startPatternReplacement, String endPatternReplacement, boolean append) throws IOException, ExtractionException
  {
    LineIterator it = null;
    FileWriter writer = null;
    try
    { it = FileUtils.lineIterator(inputFile, this.encoding);
      writer = new FileWriter(outputFile,append);
      boolean hasExtractionStarted = false;
      boolean wasExtractionSuccessful = false;
      logger.debug("Extracting from line starting with {} to line ending with {} ",startPattern, endPattern);
      // match only one occurrence of {startpattern - stuff_to_extract - endpattern }
      while (it.hasNext() && !wasExtractionSuccessful )  // TODO could be optimized with a state machine
      { String line = it.nextLine();
        if (line.startsWith(startPattern))
        { logger.debug("Found start pattern in line: " + line);
          hasExtractionStarted = true;
          if (startPatternReplacement != null) {line=line.replace(startPattern, startPatternReplacement);}
          writer.append(line);
          writer.append(lineSeparator);
        }
        else if (hasExtractionStarted && line.endsWith(endPattern))
        { logger.debug("Found end pattern in line: " + line);
          hasExtractionStarted = false;
          wasExtractionSuccessful=true;
          if (endPatternReplacement != null) {line=line.replace(endPattern, endPatternReplacement);}
          writer.append(line);
          writer.append(lineSeparator);
        }
        else if (hasExtractionStarted)
        { writer.append(line);
          writer.append(lineSeparator);
        }
      }
      if (! wasExtractionSuccessful)
      { throw new ExtractionException(String.format("Impossible d'extraire une portion du fichier [%s] commençant par [%s] et terminant par [%s].", inputFile.getAbsolutePath(), startPattern, endPattern));
      }
    }
    catch (IOException ie)
    { logger.debug("Error while processing file", ie);
      throw ie;
    }
    finally
    {
      if (it != null) {
        LineIterator.closeQuietly(it);
      }
      if (writer != null) {
        writer.close();
      }
    }
  }

}
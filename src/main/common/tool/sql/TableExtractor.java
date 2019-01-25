package main.common.tool.sql;

import main.common.tool._excp.TableExtractionException;
import main.common.tool.sql.Extractor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Extract the commands to restore a table, from a SQL dump
 * @author fhill
 */
public class TableExtractor
{
  private static Logger logger = LoggerFactory.getLogger(Extractor.class);

  private File dumpFile;
  private String table;
  private String tableNew;


  /**
   *
   * @param dumpFile sql postgresql dump file of a database from which we desire to extract a table
   * @param table    full "qualified" name of table as it appears in the dump file, usually in the form of schema.table
   * @param tableNew replace <param>table</param> with this new table name (schema.table or just table, whatever)
   *                 if null, there will be no table name replacement.
   */
  public TableExtractor(File dumpFile, String table, String tableNew)
  {
    this.dumpFile = dumpFile;
    this.table    = table;
    this.tableNew = table;
    if (tableNew != null) this.tableNew = tableNew;
  }


  /**
   * Extract the SQL commands to restore the table in the given file
   * @param outputFile
   */
  public void extract(File outputFile) throws TableExtractionException
  {
    try
    { logger.debug("Extracting to {} ...", outputFile.getAbsolutePath());
      String    createTablePattern    = String.format("CREATE TABLE %s", table);
      String    createTablePatternNew = String.format("CREATE TABLE %s", tableNew);
      String    copyPattern           = String.format("COPY %s", table);
      String    copyPatternNew        = String.format("COPY %s", tableNew);

      Extractor xtr = new Extractor(this.dumpFile, outputFile);

      // 0. Insert DROP command
      FileUtils.writeStringToFile(outputFile, String.format("DROP TABLE IF EXISTS %s CASCADE;", tableNew), "UTF-8", false); // TODO extract encoding

      // 1. Extract CREATE command
      xtr.extract(createTablePattern, ";", createTablePatternNew, null, true);  // TODO extract the pattern in conf file ? make less brittle (eg replace space with /s+ for example
/* deprecated
      // Replace table name  // TODO could be optimized if table and tableNew are the same no need to do the replacement - maybe replace does the check ... who knows
      MyFileUtils.replace( outputFile, createTablePattern, createTablePatternNew);
*/
      // 2. Extract COPY commands
      xtr.extract(copyPattern, "\\.", copyPatternNew, null, true);         // TODO same
    }
    catch (Exception e)
    {
      throw new TableExtractionException(String.format("Erreur d'extraction de la table [%s] depuis le dump [%s] vers le fichier [%s].", table, dumpFile.getAbsolutePath(), outputFile.getAbsolutePath()), e);
    }





  }

}

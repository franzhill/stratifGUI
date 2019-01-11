package main.common.tool.bat;

import freemarker.template.Configuration;
import freemarker.template.Template;
import main.common.tool._excp.TemplateProcessingException;
import main.common.tool.sql.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Process (interpolate) Freemarker templates
 * @author fhill
 */
public class TemplateProcessor
{
  private static Logger logger = LoggerFactory.getLogger(Extractor.class);

  /**
   * Freemarker config
   */
  private Configuration cfg;

  /**
   * Data for interpolation
   */
  private Map<String, Object> data = new HashMap<String, Object>();



  public TemplateProcessor()
  {
    cfg = new Configuration();   // Freemarker configuration object
  }


  public void process(File template, File output) throws TemplateProcessingException
  {
    Template templ;
    try
    { templ = cfg.getTemplate(template.getPath());
    }
    catch (IOException e)
    { throw new TemplateProcessingException(String.format("Impossible de charger le template [%s].", template.getAbsolutePath()), e);
    }

    // Interpolate and output file
    Writer filewriter;
    logger.debug("output file path =" + output.getAbsolutePath());
    try
    { filewriter = new FileWriter(output);
    }
    catch (IOException e)
    { throw new TemplateProcessingException(String.format("Erreur d'accès pour écriture au fichier cible [%s].", output.getAbsolutePath()), e);
    }
    try
    { templ.process(data, filewriter);
      filewriter.flush();
      filewriter.close();
    }
    catch (Exception e)
    { throw new TemplateProcessingException(String.format("Erreur lors du traitement du template [%s], fichier cible [%s].", template.getAbsolutePath(), output.getAbsolutePath()), e);
    }
  }

  /**
   * Providing a key that already exists will just overwrite (TODO check)
   * @param key
   * @param value
   */
  public void addData(String key, Object value)
  {
    data.put(key, value);
  }

}

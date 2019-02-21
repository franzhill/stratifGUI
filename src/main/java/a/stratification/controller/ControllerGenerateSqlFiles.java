package main.java.stratification.controller;

import main.java.Gui;
import main.java.common._excp.ExecutionException;
import main.java.common._excp.UserLevelException;
import main.java.common.tool.bat.TemplateProcessor;
import main.java.stratification.model.ModelStrat;
import main.java.utils.MyFileUtils;

import java.io.File;
import java.util.Arrays;


/**
 * Generate sql scripts for each departement specified by the user via the GUI.
 * Also generate the bat script (one per departement also) that will play these sql scripts against the DB.
 */
public class ControllerGenerateSqlFiles extends AControllerStrat
{
  /**
   * Used to process (interpolate) the sql templates
   */
  private TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerGenerateSqlFiles(Gui gui, ModelStrat model)
  {   super(gui, model);
  }


  @Override
  protected void doo_() throws UserLevelException
  { // Since generating the scripts doesn't take too long we can do it here in the Event Dispatcher Thread (i.e. here),
    // sparing ourselves the need for a SwingWorker.

    gui.loggerGui.info("Génération des scripts sql et des scripts bat ... Veuillez patienter...");
    gui.loggerGui.info("...");

    int counter_sql = 0;
    int counter_bat = 0;

    try
    { emptyWorkDir();
      for (String dep : model.selDeps)
      { // Generate all sql files for each departement
        for (File f : model.sqlFiles)
        { // Replace placeholder by dep
          try
          { File dest_f = new File(model.workFolder + File.separator + dep + File.separator + f.getName());
            MyFileUtils.replace(f, model.depPlaceholder, dep, dest_f, "UTF-8");  // creates new file and parent dirs if necessary
            MyFileUtils.replace(dest_f, "#DEPT2", dep.substring(1), null, "UTF-8");  // TODO
            counter_sql++;
          }
          catch (Exception e)
          { throw new ExecutionException(String.format("Erreur lors de la génération du fichier sql [%s] pour le département [%s]", f.getAbsolutePath(), dep), e);
          }
        }

        // Generate bat files (one per departement) that each executes all sql files for a departement
        File template = new File("resources/stratification.ftl.bat"); // TODO  put in conf file ? / function
        tmplproc.addData("model", model);
        tmplproc.addData("dep", dep);
        File outputFile = new File(String.format(model.workFolder.getAbsolutePath() + File.separator + "stratification_%s.bat", dep));  // TODO put in conf file
        try
        { tmplproc.process(template, outputFile);
          counter_bat++;
        }
        catch (Exception e)
        { throw new ExecutionException(String.format("Erreur lors de la génération du script bat d'exécution des sql à partir du template, pour le département = [%s}.", dep), e);
        }
      }
    }
    catch (Exception e)
    { throw new UserLevelException("Erreur lors de la génération des scripts de stratification.", e);
    }

    // Operations are not executed asynchronously via a swingworker so it will make sense to display a everything OK message to the user here:
    gui.loggerGui.info("Génération des scripts sql ({}) et des scripts bat ({}) terminée.", counter_sql, counter_bat);
  }


  @Override
  protected void updateModel__()
  {
    model.allDeps            = model.allDeps.isEmpty()   // do only the first time, since hot configuration change is not supported.
                                   ? Arrays.asList(gui.userConfig.getProp("strat.departements").split("\\s*,\\s*"))  // split and trim leading and trailing white spaces
                                   : model.allDeps ;
    model.processAllDeps     = gui.rdoStratDepTous.isSelected();
    model.depPlaceholder     = gui.txtStratDepPlacheholder.getText();
    model.setTempFolderPath   (gui.txtTempDir.getText());
    model.setNbThreads        (gui.txtNbThreads.getText());
    model.setPostgresqlBinPath(gui.txtPostgresqlBinDir .getText());
    model.setTempFolderPath   (gui.txtTempDir          .getText());
    model.selDeps            = Arrays.asList(gui.txtaStratDepSelect.getText().split("\\s*[,\\s]{1}\\s*"));  // split and trim leading and trailing white spaces
    logger.debug("model.selDeps={}", model.selDeps);
  }


  @Override
  protected void preDoChecks() throws Exception
  {
    if (!model.processAllDeps)
    {
      // Vérification :
      // - La liste ne peut être vide
      // - La liste doit être une sous-liste de la liste de tous les départements
      if (    model.selDeps.isEmpty()
          || !model.allDeps.containsAll(model.selDeps)
         )
      { //throw new Exception
        throw new ExecutionException("La liste des départements fournie est incorrecte.");
      }
    }
    if (model.sqlFiles.isEmpty())
    { throw new ExecutionException("Aucun fichier sql renseigné.");
    }
  }


  @Override
  protected boolean shouldEmptyWorkDirectory()
  { return  gui.chbStratEmptyWorkDirFirst.isSelected();
  }

}

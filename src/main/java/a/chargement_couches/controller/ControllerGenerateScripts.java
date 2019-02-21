package main.java.a.chargement_couches.controller;

import main.java.Gui;
import main.java.a.chargement_couches.model.FileDep;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.a.chargement_couches.model.ModelCouche;
import main.java.a.chargement_couches.swing_worker.SwingWorkerGenerateScripts;
import main.java.common.tool.bat.PlaceHolderReplacer;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.common.tool.exec.outputHandler.OutputHandlerGui;
import main.java.common._excp.ExecutionException;
import main.java.common.tool.bat.TemplateProcessor;

import java.io.File;


/**
 * Generate all scripts, as per inputs provided by user through the GUI
 */
public class ControllerGenerateScripts extends AControllerCharg
{
  /**
   * Used to process (interpolate) the script templates
   */
  TemplateProcessor tmplproc = new TemplateProcessor();


  public ControllerGenerateScripts(Gui gui, ModelCharg model)
  {   super(gui, model);
  }


  @Override
  protected void updateModel__()
  {  //updateModelDb(); // Done in parent functions
      model.setPostgresqlBinPath        (gui.txtPostgresqlBinDir .getText());
      model.setTempFolderPath           (gui.txtTempDir          .getText());
  }


  /**
   */
  @Override
  protected void doo_()
  {
    gui.loggerGui.info("Génération des scripts... Veuillez patienter...");
    gui.loggerGui.info("...");

    // Save configs (they might have been edited by user)
    // TODO for the time being we won't be saving the config
    //gui.saveUserConfigDisplay();

    try
    { emptyWorkDir();
      if (gui.rdoCoucheFoncier.isSelected())  // TODO change for model.couche.type == ModelCouche.FONCIER);
      { generateScriptsFoncier();
      }
      else
      { generateScripts();
        gui.loggerGui.info("Génération des scripts teminée.");
      }
    }
    catch (Exception e1)
    { gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
    }
  }


  @Override
  protected void preDoChecks() throws Exception
  {
    // Create workdir if not exists
    model.workFolder.mkdir();

    // Perform some checks : have all details been provided?
    if (model.isIncomplete())
    {
      throw new ExecutionException("Il manque des paramètres. Vérifier que tous les éléments nécessaires ont été indiqués.");
    }
    /*else if (model.depFiles.isEmpty())
    {
      gui.showMessageError("Aucun fichier sélectionné !");
      return false;
    }*/
  }




  /**
   * This operation might be long so we'll be doing it in a swing worker
   */
  private void generateScriptsFoncier()
  {
    // Outputhandler : choose one of the following:
    //IOutputHandler ouh = new OutputHandlerSysOut();  // will log output of scripts on STDO
    IOutputHandler ouh = new OutputHandlerGui(gui);  // will log output of scripts in GUI
    //IOutputHandler ouh = new OutputHandlerNull();  // silent
    SwingWorkerGenerateScripts gssw = new SwingWorkerGenerateScripts(gui, model, gui.buttGenerateScripts, gui.progbCouche, model.workFolder);
    gssw.addPropertyChangeListener(gssw);
    gssw.execute();

  }


  /**
   * Generate bat scripts for all files provided by user
   * @throws ExecutionException
   */
  private void generateScripts() throws ExecutionException
  {
    boolean isCvi = (model.couche.type == ModelCouche.CVI);


    // Use the template processor to generate the bat file that will load the "couche" in DB

    File template   = new File (String.format("resources/chargement_couche.%s.ftl.bat", model.couche.type.toLowerCase())) ; // TODO  put in conf file ? / function

    // Only used for couche CVI :
    File template12 = new File ("resources/create_table.cvi.ftl.sql") ; // TODO  put in conf file ? / function

    tmplproc.addData("model", model);

    // Process each file
    for (FileDep fd : model.depFiles) // TODo rename FileDep in DepFile or depFiles in fileDeps
    {
      logger.debug("Processing file : " + fd.toString());
      if (fd.departement.isEmpty())
      { throw new ExecutionException(String.format("Département manquant pour le fichier [%s].", fd.file.getAbsolutePath()));
      }

      File sqlFile12  = new File(model.workFolder.getAbsolutePath() + File.separator + String.format(     "create_table_%s_%s_%s.sql", model.couche.type, fd.getName(), fd.departement)); // TODO put in conf file)
      fd.sqlFile2     = sqlFile12;  // For CVI
      fd.schemaTable  = new PlaceHolderReplacer(model.couche.schema + "." + model.couche.table).addDep(fd.departement.toLowerCase()).replace(); // For CVI // toLowerCase for departements like 2A and 2B, inside the dump it's 2a and 2b

      // Provide templating engine with remaining data for interpolation
      tmplproc.addData("fd", fd);

      File   outputFile     = new File(String.format(model.workFolder.getAbsolutePath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement));  // TODO put in conf file

      try
      { if (isCvi) {  tmplproc.process(template12, fd.sqlFile2);}
        tmplproc.process(template, outputFile);
      }
      catch (Exception e)
      { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = [%s], département = [%s].", model.couche.type, fd.departement), e);
      }
      gui.loggerGui.info("Génération du script : " + outputFile.getName() + " : DONE. ");
    }
  }


  @Override
  protected boolean shouldEmptyWorkDirectory()
  { return  gui.chbEmptyWorkDirFirst.isSelected();
  }



}

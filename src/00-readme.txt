NOTE D'ARCHITECTURE
--------------------
F.Hill - 2019.01.22


1. Spécifications techniques

IDE : IntelliJ IDEA 2018.1.3 (Community Edition)
      Build #IC-181.4892.42, built on May 8, 2018
      JRE: 1.8.0_152-release-1136-b38 amd64
      JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
      Windows 7 6.1
      Plugins : (notable) : Lombok (compréhension par intelliJ des annotations @Getter @Setter de Lombok, permettant
                            la génération tacite de getters et setters)

2. But de l'appli

L'application en elle-même se contente :
- de récolter des entrants fournis par l'utilisateur de la GUI
- afin de produire des scripts bats à partir de templates
- de les exécuter, avec parallélisation possible

Du point de vue métier, l'exécution de ces scripts correspond :
- au chargement de couches de données géographiques en BD
- à la "stratification", opération qui permet, à partir des couches évoquées ci-dessus, de déterminer le type géographique
(forêt, eau, bâtiment) de chacun des 9 millions de points résultant d'un maillage du territoire français.


3 Organisation du code

3.1. Philosophie de développement

Mode "POC" càd développement pragmatique, privilégiant le confort et la rapidité de
développement sur le respect absolu de normes ou bonnes pratiques péremptoires, tout en assurant une architecture
relativement saine et maintenable, et sans gréver l'expérience utilisateur.
Il se peut donc que certaines "bonnes pratiques" aient été allègrement  bypassées (ex : accès aux membres d'une classe
via Getter et Setters => membres publics ;o), pas de tests unitaires etc.)

3.2 Packages

- Le package "chargement_couches" regroupe les composants nécéessaires à la réalisation du chargement des couches
(topo, foncier, alti, forêt), en BD. Sur la GUI cela correspond à un onglet, "chargement couches". En général les couches
sont fournies par les producteurs de données (IGN, Geoide) sous la forme de zips (à dézipper avant de pouvoir le charger
via la GUI), un zip par département.

- Le package "stratification" regroupe les composants nécéessaires à la réalisation de la stratification proprement dite
(exécution d'un certain nombre de scripts SQL), en BD. Sur la GUI cela correspond à un onglet, "stratification".
La GUI donne la possibilité à l'utilisateur de choisir sur quels départements jouer ces scripts SQL.

- Le package common contient toutes les classes utilisées pour l'un ou l'autre de ces 2 buts, ainsi que les
classes parentes

3.3. Architecture

3.3.1 Généralités

L'organisation du code s'inspire du modèle MVC.

-Classe Gui : ~ "vue" + colle
-Classes [chargement_couche|stratification|common]/controller : contrôleurs
-Classes [chargement_couche|stratification|common]/model : modèles

> La classe Gui contient toutes les définitions des composants Swing (dont le design est fait avec le designer
d'application Swing intégré à IntelliJ, qui produit le fichier Gui.form, étroitement lié à la classe Gui)
ainsi que toutes les initialisations relatives à l'interface :
- initialisation de la solution de logging
- initialisation du logger d'interface (logger qui logue dans un textpane de la GUI)
- initialisation des boutons çàd liaison à leurs controleurs (actionListeners)

> Associé à chaque bouton (sur lequel un clic ~ une action) : un Contrôleur
Chaque contrôleur récupère, depuis la GUI, les données utiles à son action, qu'il stocke dans le modèle associé à l'action.
Ex: pour "chargement_couches"
-ControllerSelectCouche : associé au bouton de sélection de la couche
-ControllerSelectParentFiles : associé u bouton de sélection des répertoires contenenant les couches à charger
-ControllerGenerateScripts : se charge de la génération des scripts à partir des templates (dossier "resources")

Les Contrôleurs héritent d'une même classe parent qui définit un certain processus de traitement de l'action :
dans l'ordre, l'éxécution de :
-updateModel()   : mise à jour du modèle associé à l'action
-updateGui()     : mise à jour de la GUI
-preDoChecks()   : vérification de la validité des données fournies par l'utilisateur et stockées dans le modèle,
                   avant exécution de l'action. Possibilité aussi d'y mettre tout code à exécuter avant l'action.
                   Le cas échéant, une exception est levée interrompant le processus de traitement et un  message
                   d'erreur est affiché sur la GUI.
-doo()           : exécution de l'action
-postDo()        : éventuels traitements à effectuer après l'exécution de
Chacun des controleurs fils peut définir les traitements à effectuer pour chacune de ces étapes.

Certaines actions étant longues temporellement (exécution de multiples scripts), afin de ne pas bloquer la thread
principale dans laquelle tourne Swing ("Event Dispatch Thread), elles sont exécutées dans des SwingWorker,
fonctionnalité haut niveau fournie par Swing afin de gérer des traitements dans des threads distincts.
Ex:
- SwingWorkerGenerateScripts : swingworker détaché par le ControllerGenerateScripts afin de générer les scripts
- SwingWorkerExecuteScripts  : swingworker détaché par le ControllerExecuteScripts  afin d'exécuter les scripts

> Modèle
Contiennent (indirectement) les données fournies en entrée vie la GUI.
Les actions ne sont pas directement effecutées à partir des informations fournies via la GUI.
Celles-ci sont transférées au modèle (cf ci-dessus updateModel()) , et
Ceci afin de :
- factoriser les traitements
- vérifier/prétraiter/"marshaliser" les données

Le modèle principal pour le chargement des couches est ModelCharg, celui pour la stratification est ModelStratif.
Ils héritent tous les 2



3.3.2 Détails

- Configuration : il s'agit d'un (de) fichier(s) éditable(s) par l'utilisateur, au format .ini, placé(s) dans le répertoire conf
(et packagé(s) en dehors du .jar livrable).
A l'heure d'écriture de ces lignes la fonctionnalité consistant à mettre à jour la conf avec ce que l'utilisateur a entré
dans la GUI n'a pas été incluse. Une fonctionnalité de gestion de différentes configurations pourrait même être
envisageable.
- Templating : effectué avec le moteur Freemarker.
Les templates sont dans le dossier resources.
Les remplacements des placeholders sont en général issus des modèles.
Le processeur de templates est : common.tool.bat.TemplateProcessor
- Exécution d'une commande système (cmd.exe <nom de script bat) : common/tool/exec/SysCommand
- MultiThreadBatFolderExecutor : permet d'exécuter plusieurs bats en même temps (1 process pour chaque), en fonction
du nombre fourni via la GUI (nb max de connexions simultanées à la BD). Le multi
- Gestion des exceptions :
Les problèmes rencontrés sont en dernier lieu enveloppés dans une exception ad hoc (par exemple ExecutionException) qui
donne lieu à un message d'erreur sur la GUI.
Pour l'instant cette gestion est laissée à chaque implémentation fille de AController (dans la méthode doo()),
mais il est envisageable de factoriser ce comportement dans AController. (2018.09.23 : DONE)
- Logging : la solution de logging est Log4j2. Originellement la couche d'abstraction SLF4J était utilisée, mais suite à
 des restrictions techniques rencontrées, l'implémentation Log4J2 a été utilisée directement à certains endroits
 (=> idéalement il faudrait nettoyer, par exemple enlever SLF4J complètement)
- Logging dans la GUI :
Le panneau inférieur de la GUI est dédié aux messages (logs).
A l'heure d'écriture de ces lignes, il existe deux zones de logs : celle du haut affiche les messages généraux de
l'application. C'est le logger "logger_gui" (ou "loggerGui") qui alimente cette zone.
Celle du bas affiche la sortie des exécutions des scripts .bat. C'est le logger "logger_gui2" (ou "loggerGui2")
qui alimente cette zone.
Afin de permettre à un logger log4j2 de loguer dans un panneau dédié de la GUI (présente un certain nombre d'avantages
par rapport à l'écriture directe par setText() par exemple dans le panneau : gestion des niveaux, du format, sortie
additionnelle dans un fichier...), l'appender outpustream de Log4J2 a été utilisé, en conjonction avec l'outpustream
ad hoc :   common/gui/GuiLogOutputStream.
La configuration de log4j2 pour les 2 appenders de ces 2 loggers est de ce fait effectuée programmatiquement, et non via
 le fichier de config log4j2, comme c'est le cas pour les autres appenders, ainsi que pour les loggers. Les appenders
 y sont quand même définis afin de pouvoir y spécifier le layout (plus facile que d'avoir à le faire programmatiquement)
 et est repris au moment de la configuration programmatique.
- Logging dans la GUI de la sortie de l'exécution de scripts .bat dans des process différents : fait via
 common/tool/exec/outputHandler/OutputHandlerGui + StreamGobbler (en plus de l'appender évoqué ci-dessus)



3.3.3 Packages



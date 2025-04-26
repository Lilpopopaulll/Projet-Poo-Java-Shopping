@echo off
echo Execution du module Reporting...

:: Définir les chemins
set JAVA_HOME=C:\Program Files\Java\jre-1.8
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
set PROJECT_ROOT=Reporting
set SRC_DIR=%PROJECT_ROOT%\src
set LIB_DIR=%PROJECT_ROOT%\lib

:: Créer le répertoire lib s'il n'existe pas
if not exist %LIB_DIR% (
    echo Le répertoire lib n'existe pas. Création...
    mkdir %LIB_DIR%
)

:: Vérifier si les bibliothèques JFreeChart existent
if not exist %LIB_DIR%\jfreechart-1.5.3.jar (
    echo Téléchargement de JFreeChart...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/jfree/jfreechart/1.5.3/jfreechart-1.5.3.jar' -OutFile '%LIB_DIR%\jfreechart-1.5.3.jar'"
)

if not exist %LIB_DIR%\jcommon-1.0.24.jar (
    echo Téléchargement de JCommon...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/jfree/jcommon/1.0.24/jcommon-1.0.24.jar' -OutFile '%LIB_DIR%\jcommon-1.0.24.jar'"
)

echo Exécution de l'application Reporting...

:: Exécuter l'application avec le chemin complet vers java.exe et les sources directement
"%JAVA_EXE%" -cp "%SRC_DIR%;%LIB_DIR%\jfreechart-1.5.3.jar;%LIB_DIR%\jcommon-1.0.24.jar" main.Main

pause

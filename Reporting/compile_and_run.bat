@echo off
echo Compilation du module Reporting...

:: Définir les chemins
set PROJECT_ROOT=.
set SRC_DIR=%PROJECT_ROOT%\src
set BIN_DIR=%PROJECT_ROOT%\bin
set LIB_DIR=%PROJECT_ROOT%\lib
set CLASSPATH=%BIN_DIR%;%LIB_DIR%\jfreechart-1.5.3.jar;%LIB_DIR%\jcommon-1.0.24.jar

:: Créer le répertoire bin s'il n'existe pas
if not exist %BIN_DIR% mkdir %BIN_DIR%

:: Compiler les sources
javac -d %BIN_DIR% -cp %CLASSPATH% %SRC_DIR%\main\Main.java %SRC_DIR%\controller\*.java %SRC_DIR%\model\*.java %SRC_DIR%\view\*.java

:: Vérifier si la compilation a réussi
if %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation!
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation terminée avec succès.
echo Exécution de l'application...

:: Exécuter l'application
java -cp %CLASSPATH% main.Main

pause

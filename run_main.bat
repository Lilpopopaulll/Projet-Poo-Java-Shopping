@echo off
echo Execution du projet principal...

:: Définir les chemins
set BIN_DIR=bin
set LIB_DIR=lib
set CLASSPATH=%BIN_DIR%;%LIB_DIR%\mysql-connector-j-9.3.0.jar

:: Vérifier si le répertoire bin existe
if not exist %BIN_DIR% (
    echo Le répertoire bin n'existe pas. Compilation nécessaire...
    mkdir %BIN_DIR%
    
    :: Compiler les sources
    javac -d %BIN_DIR% -cp %CLASSPATH% src\Main.java src\controller\*.java src\model\*.java src\view\*.java src\view\theme\*.java src\dao\*.java
    
    :: Vérifier si la compilation a réussi
    if %ERRORLEVEL% NEQ 0 (
        echo Erreur de compilation!
        pause
        exit /b %ERRORLEVEL%
    )
    
    echo Compilation terminée avec succès.
)

echo Exécution de l'application principale...

:: Exécuter l'application
java -cp %CLASSPATH% Main

pause

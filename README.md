# csc413-ChessGame


| Student Information |                |
|:-------------------:|----------------|
|  Student Name       |  Yuvraj Gupta  |
|  Student Email      | ygupta@sfsu.edu|


## Purpose of jar Folder 
The jar folder will be used to store the built jar of your term-project.

`NO SOURCE CODE SHOULD BE IN THIS FOLDER. DOING SO WILL CAUSE POINTS TO BE DEDUCTED`

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: 
Java 20 (Oracle OpenJDK 20.0.2 - aarch64)

## IDE used: 
IntelliJ IDEA

## Steps to Import project into IDE:
1. Open IntelliJ IDEA.
2. Click on File > Open... and select the project folder.
3. Ensure that the project SDK is set to JDK 20.
4. Under Project Structure, navigate to Modules > Dependencies and ensure that the JavaFX SDK is added as a module dependency.

## Steps to Build your Project:
1. In IntelliJ IDEA, navigate to Build > Build Artifacts....
2. Select your project and click Build.
3. The JAR file will be generated in the out/artifacts directory.
 
## Steps to run your Project:
1. To run the JAR file, use the following command: java --module-path /Users/Yuvraj/Downloads/javafx-sdk-22.0.2/lib --add-modules=javafx.controls,javafx.fxml,javafx.swing -jar csc413-tankgame-YuvrajGupta1808.jar
2. Ensure that the Stockfish and Javafx path in the code is an absolute path that points to the correct location on your system.
3. If not able to run in terminal run by intellj setting the module dependency.

## Controls to play your Game:
Both the Player Uses Mouse Movements

## JavaFX VM Options:
Add the following VM options in your run configuration to ensure JavaFX modules are properly loaded.
--module-path /Users/Yuvraj/Downloads/javafx-sdk-22.0.2/lib --add-modules=javafx.controls,javafx.fxml,javafx.swing --add-exports javafx.base/com.sun.javafx.logging=ALL-UNNAMED
Remember to Change Module Path.

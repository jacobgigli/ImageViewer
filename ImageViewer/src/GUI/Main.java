package GUI;

import ImageProgram.ImageManager;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Opens Image Manager GUI.
 */

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    String sp = File.separator;
    //reads CollectionOfImageDirectories and CollectionOfTags Serializable o startup
    try {
      ImageManager.readImageDirectoryFromFile("data" + sp + "CollectionImageDirectories.ser");
      ImageManager.readTags();
      ImageManager.readGlobalLogFromFile("data" + sp + "GlobalLog.ser");
    } catch (ClassNotFoundException cnfe) {
      ImageManager.collectionOfImageDir.clear();
    }

    //using JavaFX Scene Builder
    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Image Program");
    primaryStage.setScene(new Scene(root, 1000, 700));
    primaryStage.setResizable(false);
    System.setProperty("javafx.userAgentStylesheetUrl", "CASPIAN");
    primaryStage.show();

    //Saves CollectionOfImageDirectories and CollectionOfTags as Serializable when closed via 'X'
    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        try {
          ImageManager.saveImageDirectoryToFile("data" + sp + "CollectionImageDirectories.ser");
          ImageManager.saveGlobalLogToFile("data" + sp + "GlobalLog.ser");
        } catch (IOException ioex) {
          primaryStage.close();
        }
      }
    });
  }
}

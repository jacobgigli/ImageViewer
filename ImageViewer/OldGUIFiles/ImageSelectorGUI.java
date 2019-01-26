package GUI;

import ImageProgram.ImageDirectory;
import ImageProgram.ImageManager;
import ImageProgram.ImageObject;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ImageSelectorGUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      ImageManager.readImageDirectoryFromFile("data/CollectionImageDirectories.ser");
      ImageManager.readTags();
    } catch (ClassNotFoundException cnfe) {
      ImageManager.collectionOfImageDir.clear();
    }
    primaryStage.setTitle("Input Image");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Input image name then Directory of image");
    grid.add(title, 0, 0, 2, 1);

    Label tagName = new Label("Image Name:");
    grid.add(tagName, 0, 1);

    TextField imageTextField = new TextField();
    grid.add(imageTextField, 1, 1);

    TextField dirTextField = new TextField();
    grid.add(dirTextField, 1, 3);

    Button enter = new Button("Tag Image");
    Button remover = new Button("UnTag Image");

    HBox enterBox = new HBox(10);
    HBox removerBox = new HBox(15);
    enterBox.setAlignment(Pos.BOTTOM_CENTER);
    enterBox.getChildren().add(enter);

    removerBox.setAlignment(Pos.BOTTOM_CENTER);
    removerBox.getChildren().add(remover);

    grid.add(enterBox, 1, 8);
    grid.add(removerBox, 1, 6);

    enter.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            String imageName = imageTextField.getText();
            String dirName = dirTextField.getText();
            ImageDirectory dir = ImageManager.findDirectory(dirName);
            if (!(dir == null)) {
              ImageObject image = dir.getImage(imageName);
              if (!(image == null)) {
                ImageTaggerGUI tagger = new ImageTaggerGUI();
                tagger.setImageToTag(image);
                Stage stg = new Stage();
                tagger.start(stg);
                System.out.println(dir);
              }
            }
          }
        });

    remover.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            String imageName = imageTextField.getText();
            String dirName = dirTextField.getText();
            ImageDirectory dir = ImageManager.findDirectory(dirName);
            if (!(dir == null)) {
              ImageObject image = dir.getImage(imageName);
              if (!(image == null)) {
                ImageTagRemoverGUI tagger = new ImageTagRemoverGUI();
                tagger.setImageToUnTag(image);
                Stage stg = new Stage();
                tagger.start(stg);
              }
            }
          }
        });

    Scene scene = new Scene(grid, 300, 275);
    primaryStage.setScene(scene);
    primaryStage.show();

    primaryStage.setOnCloseRequest(
        new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            try {
              ImageManager.saveImageDirectoryToFile("data/CollectionImageDirectories.ser");
            } catch (IOException ioex) {
              primaryStage.close();
            }
          }
        });
  }
}

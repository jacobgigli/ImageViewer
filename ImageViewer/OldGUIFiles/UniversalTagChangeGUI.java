package GUI;

import ImageProgram.ImageManager;
import ImageProgram.Tag;
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

public class UniversalTagChangeGUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      ImageManager.readImageDirectoryFromFile("data/CollectionImageDirectories.ser");
      ImageManager.readTags();
    } catch (ClassNotFoundException cnfe) {
      ImageManager.collectionOfImageDir.clear();
    }
    primaryStage.setTitle("Change All Tags With Given Name");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Input old name at top and new name at bottom.");
    grid.add(title, 0, 0, 2, 1);

    Label tagName = new Label("Tag to Change");
    grid.add(tagName, 0, 1);

    TextField oldTagTextField = new TextField();
    grid.add(oldTagTextField, 1, 1);

    TextField newTagTextField = new TextField();
    grid.add(newTagTextField, 1, 3);

    Button enter = new Button("Enter");
    Button back = new Button("Back to main menu.");

    HBox enterBox = new HBox(10);
    HBox backBox = new HBox(15);
    enterBox.setAlignment(Pos.BOTTOM_CENTER);
    enterBox.getChildren().add(enter);

    backBox.setAlignment(Pos.BOTTOM_CENTER);
    backBox.getChildren().add(back);

    grid.add(enterBox, 1, 8);
    grid.add(backBox, 1, 6);

    enter.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            String oldTagName = oldTagTextField.getText();
            Tag oldTag = Tag.getTag(oldTagName);
            if (!(oldTag == null)) {
              oldTag.setTagName(newTagTextField.getText());
            }
          }
        });

    back.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            primaryStage.close();
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

package GUI;

import ImageProgram.ImageManager;
import ImageProgram.ImageObject;
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
import java.util.ArrayList;

public class SearchByTagGUI extends Application {

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

    Text title = new Text("Input tag names to return list of images with those tags.");
    grid.add(title, 0, 0, 2, 1);

    Label tagName = new Label("Tag Name:");
    grid.add(tagName, 0, 1);

    TextField tagTextField = new TextField();
    grid.add(tagTextField, 1, 1);

    Button enter = new Button("Search");
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
            String oldTagName = tagTextField.getText();
            String[] tagNames = oldTagName.split(",");
            ArrayList<ImageObject> returnList = new ArrayList<>();
            Boolean firstTag = true;
            for (String s : tagNames) {
              Tag tag = Tag.getTag(s);
              ArrayList<ImageObject> tagImages = tag.getImagesOfTag();
              if (firstTag) { // If it is the first tag, we need to initialize the list
                firstTag = false;
                returnList.addAll(tagImages);
              } else {
                returnList.retainAll(
                    tagImages); // For every other Tag, we only retain the images from the
                                // collection
              }
            }
            for (ImageObject i : returnList) {
              System.out.println(i.toString());
            }
            System.out.println("------");
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

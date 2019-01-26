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

/** GUI used to Untag an Image */
public class ImageTagRemoverGUI extends Application {

  /** ImageObject to be Untagged */
  private ImageObject imageToUntag;

  public void setImageToUnTag(ImageObject imageToTag) {
    this.imageToUntag = imageToTag;
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      ImageManager.readImageDirectoryFromFile("data/CollectionImageDirectories.ser");
      ImageManager.readTags();
    } catch (ClassNotFoundException cnfe) {
      ImageManager.collectionOfImageDir.clear();
    }
    if (!(imageToUntag == null)) {
      primaryStage.setTitle("Remove Tags from " + imageToUntag.getName());
    }

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Input tag name");
    grid.add(title, 0, 0, 2, 1);

    Label tagName = new Label("Tag");
    grid.add(tagName, 0, 1);

    TextField newTagTextField = new TextField();
    grid.add(newTagTextField, 1, 1);

    Button enter = new Button("Untag Image");
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
            imageToUntag.readName();
            String newTagName = newTagTextField.getText();
            System.out.println(imageToUntag.getTags());
            if (Tag.exists(newTagName)) { // Check to make sure this is already a Tag in the system
              try {
                imageToUntag.removeTag(Tag.getTag(newTagName));
                System.out.println("Image Untagged");
              } catch (IOException ioexp) {
                System.out.println("Image does not have this Tag.");
              }
            } else {
              System.out.println("Not a Tag of this image.");
            }
          }
        });

    back.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            Stage menuStage = new Stage();
            GUI_2 newMenu = new GUI_2();
            try {
              newMenu.start(menuStage);
              primaryStage.close();
            } catch (IOException ioex) {
              primaryStage.close();
            }
          }
        });
    Scene scene = new Scene(grid, 400, 275);
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

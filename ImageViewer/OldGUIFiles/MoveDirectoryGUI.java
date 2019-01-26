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

import java.awt.*;
import java.io.IOException;

/** GUI.GUI Whose job is to guide the user to moving an image from one directory to another. */
public class MoveDirectoryGUI extends Application {

  private ImageObject selectedImage;
  private boolean imageNull = true;

  public void setSelectedImage(ImageObject img){
    this.selectedImage = img;
  }

  public boolean getImageNull(){
    return imageNull;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Move an image between Directories");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title =
            new Text("Input start directory,end directory and image name(remove tags from name). ");
    grid.add(title, 0, 0, 2, 1);

    Label userName = new Label("Directory:");
    grid.add(userName, 0, 1);

    TextField directoryFromTextField = new TextField();
    Label From = new Label("(Start Directory)");
    grid.add(directoryFromTextField, 1, 1);

    TextField directoryToTextField = new TextField();
    Label TO = new Label("(End Directory)");
    grid.add(directoryToTextField, 1, 3);

    TextField imageNameTextField = new TextField();
    Label IM = new Label("(Image Name)");
    grid.add(imageNameTextField, 1, 5);

    grid.add(From, 5, 1);
    grid.add(TO, 5, 3);
    grid.add(IM, 5, 5);

    Button move = new Button("Make Move");
    Button back = new Button("Back to main menu.");

    HBox moveBox = new HBox(10);
    HBox backBox = new HBox(15);
    moveBox.setAlignment(Pos.BOTTOM_CENTER);
    moveBox.getChildren().add(move);

    backBox.setAlignment(Pos.CENTER);
    backBox.getChildren().add(back);

    grid.add(moveBox, 1, 4);
    grid.add(backBox, 1, 6);

    move.setOnAction(
            new EventHandler<ActionEvent>() {

              @Override
              public void handle(ActionEvent event) {
                ImageObject imageToMove = null;
                String directoryFromName = directoryFromTextField.getText();
                String directoryToName = directoryToTextField.getText();
                String imageName = imageNameTextField.getText();

                ImageDirectory directoryFrom;
                if (ImageManager.directoryExists(directoryFromName)) {
                  directoryFrom = ImageManager.findDirectory(directoryFromName);
                } else {
                  try {
                    directoryFrom = new ImageDirectory(directoryFromName);
                  } catch (IOException ioexp) {
                    directoryFrom = null;
                    System.out.println(ioexp.toString());
                  }
                }

                ImageDirectory directoryTo;
                if (ImageManager.directoryExists(directoryToName)) {
                  directoryTo = ImageManager.findDirectory(directoryToName);
                } else {
                  try {
                    directoryTo = new ImageDirectory(directoryToName);
                  } catch (IOException ioexp) {
                    directoryTo = null;
                    System.out.println(ioexp.toString());
                  }
                }

                if (!(directoryFrom == null)) {
                  try {
                    directoryFrom.scan();
                  } catch (IOException ioexp) {
                    System.out.println(ioexp.toString());
                  }
                  imageToMove = directoryFrom.getImage(imageName);

                  System.out.println(directoryTo);

                  if (!(imageToMove == null)) {
                    ImageManager.moveImageToDirectory(imageToMove, directoryTo);
                  }
                }
                if (imageToMove != null) {
                  selectedImage = imageToMove;
                  imageNull = false;
                }
              }
            });

    back.setOnAction(
            event -> primaryStage.close());

    Scene scene = new Scene(grid, 480, 275);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public ImageObject getSelectedImage() {
    return selectedImage;
  }
}

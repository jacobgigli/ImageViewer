package GUI;

import ImageProgram.ImageDirectory;
import ImageProgram.ImageManager;
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

import java.io.IOException;
import java.util.ArrayList;

public class AddDirectoryGUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Add a Directory");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Add a new directory with the given filepath.");
    grid.add(title, 0, 0, 2, 1);

    Label userName = new Label("Directory:");
    grid.add(userName, 0, 1);

    TextField directoryTextField = new TextField();
    grid.add(directoryTextField, 1, 1);

    Button enter = new Button("Enter");
    Button back = new Button("Back to main menu.");

    HBox enterBox = new HBox(10);
    HBox backBox = new HBox(15);
    enterBox.setAlignment(Pos.BOTTOM_CENTER);
    enterBox.getChildren().add(enter);

    backBox.setAlignment(Pos.BOTTOM_CENTER);
    backBox.getChildren().add(back);

    grid.add(enterBox, 1, 4);
    grid.add(backBox, 1, 6);

    enter.setOnAction(
        new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            String directoryName = directoryTextField.getText();

            try {
              ImageManager.createImageDirectory(directoryName);
              System.out.println("Directory Added");
              ArrayList<ImageDirectory> col = ImageManager.collectionOfImageDir;
              System.out.println(col);
            } catch (IOException ioex) {
              System.out.println("invalid path");
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
  }
}

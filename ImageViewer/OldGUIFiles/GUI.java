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

public class GUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("ImageObject Tagger");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Insert your image directory");
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
            ImageDirectory inputDir = ImageManager.findDirectory(directoryName);
            if (!(inputDir == null)) {
              System.out.println("Directory Valid");
              DirectoryGUI directoryInterface = new DirectoryGUI(inputDir);
              directoryInterface.start(primaryStage);

            } else {
              System.out.println("Directory Invalid");
            }
          }
        });
    Scene scene = new Scene(grid, 300, 275);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

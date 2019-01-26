package GUI;

import ImageProgram.ImageDirectory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DirectoryGUI extends Application {

  /** The ImageDirectory represented by this GUI.DirectoryGUI */
  private ImageDirectory directory;

  /** Build a GUI.DirectoryGUI with ImageDirectory dir. */
  DirectoryGUI(ImageDirectory dir) {
    this.directory = dir;
  }

  /**
   * Initialize this Graphical user Interface.
   *
   * @param primaryStage Stage of GUI.DirectoryGUI
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle(directory.getFilePath());
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setPadding(new Insets(30, 30, 30, 30));

    Text title = new Text("Choose an image withing the directory");
    grid.add(title, 0, 0, 2, 1);

    final FileChooser imageChooser = new FileChooser();

    Button chooseImage = new Button("Choose an image");
    HBox imageBox = new HBox(10);

    imageBox.setAlignment(Pos.BOTTOM_CENTER);
    imageBox.getChildren().add(chooseImage);

    grid.add(imageBox, 1, 4);

    Scene scene = new Scene(grid, 300, 275);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

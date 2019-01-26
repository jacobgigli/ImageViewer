package GUI;

import ImageProgram.ImageManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/** Graphical User Interface for ImageObject Tagging Software */
public class GUI_2 extends Application {

  /** running the program */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Creates the Window and its functionality
   *
   * @param primaryStage the stage/window that is being used
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    // Loads CollectionOfImageDirectories as Serializable when software opened
    try {
      ImageManager.readImageDirectoryFromFile("data/CollectionImageDirectories.ser");
      ImageManager.readTags();
    } catch (ClassNotFoundException cnfe) {
      ImageManager.collectionOfImageDir.clear();
    }

    // Root Layout BorderPane
    HBox root = new HBox();
    Scene scene = new Scene(root, 640, 480, Color.WHITE);

    // Main Menu Bar
    MenuBar menuBar = new MenuBar();
    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    root.getChildren().addAll(menuBar);

    // Menu for File
    Menu MenuFile = new Menu("File");
    Menu MenuSearch = new Menu("Search");

    MenuItem menuItemExit = new MenuItem("Exit");
    // Saves CollectionOfImageDirectories as Serializable when closed via 'X'
    menuItemExit.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              ImageManager.saveImageDirectoryToFile("data/CollectionImageDirectories.ser");
              primaryStage.close();
            } catch (IOException ioex) {
              primaryStage.close();
            }
          }
        });

    // new Menu Item "Add Directory"

    MenuItem menuItemAddDir = new MenuItem("Add a Directory");

    menuItemAddDir.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            AddDirectoryGUI dirAdder = new AddDirectoryGUI();
            Stage adderStage = new Stage();
            dirAdder.start(adderStage);
          }
        });

    MenuItem menuItemImage = new MenuItem("Select an Image");

    menuItemImage.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            ImageSelectorGUI dirAdder = new ImageSelectorGUI();
            Stage adderStage = new Stage();
            dirAdder.start(adderStage);
          }
        });

    // new Menu Item "Move image to directory"

    MenuItem menuItemMoveDir = new MenuItem("Move image to a Directory");

    menuItemMoveDir.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            MoveDirectoryGUI dirMover = new MoveDirectoryGUI();
            Stage adderStage = new Stage();
            dirMover.start(adderStage);
          }
        });

    // new Menu Item "Universal Tag Change"

    MenuItem menuItemChangeTag = new MenuItem("Universal Tag Change");

    menuItemChangeTag.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            UniversalTagChangeGUI dirChanger = new UniversalTagChangeGUI();
            Stage adderStage = new Stage();
            dirChanger.start(adderStage);
          }
        });

    // new Menu Item "Search By Tag"

    MenuItem menuItemSearchTag = new MenuItem("Search By Tag");

    menuItemSearchTag.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            SearchByTagGUI tagSearcher = new SearchByTagGUI();
            Stage adderStage = new Stage();
            tagSearcher.start(adderStage);
          }
        });

    // Adds items to MenuFile
    MenuFile.getItems()
        .addAll(
            new SeparatorMenuItem(),
            new SeparatorMenuItem(),
            menuItemAddDir,
            menuItemMoveDir,
            new SeparatorMenuItem(),
            menuItemExit,
            menuItemImage);

    // Add items to MenuSearch
    MenuSearch.getItems().addAll(menuItemChangeTag, new SeparatorMenuItem(), menuItemSearchTag);

    // Adds all menus to Menu bar
    menuBar.getMenus().addAll(MenuFile, MenuSearch);

    // Creates the primary stage
    primaryStage.setTitle("Image Management");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();

    // Saves CollectionOfImageDirectories as Serializable when closed via 'X'
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

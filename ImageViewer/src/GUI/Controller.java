package GUI;

import static ImageProgram.ImageManager.getListFilePaths;
import static ImageProgram.Tag.getListOfTags;

import ImageProgram.ImageDirectory;
import ImageProgram.ImageManager;
import ImageProgram.ImageObject;
import ImageProgram.Log;
import ImageProgram.Tag;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/*
-https://stackoverflow.com/questions/36657299/how-can-i-populate-a-listview-in-javafx-using-custom-objects
-Ihttps://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html
-http://www.tutorialspoint.com/javafx/
-http://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html
 */

/**
 * Controller that is used by GUI
 */
public class Controller implements Initializable {

  private static ImageObject selectedImage;
  private ImageDirectory currImageDirectory;
  // _____________________________________________________MENU_____________________________________________________
  @FXML
  private MenuItem miOpenDirectory;

  @FXML
  private MenuItem miCloseProgram;

  @FXML
  private ListView<String> imageObjectListView;

  @FXML
  private ListView<String> tagListView;

  @FXML
  private ListView<String> imageDirectoryListView;

  @FXML
  private Label lImageDetails;

  @FXML
  private ListView<String> selectTagList;

  @FXML
  private TextArea taJournal;

  @FXML
  private Label lImageDirectory;

  @FXML
  private Label lImageNumTags;

  @FXML
  private Label lPath;

  @FXML
  private Label lName;

  @FXML
  private Label lfavourite;

  @FXML
  private Label lviews;

  @FXML
  private Button btSetNameImage;

  @FXML
  private Button btDeleteTag;

  @FXML
  private TextField tfUserInputNewTag;

  @FXML
  private Button btAddTagNew;

  @FXML
  private Button btAddTagExist;

  @FXML
  private ListView<String> selectTagList1;

  @FXML
  private ListView<String> tagListViewForAdder;

  @FXML
  private TextField tfImageName;

  @FXML
  private Button btPreviousLog;

  @FXML
  private TextField tfLogEntryLoad;

  @FXML
  private Button btImageSelecter;

  @FXML
  private Button btMoveDirectory;

  @FXML
  private ListView<String> imageOfTagsListView;

  @FXML
  private Label lTagDetails;

  @FXML
  private Button btDeleteTagAll;

  @FXML
  private Button btOpenSelectedDirectory;

  @FXML
  private Button btSortByTagsNum;

  @FXML
  private Button btSortByAlphabet;

  @FXML
  private Button btSortByFavourite;

  @FXML
  private Button btSortByViews;

  @FXML
  private MenuItem mtClearData;

  @FXML
  private Button btLoadSelectedDirectory;

  @FXML
  private Button btSetFavourite;

  @FXML
  private javafx.scene.image.ImageView ivPreviewImage;

  @FXML
  private javafx.scene.image.ImageView ivPreviewImageFull;

  @FXML
  private TextArea taGlobalLog;

  @FXML
  private Button btCreateTag;

  @FXML
  private TextField tfCreateTag;

  public void createNewTag() {
    String tagName = tfCreateTag.getText();
    if (tagName != null) {
      try {
        Tag addedTag = new Tag(tagName);
        initializeTagList();
        initializeTagListForAdder();
      } catch (IllegalArgumentException iae) {
        popup("\"" + tagName + "\"" + " Tag Already Exists");
      }
    }
  }

  /**
   * Opens About.txt to show instructions on how to use the GUI.
   */
  public void viewHelp() {
    File file = new File("About.txt");
    try {
      Desktop.getDesktop().open(file);
    } catch (IOException | IllegalArgumentException ioex) {
      popup(ioex.toString());
    }
  }

  /**
   * Resets all data in GUI
   */
  public void clearData() {
    ImageManager.reset();
    ObservableList<String> collectionImageObjects = FXCollections.observableArrayList();
    imageObjectListView.setItems(collectionImageObjects);
    imageDirectoryListView.setItems(collectionImageObjects);
    imageOfTagsListView.setItems(collectionImageObjects);
    tagListViewForAdder.setItems(collectionImageObjects);
    selectTagList1.setItems(collectionImageObjects);
    selectTagList.setItems(collectionImageObjects);
    tagListView.setItems(collectionImageObjects);
    lName.setText("");
    lImageDetails.setText("");
    lPath.setText("");
    lImageDirectory.setText("");
    taGlobalLog.setText("");
    currImageDirectory = null;
  }

  /**
   * Moves Image from one directory to Another
   */
  public void moveDirectory() {
    Stage stageMover = new Stage();
    MoveDirectoryGUI imgMover = new MoveDirectoryGUI();
    imgMover.setSelectedImage(selectedImage);
    imgMover.start(stageMover);
    stageMover.setOnCloseRequest(event -> updateAll());
  }

  /**
   * Loads Previous Log in GUI
   */
  public void LoadPreviousLog() {
    if (selectedImage != null) {
      try {
        int UserInp = Integer.valueOf(tfLogEntryLoad.getText());
        if (UserInp < selectedImage.getLog().size()) {
          selectedImage.revertChanges(UserInp);
          updateAll();
          initializeSelectTagList1(selectedImage);
          initializeSelectTagList(selectedImage);
          LogView();
          GlobalLogView();
          lName.setText(selectedImage.getFullName());
          lPath.setText(selectedImage.getFullPath());
        } else {
          popup("Invalid Entry number.");
        }
      } catch (IOException ioex) {
        popup(ioex.toString());
      } catch (IllegalArgumentException iae) {
        popup(
            "An Image of that name already exists in the Directory or you have not selected a log number.");
      }
    }
  }

  /**
   * Set ImageObject name in GUI
   */
  public void setImageName() {
    if (selectedImage != null) {
      try {
        String UserInp = tfImageName.getText();
        selectedImage.setName(UserInp);
        lName.setText(selectedImage.getFullName());
        lPath.setText(selectedImage.getFullPath());
        lImageDetails.setText(UserInp);
        initializeImageList();
        LogView();
        GlobalLogView();
      } catch (IOException ioex) {
        popup("An File of that name already exists in the Directory.");
      } catch (IllegalArgumentException iae) {
        popup("An Image of that name already exists in the Directory.");
      }
    }
  }

  /**
   * opens a certain directory in the GUI.GUI. default directory is "images".
   */
  public void openDirectory() {
    try {
      Stage primaryStage = new Stage();
      DirectoryChooser chooseDirectory = new DirectoryChooser();
      // Set the images folder as the initial default directory if it exists.
      File initialDirectory = new File("images");
      if (initialDirectory.exists()) {
        chooseDirectory.setInitialDirectory(initialDirectory);
      }
      File OpenImageDirectory = chooseDirectory.showDialog(primaryStage);
      if (OpenImageDirectory != null) {
        String imageDirectoryPath = OpenImageDirectory.getAbsolutePath();
        currImageDirectory = ImageManager.findDirectory(imageDirectoryPath);
        if (currImageDirectory == null) {
          try {
            currImageDirectory = new ImageDirectory(imageDirectoryPath);
            ImageManager.recognizeImageDirectory(currImageDirectory);
          } catch (IOException ioxe) {
            popup(ioxe.toString());
            primaryStage.close();
          }
        }
        if (!ImageManager.directoryExists(imageDirectoryPath)) {
          try {
            currImageDirectory = new ImageDirectory(imageDirectoryPath);
            ImageManager.addImageDirectory(currImageDirectory);
            currImageDirectory.scan();
            updateAll();

          } catch (IOException ioex) {
            popup(ioex.toString());
          }
        } else {
          currImageDirectory = ImageManager.findDirectory(imageDirectoryPath);
          try {
            if (currImageDirectory != null) {
              currImageDirectory.scan();
            }
            updateAll();
          } catch (IOException ioex) {
            popup(ioex.toString());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      popup(
          "Unexpected Error when scanning the Directory's contents! Data may be corrupt! Clearing Data is advised.");
    }
  }

  /**
   * Saves Collection of Image Directories and Collection Of Tags as ".ser" files when program is
   * closed
   */
  public void closeProgram() {
    try {
      String sp = File.separator;
      ImageManager.saveImageDirectoryToFile("data" + sp + "CollectionImageDirectories.ser");
      ImageManager.saveGlobalLogToFile("data" + sp + "GlobalLog.ser");
      Platform.exit();
    } catch (IOException ioex) {
      Platform.exit();
      ioex.printStackTrace();
    }
  }

  // _____________________________________________________LIST_VIEWS___________________________________________________

  /**
   * ListView of Images
   */
  private void initializeImageList() {
    ArrayList<String> imageList = new ArrayList<>(currImageDirectory.getImageNames());
    initializeList(imageObjectListView, imageList);
  }

  /**
   * Initializes ListView lstView with strings from ObservableList lstOfStr
   *
   * @param lstView ListView<String> the list to set.
   * @param lstOfStr ArrayList of Strings to set lstView.
   */
  private void initializeList(
      ListView<String> lstView,
      ArrayList<String> lstOfStr) { // Added method to mitigate copy/pasting
    ObservableList<String> collectionObjects = FXCollections.observableArrayList();
    lstView.setItems(collectionObjects);
    collectionObjects.addAll(lstOfStr);
    lstView.setItems(collectionObjects);
  }

  /**
   * ListView of Tags
   */
  private void initializeTagList() {
    ArrayList<String> tagList = new ArrayList<>(getListOfTags());
    initializeList(tagListView, tagList);
  }

  /**
   * ListView of Tags that is picked for adding
   */
  private void initializeTagListForAdder() {
    ArrayList<String> tagList = new ArrayList<>(getListOfTags());
    initializeList(tagListViewForAdder, tagList);
  }

  /**
   * ListView of ImageDirectories
   */
  private void initializeImageDirectoryList() {
    ArrayList<String> directoryList = new ArrayList<>(getListFilePaths());
    initializeList(imageDirectoryListView, directoryList);
  }

  /**
   * updates all the ListViews in the GUI
   */
  private void updateAll() {
    initializeImageList();
    initializeTagList();
    initializeImageDirectoryList();
    initializeTagListForAdder();
  }

  /**
   * ListView for Tags for particular ImageObject
   *
   * @param tempImageObject ImageObject used to check Tags
   */
  private void initializeSelectTagList(ImageObject tempImageObject) {
    ArrayList<String> tagList = new ArrayList<>();
    if (tempImageObject != null) {
      tagList.addAll(tempImageObject.getListTagNames());
    }
    initializeList(selectTagList, tagList);
  }

  /**
   * ListView for Tags for particular ImageObject. Different from initializeSelectTagList.
   *
   * @param tempImageObject ImageObject used to check Tags
   */
  private void initializeSelectTagList1(ImageObject tempImageObject) {
    ArrayList<String> tagList = new ArrayList<>();
    if (tempImageObject != null) {
      tagList.addAll(tempImageObject.getListTagNames());
    }
    initializeList(selectTagList1, tagList);
  }

  /**
   * Views the Log for a particular ImageObject
   */
  private void LogView() {
    if (selectedImage != null) {
      taJournal.setText(selectedImage.getLog().toString());
    }
  }

  private void GlobalLogView() {
    taGlobalLog.setText(Log.globalLog.toString());
  }

  /**
   * Selecting a Tag for a particular ImageObject for deletion
   */
  public void selectTagForDeletion() {
    selectTagList1
        .getFocusModel()
        .focusedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> btDeleteTag.setOnAction(
                event -> {
                  if (selectedImage != null && newValue != null) {
                    try {
                      selectedImage.removeTag(selectedImage.getTagString(newValue));
                    } catch (Exception ioex) {
                      popup(ioex.toString());
                    }
                    updateAll();
                    initializeSelectTagList1(selectedImage);
                    initializeSelectTagList(selectedImage);
                    LogView();
                    GlobalLogView();
                    lName.setText(selectedImage.getFullName());
                    lPath.setText(selectedImage.getFullPath());
                  }
                }));
  }

  /**
   * Selecting an existing tag to be added to the particular ImageObject
   */
  public void selectTagExistForAdder() {
    tagListViewForAdder.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tagListViewForAdder
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              ObservableList<String> selectMultipleTagToAdd =
                  tagListViewForAdder.getSelectionModel().getSelectedItems();
              if (!selectMultipleTagToAdd.isEmpty()) {
                btAddTagExist.setOnAction(
                    event -> {
                      for (String str :
                          selectMultipleTagToAdd) { // Changed for loop to foreach, removed
                        // redundant variable UserInput
                        if (selectedImage != null) {
                          try {
                            Tag temp = Tag.getTag(str);
                            selectedImage.addTag(temp);
                          } catch (IOException ioex) {
                            popup(ioex.toString());
                          } catch (IllegalArgumentException iae) {
                            popup("\"" + str + "\"" + " Tag Already Exists");
                          }
                        }
                      }
                      if (selectedImage != null) {
                        initializeSelectTagList(selectedImage);
                        initializeSelectTagList1(selectedImage);
                        updateAll();
                        LogView();
                        GlobalLogView();
                        lName.setText(selectedImage.getFullName());
                        lPath.setText(selectedImage.getFullPath());
                      }
                    });
              }
            });
  }

  /**
   * Select ImageObject from List of ImageObjects
   */
  public void selectFromList() {
    if (currImageDirectory != null) {
      btSortByTagsNum.setOnAction( // Sort ImageObjects by number of tags
          event -> {
            currImageDirectory.sortTagNum();
            initializeImageList();
          });
      btSortByAlphabet.setOnAction( // Sort ImageObjects in alphabetical order
          event -> {
            currImageDirectory.sortAlphabetNum();
            initializeImageList();
          });
      btSortByFavourite
          .setOnAction( // Sort ImageObjects whether an image is favourited by the user or not
              event -> {
                currImageDirectory.sortFavourite();
                initializeImageList();
              });
    }

    imageObjectListView
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(
            (observable, oldValue, newValue) -> imageObjectListView.setOnMouseClicked(
                event -> {
                  if (event.getButton() == MouseButton.PRIMARY
                      && event.getClickCount() == 1) {
                    if ((int) newValue != -1) {
                      selectedImage =
                          currImageDirectory.getImageObjectsList().get((int) newValue);
                    }
                    if (selectedImage != null) {
                      lImageDetails.setText(selectedImage.getName());
                      lName.setText(selectedImage.getFullName());
                      lPath.setText(selectedImage.getFullPath());
                      lImageNumTags.setText(" " + selectedImage.getNumberOfTags());
                      if (selectedImage.isFavourite()) {
                        lfavourite.setText("Favourite: Yes");
                      } else {
                        lfavourite.setText("Favourite: No");
                      }
                      lImageDirectory.setText(selectedImage.getDirectory().getFilePath());
                      initializeSelectTagList(selectedImage);
                      initializeSelectTagList1(selectedImage);
                      Image image =
                          new Image(new File(selectedImage.getFullPath()).toURI().toString());
                      ivPreviewImage.setImage(image);
                      ivPreviewImageFull.setImage(image);

                      LogView();
                      GlobalLogView();
                      selectedImage.increaseClicks();
                      lviews.setText("Views: " + selectedImage.getClicks());
                      btSortByTagsNum
                          .setOnAction( // Sort ImageObjects by number of tags an image has
                              event1 -> {
                                currImageDirectory.sortTagNum();
                                initializeImageList();
                              });
                      btSortByAlphabet.setOnAction( // Sort ImageObjects in alphabetical order
                          event12 -> {
                            currImageDirectory.sortAlphabetNum();
                            initializeImageList();
                          });
                      btSortByFavourite.setOnAction(
                          event13 -> {
                            currImageDirectory.sortFavourite();
                            initializeImageList();
                          });
                      btSortByViews.setOnAction(
                          event14 -> {
                            currImageDirectory.sortViews();
                            initializeImageList();
                          });
                      btSetFavourite.setOnAction(
                          event15 -> {
                            if (selectedImage.isFavourite()) {
                              selectedImage.unFavourite();
                              lfavourite.setText("Favourite: No");
                            } else {
                              selectedImage.setFavourite();
                              lfavourite.setText("Favourite: Yes");
                            }
                          });

                      btOpenSelectedDirectory.setOnAction(
                          event16 -> {
                            if (selectedImage != null) {
                              try {
                                Desktop.getDesktop()
                                    .open(new File(selectedImage.getPath()));
                              } catch (IOException ioex) {
                                popup(ioex.toString());
                              }
                            }
                          });
                    }
                  }
                }));
  }

  /**
   * Select Image Directory in Image Directory List
   */
  public void selectImageDirectoryList() {
    imageDirectoryListView
        .getFocusModel()
        .focusedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                btLoadSelectedDirectory.setOnAction(
                    event -> {
                      if (!ImageManager.directoryExists(newValue)) {
                        popup("SOMETHING GONE WRONG");
                      } else {
                        currImageDirectory = ImageManager.findDirectory(newValue);
                        try {
                          if (currImageDirectory != null) {
                            currImageDirectory.scan();
                          }
                          updateAll();
                        } catch (IOException ioex) {
                          popup(ioex.toString());
                        }
                      }
                    });
              }
            });
  }

  /**
   * Select Tags from Collection of Tags
   */
  public void selectAllTagFromList() {
    tagListView
        .getFocusModel()
        .focusedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                Tag currentTag = Tag.getTag(newValue);
                if (currentTag != null) {
                  lTagDetails.setText(currentTag.getTagName());
                  initializeImageOfTagsList(currentTag);
                  btDeleteTagAll.setOnAction(
                      event -> {
                        if (currImageDirectory != null) {
                          // Tag.collectionOfTags.remove(currentTag); //TODO: removes tag from
                          // collection
                          currentTag.clearTag();
                          for (ImageObject img : currImageDirectory.getImageObjectsList()) {
                            updateAll();
                            initializeImageOfTagsList(currentTag);
                            initializeSelectTagList1(img);
                            initializeSelectTagList(img);
                            LogView();
                            GlobalLogView();
                            if (selectedImage != null) {
                              lName.setText(selectedImage.getFullName());
                              lPath.setText(selectedImage.getFullPath());
                            }
                          }
                        } else {
                          currentTag.clearTag();
                          initializeTagList();
                        }
                      });
                }
              }
            });
  }

  /**
   * ListView for ImageObjects that relate to a particular Tag
   *
   * @param tempTag Tag used to display its ImageObjects
   */
  private void initializeImageOfTagsList(Tag tempTag) { // Optimized
    ArrayList<String> imgList = new ArrayList<>();
    if (tempTag != null) {
      imgList.addAll(tempTag.getListOfImages());
    }
    initializeList(imageOfTagsListView, imgList);
  }

  /**
   * Adds new Tag for the Selected imageObject via UserInput.
   */
  public void AddNewTagForSelectedImage() { // Optimized
    String UserInput = tfUserInputNewTag.getText();
    if (selectedImage != null) {
      try {
        Tag temp = Tag.getTag(UserInput);
        selectedImage.addTag(temp);
        initializeSelectTagList(selectedImage);
        initializeSelectTagList1(selectedImage);
        LogView();
        GlobalLogView();
        lName.setText(selectedImage.getFullName());
        lPath.setText(selectedImage.getFullPath());
        updateAll();
      } catch (IOException ioex) {
        popup(ioex.toString());
      } catch (IllegalArgumentException iae) {
        popup("Tag Already Exists");
      }
    }
  }

  /**
   * Pop-op messages in case of errors or invalid entries.
   *
   * @param message message to be displayed
   */
  private void popup(String message) {
    Stage alert = new Stage();
    alert.setTitle("ALERT");
    Label except = new Label(message);
    VBox temp = new VBox();
    temp.getChildren().addAll(except);
    temp.setAlignment(Pos.CENTER);
    alert.setScene(new Scene(temp, 300, 100));
    alert.setResizable(false);
    alert.show();
  }

  /**
   * On startup, Image directories and TagLists will be displayed
   *
   * @param location Not applicable for this GUI
   * @param resources Not applicable for this GUI
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeImageDirectoryList();
    initializeTagList();
    GlobalLogView();
  }
}

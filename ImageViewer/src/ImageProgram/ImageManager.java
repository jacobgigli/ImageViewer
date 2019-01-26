package ImageProgram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * An ImageManager that manages and controls the program.
 */
public class ImageManager {

  /**
   * Collection of ImageDirectories in this ImageManager.
   */
  public static ArrayList<ImageDirectory> collectionOfImageDir = new ArrayList<>();

  /**
   * Moves ImageObject from one ImageDirectory to another ImageDirectory.
   *
   * @param img The ImageObject to move.
   * @param dest The ImageDirectory to move it to.
   */
  public static void moveImageToDirectory(ImageObject img, ImageDirectory dest) {
    ImageDirectory fromDirectory = img.getDirectory();

    try {
      img.setDirectory(dest);
      if (img.getDirectory() == dest){
        fromDirectory.removeImage(img);
      }
    } catch (IOException ioexp) {
      System.out.println("Image Could not be moved");
    }
  }

  /**
   * Adds a new ImageDirectory to collectionOfImageDir. if the ImageDirectory already in
   * collectionOfImageDir, then nothing is added.
   *
   * @param imageDir ImageDirectory that is being added.
   */
  public static void addImageDirectory(ImageDirectory imageDir) {
    if (!collectionOfImageDir.contains(imageDir)) {
      collectionOfImageDir.add(imageDir);
    }
  }

  /**
   * Saves collection of ImageDirectories as a serializable file and stores it in the given file
   * path on the computer.
   *
   * @param filepath The file path.
   * @throws IOException when filepath is not found.
   */
  public static void saveImageDirectoryToFile(String filepath) throws IOException {
    File f = new File("data");
    if (!f.exists()){
      f.mkdir();
    }
    OutputStream Imagefile = new FileOutputStream(filepath);
    OutputStream buffer = new BufferedOutputStream(Imagefile);
    ObjectOutput output = new ObjectOutputStream(buffer);

    output.writeObject(collectionOfImageDir);
    output.close();
  }

  /**
   * Saves Global Tag as a serializable file and stores it in the given file path on the computer.
   *
   * @param filepath The file path.
   * @throws IOException when filepath is not found.
   */
  public static void saveGlobalLogToFile(String filepath) throws IOException {
    File f = new File("data");
    if (!f.exists()){
      f.mkdir();
    }
    OutputStream Imagefile = new FileOutputStream(filepath);
    OutputStream buffer = new BufferedOutputStream(Imagefile);
    ObjectOutput output = new ObjectOutputStream(buffer);

    output.writeObject(Log.getGlobalLog());
    output.close();
  }

  /**
   * Instantiates the static collection of Tags.
   */
  public static void readTags() {
    for (ImageDirectory id : ImageManager.collectionOfImageDir) {
      for (ImageObject i : id.getDirectImageObjectsList()) {
        for (Tag t : i.getTagObjects()) {
          if (!Tag.exists(t.getTagName())) {
            Tag.collectionOfTags.add(t);
          }
        }
      }
    }
  }

  /**
   * Reads serializable file of Tag Collection and instantiates it to Tag.collectionOfTags.
   *
   * @param filepath The file path.
   * @throws ClassNotFoundException when Serializable object is not found.
   */
  @SuppressWarnings("unchecked")
  public static void readGlobalLogFromFile(String filepath) throws ClassNotFoundException {
    File f = new File("data");
    if (!f.exists()){
      f.mkdir();
    }
    try {
      InputStream imageFile = new FileInputStream(filepath);
      InputStream buffer = new BufferedInputStream(imageFile);
      ObjectInput input = new ObjectInputStream(buffer);

      Log.setGlobalLog((Log) input.readObject());
      System.out.println("Global Log data detected, loading...");
    } catch (IOException ioex) {
      System.out.println("Global Log data file at: " + filepath
          + " not detected, will be generated on program close.");
    }
  }

  /**
   * Reads serializable file of ImageDirectories and instantiates it to collectionOfImageDir.
   *
   * @param filepath The file path.
   * @throws ClassNotFoundException when Serializable object is not found.
   */
  @SuppressWarnings("unchecked")
  public static void readImageDirectoryFromFile(String filepath) throws ClassNotFoundException {
    File f = new File("data");
    if (!f.exists()){
      f.mkdir();
    }
    try {
      InputStream imageFile = new FileInputStream(filepath);
      InputStream buffer = new BufferedInputStream(imageFile);
      ObjectInput input = new ObjectInputStream(buffer);

      collectionOfImageDir = (ArrayList<ImageDirectory>) input.readObject();
      System.out.println("Directories data detected, loading...");
    } catch (IOException ioex) {
      System.out.println("Directories data file at : " + filepath
          + " not detected, will be generated on program close.");
    }
  }

  /**
   * Find an ImageDirectory and return that ImageDirectory. If it does not exist, return null.
   *
   * @param directoryName: Name of the ImageDirectory to find.
   * @return The ImageDirectory of the given name.
   */
  public static ImageDirectory findDirectory(String directoryName) {
    for (ImageDirectory directories : collectionOfImageDir) {
      if (directories.getFilePath().equals(directoryName)) {
        return directories;
      }
    }
    return null;
  }

  /**
   * Returns whether a ImageDirectory with the name directoryPath exists within the ImageManager.
   *
   * @param directoryPath The path of the directory.
   * @return Whether the directory exists.
   */
  public static boolean directoryExists(String directoryPath) {
    boolean exists = false;
    for (ImageDirectory directories : collectionOfImageDir) {
      if (directories.getFilePath().equals(directoryPath)) {
        exists = true;
      }
    }
    return exists;
  }


  /**
   * Resets ImageManager to a fresh state by clearing all static collections in the program.
   */
  public static void reset() {
    collectionOfImageDir.clear();
    Tag.collectionOfTags.clear();
    Log.clearGlobalClear();
  }

  /**
   * Creates a root ImageDirectory, runs it scan method, and adds it to the collection of image
   * directories.
   *
   * @param rootPath The path name of the root ImageDirectory.
   * @throws IOException when cannot create ImageDirectory.
   */
  public static void createImageDirectory(String rootPath) throws IOException {
    if (!ImageManager.directoryExists(rootPath)) {
      File newDirFile = new File(rootPath);
      boolean success = newDirFile.mkdirs();
      if (!success) {
        throw new IOException("The directory was not able to be made.");
      }
      ImageDirectory newDir = new ImageDirectory(newDirFile);
      newDir.scan();
      collectionOfImageDir.add(newDir);
    }
  }

  /**
   * Adds this Directory to the list of image directories, and Scans the directory for any
   * subdirectories
   *
   * @throws IOException If the directory does not exist.
   */
  public static void recognizeImageDirectory(ImageDirectory newDir) throws IOException {
    ImageManager.addImageDirectory(newDir);
    newDir.scan();
  }

  /**
   * Returns the collectionOfImageDir ArrayList.
   *
   * @return collectionOfImageDir ArrayList of image directories.
   */
  public ArrayList<ImageDirectory> getCollectionOfImageDir() {
    return collectionOfImageDir;
  }

  /**
   * Return an ArrayList<String> of file paths from Image directories in collectionOfImageDir.
   *
   * @return pathList ArrayList of strings (file paths).
   */
  public static ArrayList<String> getListFilePaths() {
    ArrayList<String> pathList = new ArrayList<>();
    for (ImageDirectory dir : collectionOfImageDir) {
      pathList.add(dir.getFilePath());
    }
    return pathList;
  }
}

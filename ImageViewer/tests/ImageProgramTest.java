import ImageProgram.ImageManager;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class ImageProgramTest {

  static String sp = File.separator;

  static String directoryPath;
  static String fileName;

  @BeforeAll
  static void testPrepareAll() throws IOException {
    if (directoryPath != null) {
      // Clear the test directory if it exists and re-create it before testing begins.
      File dir = new File(directoryPath);
      if (dir.exists()) {
        File[] files = dir.listFiles();
        for (File f : files) {
          f.delete();
        }
        dir.delete();
      }
      boolean success = dir.mkdir();
      if (!success) {
        System.out.println("Test Directory was not able to be created! Tests will likely fail!");
      }
    }
  }

  @AfterAll
  public static void testCleanupAll() {
    if (directoryPath != null) {
      // Clear out all files in the test directory after testing has finished
      File dir = new File(directoryPath);
      deleteFiles(dir);
    }
  }

  @BeforeEach
  public void testPrepareTest() throws IOException {
    // Create a test image file for the current test.
    if ((directoryPath != null) && (fileName != null)) {
      File f = new File(directoryPath + fileName);
      boolean success = f.createNewFile();
      if (!success) {
        System.out.println("Test File was not able to be created! Test will likely fail!");
      }
    }
  }

  @AfterEach
  public void testCleanup() {
    // Clear out all files in the test directory after the current test.
    if (directoryPath != null) {
      File dir = new File(directoryPath);
      deleteFiles(dir);
    }
    ImageManager.reset();
  }

  File getRandomFile() throws IOException {
    File dir = new File(directoryPath);
    File[] files = dir.listFiles();
    File f;
    try {
      f = files[0];
    } catch (ArrayIndexOutOfBoundsException e) {
      f = new File(dir.getPath() + sp + "TestImage.jpg");
      f.createNewFile();
    }
    return f;
  }

  private static void deleteFiles(File file) {
    File[] files = file.listFiles();
    for (File f : files) {
      if (f.isDirectory()) {
        deleteFiles(f);
      }
      boolean success = f.delete();
      if (!success) {
        System.out.println("Files were not able to be cleared! Tests will likely fail!");
      }
    }
  }
}

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ImageProgram.ImageDirectory;
import ImageProgram.ImageManager;
import ImageProgram.Tag;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageManagerTest {

  public static String sp = File.separator;

  @BeforeEach
  public void testPrepare() {
    ImageManager.reset();
  }

  @Test
  public void testReadWriteImageDirectoriesAndSubDirectories()
      throws IOException, ClassNotFoundException {
    String directoryPath = (new File("tests" + sp + "testImageManager" + sp)).getAbsolutePath();
    File temp = new File(directoryPath);
    if (!temp.exists()) {
      temp.mkdir();
    }
    for (File f : temp.listFiles()) {
      f.delete();
    }
    File tempimage1 = new File(temp.getPath() + sp + "IMAGE1 @Tag1 @Tag2 @Tag3.jpg");
    tempimage1.createNewFile();
    File tempimage2 = new File(temp.getPath() + sp + "IMAGE2 @Tag2 @Tag3.jpg");
    tempimage2.createNewFile();
    File tempimage3 = new File(temp.getPath() + sp + "IMAGE3 @Tag2.jpg");
    tempimage3.createNewFile();

    ImageDirectory tempImageDirectory = new ImageDirectory(directoryPath);
    ImageManager.addImageDirectory(tempImageDirectory);
    tempImageDirectory.scan();
    assertTrue(Tag.collectionOfTags.size() == 3);
    tempImageDirectory.getImage("IMAGE3").addTag(new Tag("Test1"));
    tempImageDirectory.getImage("IMAGE3").addTag(new Tag("Test2"));

    String logbefore = tempImageDirectory.toString();

    ImageManager.saveImageDirectoryToFile(
        "data" + sp + "TestSerializable" + sp
            + "testReadWriteImageDirectoriesAndSubDirectoriesImages.ser");

    ImageManager.collectionOfImageDir.clear();
    Tag.collectionOfTags.clear();

    ImageManager.readImageDirectoryFromFile(
        "data" + sp + "TestSerializable" + sp
            + "testReadWriteImageDirectoriesAndSubDirectoriesImages.ser");
    ImageManager.readTags();

    String logafter = ImageManager.collectionOfImageDir.get(0).toString();

    assertTrue(Tag.collectionOfTags.size() == 5);
    assertEquals(logbefore, logafter);

  }
}
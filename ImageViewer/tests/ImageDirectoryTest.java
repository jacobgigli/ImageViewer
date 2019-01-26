import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ImageProgram.ImageDirectory;
import ImageProgram.Tag;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ImageDirectoryTest extends ImageProgramTest {

  @BeforeAll
  public static void testPrepareAll() throws IOException {
    ImageProgramTest.directoryPath =
        (new File("tests" + sp + "testImageDirectory" + sp)).getAbsolutePath() + sp;
    ImageProgramTest.fileName = "ImageDirectoryTest.jpg";
    ImageProgramTest.testPrepareAll();
  }

  @Test
  void testCreateDirectory() throws IOException {
    File f = new File(directoryPath + "test1" + sp);
    f.mkdir();
    for (File file : f.listFiles()) {
      file.delete();
    }
    File image1 = new File(f.getPath() + sp + "IMAGE1 @Tag1 @Tag2.jpg");
    image1.createNewFile();
    File image2 = new File(f.getPath() + sp + "IMAGE2 @Tag2 @Tag3.jpg");
    image2.createNewFile();
    ImageDirectory id = new ImageDirectory(f);
    id.scan();
    assertTrue(id.containsImage("IMAGE1"));
    assertTrue(id.containsImage("IMAGE2"));
    assertEquals(id.getImage("IMAGE1").getFullName(), "IMAGE1 @Tag1 @Tag2");
    assertEquals(id.getImage("IMAGE2").getFullName(), "IMAGE2 @Tag2 @Tag3");
    assertTrue(Tag.collectionOfTags.size() == 3);
  }
}

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ImageProgram.ImageDirectory;
import ImageProgram.ImageObject;
import ImageProgram.Tag;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ImageObjectTest extends ImageProgramTest {

  private static ImageDirectory d;

  @BeforeAll
  static void testPrepareAll() throws IOException {
    ImageProgramTest.directoryPath =
        (new File("tests" + sp + "testImageObject" + sp)).getAbsolutePath() + sp;
    ImageProgramTest.fileName = "ImageTest.jpg";
    ImageProgramTest.testPrepareAll();
    d = new ImageDirectory(directoryPath);
  }

  @Test
  void testCreateImage() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }

  @Test
  void testRenameImage() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    imageObject.setName("TEST");
    assertEquals(directoryPath + "TEST.jpg", imageObject.getFile().getPath());
    imageObject.setName(name);
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }

  @Test
  void testAddTagOne() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    Tag t = new Tag("AddTagTest");
    imageObject.addTag(t);
    assertTrue(t.contains(imageObject));
    assertEquals(directoryPath + name + " @AddTagTest.jpg", imageObject.getFile().getPath());
    imageObject.removeTag(t);
    assertFalse(t.contains(imageObject));
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }

  @Test
  void testAddTagTwo() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    Tag t1 = new Tag("AddTagTest1");
    Tag t2 = new Tag("AddTagTest2");
    imageObject.addTag(t1);
    assertTrue(t1.contains(imageObject));
    assertEquals(directoryPath + name + " @AddTagTest1.jpg", imageObject.getFile().getPath());
    imageObject.addTag(t2);
    assertTrue(t1.contains(imageObject));
    assertTrue(t2.contains(imageObject));
    assertEquals(directoryPath + name + " @AddTagTest1 @AddTagTest2.jpg",
        imageObject.getFile().getPath());
    imageObject.removeTag(t1);
    assertFalse(t1.contains(imageObject));
    assertTrue(t2.contains(imageObject));
    assertEquals(directoryPath + name + " @AddTagTest2.jpg", imageObject.getFile().getPath());
    imageObject.removeTag(t2);
    assertFalse(t1.contains(imageObject));
    assertFalse(t2.contains(imageObject));
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }

  @Test
  void testRevertLogBasic() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    Tag t = new Tag("RevertLogTest");
    imageObject.addTag(t);
    assertEquals(imageObject.getLog().size(), 1);
    assertEquals(directoryPath + name + " @RevertLogTest.jpg", imageObject.getFile().getPath());
    imageObject.revertChanges(0);
    assertEquals(imageObject.getLog().size(), 2);
    assertFalse(t.contains(imageObject));
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }

  @Test
  void testRevertLast() throws IOException {
    ImageObject imageObject = new ImageObject(getRandomFile());
    String name = imageObject.getName();
    Tag t = new Tag("RevertLastTest");
    imageObject.addTag(t);
    assertEquals(imageObject.getLog().size(), 1);
    assertEquals(directoryPath + name + " @RevertLastTest.jpg", imageObject.getFile().getPath());
    imageObject.revertLast();
    assertEquals(imageObject.getLog().size(), 0);
    assertFalse(t.contains(imageObject));
    assertEquals(directoryPath + name + ".jpg", imageObject.getFile().getPath());
  }
}

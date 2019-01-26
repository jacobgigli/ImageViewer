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


public class ImageObjectUnittest extends ImageProgramTest {

  @BeforeAll
  public static void testPrepareAll() throws IOException {
    ImageProgramTest.directoryPath =
        (new File("unittests" + sp + "ImageObjectTestFiles" + sp)).getAbsolutePath() + sp;
    ImageProgramTest.fileName = "ImageTest.jpg";
    ImageProgramTest.testPrepareAll();
  }

  // (1) testHasTagFalse
  @Test
  void testHasTagFalse() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    assertFalse(image1.hasTag("Tag"));
  }

  // (2) testHasTagTrue
  @Test
  void testHasTagTrue() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag");
    image1.addTag(tag1);
    assertTrue(image1.hasTag("Tag"));
  }

  // (3) testChangeImageDirectory
  @Test
  void testChangeImageDirectory() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    File f1 = new File(directoryPath + "dir1" + sp);
    f1.mkdir();
    File f2 = new File(directoryPath + "dir2" + sp);
    f2.mkdir();
    ImageDirectory dir1 = new ImageDirectory(f1);
    ImageDirectory dir2 = new ImageDirectory(f2);
    image1.setDirectory(dir1);
    assertTrue(image1.getDirectory() == dir1);
    image1.setDirectory(dir2);
    assertTrue(image1.getDirectory() == dir2);
  }

  // (4) testAddTags
  @Test
  void testAddTags() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag 1");
    Tag tag2 = new Tag("Tag 2");
    image1.addTag(tag1);
    assertTrue(image1.hasTag("Tag 1"));
    assertFalse(image1.hasTag("Tag 2"));
  }

  // (5) testRemoveTags
  @Test
  void testRemoveTags() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag 1");
    Tag tag2 = new Tag("Tag 2");
    image1.addTag(tag1);
    image1.addTag(tag2);
    assertTrue(image1.hasTag("Tag 1"));
    assertTrue(image1.hasTag("Tag 2"));
    image1.removeTag(tag1);
    assertFalse(image1.hasTag("Tag 1"));
    assertTrue(image1.hasTag("Tag 2"));
    image1.removeTag(tag2);
    assertFalse(image1.hasTag("Tag 1"));
    assertFalse(image1.hasTag("Tag 2"));
  }

  // (6) testLogHistory
  @Test
  void testLogHistory() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag 1");
    // TODO: make meaningful test for testing log history
  }

  // (7) testRevertChanges
  @Test
  void testRevertChanges() throws IOException, IllegalArgumentException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag 1");
    Tag tag2 = new Tag("Tag 2");
    image1.addTag(tag1);
    image1.addTag(tag2);
    assertTrue(image1.hasTag("Tag 1"));
    assertTrue(image1.hasTag("Tag 2"));
    image1.revertChanges(0);
    assertFalse(image1.hasTag("Tag 1"));
    assertFalse(image1.hasTag("Tag 2"));
  }

  // (8) testRevertLast
  @Test
  void testRevertLast() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("Tag 1");
    Tag tag2 = new Tag("Tag 2");
    image1.addTag(tag1);
    image1.addTag(tag2);
    assertTrue(image1.hasTag("Tag 1"));
    assertTrue(image1.hasTag("Tag 2"));
    image1.revertLast();
    assertTrue(image1.hasTag("Tag 1"));
    assertFalse(image1.hasTag("Tag 2"));
  }

  // (9) testGetName
  @Test
  void testGetName() throws IOException {
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("tag1");
    image1.addTag(tag1);
    assertEquals(fileName.substring(0, fileName.lastIndexOf(".")) + " @tag1", image1.getFullName());
  }

  // (10) testDeleteImage
  @Test
  void testDeleteImage() throws IOException {
    ImageDirectory dir1 = new ImageDirectory(directoryPath);
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    Tag tag1 = new Tag("tag1");
    image1.setDirectory(dir1);
    image1.addTag(tag1);
    image1.delete();
    assertFalse(tag1.contains(image1));
    assertFalse(image1.getDirectory() == dir1);
  }

}

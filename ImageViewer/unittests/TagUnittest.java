import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ImageProgram.ImageObject;
import ImageProgram.Tag;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TagUnittest extends ImageProgramTest {

  @BeforeAll
  public static void testPrepareAll() throws IOException {
    ImageProgramTest.directoryPath =
        (new File("unittests" + sp + "TagTestFiles" + sp)).getAbsolutePath() + sp;
    ImageProgramTest.fileName = "TagTest.jpg";
    ImageProgramTest.testPrepareAll();
  }

  // (1) testAutoAddToCollections
  @Test
  void testAutoAddToCollections() {
    Tag tag1 = new Tag("Tag 1");
    assertTrue(Tag.exists("Tag 1"));
  }

  // (2) testTagNameAndToString
  @Test
  void testTagNameAndToString() {
    Tag tag1 = new Tag("Tag 1");
    assertEquals("Tag 1", tag1.getTagName());
    assertEquals("@Tag 1", tag1.toString());
  }

  // (3) testTagNameAtChar
  @Test
  void testTagNameAtChar() {
    Tag tag1 = new Tag("@Tag");
    assertEquals("@Tag", tag1.getTagName());
    assertEquals("@@Tag", tag1.toString());
  }

  // (4) testTagNameEmptyString
  @Test
  void testTagNameEmptyString() {
    Tag tag1 = new Tag("");
    assertEquals("", tag1.getTagName());
    assertEquals("@", tag1.toString());
  }

  // (5) testAddImageToCollection
  @Test
  void testAddImageToCollection() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    tag1.addToCollection(image1);
    assertTrue(tag1.contains(image1));
  }

  // (6) testRemoveImageFromCollection
  @Test
  void testRemoveImageFromCollection() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    File file2 = new File(directoryPath + "2" + fileName);
    file2.createNewFile();
    ImageObject image1 = new ImageObject(file1);
    ImageObject image2 = new ImageObject(file2);
    tag1.addToCollection(image1);
    tag1.addToCollection(image2);
    assertTrue(tag1.contains(image1));
    assertTrue(tag1.contains(image2));
    tag1.removeFromCollection(image2);
    assertTrue(tag1.contains(image1));
    assertFalse(tag1.contains(image2));
  }

  // (7) testGetImagesOfTagNoImages
  @Test
  void testGetImagesOfTagNoImages() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    assertFalse(tag1.contains(image1));
  }

  // (8) testGetImagesOfTagOneImage
  @Test
  void testGetImagesOfTagOneImage() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    tag1.addToCollection(image1);
    assertTrue(tag1.contains(image1));
  }

  // (9) testGetImagesOfTagMultipleImages
  @Test
  void testGetImagesOfTagMultipleImages() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    File file2 = new File(directoryPath + "2" + fileName);
    file2.createNewFile();
    ImageObject image1 = new ImageObject(file1);
    ImageObject image2 = new ImageObject(file2);
    tag1.addToCollection(image1);
    tag1.addToCollection(image2);
    assertTrue(tag1.contains(image1));
    assertTrue(tag1.contains(image2));
  }

  // (10) testClearTag
  @Test
  void testClearTag() throws IOException {
    Tag tag1 = new Tag("Tag");
    File file1 = new File(directoryPath + fileName);
    ImageObject image1 = new ImageObject(file1);
    image1.addTag(tag1);
    assertTrue(tag1.contains(image1));
    assertTrue(image1.hasTag("Tag"));
    assertTrue(Tag.collectionOfTags.contains(tag1));
    tag1.clearTag();
    assertFalse(tag1.contains(image1));
    assertFalse(image1.hasTag("Tag"));
    assertFalse(Tag.collectionOfTags.contains(tag1));
  }

}

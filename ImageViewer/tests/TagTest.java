import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ImageProgram.ImageObject;
import ImageProgram.Tag;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TagTest extends ImageProgramTest {

  @BeforeAll
  public static void testPrepareAll() throws IOException {
    ImageProgramTest.directoryPath =
        (new File("tests" + sp + "testTagTest" + sp)).getAbsolutePath() + sp;
    ImageProgramTest.fileName = "TagTest.jpg";
    ImageProgramTest.testPrepareAll();
  }

  @Test
  void testCreateTag() {
    Tag t = new Tag("TestTag");
    assertTrue(Tag.exists("TestTag"));
    assertEquals("TestTag", t.getTagName());
    assertEquals("@TestTag", t.toString());
  }

  @Test
  void testRenameTag() {
    Tag t = new Tag("Name1");
    assertTrue(Tag.exists("Name1"));
    assertEquals("Name1", t.getTagName());
    assertEquals("@Name1", t.toString());
    t.setTagName("Name2");
    assertFalse(Tag.exists("Name1"));
    assertTrue(Tag.exists("Name2"));
    assertEquals("Name2", t.getTagName());
    assertEquals("@Name2", t.toString());
  }

  @Test
  void testCreateDuplicateTags() {
    Tag a = new Tag("Tag");
    boolean correct;
    try {
      Tag b = new Tag("Tag");
      correct = false;
    } catch (IllegalArgumentException e) {
      correct = true;
    }
    assertTrue(correct);
  }

  @Test
  void testRenameDuplicateTags() {
    Tag a = new Tag("TagA");
    Tag b = new Tag("TagB");
    boolean correct;
    try {
      b.setTagName("TagA");
      correct = false;
    } catch (IllegalArgumentException e) {
      correct = true;
    }
    assertTrue(correct);
  }

  @Test
  void testRenameTags() throws IOException {
    Tag a = new Tag("TagC");
    File f1 = new File(directoryPath + "File1.jpg");
    f1.createNewFile();
    File f2 = new File(directoryPath + "File2.jpg");
    f2.createNewFile();
    ImageObject image1 = new ImageObject(f1);
    ImageObject image2 = new ImageObject(f2);
    image1.addTag(a);
    image2.addTag(a);
    assertTrue(a.getImagesOfTag().contains(image1));
    assertTrue(image1.hasTag("TagC"));
    assertTrue(a.getImagesOfTag().contains(image2));
    assertTrue(image2.hasTag("TagC"));

    a.setTagName("TagG");

    assertTrue(a.getImagesOfTag().contains(image1));
    assertTrue(image1.hasTag("TagG"));
    assertTrue(a.getImagesOfTag().contains(image2));
    assertTrue(image2.hasTag("TagG"));

  }
}

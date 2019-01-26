import static org.junit.jupiter.api.Assertions.assertEquals;

import ImageProgram.Log;
import ImageProgram.Tag;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class LogTest {

  @Test
  void testLogNoChange() {
    Log log = new Log();
    assertEquals("", log.toString());
  }

  @Test
  void testLogOneChange() {
    Log log = new Log();
    ArrayList<Tag> tags = new ArrayList<Tag>();
    log.updateLog("old name", "new name", new ArrayList<Tag>(), new ArrayList<Tag>());
    assertEquals(1, log.size());
    assertEquals("old name", log.getJournal().get(0).getOldName());
    assertEquals("new name", log.getJournal().get(0).getNewName());
  }

  @Test
  void testLogMultipleChange() {
    Log log = new Log();
    ArrayList<Tag> tags = new ArrayList<Tag>();
    Tag tag = new Tag("apple");
    tags.add(tag);
    log.updateLog("old name", "new name", new ArrayList<Tag>(), tags);
    log.updateLog("new name", "newer name", tags, tags);
    assertEquals("old name", log.getJournal().get(0).getOldName());
    assertEquals("new name", log.getJournal().get(0).getNewName());
    assertEquals("new name", log.getJournal().get(1).getOldName());
    assertEquals("newer name", log.getJournal().get(1).getNewName());
  }

}

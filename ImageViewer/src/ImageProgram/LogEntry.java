package ImageProgram;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A LogEntry containing the changes from one state of an ImageObject to another.
 */
public class LogEntry implements Serializable {

  /**
   * The previous name of the ImageObject.
   */
  String oldName;
  /**
   * The new name of the ImageObject.
   */
  String newName;
  /**
   * The old Tags of the ImageObject.
   */
  ArrayList<Tag> oldTags;
  /**
   * The new Tags of the ImageObject.
   */
  ArrayList<Tag> newTags;
  /**
   * TimeStamp of this LogEntry.
   */
  Timestamp timeStamp;

  /**
   * Builds a new LogEntry.
   *
   * @param oldName The previous name of the ImageObject.
   * @param newName The new name of the ImageObject.
   * @param oldTags The old list of Tags that this ImageObject had.
   * @param newTags The new list of Tags that this ImageObject had.
   */
  LogEntry(String oldName, String newName, ArrayList<Tag> oldTags, ArrayList<Tag> newTags) {
    this.oldName = oldName;
    this.newName = newName;
    this.oldTags = oldTags;
    this.newTags = newTags;
    this.timeStamp = new Timestamp(System.currentTimeMillis());
  }

  /**
   * Returns whether this LogEntry is equivalent to the given LogEntry
   * @param l The LogEntry to compare this one to.
   * @return Whether the two LogEntries are equivalent.
   */
  boolean equals(LogEntry l){
    boolean equals = true;
    if (!this.oldName.equals(l.oldName)){
      equals = false;
    } else if (!this.newName.equals(l.newName)){
      equals = false;
    } else if (!this.oldTags.equals(l.oldTags)){
      equals = false;
    } else if (!this.newTags.equals(l.newTags)){
      equals = false;
    }
    return equals;
  }

  /**
   * Returns the old name of the ImageObject.
   *
   * @return Old name of the ImageObject.
   */
  public String getOldName() {
    return oldName;
  }

  /**
   * Returns the new name of the ImageObject.
   *
   * @return New name of the ImageObject.
   */
  public String getNewName() {
    return newName;
  }

  /**
   * Returns the old tags of the ImageObject.
   *
   * @return the old tags of the ImageObject.
   */
  public ArrayList<Tag> getOldTags() {
    return oldTags;
  }

  /**
   * Returns the new tags of the ImageObject.
   *
   * @return the new tags of the ImageObject.
   */
  public ArrayList<Tag> getNewTags() {
    return newTags;
  }

}

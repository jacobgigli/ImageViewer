package ImageProgram;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A log containing old names, new names and timestamps of images.
 */
public class Log implements Serializable {

  /**
   * A global Log of all naming changes that have occurred in the program.
   */
  public static Log globalLog = new Log();

  /**
   * A journal keeping track of all renaming of an image. Stores information on the old name, new
   * name, and timestamp.
   */
  ArrayList<LogEntry> journal;

  /**
   * Creates a new log for an image's renaming history.
   */
  public Log() {
    this.journal = new ArrayList<>();
  }

  /**
   * Updates journal to record most recent renaming of image.
   *
   * @param oldName The old name of the image.
   * @param newName The new name of the image.
   * @param oldTags Previous tags of the image.
   * @param newTags New tags of the image.
   */
  public void updateLog(String oldName, String newName, ArrayList<Tag> oldTags,
      ArrayList<Tag> newTags) {
    LogEntry newUpdate = new LogEntry(oldName, newName, oldTags, newTags);
    this.journal.add(newUpdate);
    // Prevent duplicate entries to the global log.
    if (globalLog.journal.size() > 0) {
      if (!newUpdate.equals(globalLog.journal.get(globalLog.journal.size() - 1))) {
        globalLog.journal.add(newUpdate);
      }
    } else {
      globalLog.journal.add(newUpdate);
    }
  }

  /**
   * clears Global Log.
   */
  static void clearGlobalClear() {
    globalLog = new Log();
  }

  /**
   * Return readable representation of contents of journal.
   *
   * @return A string representing contents of journal.
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    final String sep = System.lineSeparator();
    for (int i = 0; i < this.journal.size(); i += 1) {
      String timeStamp = this.journal.get(i).timeStamp.toString();
      String version = "Entry #" + String.valueOf(i) + ": ";

      result.append(version);
      result.append(this.journal.get(i).oldName);
      result.append(this.journal.get(i).oldTags);
      result.append(" --> ");
      result.append(this.journal.get(i).newName);
      result.append(this.journal.get(i).newTags);
      result.append(" @ ");
      result.append(timeStamp);
      result.append(sep);
    }
    return result.toString();
  }

  /**
   * Returns the size of this Log.
   *
   * @return The size of this Log.
   */
  public int size() {
    return this.journal.size();
  }

  /**
   * Removes the specified number of newest items from the Log.
   *
   * @param last The number of entries to remove.
   */
  void removeLast(int last) {
    for (int i = 0; i < last; i++) {
      this.journal.remove(this.journal.size() - 1);
    }
  }


  /**
   * Returns the global log.
   *
   * @return globalLog: the global log that is going to be returned.
   */
  static Log getGlobalLog() {
    return globalLog;
  }


  /**
   * Sets the global log.
   *
   * @param globalLog: Log that the global log will be set to.
   */
  static void setGlobalLog(Log globalLog) {
    Log.globalLog = globalLog;
  }

  /**
   * Returns the Journal of a log.
   *
   * @return Journal of the log that will be returned.
   */
  public ArrayList<LogEntry> getJournal() {
    return this.journal;
  }
}

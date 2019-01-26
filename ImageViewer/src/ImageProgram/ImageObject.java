package ImageProgram;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An ImageObject that can contain Tags, associated with an actual image file on the system.
 */
public class ImageObject implements Serializable {

    /**
     * The File object associated with this ImageObject.
     */
    private File file;
    /**
     * The file name of this ImageObject, without it's file extension or Tags.
     */
    private String name;
    /**
     * The file path of this ImageObject, excluding the file name and extension.
     */
    private String path;
    /**
     * The ImageDirectory that is associated with this ImageObject.
     */
    private ImageDirectory directory;
    /**
     * The Log containing all the changes that have been made to this ImageObject.
     */
    private Log log;
    /**
     * The file extension of this ImageObject Example: ".jpg".
     */
    private String extension;
    /**
     * The Tags that are associated with this ImageObject.
     */
    private ArrayList<Tag> tags = new ArrayList<>();

    /**
     * Stores whether this ImageObject is a favourite of the user's or not.
     */
    private boolean isFavourite;

    /**
     * Number of clicks this ImageObject has accumulated.
     */
    private int clicks;


    /**
     * Creates an ImageObject object associated with an actual image file.
     *
     * @param file the File that represents an actual image file on the computer
     * @throws IOException if the file does not exist
     */
    public ImageObject(File file)
            throws IOException {
        // Checks if the file actually exists on the computer.
        this.file = file;
        if (!file.exists()) {
            throw new IOException("ImageObject file does not exist!");
        }
        // Set the relative file path and full image name.
        this.path = file.getParentFile().getAbsolutePath() + File.separator;
        this.name = file.getName();
        // Separate the image name from it's extension.
        int i = this.name.lastIndexOf(".");
        if (i > 0) {
            this.extension = this.name.substring(i, this.name.length());
            this.name = this.name.substring(0, i);
        } else { // If the file has no extension
            this.extension = "";
        }

        // Initialize new Log associate ImageObject with it's ImageDirectory.
        this.log = new Log();
        // TODO: Might not be necessary because ImageObjects are never created in isolation and so will always have a parent directory.
        String directoryPath = file.getParentFile().getAbsolutePath();
        boolean dirExists = false;
        for (ImageDirectory id : ImageManager.collectionOfImageDir) {
            if (id.getFilePath().equals(directoryPath)) {
                this.directory = id;
                dirExists = true;
            }
        }
        if (!dirExists) {
            this.directory = new ImageDirectory(directoryPath);
        }


    }

    /**
     * Returns the name of this ImageObject, without it's tags.
     *
     * @return the name of this ImageObject without it's tags
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes the name of this ImageObject to newName.
     * The file on the system is updated to the changes.
     *
     * @param newName the new name to change to
     * @throws IOException if changing the name will cause a file to be overwritten
     */
    public void setName(String newName) throws IOException {
        if (!name.equals(newName)) {
            if (!directory.containsImage(newName)) {
                String oldName = this.name;
                this.name = newName;
                this.log
                        .updateLog(oldName, newName, new ArrayList<>(this.tags), new ArrayList<>(this.tags));
            } else {
                throw new IllegalArgumentException(
                        "An Image of that name already exists in this directory.");
            }
        }
        this.update();
    }

    /**
     * Returns path name of this ImageObject.
     *
     * @return String representation of this ImageObject's file path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Returns ImageDirectory associated with this ImageObject.
     *
     * @return ImageDirectory associated with this ImageObject
     */
    public ImageDirectory getDirectory() {
        return this.directory;
    }

    /**
     * Sets a new ImageDirectory for this ImageObject. The file path on the system is updated
     * appropriately.
     *
     * @param newDirectory the new ImageDirectory for this ImageObject to be associated with
     * @throws IOException if moving the file involves overwriting
     */
    public void setDirectory(ImageDirectory newDirectory) throws IOException {
        if (!(this.directory == newDirectory) && !(newDirectory.containsImage(this.getName()))) {
            this.directory = newDirectory;
            newDirectory.addImage(this);
            this.path = directory.getFilePath() + File.separator;
            this.update();
        }
    }

    /**
     * Returns Log associated with this ImageObject.
     *
     * @return Log assoicated with this ImageObject
     */
    public Log getLog() {
        return this.log;
    }

    /**
     * Gets the full name of this ImageObject, with all of the associated Tags,
     * and without it's file path or extension.
     *
     * @return the full name of this ImageObject
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(this.name);
        if (tags.size() > 0) {
            fullName.append(" ");
            fullName.append(this.getTags());
        }
        return fullName.toString();
    }

    /**
     * Returns the full file path of this ImageObject's file, including it's extension and Tags.
     *
     * @return the full file path of this ImageObject
     */
    public String getFullPath() {
        return this.path + this.getFullName() + this.extension;
    }

    /**
     * Returns a shortened full file path of this ImageObject's file.
     *
     * @return a shortened full file path of this ImageObject's file
     */
    String getShortPath() {
        // Substring of this ImageObject's path between the 2nd last slash to the last slash.
        String shortPath = this.path
                .substring(this.path.lastIndexOf(File.separator, this.path.lastIndexOf(File.separator) - 1),
                        this.path.length());
        return shortPath + this.getName();
    }

    /**
     * Returns the String representation of the Tags associated with this ImageObject.
     *
     * @return the String representation of the Tags in this ImageObject
     */
    public String getTags() {
        StringBuilder builder = new StringBuilder();
        for (Tag t : tags) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(t.toString());
        }
        return builder.toString();
    }

    /**
     * Returns the Tag with the given name that is associated with this ImageObject.
     *
     * @param name the name of the Tag
     * @return the Tag matching the name
     * @throws IllegalArgumentException if no Tag by the given name is found in this ImageObject
     */
    public Tag getTagString(String name) throws IllegalArgumentException {
        for (Tag tag : tags) {
            if (tag.getTagName().equals(name)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("No Tag by that name is in this Image!");
    }

    /**
     * Returns the ArrayList of Tag for this ImageObject.
     *
     * @return ArrayList of Tag for this ImageObject's tags.
     * @see Tag
     */
    ArrayList<Tag> getTagObjects() {
        return new ArrayList<>(tags);
    }

    /**
     * Returns whether this ImageObject has a Tag with the specified name.
     *
     * @param name the name of the Tag to search for
     * @return whether this ImageObject has a Tag of the given name
     */
    public boolean hasTag(String name) {
        boolean contains = false;
        for (Tag t : tags) {
            if (t.getTagName().equals(name)) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Updates the actual file of the image on the computer to match any changes made to this
     * ImageObject.
     *
     * @throws IOException if the new file path already exists or the current file is not able to be
     *                     changed
     */
    private void update() throws IOException {
        // If the new file path already exists, don't overwrite it.
        File newFile = new File(this.getFullPath());
        if (newFile.exists()) {
            throw new IOException("File already exists.");
        }
        // The file path change operation may fail for other reasons, like the image currently being
        // opened by another application
        boolean success = this.file.renameTo(newFile);
        if (!success) {
            throw new IOException("File was not able to be changed.");
        }
        this.file = newFile;
    }

    /**
     * Adds the new Tag newTag to this ImageObject.
     *
     * @param newTag the new Tag to be added to this ImageObject
     * @throws IOException              if overwriting an existing file
     * @throws IllegalArgumentException if the Tag already has this Image
     */
    public void addTag(Tag newTag) throws IOException, IllegalArgumentException {
        boolean valid = true;
        for (Tag t : tags) {
            if (t.getTagName().equals(newTag.getTagName())) {
                valid = false;
            }
        }
        if (tags.contains(newTag)) {
            valid = false;
        }
        if (valid) {
            ArrayList<Tag> oldTags = new ArrayList<>(this.tags);
            tags.add(newTag);
            this.log.updateLog(this.name, this.name, oldTags, new ArrayList<>(this.tags));
            this.update();
            newTag.addToCollection(this);
        } else {
            throw new IllegalArgumentException("Tag already exists in this ImageObject");
        }
    }

    /**
     * Removes the association between Tag tag and this ImageObject.
     *
     * @param tag the Tag object to remove from thisImageObject
     * @throws IOException              if overwriting an existing file
     * @throws IllegalArgumentException if the Tag does not exist in the ImageObject
     */
    public void removeTag(Tag tag) throws IOException, IllegalArgumentException {
        if (tags.contains(tag)) {
            ArrayList<Tag> oldTags = new ArrayList<>(this.tags);
            tags.remove(tag);
            this.log.updateLog(this.name, this.name, oldTags, new ArrayList<>(this.tags));
            this.update();
            tag.removeFromCollection(this);
        } else {
            throw new IllegalArgumentException("Tag not found in ImageObject.");
        }
    }

    /**
     * Reverts this ImageObject to the specified index in the Log.
     *
     * @param index the index of the change to revert to
     * @throws IOException              if the new file name could not be written
     * @throws IllegalArgumentException if the index is out of the range of the Log
     */
    public void revertChanges(int index) throws IOException, IllegalArgumentException {
        LogEntry change = this.log.journal.get(index);
        String currName = this.name;
        ArrayList<Tag> currTags = new ArrayList<>(this.tags);
        for (Tag t : tags) {
            t.removeFromCollection(this);
        }
        // Adds this ImageObject to it's new Tags.
        for (Tag t : change.oldTags) {
            t.addToCollection(this);
            // If the Tag has been deleted from the static collection of Tags, add it back.
            if (!Tag.collectionOfTags.contains(t)) {
                Tag.collectionOfTags.add(t);
            }
        }
        this.tags = new ArrayList<>(change.oldTags);
        this.setName(change.oldName); // Will also call update method and updateLog method
        if (!currName.equals(change.oldName)) { // If there is a name change
            this.log.removeLast(1); // Remove the extra log entry from setName
        }
        this.log.updateLog(currName, this.name, currTags, new ArrayList<>(this.tags));
    }

    /**
     * Reverts this ImageObject to it's previous state. The last LogEntry in it's Log is also
     * removed.
     *
     * @throws IOException if the file name could not be written
     */
    public void revertLast() throws IOException {
        if (this.log.size() > 0) {
            this.revertChanges(this.log.size() - 1);
            // Remove both the reversion log entry and last log entry.
            this.log.removeLast(2);
        }
    }

    /**
     * Reads the name of this ImageObject and makes sure that all Tags
     * are included in its tags.
     */
    public void readName() {
        String[] a = name.split(" @");
        ArrayList<String> newTags = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                // The name of the image without it's tags.
                name = a[i];
            } else {
                newTags.add(a[i]);
            }
        }
        for (String tagName : newTags) {
            Tag t = Tag.getTag(tagName);
            if (!tags.contains(t)) {
                tags.add(t);
                t.addToCollection(this);
            }
        }
    }

    /**
     * Returns number of tags this ImageObject has associated with it.
     *
     * @return number of tags this ImageObject has
     */
    public int getNumberOfTags() {
        return this.tags.size();
    }


    /**
     * Makes this ImageObject a favourite of the user.
     */
    public void setFavourite() {
        this.isFavourite = true;
    }

    /**
     * Makes this ImageObject not a favourite of the user.
     */
    public void unFavourite() {
        this.isFavourite = false;
    }

    /**
     * Return whether this ImageObject is a favourite of the user.
     *
     * @return whether this ImageObject is a favourite of the user
     */
    public boolean isFavourite() {
        return this.isFavourite;
    }

    /**
     * Increases the number of clicks an Image has by 1.
     */
    public void increaseClicks() {
        this.clicks = this.clicks + 1;
    }

    /**
     * Return the number of clicks an Image has accumulated.
     *
     * @return number of clicks an Image has accumulated
     */
    public int getClicks() {
        return this.clicks;
    }


    /**
     * Returns String representation of contents of ImageObject.
     *
     * @return String representation of this ImageObject
     */
    public String toString() {
        return "\nName: " + this.name + "\nParent Directory: " + this.directory.getFilePath()
                + "\nFile Path: " + this.getFullPath()
                + "\nTags: " + this.tags + "\nLog: " + this.log.toString();
    }

    /**
     * Deletes this ImageObject from the program. To be used if the image that this ImageObject is
     * associated with no longer exists on system.
     */
    public void delete() {
        this.directory.removeImage(this);
        this.directory = null;
        for (Tag t : tags) {
            t.removeFromCollection(this);
        }
        this.tags = new ArrayList<>();
    }

    /**
     * Return the list of tag names associated with this ImageObject.
     *
     * @return list of tag names associated with this ImageObject
     */
    public ArrayList<String> getListTagNames() {
        ArrayList<String> tagList = new ArrayList<>();
        for (Tag t : this.getTagObjects()) {
            tagList.add(t.getTagName());
        }
        return tagList;
    }

    /**
     * Return file associated with this ImageObject.
     *
     * @return file associated with this ImageObject
     */
    public File getFile() {
        return file;
    }
}

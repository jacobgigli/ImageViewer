package ImageProgram;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Tag for an ImageObject
 */
public class Tag implements Serializable {

    /**
     * Collection of all Tags.
     */
    public static ArrayList<Tag> collectionOfTags = new ArrayList<>();
    /**
     * Collection of Images that have this Tag.
     */
    private ArrayList<ImageObject> collectionOfImageObject = new ArrayList<>();
    /**
     * The name of this Tag.
     */
    private String name;

    /**
     * Create new Tag with the given name.
     *
     * @param name the name of this Tag
     * @throws IllegalArgumentException if the Tag name is already taken by another Tag
     */
    public Tag(String name) throws IllegalArgumentException {
        for (Tag t : collectionOfTags) {
            if (t.getTagName().equals(name)) {
                throw new IllegalArgumentException("That Tag name is already taken by another Tag.");
            }
        }
        this.name = name;
        collectionOfTags.add(this);
    }

    /**
     * Returns whether a Tag with the given tagName exists.
     *
     * @param tagName the name to identify the Tag
     * @return whether the Tag with name tagName exists or not
     */
    public static boolean exists(String tagName) {
        boolean exists = false;
        for (Tag t : collectionOfTags) {
            if (t.name.equals(tagName)) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Returns the Tag that has the given tagName. If one isn't found, a new Tag is created with the
     * given name and returned.
     *
     * @param tagName the name of the Tag to either find or create
     * @return the Tag with the given name, either by finding it or creating it
     */
    public static Tag getTag(String tagName) {
        for (Tag t : collectionOfTags) {
            if (t.name.equals(tagName)) {
                return t;
            }
        }
        return new Tag(tagName);
    }

    /**
     * Returns name of this Tag.
     *
     * @return String representation of tag name
     */
    public String getTagName() {
        return name;
    }

    /**
     * Changes the name of this Tag to given name.
     *
     * @param name The new name for the Tag
     * @throws IllegalArgumentException if the Tag name is already taken by another Tag
     */
    public void setTagName(String name) throws IllegalArgumentException {
        for (Tag t : collectionOfTags) {
            if (t.name.equals(name)) {
                throw new IllegalArgumentException("That Tag name is already taken by another Tag.");
            }
        }
        this.name = name;
    }

    /**
     * Add ImageObject img to the collection of ImageObjects.
     *
     * @param img ImageObject to add to collection of ImageObjects
     * @throws IllegalArgumentException if the Tag name is already taken by another Tag
     */
    public void addToCollection(ImageObject img) throws IllegalArgumentException {
        if (collectionOfImageObject.contains(img)) {
            throw new IllegalArgumentException("That Image already exists in the collection!");
        } else {
            collectionOfImageObject.add(img);
        }
    }

    /**
     * Remove ImageObject from the collection of ImageObjects.
     *
     * @param img ImageObject to add to collection of ImageObjects
     */
    public void removeFromCollection(ImageObject img) {
        collectionOfImageObject.remove(img);
    }

    /**
     * Returns whether ImageObject img is associated with this Tag.
     *
     * @param img The ImageObject to check for association with this Tag
     * @return whether the ImageObject is associated with this Tag
     */
    public boolean contains(ImageObject img) {
        return this.collectionOfImageObject.contains(img);
    }

    /**
     * Returns the String representation of this Tag.
     *
     * @return The String representation of this Tag
     */
    public String toString() {
        return "@" + this.name;
    }

    /**
     * Returns an ArrayList of ImageObjects that are associated with this Tag.
     *
     * @return all the ImageObjects associated with this Tag
     */
    public ArrayList<ImageObject> getImagesOfTag() {
        return new ArrayList<>(collectionOfImageObject);
    }

    /**
     * Clear this Tag from all images it references and from the collectionOfTags.
     */
    public void clearTag() {
        ArrayList<ImageObject> temp = new ArrayList<>(collectionOfImageObject);
        for (ImageObject image : temp) {
            if (image.hasTag(this.getTagName())) {
                try {
                    image.removeTag(this);
                } catch (IOException io) {
                    System.out.println("IOException.");
                }
            }
        }
        this.collectionOfImageObject.clear();
        collectionOfTags.remove(this);
    }

    /**
     * Return list of all tag names from collection of all Tags.
     *
     * @return list of all tag names in collectionOfTags
     */

    public static ArrayList<String> getListOfTags() {
        ArrayList<String> tagList = new ArrayList<>();
        for (Tag t : collectionOfTags) {
            tagList.add(t.getTagName());
        }
        return tagList;
    }

    /**
     * Return the list of the names of all ImageObjects associated with
     * this tag.
     *
     * @return all ImageObject names that have this tag
     */
    public ArrayList<String> getListOfImages() {
        ArrayList<String> imageNameList = new ArrayList<>();
        for (ImageObject img : this.getImagesOfTag()) {
            imageNameList.add(img.getName());
        }
        return imageNameList;
    }
}

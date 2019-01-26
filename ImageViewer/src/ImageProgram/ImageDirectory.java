package ImageProgram;

import ImageSorting.AlphabeticalComparator;
import ImageSorting.FavouriteComparator;
import ImageSorting.TagsComparator;
import ImageSorting.ViewsComparator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An ImageDirectory that holds ImageObjects and subDirectories.
 */
public class ImageDirectory implements Serializable {

    /**
     * The File representing the folder on the system that this ImageDirectory represents.
     */
    private File folder;

    /**
     * The filepath of this ImageDirectory.
     */
    private String filePath;

    /**
     * List of imageObjects in this ImageDirectory.
     */
    private ArrayList<ImageObject> imageObjects = new ArrayList<>();

    /**
     * List of ImageDirectories that are under this ImageDirectory.
     */
    private ArrayList<ImageDirectory> subDirectories = new ArrayList<>();

    /**
     * Construct this directory with the filepath of this ImageDirectory.
     *
     * @param folder: The File object representing this ImageDirectory's folder on the system.
     * @throws IOException if the folder does not exist on the system or an ImageDirectory of this
     *                     name already exists
     */
    public ImageDirectory(File folder) throws IOException {
        this.folder = folder;

        if (!folder.exists()) {
            throw new IOException("This folder does not exist on the system.");
        }
        for (int i = 0; i < ImageManager.collectionOfImageDir.size(); i++) {
            if (ImageManager.collectionOfImageDir.get(i).getFilePath().equals(folder.getAbsolutePath())) {
                throw new IOException("A ImageDirectory of this name already exists.");
            }
        }
        this.filePath = folder.getAbsolutePath();
        ImageManager.addImageDirectory(this);
    }

    /**
     * Construct this directory with the filepath of this ImageDirectory.
     *
     * @param filePath: The String representing this ImageDirectory's file path on the system.
     * @throws IOException if the file could not be created
     */
    public ImageDirectory(String filePath) throws IOException {
        this(new File(filePath));
    }

    /**
     * Updates this ImageDirectory's folder on the system to match any changes made.
     *
     * @throws IOException if the folder already exists or the folder was not able to be changed
     */
    private void update() throws IOException {
        File newFolder = new File(this.getFilePath());
        if (newFolder.exists()) {
            throw new IOException("Folder already exists.");
        }
        boolean success = this.folder.renameTo(newFolder);
        if (!success) {
            throw new IOException("Folder was not able to be changed.");
        }
        this.folder = newFolder;
    }

    /**
     * Scans this ImageDirectory's folder on the system for images and creates ImageObjects to
     * represent them. Also recursively go through any sub-folders, creating ImageDirectories to
     * represent those if they don't already exist and recursively processing their contents.
     *
     * @throws IOException if the folder already exists or the folder was not able to be changed
     */
    public void scan() throws IOException {
        // If an Image no longer exists on the system, delete it from the program.
        ArrayList<ImageObject> io = new ArrayList<>(imageObjects);
        for (ImageObject i : io) {
            if (!i.getFile().exists()) {
                i.delete();
            }
        }

        File[] files = this.folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    // Check if the image already has an ImageObject representation in this ImageDirectory.
                    boolean exists = false;
                    for (ImageObject image : imageObjects) {
                        if (image.getFullPath().equals(f.getAbsolutePath())) {
                            exists = true;
                        }
                    }
                    // If not, create an ImageObject representation and add it to the collection.
                    if (!exists) {
                        ImageObject image = new ImageObject(f);
                        imageObjects.add(image);
                        image.setDirectory(this);
                        // Process the Image file's name and Tags.
                        image.readName();
                    }
                } else if (f.isDirectory()) {
                    // If it is a directory, and an ImageDirectory doesn't already exist for it, make one.
                    if (!this.containsDirectory(f.getAbsolutePath())) {
                        ImageDirectory newID = new ImageDirectory(f);
                        subDirectories.add(newID);
                        newID.scan();
                    } else { // Scan all existing sub-directories for any changes.
                        ImageDirectory id = this.getImageDirectory(f.getAbsolutePath());
                        if (!(id == null)) {
                            id.scan();
                        }
                    }
                }
            }
        }
    }

    /**
     * Return the name of the file path of this ImageDirectory.
     *
     * @return name of the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the the file path of this ImageDirectory to newFilePath.
     *
     * @param newFilePath: the new path of this image directory
     * @throws IOException if the folder already exists or the folder was not able to be changed
     */
    public void setFilePath(String newFilePath) throws IOException {
        this.filePath = newFilePath;
        this.update();
    }

    /**
     * Add this ImageObject to the collection of imageObjects of this directory.
     *
     * @param imageObject ImageObject to be added to the collection of images of this directory
     */
    void addImage(ImageObject imageObject) {
        if (!containsImage(imageObject)) {
            this.imageObjects.add(imageObject);
        }
    }

    /**
     * Remove this ImageObject from the ImageObject directory.
     *
     * @param imageObject ImageObject to be removed from ImageObject directory
     */
    void removeImage(ImageObject imageObject) {
        if (containsImage(imageObject)) {
            this.imageObjects.remove(imageObject);
        }
    }

    /**
     * Returns a String representation of all the images in this ImageDirectory.
     *
     * @return String representation of the ImageObjects contained within this ImageDirectory
     */
    private String getImageObjects() {
        StringBuilder builder = new StringBuilder();
        for (ImageObject imageObject : imageObjects) {
            builder.append(imageObject.toString());
        }
        return builder.toString();
    }

    /**
     * Returns the collection of ImageObjects within this directory and its sub-directories.
     * <p>
     * a@return All ImageObjects contained within and below this ImageDirectory
     */
    public ArrayList<ImageObject> getImageObjectsList() {
        ArrayList<ImageObject> newList = new ArrayList<>(imageObjects);
        for (ImageDirectory id : subDirectories) {
            newList.addAll(id.getImageObjectsList());
        }
        return newList;
    }

    /**
     * Returns list of ImageObjects built from ImageObjects of this ImageDirectory.
     *
     * @return list of ImageObjects built from ImageObjects of this ImageDirectory
     */
    ArrayList<ImageObject> getDirectImageObjectsList() {
        return new ArrayList<>(imageObjects);
    }

    /**
     * Returns the ImageObject that has the given name, returns null if such an ImageObject does not
     * exist.
     *
     * @param name The name of this ImageObject.
     * @return the ImageObject with given name
     */
    public ImageObject getImage(String name) {
        for (ImageObject image : imageObjects) {
            if (image.getName().equals(name)) {
                return image;
            }
        }
        for (ImageDirectory id : subDirectories) {
            if (id.containsImage(name)) {
                return id.getImage(name);
            }
        }
        return null;
    }

    /**
     * Returns if the ImageObject in contained in this ImageDirectory.
     *
     * @param imageObject ImageObject to check for
     * @return whether ImageDirectory contains imageObject
     */
    private boolean containsImage(ImageObject imageObject) {
        return imageObjects.contains(imageObject);
    }

    /**
     * Returns whether this ImageDirectory contains ImageObject with the given name. Also
     * searches recursively through the sub-directories as well.
     *
     * @param name the name to check for
     * @return the ImageObject with given name
     */
    public boolean containsImage(String name) {
        boolean contains = false;
        for (ImageObject i : imageObjects) {
            if (i.getName().equals(name)) {
                contains = true;
            }
        }
        if (!contains) {
            for (ImageDirectory id : subDirectories) {
                if (id.containsImage(name)) {
                    contains = true;
                }
            }
        }
        return contains;
    }

    /**
     * Returns the ImageDirectory of the given directoryName. Returns null if it can't be found. Will
     * recursively search sub-directories as well.
     *
     * @param directoryName the name of the ImageDirectory to find
     * @return the ImageDirectory being searched for
     */
    private ImageDirectory getImageDirectory(String directoryName) {
        for (ImageDirectory id : subDirectories) {
            if (id.getFilePath().equals(directoryName)) {
                return id;
            } else if (id.containsDirectory(directoryName)) { // Recursive Call.
                return id.getImageDirectory(directoryName);
            }
        }
        return null;
    }

    /**
     * Returns whether this ImageDirectory contains a ImageDirectory of the given directoryName. Will
     * recursively search sub-directories as well.
     *
     * @param directoryName the name of the ImageDirectory to find
     * @return whether the ImageDirectory can be found
     */
    private boolean containsDirectory(String directoryName) {
        boolean contains = false;
        for (ImageDirectory id : subDirectories) {
            if (id.getFilePath().equals(directoryName)) {
                contains = true;
            } else if (id.containsDirectory(directoryName)) { // Recursive Call.
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Sorts the Image Directory and its subdirectories by the number of tags each image has. Note:
     * The sorting of the subdirectories in independent of the other subdirectories.
     */
    public void sortTagNum() {
        imageObjects.sort(new TagsComparator());
        for (ImageDirectory id : subDirectories) {
            id.sortTagNum();
        }
    }

    /**
     * Sorts the Image Directory and its subdirectories in alphabetical order of its images. Note: The
     * sorting of the subdirectories in independent of the other subdirectories.
     */
    public void sortAlphabetNum() {
        imageObjects.sort(new AlphabeticalComparator());
        for (ImageDirectory id : subDirectories) {
            id.sortAlphabetNum();
        }

    }

    /**
     * Sorts the Image Directory and its subdirectories in order of the user's favourites. Note: The
     * sorting of the subdirectories in independent of the other subdirectories.
     */
    public void sortFavourite() {
        imageObjects.sort(new FavouriteComparator());
        for (ImageDirectory id : subDirectories) {
            id.sortFavourite();
        }

    }

    /**
     * Sorts the Image Directory and its subdirectories in order of the number of views each image has.
     * Note: The sorting of the subdirectories in independent of the other subdirectories.
     */
    public void sortViews() {
        imageObjects.sort(new ViewsComparator());
        for (ImageDirectory id : subDirectories) {
            id.sortViews();
        }

    }


    /**
     * Returns a String representation of this ImageDirectory containing it's file path, and String
     * representations of all it's ImageObjects. Also iterate through subdirectories recursively and
     * get their String representations.
     *
     * @return String representation of this ImageDirectory
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getFilePath());
        builder.append("\n");
        builder.append(this.getImageObjects());
        for (ImageDirectory subDirectory : subDirectories) {
            builder.append("\n");
            builder.append(subDirectory.toString());
        }
        return builder.toString();
    }

    /**
     * Return a list of all image names in this directory.
     *
     * @return List of all image names in this directory
     */
    public ArrayList<String> getImageNames() {
        ArrayList<String> imageNameList = new ArrayList<>();
        for (ImageObject img : this.imageObjects) {
            imageNameList.add(img.getShortPath());
        }
        for (ImageDirectory id : subDirectories) {
            imageNameList.addAll(id.getImageNames());
        }
        return imageNameList;
    }
}

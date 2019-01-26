package ImageSorting;

import ImageProgram.ImageObject;
import java.util.Comparator;

/**
 * A Comparator that compares alphabetical order of names of images
 */

public class AlphabeticalComparator implements Comparator<ImageObject> {

  /**
   * Compares its two arguments for order.
   *
   * Returns a positive integer if name of image1 comes alphabetically before name of image2.
   * Returns 0 if name of image1 and name of image2 are alphabetically equivalent. Returns a
   * negative integer if name of image2 comes alphabetically before name of image1.
   *
   * @param image1 the first image with name to compare
   * @param image2 the second name with name to compare
   * @return - a positive integer if name of image1 comes alphabetically before name of image2. - 0
   * if name of image1 and name of image2 are alphabetically equivalent. - a negative integer if
   * name of image2 comes alphabetically before name of image1.
   */
  public int compare(ImageObject image1, ImageObject image2) {
    String imageName1 = image1.getName().toUpperCase();
    String imageName2 = image2.getName().toUpperCase();
    return imageName1.compareTo(imageName2);

  }
}

package ImageSorting;

import ImageProgram.ImageObject;
import java.util.Comparator;

/**
 * A Comparator that compares images based on the number of tags they have
 */

public class TagsComparator implements Comparator<ImageObject> {

  /**
   * Compares its two arguments for order.
   *
   * Returns a negative integer, zero, or a positive integer as image2 is less than, equal to, or
   * greater than image1 in terms of number of tags.
   *
   * @param image1 the first number of tags to compare
   * @param image2 the second number of tags to compare
   * @return a negative integer, zero, or a positive integer as image2 is less than, equal to, or
   * greater than image1.
   */
  @Override
  public int compare(ImageObject image1, ImageObject image2) {
    return image2.getNumberOfTags() - image1.getNumberOfTags();

  }
}

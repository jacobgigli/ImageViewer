package ImageSorting;

import ImageProgram.ImageObject;
import java.util.Comparator;

/**
 * A Comparator that compares images based on the number of views they have
 */

public class ViewsComparator implements Comparator<ImageObject> {

  /**
   * Compares its two arguments for order.
   *
   * Returns a negative integer, zero, or a positive integer as image2 is less than, equal to, or
   * greater than image1 in terms of number of views.
   *
   * @param image1 the first number of views to compare
   * @param image2 the second number of views to compare
   * @return a negative integer, zero, or a positive integer as image2 is less than, equal to, or
   * greater than image1.
   */
  public int compare(ImageObject image1, ImageObject image2) {
    return image2.getClicks() - image1.getClicks();
  }
}
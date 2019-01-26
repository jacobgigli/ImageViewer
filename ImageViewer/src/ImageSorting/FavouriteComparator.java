package ImageSorting;

import ImageProgram.ImageObject;
import java.util.Comparator;

/**
 * A Comparator that compares images based on if they are favourited or not
 */

public class FavouriteComparator implements Comparator<ImageObject> {

  /**
   * Compares its two arguments for order. Returns 1 if image2 is favourited and image1 is not
   * Returns 0 if both image1 and image2 are favourited or not favourited Returns -1 if image 1 is
   * favourited and image2 is not
   *
   * @param image1 the first number of tags to compare
   * @param image2 the second number of tags to compare
   * @return - (1) if image2 is favourited and image1 is not - (0) if both image1 and image2 are
   * favourited or not favourited - (-1) if image 1 is favourited and image2 is not
   */
  public int compare(ImageObject image1, ImageObject image2) {
    int image1Int = (image1.isFavourite()) ? 1 : 0;
    int image2Int = (image2.isFavourite()) ? 1 : 0;
    return image2Int - image1Int;
  }

}

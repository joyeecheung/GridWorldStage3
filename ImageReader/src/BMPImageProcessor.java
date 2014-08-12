import imagereader.IImageProcessor;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

/**
 * The image processor for BMP image.
 *
 * @author joyeecheung
 *
 */
public class BMPImageProcessor implements IImageProcessor
{

    /**
     * Get the gray scale image.
     *
     * @param sourceImage
     *            the source image.
     */
    @Override
    public Image showGray(Image sourceImage)
    {
        return Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(
                        sourceImage.getSource(),
                        new GrayFilter()));
    }

    /**
     * Get the red channel image.
     *
     * @param sourceImage
     *            the source image.
     */
    @Override
    public Image showChanelR(Image sourceImage)
    {
        return Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(
                        sourceImage.getSource(),
                        new ChannelFilter(ChannelFilter.RED_MASK)));
    }

    /**
     * Get the green channel image.
     *
     * @param sourceImage
     *            the source image.
     */
    @Override
    public Image showChanelG(Image sourceImage)
    {
        return Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(
                        sourceImage.getSource(),
                        new ChannelFilter(ChannelFilter.GREEN_MASK)));
    }

    /**
     * Get the blue channel image.
     *
     * @param sourceImage
     *            the source image.
     */
    @Override
    public Image showChanelB(Image sourceImage)
    {
        return Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(
                        sourceImage.getSource(),
                        new ChannelFilter(ChannelFilter.BLUE_MASK)));
    }
}

/**
 * The filter class for obtaining channel images.
 *
 * @author joyeecheung
 *
 */
class ChannelFilter extends RGBImageFilter
{
    // the color mask
    private int mask;

    // predefined masks
    public static final int RED_MASK = 0xFFFF0000;
    public static final int GREEN_MASK = 0xFF00FF00;
    public static final int BLUE_MASK = 0xFF0000FF;

    /**
     * Construct a channel filter with given mask.
     *
     * @param mask
     *            mask for each pixel.
     */
    public ChannelFilter(int mask)
    {
        this.mask = mask;
        // From Java API ---
        // The filter's operation does not depend on the
        // pixel's location, so IndexColorModels can be
        // filtered directly.
        canFilterIndexColorModel = true;
    }

    /**
     * Convert a single input pixel in the default RGB
     * ColorModel to a filtered pixel.
     *
     * @param x
     *            the X coordinate of the pixel
     * @param y
     *            the Y coordinate of the pixel
     * @param rgb
     *            the integer pixel representation in the default RGB color
     *            model
     * @return a filtered pixel in the default RGB color model.
     */
    @Override
    public int filterRGB(int x, int y, int rgb)
    {
        return rgb & mask;
    }
}

/**
 * The filter class for obtaining gray scale images.
 *
 * @author joyeecheung
 *
 */
class GrayFilter extends RGBImageFilter
{
    // masks to extract each channel
    private static final int RED_ONLY_MASK = 0x00FF0000;
    private static final int GREEN_ONLY_MASK = 0x0000FF00;
    private static final int BLUE_ONLY_MASK = 0x000000FF;
    // weight for each channel to get gray scale
    private static final double RED_WEIGHT = 0.299;
    private static final double GREEN_WEIGHT = 0.587;
    private static final double BLUE_WEIGHT = 0.114;

    /**
     * Construct a gray scale filter with given mask.
     *
     * @param mask
     *            mask for each pixel.
     */
    public GrayFilter()
    {
        // From Java API ---
        // The filter's operation does not depend on the
        // pixel's location, so IndexColorModels can be
        // filtered directly.
        canFilterIndexColorModel = true;
    }

    /**
     * Convert a single input pixel in the default RGB
     * ColorModel to a gray scale pixel.
     *
     * @param x
     *            the X coordinate of the pixel
     * @param y
     *            the Y coordinate of the pixel
     * @param rgb
     *            the integer pixel representation in the default RGB color
     *            model
     * @return a gray scale pixel in the default RGB color model.
     */
    @Override
    public int filterRGB(int x, int y, int rgb)
    {
        int red = (RED_ONLY_MASK & rgb) >> 16;
        int green = (GREEN_ONLY_MASK & rgb) >> 8;
        int blue = BLUE_ONLY_MASK & rgb;
        int gray = (int) (red * RED_WEIGHT
                + green * GREEN_WEIGHT
                + blue * BLUE_WEIGHT);
        return (rgb & 0xFF000000) | (gray << 16) | (gray << 8) | gray;
    }
}

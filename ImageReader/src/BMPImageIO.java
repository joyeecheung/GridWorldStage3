import imagereader.IImageIO;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * ImageIO class for BMP images.
 *
 * @author joyeecheung
 *
 */
public class BMPImageIO implements IImageIO
{
    // transparency
    private static final int ALPHA = 0xFF << 24;
    // size of file header
    private static final int F_HEADER_SIZE = 14;
    // size of DIB header
    private static final int DIB_HEADER_SIZE = 40;
    // default bit per pixel
    private static final int DEFAULT_BPP = 24;
    // count of red, green, blue = 3
    private static final int RGB_COUNT = 3;

    /**
     * @param filepath
     *            the path to the BMP image to be read.
     */
    @Override
    public Image myRead(String filepath) throws IOException
    {
        BufferedInputStream inputStream = new BufferedInputStream(
                new FileInputStream(filepath));

        // first 14 bytes containing file information
        byte[] fileHeader = new byte[F_HEADER_SIZE];
        inputStream.read(fileHeader, 0, F_HEADER_SIZE);
        int size = readBytes(fileHeader, 2, 4);

        // next 40 bytes containing image information
        byte[] dibHeader = new byte[DIB_HEADER_SIZE];
        inputStream.read(dibHeader, 0, DIB_HEADER_SIZE);

        int width = readBytes(dibHeader, 4, 4);
        int height = readBytes(dibHeader, 8, 4);

        // get the bits per pixel, which is 24 for this test
        int bitsPerPx = readBytes(dibHeader, 14, 2);

        if (bitsPerPx == DEFAULT_BPP)
        {
            // padding for each line
            int padding = (size / height) - width * RGB_COUNT;

            // pixels of the file
            int[] pixels = new int[height * width];
            byte[] rgbs = new byte[size];
            inputStream.read(rgbs, 0, size);

            // get rgb for each pixel
            // reading starts from bottom-left
            // while the array starts from top-left
            int index = 0;
            for (int i = height - 1; i >= 0; i--)
            {
                for (int j = 0; j < width; j++)
                {
                    // the rgb for each pixel is each 3 bytes | alpha
                    pixels[i * width + j] = ALPHA | readBytes(rgbs, index, RGB_COUNT);
                    index += RGB_COUNT;
                }
                index += padding;
            }

            inputStream.close();
            return Toolkit.getDefaultToolkit().createImage(
                    new MemoryImageSource(width, height, pixels, 0, width));
        }
        else
        {
            inputStream.close();
            throw new IOException("Bits per pixel is not 24");
        }
    }

    /**
     * @param image
     *            the image to be written
     * @param filename
     *            the file name of the output image
     */
    @Override
    public Image myWrite(Image image, String fileName) throws IOException
    {
        BufferedImage bufferedImage = toBufferedImage(image);
        ImageIO.write(bufferedImage, "bmp", new File(fileName));
        return image;
    }

    /**
     * Read in given number of bytes, from given offset in the byte array,
     * convert it to int, then return the integer.
     *
     * @param buffer
     *            the byte array as the data source.
     * @param offset
     *            where the reading starts
     * @param size
     *            number of bytes to read
     * @return the converted integer
     */
    private int readBytes(byte[] buffer, int offset, int size)
    {
        int result = 0;
        for (int i = 0; i < size; ++i)
        {
            result |= (buffer[offset + i] & 0x00FF) << i * 8;
        }

        return result;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img
     *            The Image to be converted
     * @return The converted BufferedImage
     */
    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}

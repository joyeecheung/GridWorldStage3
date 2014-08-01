import imagereader.IImageIO;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * ImageIO class for BMP images.
 * 
 * @author joyeecheung
 *
 */
public class BMPImageIO implements IImageIO
{
    // transparency
    private static final int ALPHA = (int) 0xFF << 24;

    // info of the image
    private int size;
    private int width;
    private int height;
    private int bitsPerPx;

    // first 14 bytes containing file information
    private byte[] fileHeader;
    // next 40 bytes containing image information
    private byte[] DIBHeader;
    // pixels of the file
    private int[] pixels;

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
            result |= ((int) buffer[offset + i] & 0x00FF) << i * 8;
        }

        return result;
    }

    /**
     * @param filepath
     *            the path to the BMP image to be read.
     */
    @Override
    public Image myRead(String filepath) throws IOException
    {
        BufferedInputStream inputStream = new BufferedInputStream(
                new FileInputStream(filepath));

        fileHeader = new byte[14];
        // read the image fileHeader
        inputStream.read(fileHeader, 0, 14);
        size = readBytes(fileHeader, 2, 4);

        DIBHeader = new byte[40];
        inputStream.read(DIBHeader, 0, 40);

        width = readBytes(DIBHeader, 4, 4);
        height = readBytes(DIBHeader, 8, 4);

        // get the bits per pixel, which is 24 for this test
        bitsPerPx = readBytes(DIBHeader, 14, 2);

        if (bitsPerPx == 24)
        {
            // padding for each line
            int padding = (size / height) - width * 3;

            // get rgb for each pixel
            // reading starts from bottom-left
            // while the array starts from top-left
            pixels = new int[height * width];
            byte[] rgbs = new byte[size];
            inputStream.read(rgbs, 0, size);

            int index = 0;
            for (int i = height - 1; i >= 0; i--)
            {
                for (int j = 0; j < width; j++)
                {
                    // the rgb for each pixel is each 3 bytes | alpha
                    pixels[i * width + j] = ALPHA | readBytes(rgbs, index, 3);
                    index += 3;
                }
                index += padding;
            }
        }
        else
        {
            inputStream.close();
            throw new IOException("Bits per pixel is not 24");
        }

        inputStream.close();

        return Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(width, height, pixels, 0, width));
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

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics graphic = bufferedImage.createGraphics();
        graphic.drawImage(image, 0, 0, null);
        graphic.dispose();

        ImageIO.write(bufferedImage, "bmp", new File(fileName));

        return image;
    }

}

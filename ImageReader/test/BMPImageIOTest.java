import junit.framework.TestCase;

import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.*;

@RunWith(Parameterized.class)
public class BMPImageIOTest
{
    private BufferedImage source;
    private BufferedImage result;
    private FileInputStream srcStream;
    private FileInputStream resStream;

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]
        {
                { "bmptest/result/1.bmp", "bmptest/1.bmp" },
                { "bmptest/result/2.bmp", "bmptest/2.bmp" }
        });
    }

    /**
     * Set up test resources.
     */
    public BMPImageIOTest(String resPath, String srcPath)
    {
        BMPImageIO imageio = new BMPImageIO();
        try
        {
            // read source with myRead
            result = toBufferedImage(imageio.myRead(srcPath));

            // write the image with myWrite
            File resFile = new File(resPath);
            if (!resFile.exists())
            {
                resFile.createNewFile();
            }
            imageio.myWrite(result, resPath);

            // read source with ImageIO.read of std lib
            source = ImageIO.read(new FileInputStream(srcPath));

            // create input stream for source and written image
            srcStream = new FileInputStream(srcPath);
            resStream = new FileInputStream(resPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check if the myRead can read as the standard library utilities.
     */
    @Test
    public void readTest()
    {
        int width = result.getWidth(null);
        int height = result.getHeight(null);
        assertEquals("The width of the read image changed!",
                width, source.getWidth(null));
        assertEquals("The height of the read image changed!",
                height, source.getHeight(null));

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                assertEquals("Pixel in (" + i + ", " + j + ") changed",
                        result.getRGB(i, j), source.getRGB(i, j));
            }
        }
    }

    /**
     * Check if the myWrite can read as the standard library utilities.
     * @throws IOException 
     * @throws ArrayComparisonFailure 
     */
    @Test
    public void WriteTest() throws ArrayComparisonFailure, IOException
    {        
        final int BUFFER_SIZE = 1024;
        byte[] srcBuffer = new byte[BUFFER_SIZE];
        byte[] resBuffer = new byte[BUFFER_SIZE];

        // skip the header
        srcStream.skip(54);
        resStream.skip(54);
        while ((srcStream.read(srcBuffer, 0, BUFFER_SIZE)) != -1) {
            resStream.read(resBuffer, 0, BUFFER_SIZE);
            assertArrayEquals("The written file changed.", srcBuffer, resBuffer);
        }
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

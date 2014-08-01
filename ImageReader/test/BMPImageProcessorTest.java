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
public class BMPImageProcessorTest
{
    private BufferedImage goal;
    private BufferedImage result;
    private FileInputStream goalStream;
    private FileInputStream resStream;
    private String fileName;

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]
        {
                { "bmptest/result/1_blue_goal.bmp", "bmptest/goal/1_blue_goal.bmp", "bmptest/1.bmp" },
                { "bmptest/result/1_red_goal.bmp", "bmptest/goal/1_red_goal.bmp", "bmptest/1.bmp" },
                { "bmptest/result/1_green_goal.bmp", "bmptest/goal/1_green_goal.bmp", "bmptest/1.bmp" },
                { "bmptest/result/1_gray_goal.bmp", "bmptest/goal/1_gray_goal.bmp", "bmptest/1.bmp" },
                { "bmptest/result/2_blue_goal.bmp", "bmptest/goal/2_blue_goal.bmp", "bmptest/2.bmp" },
                { "bmptest/result/2_red_goal.bmp", "bmptest/goal/2_red_goal.bmp", "bmptest/2.bmp" },
                { "bmptest/result/2_green_goal.bmp", "bmptest/goal/2_green_goal.bmp", "bmptest/2.bmp" },
                { "bmptest/result/2_gray_goal.bmp", "bmptest/goal/2_gray_goal.bmp", "bmptest/2.bmp" },
        });
    }

    public BMPImageProcessorTest(String resPath, String goalPath, String srcPath)
    {
        this.fileName = resPath;

        BMPImageIO imageio = new BMPImageIO();
        BMPImageProcessor processor = new BMPImageProcessor();

        try
        {
            Image original = imageio.myRead(srcPath);

            // create the result file if it doesn't exist
            File resFile = new File(resPath);
            if (!resFile.exists())
            {
                resFile.createNewFile();
            }

            // get the filetered result according to the name of the goal
            if (goalPath.contains("gray"))
            {
                result = toBufferedImage(processor.showGray(original));
            }
            else if (goalPath.contains("red"))
            {
                result = toBufferedImage(processor.showChanelR(original));
            }
            else if (goalPath.contains("green"))
            {
                result = toBufferedImage(processor.showChanelG(original));
            }
            else if (goalPath.contains("blue"))
            {
                result = toBufferedImage(processor.showChanelB(original));
            }

            imageio.myWrite(result, resPath);
            goal = ImageIO.read(new FileInputStream(goalPath));
            goalStream = new FileInputStream(goalPath);
            resStream = new FileInputStream(resPath);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check if the width and the height are the same as the goal
     */
    @Test
    public void widthAndHeightTest()
    {
        int width = result.getWidth(null);
        int height = result.getHeight(null);
        assertEquals(fileName + ": The width of the read image changed!",
                width, goal.getWidth(null));
        assertEquals(fileName + ": The height of the read image changed!",
                height, goal.getHeight(null));
    }

    /**
     * Check if rgb values are the same as the goal
     */
    @Test
    public void RGBTest()
    {
        int width = result.getWidth(null);
        int height = result.getHeight(null);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                assertEquals(fileName + ": Pixel in (" + i + ", " + j + ") changed",
                        result.getRGB(i, j), goal.getRGB(i, j));
            }
        }
    }
 
 
    /**
     * Check if the pixels are the same as the goal
     * @throws IOException 
     * @throws ArrayComparisonFailure 
     */
    @Test
    public void rawFileTest() throws ArrayComparisonFailure, IOException
    {        
        final int BUFFER_SIZE = 1024;
        byte[] srcBuffer = new byte[BUFFER_SIZE];
        byte[] goalBuffer = new byte[BUFFER_SIZE];

        goalStream.skip(54);
        resStream.skip(54);
        while ((goalStream.read(srcBuffer, 0, BUFFER_SIZE)) != -1) {
            resStream.read(goalBuffer, 0, BUFFER_SIZE);
            assertArrayEquals(fileName + ": The written file changed.", srcBuffer, goalBuffer);
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

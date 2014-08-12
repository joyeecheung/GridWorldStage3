import imagereader.Runner;

/**
 * Runner for the IO and processor.
 */
public class ImageReaderRunner
{
    /**
     * Main method, inject dependencies into the runner.
     */
    public static void main(String[] args)
    {
        BMPImageIO imageio = new BMPImageIO();
        BMPImageProcessor processor = new BMPImageProcessor();
        Runner.run(imageio, processor);
    }
}

import imagereader.Runner;

public class ImageReaderRunner
{
    public static void main(String[] args)
    {
        BMPImageIO imageio = new BMPImageIO();
        BMPImageProcessor processor = new BMPImageProcessor();
        Runner.run(imageio, processor);
    }
}

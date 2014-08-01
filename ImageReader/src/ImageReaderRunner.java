import imagereader.Runner;

public class ImageReaderRunner
{
    public static void main(String[] args)
    {
        BMPImageIO imageio = new BMPImageIO();
        BMPImageProcesser processor = new BMPImageProcesser();
        Runner.run(imageio, processor);
    }
}

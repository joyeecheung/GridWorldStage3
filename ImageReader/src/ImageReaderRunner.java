import imagereader.Runner;

public class ImageReaderRunner {
	
	public static void main(String[] args) {
		
		MyImageIO imageio = new MyImageIO();
		MyImageProcesser processor = new MyImageProcesser();
		Runner.run(imageio, processor);
	}
}

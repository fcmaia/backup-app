package scale;

import org.mozilla.universalchardet.UniversalDetector;

public class Guess {

	/**
	 * @author fcmaia - 06/02/2012 20:16:15
	 * @param args
	 */
	public static void main( String[] args ) {
	    String fileName = args[0];
	    
	    UniversalDetector detector = new UniversalDetector(null);

	    detector.handleData(fileName.getBytes(), 0, fileName.length());
	    detector.dataEnd();

	    String encoding = detector.getDetectedCharset();
	    if (encoding != null) {
	      System.out.println("Detected encoding = " + encoding);
	    } else {
	      System.out.println("No encoding detected.");
	    }

	    detector.reset();
	}

}

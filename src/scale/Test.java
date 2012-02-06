package scale;

import java.io.UnsupportedEncodingException;

public class Test {

	/**
	 * @author fcmaia - 06/02/2012 20:02:30
	 * @param args
	 */
	public static void main( String[] args ) {
		
		String fromEnc = args[0];
		String toEnc = args[1];
		
		try {
			String s = new String( args[2].getBytes( fromEnc ), toEnc);
			System.out.println(s);
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
	}

}

/**
 * @author fcmaia - 21/12/2011 10:27:13
 */
package scale;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author fcmaia - 21/12/2011 10:27:13
 * 
 */
public class Copy {

	private static String destination = ".";
	private static String currentSourcePath = "";
	
	private static boolean quickMode = false;
	private static long lastModified7Days = 0L;
	
	/**
	 * @author fcmaia - 21/12/2011 10:27:13
	 * @param args
	 */
	public static void main( String[] args ) {

		if ( args.length < 2 ) {
			System.err.println( "Use: scale.Backup <src>... <destination>" );
			return;
		}

		ArrayList<String> opts = new ArrayList<String>();
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith( "-" )) {
				if ("-q".equals( args[i] )) {
					quickMode = true;
					
					Calendar cal = Calendar.getInstance();
					cal.add( Calendar.DAY_OF_MONTH, -7 );
					lastModified7Days = cal.getTimeInMillis();
				}
			} else {
				opts.add( args[i] );
			}
		}
		
		destination = new File( opts.remove( opts.size() - 1 ) ).getAbsolutePath();

		copy( opts );
	}

	private static void copy( List<String> srcs ) {

		File df = new File( destination );
		if ( !df.exists() ) {
			df.mkdirs();
		}
		
		for ( String src : srcs ) {
			if (src == null) continue;
			
			try {
				String s = new String(src.getBytes( "UTF-8" ));
				currentSourcePath = new File(s).getAbsolutePath();
				copy( src );
			} catch ( UnsupportedEncodingException e ) {
				e.printStackTrace();
			}
		}
	}

	private static void copy( String source ) {

		File sf = new File( source );

		if ( !sf.exists() ) {
			System.out.println( "Skipping ... " + source );
			return;
		}
		
		if ( sf.isFile() ) {
			String sourcePath = sf.getAbsolutePath();
			String targetPath = destination + sourcePath.substring( currentSourcePath.length() ); 
			
			File targetDir = new File( targetPath ).getParentFile();
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			
			//System.out.println("" + sourcePath + " ----> " + targetPath);
			try {
				write( new File(sourcePath), new File(targetPath) );
			} catch ( IOException e ) {
				System.err.println("ERRO: " + e.getMessage() + ": " + sourcePath);
			}
		}
		
		if (sf.isDirectory()) {
			String[] sf_paths = sf.list();
			for (int i = 0; i < sf_paths.length; i++) {
				if (".".equals( sf_paths[i] )) continue;
				copy( sf.getAbsolutePath() + File.separator + sf_paths[i] );
			}
		}
	}

	private static void write( File fromFile, File toFile ) throws IOException {

		if (quickMode) {
			if (fromFile.lastModified() < lastModified7Days) {
				return;
			}
		}
		
		if (toFile.exists() && fromFile.lastModified() <= toFile.lastModified()) return;
		
		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream( fromFile );
			to = new FileOutputStream( toFile );
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ( ( bytesRead = from.read( buffer ) ) != -1 )
				to.write( buffer, 0, bytesRead ); // write
			
			System.out.println("" + fromFile.getAbsolutePath() + " to " + toFile.getAbsolutePath() );
		} finally {
			if ( from != null ) from.close();
			if ( to != null )   to.close();
			
			toFile.setLastModified( fromFile.lastModified() );
		}
	}
}

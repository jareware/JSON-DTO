package jsondto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility methods for the JSON-DTO module.
 * 
 * @author Jarno Rantanen <jarno@jrw.fi>
 */
public abstract class Util {

	public static final String CHARSET = "UTF-8";

	/**
	 * Helper method for converting a stream to a string.
	 * 
	 * @throws IOException
	 */
	public static String readStream(InputStream input) throws IOException {

		if (input == null)
			return "";

		String linefeed = System.getProperty("line.separator");
		StringBuilder stringBuilder = new StringBuilder();
		String currentLine;

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(input, CHARSET));

			while ((currentLine = reader.readLine()) != null)
				stringBuilder.append(currentLine).append(linefeed);

		} finally {

			input.close();

		}

		return stringBuilder.toString();

	}

}
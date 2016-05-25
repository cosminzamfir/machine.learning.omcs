package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MLUtils {

	public static String readFromConsole(String message) {
		if (message != null) {
			System.out.print(message);
		}

		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			return bufferRead.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<String> readLines(InputStream is) {
		BufferedReader reader = null;
		try {
			List<String> res = new ArrayList<String>();
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				res.add(line);
			}
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignore) {
			}
		}
	}
	
	public static List<String> readLines(String resourceName) {
			return readLines(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
	}

	public static String readFromConsole(String message) {
		System.out.print(message + " = ");

		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			return bufferRead.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T randomElem(List<T> l, List<T> used) {
		T elem = l.get((int) (Math.random() * l.size()));
		if (l.size() == used.size()) {
			throw new RuntimeException("No more elements");
		}
		while (true) {
			if (!used.contains(elem)) {
				used.add(elem);
				return elem;
			}
			elem = l.get((int) (Math.random() * l.size()));
		}
	}

}

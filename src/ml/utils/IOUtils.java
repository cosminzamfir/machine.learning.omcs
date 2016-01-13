package ml.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IOUtils {

	private static final String COMMENT = "#";

	public static void writeLine(BufferedWriter writer, String s) {
		try {
			writer.write(s);
			writer.newLine();
		} catch (Exception e) {
			throw new RuntimeException("IO exception in writeLine", e);
		}
	}

	/**
	 * Write the given String to the class-path resource 
	 */
	public static void writeToResource(Class<?> caller, String resource, String s, boolean append) {
		String fileName;
		try {
			fileName = caller.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
					.replace("target/classes/", resource).replace("target/test-classes", resource);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid resource name specified:" + resource);
		}
		writeToFile(fileName, s, append);
	}

	/**
	 * Write the given String to given file name 
	 */
	public static void writeToFile(String fileName, String s, boolean append) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
			writer.write(s);
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception writing to file:" + fileName, e);
		}
	}

	public static void clearResource(Class<?> caller, String resource) {
		writeToResource(caller, resource, "", false);
	}

	public static List<String> readLines(String fileName, boolean ignoreComment) {
		BufferedReader reader = null;
		try {
			List<String> res = new ArrayList<String>();
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName); 
			if(is == null) {
				throw new RuntimeException("No resource with name " + fileName + " found in classpath");
			}
			
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (ignoreComment && line.startsWith(COMMENT)) {
					continue;
				}
				if (!line.isEmpty()) {
					res.add(line);
				}
			}
			return res;
		} catch (Exception e) {
			throw new RuntimeException("Error reading file" + fileName, e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignore) {
			}
		}
	}
}

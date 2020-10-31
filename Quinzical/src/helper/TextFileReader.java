package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// helper class to read from files when starting game
public class TextFileReader {
	
	/**
	 * Read from a file the saved game data so that progress
	 * can be retained
	 * @param file the name of the file to read data from
	 */
	public static List<String> read(File file) {
		List<String> lines = new ArrayList<String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (IOException ex) {
			
		}
		return lines;
	}
}
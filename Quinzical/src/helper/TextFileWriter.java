package helper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

// helper class to write to files when exiting game
public class TextFileWriter {
	
	/**
	 * Create a file and write to it the game data so that progress
	 * can be saved
	 * @param file the name of the file to create
	 * @param field the field to save
	 * @param array the array to save (one element of the array on each line)
	 */
    public static void write(String file, String field, ArrayList<String> array) throws IOException {

        // write balance and answered questions to their respective files
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) { 
                    if (field != null) {
                        writer.write("" + field); 
                    }
                    else {
                        for (String string : array) {
                            writer.write(string + "\n");
                        }
                    }
        
        } catch (IOException e) {

        }
	}
}
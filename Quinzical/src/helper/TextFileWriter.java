package helper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

// helper class to write to files when exiting game
public class TextFileWriter {

    public static void write(String filename, String balance, ArrayList<String> answeredQuestions) throws IOException {

        // write balance and answered questions to their respective files
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) { 
                    if (balance != null) {
                        writer.write("" + balance); 
                    }
                    else {
                        for (String string : answeredQuestions) {
                            writer.write(string + "\n");
                        }
                    }
        
        } catch (IOException e) {

        }
	}
}
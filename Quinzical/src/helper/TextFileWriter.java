package helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

// helper class to write to files when exiting game
public class TextFileWriter {

    public static void write(String filename, Integer balance, ArrayList<String> answeredQuestions) throws IOException {

        File file = new File(filename);
        // if the file doesn't exist, create it
        if (!file.exists()) {
            FileWriter fWriter = new FileWriter(file);
            PrintWriter pWriter = new PrintWriter(fWriter);
        }

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
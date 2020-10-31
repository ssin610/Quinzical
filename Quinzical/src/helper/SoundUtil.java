package helper;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SoundUtil {
	
	/**
	 * Play a celebratory sound when the user gets a question correct
	 */
	public static void playSound(Alert a) {
		String cmd = "./aha.sh>/dev/null 2>&1";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			builder.start();
		} catch (IOException e) {
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("Audio System Crash");
			a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
		}
	}
	
	/**
	 * Use spd-say to speak the sentence using a worker thread
	 * @param sentence the sentence to speak
	 */
	public static void speak(String sentence, String speed, Alert a) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say --wait -r" + speed + " \"" + sentence + "\"";
				ProcessBuilder ttsBuilder = new ProcessBuilder("bash", "-c", cmd);
				try {
					ttsBuilder.start();
				} catch (IOException e) {
					e.printStackTrace();
					a.setAlertType(AlertType.ERROR);
					// show the dialog
					a.show();
					a.setHeaderText("Audio System Crash");
					a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
				}
			}
		};
		thread.setName("thread1");
		thread.start();
	
	}
}

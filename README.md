	   
# Run the game
To run the game, please go into the Quinzical/ folder and then excute the run.sh

	Please type ./run.sh in the terminal in Quinzical/

# Important
In your first play, the game will lag a little bit, due to setting resources and file readings.
We Thank you for your paitient!	



# File setup
Quinzical ----- backup/		(All question files as backup)
 	   ----  bin/
 	   ----- cat/ 			(Please Put all question txt files in this folder, following the same formate as the original files)
 	   ----- src/			(All project code is in this folder)
 	   ----- build.fxbuild	
 	   ----- run.sh 		(To excute the game, run this script)
 	   ----- (All other files generated during the game are not recommanded to be modified)

 	   
# Troubleshoot
- If the game cannot start properly, please open the run.sh in Qunzical/ and do the following:
	Change the following line:
		java --module-path /usr/share/java/lib --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar Quinzical.jar
	To 
		java --module-path PATH --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar Quinzical.jar
	Where the PATH is the path of where your javafx lib folder is located

 - Audio(spd-say) issue:
 	Run command: "sudo apt-get install speech-dispatcher" in the terminal to install spd-say
 	
 - Nothing display (or No response) during the game:
 	Please close the game and delete the following files and start the game again
 		Quinzical/addedCategories.txt, 
 		Quinzical/addedQuestions.txt, 
 		Quinzical/answeredQuestions.txt
 		Quinzical/winnigs.txt
 		
 - Fatal Error window telling you to check file arrangement:
 	Please close the game and make sure all question files are in the formate of "xxx,xxx,xxx" (question,answer,value)
 	Then save the modified question file and start again.

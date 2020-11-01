	   
# Run the game (You can choose any method to excute)
(1) Open the terminal in the assignment-3-and-project-team-01-master folder and type the following line:
			./run

(2) Go into the Quinzical/ folder and then excute the run.sh
	Please type ./run.sh in the terminal in Quinzical/

# Important
In your first play through, the game may lag a little due to setting resources and reading files.
We thank you for your patience!

# File setup

assignment-3-and-project-team-01-master/

	--- User Manual.pdf 	(Instructions about the game, PLEASE READ IT !!!)
	
	--- README.md 	
	
	--- run	(To start the app, run this bash script)
	
	--- Quinzical/ (The main game source folder, PLEASE DO NOT DELETE ANY FILE IN HERE!!!)
 	   	----- cat/ 		(Please put all question txt files in this folder, following the same format as the original files. Ensure there is an 'International' file)
 	   	----- src/		(All project code is in this folder)
 	   	----- run.sh 		(Run this script to execute the game)
		----- aha.sh 		(Sound effect used in the game)
		----- Quinzical.jar (Contains the app which gets executed by the script)
 	   	----- (All other files generated during the game are not recommanded to be modified)

 	   
# Troubleshooting

- Error Messeage: bash: ./run: Permission denied
	Please open the terminal and type: chmod +x run

- If the game cannot start properly, please open the run.sh in Quinzical/ and do the following:
	Change the following line:
		java --module-path /usr/share/java/lib --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar Quinzical.jar
	To 
		java --module-path PATH --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar Quinzical.jar
	Where the PATH is the path of where your javafx lib folder is located

 - Audio(spd-say) issue:
 	Run command: "sudo apt-get install speech-dispatcher" in the terminal to install spd-say
 	
 - Nothing displayed (or No response) during the game:
 	Please close the game and delete the following files and start the game again
 		Quinzical/addedCategories.txt, 
 		Quinzical/addedQuestions.txt, 
 		Quinzical/answeredQuestions.txt
 		Quinzical/winnigs.txt
 		
 - Fatal Error window telling you to check file arrangement:
 	Please close the game and make sure all question files are in the formate of "xxx,xxx,xxx" (question,answer,value)
 	Then save the modified question file and start again.

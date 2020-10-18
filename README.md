	   
# Run the game
To run the game, please go into the Quinzical/ folder and then excute the run.sh

	Please type ./run.sh in the terminal in Quinzical/
	


# File setup
Quinzical ----- backup/		(All question files as backup)
 	   ----  bin/
 	   ----- cat/ 			(Please Put all question txt files in this folder, following the same formate as the original files)
 	   ----- src/			(All project code is in this folder)
 	   ----- build.fxbuild	
 	   ----- run.sh 		(To excute the game, run this script)
 	   ----- (All other files generated during the game are not recommanded to be modified)

 	   
# Troubleshoot
 - Audio(spd-say) issue:
 	Run command: "sudo apt-get install speech-dispatcher" in the terminal to install spd-say
 	
 - Nothing display (or No response) during the game:
 	Please close the game and delete the following files and start the game again
 		Quinzical/addedCategories.txt, 
 		Quinzical/addedQuestions.txt, 
 		Quinzical/answeredQuestions.txt

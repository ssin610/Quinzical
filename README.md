	   
# Run the game
To run the game, please go into the Quinzical/ folder and then excute the run.sh

	Please type ./run.sh in the terminal in Quinzical/


# File setup
Quinzical ----- backup/		(All question files as backup)
 	   ----  bin/
 	   ----- cat/ 			(All question files ,might be modified)
 	   ----- src/
 	   	---- application/     
 	   
 	   -----build.fxbuild	

 	   
# Troubleshoot
 - Audio(spd-say) issue:
 	Run command: "sudo apt-get install speech-dispatcher" in the terminal to install spd-say
 	
 - Nothing display in Games Mode:
 	Please close the game and delete the following txt files and start the game again
 		addedCategories.txt, 
 		addedQuestions.txt, 
 		answeredQuestions.txt

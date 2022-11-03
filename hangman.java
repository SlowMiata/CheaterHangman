import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class hangman {

    /**
     * create word lists
     **/
    public static Map<Integer, List<String>> createWordLists() throws FileNotFoundException {
        Map<Integer, List<String>> wordLists = new HashMap<>();
        String fileName = "words.txt";
        Scanner read = new Scanner(new File(fileName));

        while (read.hasNextLine()) {
            String word = read.nextLine().strip().toLowerCase();
            int key = word.length();
            if (!wordLists.containsKey(key)) {

                List<String> list = new ArrayList<>();//creates a new key for the word
                wordLists.put(key, list);
            }
            wordLists.get(key).add(word); //adds the word into the key
        }

        return wordLists;
    }

    /**
     * create word families
     **/
    public static Map<String, List<String>> createWordFamilies(List<String> wordList, Set<Character> guessed) throws FileNotFoundException {


        Map<String, List<String>> wordFamilies = new HashMap<>();

        for (String word : wordList) {
            String family = findWordFamily(word, guessed);
            if (!wordFamilies.containsKey(family)) {
                List<String> list = new ArrayList<>();
                wordFamilies.put(family, list);
            }
            wordFamilies.get(family).add(word);

        }


        return wordFamilies;
    }

    //  figure out the word family for the string
    public static String findWordFamily(String word, Set<Character> guessed) {
        String family = "";

        for (char c : word.toCharArray()) {
            if (guessed.contains(c)) {
                family += c;
            } else {
                family += '_';
            }

        }


        return family;
    }

    //get best family
    public static String getBestFamily(Map<String, List<String>> wordFamilies) {
        //choose the largest family


        String bestKey = "";
        int bestKeySize = 0;

        for (String word : wordFamilies.keySet()) {
            int KeySize = wordFamilies.get(word).size();
            if (KeySize > bestKeySize) {
                bestKey = word;
                bestKeySize = KeySize;
                ;
            }

        }


        return bestKey;


    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("how many letters would you like");
        int letters = input.nextInt();

        while (!(createWordLists().containsKey(letters))) {
            System.out.println("There are no words that length\n");
            System.out.println("how many letters would you like");
            letters = input.nextInt();
        }
        //scanner 2 for guesses
        Scanner tries = new Scanner(System.in);
        System.out.println("how many guesses would you like?");
        int guesses = tries.nextInt();//for dealing with  how many guesses they want
        int attempts = 0;
        boolean play = true;


        Scanner wordGuesses = new Scanner(System.in);
        Scanner Again = new Scanner(System.in);


        while (play) {
            //initialize the variables
            Set resultSet = new HashSet();//outside loop
            List<String> wordList = createWordLists().get(letters);
            Map<String, List<String>> wordFamilies = createWordFamilies(wordList, resultSet);//outside loop
            String newGameState = getBestFamily(wordFamilies);//outside loop

            while (attempts < guesses) {
                //code for actually playing the game
                System.out.println("guess a letter");//in loop
                char guessed = wordGuesses.next().toLowerCase().charAt(0);


                while (resultSet.add(guessed) == false) {//already guessed the word
                    System.out.println("you have already entered this letter, please enter a different letter");
                    guessed = wordGuesses.next().toLowerCase().charAt(0);
                }

                wordList = wordFamilies.get(newGameState);//in loop // get the best family
                wordFamilies = createWordFamilies(wordList, resultSet);//in loop // get the best family with after the guess
                newGameState = getBestFamily(wordFamilies); // update the best family for next guess
                System.out.println(findWordFamily(newGameState, resultSet)); // prints the first guess
                System.out.println(resultSet); // prints the guesses
                System.out.println("you have " + (guesses - attempts - 1) + " guesses left"); // attempts remaining

                if (!findWordFamily(newGameState, resultSet).contains("_")) { // if they guessed the word
                    break;

                }
                else if (!findWordFamily(newGameState, resultSet).contains(String.valueOf(guessed))){
                    attempts++;
                }



            }
                if (findWordFamily(newGameState, resultSet).contains("_")) {
                    System.out.println("the word was " + wordFamilies.get(newGameState).get(1));
                    System.out.println("you lose");
                }else{
                    System.out.println("the word was " + wordFamilies.get(newGameState));
                    System.out.println("you win");
                }





            //do they want to play again?
            String Replay;
           do {
               System.out.println("do you want to play again? (yes) (no)");
               Replay = Again.nextLine();



               if (Replay.equalsIgnoreCase("yes")) {
                    main(args);

                } else if(Replay.equalsIgnoreCase("no")) {
                    play = false;
            }

            }
            while (!(Replay.equalsIgnoreCase("yes")||Replay.equalsIgnoreCase("no")));

        }
    }

}



//8 letters
/*
E - wrong
A - wrong
I - yes
N - yes
G - yes

 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Hangman 
{
	public static String chosenWord = "";
	public static boolean wordChosen = false;
	public static boolean gameWon = false;
	public static int lengthChoice = 0;
	public static int tries = 0;
	
	public static void newGame(List<String> list)
	{	
		List<Character> guessed = new ArrayList<Character>();
		List<Character> validGuesses = new ArrayList<Character>();
		
		while (tries > 0)
		{
			System.out.println("\n*********************\n");
			
			//print board
			System.out.println("\nCURRENT BOARD: ");
			newBoard(validGuesses);
			
			if (!wordChosen)
			{
				System.out.println(list.size() + " possible words: " + list);
			}
			else
			{
				//System.out.println("Hidden word: " + chosenWord);
			}
			System.out.println("Guessed letters: " + guessed);
			System.out.println("Valid guesses: " + validGuesses);
			System.out.println("Tries left: " + tries);
			
			char guess = getGuess(guessed);
			list = modifyList(list, validGuesses, guess);
			if (checkGameStatus(validGuesses))
			{
				tries = 0;
			}
			tries--;
		}
		
		//if ran out of tries and no correct letter was found, choose a random word & pretend it was the word all along
		if (!wordChosen)
		{
			Random randomizer = new Random();
			chosenWord = list.get(randomizer.nextInt(list.size()));
		}
	}
	
	public static Character getGuess(List<Character> guessed)
	{
		Scanner kb = new Scanner(System.in);
		System.out.println("Make a guess: ");
		String input = kb.nextLine();
		input.replaceAll("\\d", "");
		
		if (input.length() != 1 || !checkGuess(guessed, input.charAt(0)))
		{
			System.out.println("\n(!) INVALID OR DUPLICATE GUESS. TRY AGAIN.");
			getGuess(guessed);
		}
		guessed.add(input.charAt(0));
		return input.charAt(0);
	}
	
	public static boolean checkGuess(List<Character> guessed, char guess)
	{
		//makes sure guess is valid
		for (int i = 0; i < guessed.size(); i++)
		{
			if (guessed.get(i) == guess)
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkGameStatus(List<Character> validGuesses)
	{
		//if whole word guessed, returns true & player wins game; otherwise returns false
		int correct = 0;
		if (chosenWord == "")
		{
			return false;
		}
		else
		{
			for (int i = 0; i < chosenWord.length(); i++)
			{
				for (int j = 0; j < validGuesses.size(); j++)
				if (chosenWord.charAt(i) == validGuesses.get(j))
				{
					correct++;
				}
			}
		}
		if (correct == chosenWord.length())
		{
			gameWon = true;
			return true;
		}
		return false;
	}

	public static List<String> makeList(List<String> list)
	{
		try
		{
			Scanner fileScanner = new Scanner(new File("words.txt"));
			while (fileScanner.hasNextLine()) 
			{
				String line = fileScanner.nextLine();
				String[] words = line.split("\\s+");
				for (String word : words) 
				{
					word = word.toLowerCase();
					if(word.length() == lengthChoice)
					{
						list.add(word);
					}
				}
			}
			fileScanner.close();
		}
		catch (FileNotFoundException exception) 
		{
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		//if no words of this length found, pick another
		if (list.size() == 0)
		{
			Scanner kb = new Scanner(System.in);
			System.out.println("No words of length " + lengthChoice);
			System.out.println("Select another word size: ");
			lengthChoice = Integer.parseInt(kb.nextLine());
			makeList(list);
			kb.close();
		}
		return list;
	}
	
	public static List<String> modifyList(List<String> list, List<Character> validGuesses, char guess)
	{
		//to do
		List<String> shortList = new ArrayList<String>();
		
		if(!wordChosen)
		{
			for (int i = 0; i < list.size(); i++)
			{
				boolean canAdd = true;
				String currentWord = list.get(i);
				for (int j = 0; j < currentWord.length(); j++)
				{
					if (currentWord.charAt(j) == guess)
					{
						canAdd = false;
					}
				}
				if (canAdd)
				{
					shortList.add(list.get(i));
				}
			}
			
			//FORCED TO KEEP LIST, PICK A WORD, REVEAL LETTER
			if (shortList.size() == 0)
			{
				//pick a word
				wordChosen = true;
				Random randomizer = new Random();
				chosenWord = list.get(randomizer.nextInt(list.size()));
				//System.out.println("\nSystem was forced to pick a word: " + chosenWord);
				shortList = list;
				validGuesses.add(guess);
				tries++;
			}
		}
		
		//word has already been picked
		else
		{	
			for (int i = 0; i < chosenWord.length(); i++)
			{
				if (chosenWord.charAt(i) == guess && !validGuesses.contains(guess))
				{
					validGuesses.add(guess);
					tries++;
				}
			}
			//!!
		}
		
		
		return shortList;
	}
	
	public static void newBoard(List<Character> validGuesses)
	{
		String newBoard = "[ ";
		if (wordChosen)
		{
			for (int i = 0; i < chosenWord.length(); i++)
			{
				boolean placedLetter = false;
				for (int j = 0; j < validGuesses.size(); j++)
				{
					//if matches, reveal
					if (chosenWord.charAt(i) == validGuesses.get(j))
					{
						newBoard += chosenWord.charAt(i);
						placedLetter = true;
					}
				}
				if (!placedLetter)
				{
					newBoard += "-";
				}
			}
		}
		else
		{
			for (int i = 0; i < lengthChoice; i++)
			{
				newBoard += "-";
			}
		}
		newBoard += " ]";
		System.out.println(newBoard);
	}
		
	public static void main(String[] args) 
	{	
		boolean keepPlaying = true;
		Scanner kb = new Scanner(System.in);

		while (keepPlaying)
		{
			System.out.println("");
			System.out.println("*********************");
			System.out.println("[ NEW GAME STARTED ]");
			System.out.println("*********************");
			System.out.println("");
			
			List<String> wordList = new ArrayList<String>();
			
			//getting word length
			System.out.println("Select word size: ");
			lengthChoice = Integer.parseInt(kb.nextLine());
			makeList(wordList);
			
			//getting number tries
			System.out.println("How many guesses before game ends?");
			tries = Integer.parseInt(kb.nextLine());
		
			System.out.println("Number of words of length " + lengthChoice + ": " + wordList.size());
			
			//starts new game
			newGame(wordList);
			
			//game over 
			if (gameWon)
			{
				System.out.println("\nGood job, you beat me! The word was " + chosenWord + ".");
			}
			else
			{
				System.out.println("\nSorry, you lost! The word was " + chosenWord + ".");
			}
			System.out.println("Enter 0 to quit or any other number to play again: ");
			if (Integer.parseInt(kb.nextLine()) == 0)
			{
				System.out.println("THANKS FOR PLAYING! :-)");
				keepPlaying = false;
			}
			else
			{
				//resetting for new game
				chosenWord = "";
				wordChosen = false;
				gameWon = false;
				lengthChoice = 0;
			}
		}
		kb.close();
	}	
}
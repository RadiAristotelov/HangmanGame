package engine;

import printer.Printer;

import java.util.*;

public class EngineImpl implements Engine {
    private List<String> words;
    private Random random;
    private Scanner scanner;
    private String word;
    private List<Character> wordToGuess;
    private List<Character> progressOnWord;
    private List<Character> guessedLetters;
    private int mistakeCounter;
    private boolean lost;
    private boolean won;


    public EngineImpl() {
        //Insert words
        this.words = new ArrayList<>(Arrays.asList
                ("laptop", "university", "avalanche", "headphones", "wallet",
                        "skyscraper", "mountain", "skateboard", "calendar",
                        "christmas", "smartphone", "smart watch", "sand storm", "bed sheets"));
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            getWordToGuess();
            this.progressOnWord = new ArrayList<>();
            fillProgress();
            this.guessedLetters = new ArrayList<>();
            this.mistakeCounter = 0;
            this.won = false;
            this.lost = false;
            startGame();

            System.out.println("Play again?(Y/N)");
            String choice = getInput();

            if (!(choice.equals("Y") || choice.equals("y") || choice.equals("N") || choice.equals("n"))) {
                choice = getInput();
            }

            if (choice.equals("N") || choice.equals("n")) {
                break;
            }
        }
    }

    private void startGame() {
        while (!this.won && !this.lost) {
            printProgress();
            printField();

            if (!this.guessedLetters.isEmpty()) {
                printGuessedLetters();
            }

            String guess = getInputGuess();

            if (guess.length() == 1) {
                if (this.wordToGuess.contains(Character.toLowerCase(guess.charAt(0)))) {
                    if (!(this.progressOnWord.contains(Character.toLowerCase(guess.charAt(0))))
                            && !(this.progressOnWord.contains(Character.toUpperCase(guess.charAt(0))))) {
                        ChangeProgressOneLetter(guess);
                    } else {
                        this.mistakeCounter++;
                    }
                } else {
                    this.mistakeCounter++;
                    if (!(this.guessedLetters.contains(Character.toLowerCase(guess.charAt(0))))) {
                        this.guessedLetters.add(Character.toLowerCase(guess.charAt(0)));
                    }
                }
            } else if (guess.length() > 1) {
                if (CheckIfGuessEqualsWord(guess)) {
                    changeProgressForWholeWordGuess();
                } else {
                    this.mistakeCounter++;
                }
            }

            if (this.mistakeCounter == 9) {
                this.lost = true;
            }

            if (!(this.progressOnWord.contains('_'))) {
                this.won = true;
            }
        }

        if (this.lost) {
            youLost();
        }

        if (this.won) {
            youWon();
        }
    }

    private void youWon() {
        printProgress();
        printField();

        if (!this.guessedLetters.isEmpty()) {
            printGuessedLetters();
        }

        System.out.println("Congratulations!");
    }

    private void youLost() {
        printProgress();
        printField();

        if (!this.guessedLetters.isEmpty()) {
            printGuessedLetters();
        }

        System.out.printf("Too bad! The word was %s.%n", this.word);
    }

    private void ChangeProgressOneLetter(String guess) {
        for (int i = 0; i < this.wordToGuess.size(); i++) {
            if (this.wordToGuess.get(i) == Character.toLowerCase(guess.charAt(0))) {
                if (i == 0) {
                    this.progressOnWord.set(i, Character.toUpperCase(guess.charAt(0)));
                } else {
                    this.progressOnWord.set(i, Character.toLowerCase(guess.charAt(0)));
                }
            }
        }
    }

    private boolean CheckIfGuessEqualsWord(String guess) {
        boolean equals = true;

        List<Character> guessWord = new ArrayList<>();

        for (int i = 0; i < guess.length(); i++) {
            guessWord.add(Character.toLowerCase(guess.charAt(i)));
        }

        for (int i = 0; i < guessWord.size(); i++) {
            if (!(this.wordToGuess.get(i) == guessWord.get(i))) {
                equals = false;
            }
        }

        return equals;
    }

    private void changeProgressForWholeWordGuess() {
        for (int i = 0; i < this.wordToGuess.size(); i++) {
            if (i == 0) {
                this.progressOnWord.set(i, Character.toUpperCase(this.wordToGuess.get(i)));
            } else {
                this.progressOnWord.set(i, Character.toLowerCase(this.wordToGuess.get(i)));
            }
        }
    }

    private String getInputGuess() {
        System.out.println("Enter your guess(a letter or the whole word):");
        return this.scanner.nextLine();
    }

    private void printProgress() {
        for (Character character : this.progressOnWord) {
            System.out.print(character + " ");
        }
        System.out.println();
    }

    private void printGuessedLetters() {
        System.out.print("Used letters: ");
        Collections.sort(this.guessedLetters);
        for (int i = 0; i < this.guessedLetters.size(); i++) {
            System.out.print(this.guessedLetters.get(i));
            if (!(i == this.guessedLetters.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private void getWordToGuess() {
        int wordIndex = this.random.nextInt(words.size());
        this.word = this.words.get(wordIndex);
        this.wordToGuess = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            if (this.word.charAt(i) == ' ') {
                this.wordToGuess.add(' ');
            } else {
                this.wordToGuess.add(Character.toLowerCase(word.charAt(i)));
            }
        }
    }

    private void fillProgress() {
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i) == ' ') {
                this.progressOnWord.add(' ');
            } else {
                if (i == 0) {
                    this.progressOnWord.add(Character.toUpperCase(this.word.charAt(i)));
                } else if (i == this.word.length() - 1) {
                    this.progressOnWord.add(Character.toLowerCase(this.word.charAt(i)));
                } else if (this.word.charAt(i + 1) == ' ') {
                    this.progressOnWord.add(Character.toLowerCase(this.word.charAt(i)));
                } else if (this.word.charAt(i - 1) == ' ') {
                    this.progressOnWord.add(Character.toLowerCase(this.word.charAt(i)));
                } else {
                    this.progressOnWord.add('_');
                }
            }
        }

        int indexOfSpace = -1;

        if (word.contains(" ")) {
            for (int j = 0; j < this.progressOnWord.size(); j++) {
                if (this.progressOnWord.get(j) == ' ') {
                    indexOfSpace = j;
                }
            }
        }

        for (int i = 0; i < this.word.length(); i++) {
            if (i != 0 && i != this.word.length() - 1) {
                if (this.word.charAt(i) == this.word.charAt(0)) {
                    this.progressOnWord.set(i, Character.toLowerCase(this.word.charAt(0)));
                } else if (this.word.charAt(i) == this.word.charAt(this.word.length() - 1)) {
                    this.progressOnWord.set(i, Character.toLowerCase(this.word.charAt(this.word.length() - 1)));
                } else if (this.word.contains(" ")) {
                    if (this.word.charAt(i) == this.word.charAt(indexOfSpace - 1)) {
                        this.progressOnWord.set(i, Character.toLowerCase(this.word.charAt(indexOfSpace - 1)));
                    } else if (this.word.charAt(i) == this.word.charAt(indexOfSpace + 1)) {
                        this.progressOnWord.set(i, Character.toLowerCase(this.word.charAt(indexOfSpace + 1)));
                    }
                }
            }
        }
    }

    private String getInput() {
        return scanner.nextLine();
    }

    private void printField() {
        switch (mistakeCounter) {
            case 0:
                Printer.baseField();
                break;

            case 1:
                Printer.firstMistake();
                break;

            case 2:
                Printer.secondMistake();
                break;

            case 3:
                Printer.thirdMistake();
                break;

            case 4:
                Printer.fourthMistake();
                break;

            case 5:
                Printer.fifthMistake();
                break;

            case 6:
                Printer.sixthMistake();
                break;

            case 7:
                Printer.seventhMistake();
                break;

            case 8:
                Printer.eightMistake();
                break;

            case 9:
                Printer.ninthMistake();
                break;
        }
    }
}

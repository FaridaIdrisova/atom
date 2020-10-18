import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BullsAndCows {
    public static void main(String[] args) throws IOException {
        while (true) {
            boolean isTryAgain = startGame();

            if (!isTryAgain) {
                break;
            }
        }
    }

    public static boolean startGame() throws IOException {
        ArrayList<String> dictionary = getDictionary("dictionary.txt");
        String keyWord = getAnyItem(dictionary).toLowerCase();

        int triesLeft = 10;
        boolean isWon = false;

        System.out.println("I offered a " + keyWord.length() + "-letter word.");
        System.out.println("Try to guess the word...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (triesLeft > 0) {
            String wordByUser = reader.readLine().toLowerCase();
            if (wordByUser.length() != keyWord.length()) {
                System.out.println("Incorrect word length (" + wordByUser.length() + "), it should be " + keyWord.length());
            }
            if (wordByUser.equals(keyWord)) {
                isWon = true;
                break;
            } else {
                int[] bullsAndCows = getBullsAndCows(keyWord, wordByUser);

                System.out.println("Bulls: " + bullsAndCows[0]);
                System.out.println("Cows: " + bullsAndCows[1]);
            }

            triesLeft--;
        }

        if (isWon) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose. Key word is " + keyWord);
        }

        System.out.println("Try again? Y/N");
        String response = reader.readLine();
        if (response.equals("Y")) {
            return true;
        }

        return false;
    }

    public static ArrayList<String> getDictionary(String fileName) {
        ArrayList<String> result = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNext()) {
                result.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found!");
        }

        return result;
    }

    public static String getAnyItem(ArrayList<String> items) {
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public static int[] getBullsAndCows(String keyWord, String wordByUser) {
        int bulls = 0;
        int cows = 0;
        int finalWordIndex = keyWord.length();
        if (wordByUser.length() < keyWord.length()) {
            finalWordIndex = wordByUser.length();
        }

        for (int i = 0; i < finalWordIndex; i++) {
            if (keyWord.charAt(i) == wordByUser.charAt(i)) {
                bulls++;
            } else if (keyWord.indexOf(wordByUser.charAt(i)) > -1) {
                cows++;
            }
        }

        return new int[]{bulls, cows};
    }
}

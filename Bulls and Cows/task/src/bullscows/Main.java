package bullscows;

import java.util.*;

/**
 * Simple code guessing game exercise as part of jetbrains academy java basics course
 * User chooses length of code and a set of characters which are used to generate a code, they then enter guesses
 * which are graded to indicate if characters in guess are present in code (cows) or present and in correct position (bulls)
 *
 * The code below meets the specification required for completion of the exercise, which requires the program to exit
 * following any incorrect input, this obviously does not make for a great user experience, and the input validation
 * in the main method should really allow program to request new input and continue.
 *
 * In addition the inclusion of the input validation code in Main is somewhat messy and should be moved to separate
 * methods.
 */

public class Main {
    public static void main(String[] args) {

        String code;
        int codeLength = 37; //Set above max number of characters (36)
        int possibleChars = 37; //Set above max number of characters (36)

        Scanner scanner = new Scanner(System.in);


        /*****
         Input and input validation of codelength needs to be integer in range 1-36
         * */

        System.out.println("Please, enter the secret code's length:");
        try {
            codeLength = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Error: \"" + codeLength + "\" isn't a valid number.");
        }


        if(codeLength == 0) {
            System.out.println("Error: \"" + codeLength + "\" isn't a valid number.");
            System.exit(0);
        } else if (codeLength > 36) {

            System.out.println("Error: can't generate a secret number with a length greater than 36 because there" +
                    " aren't enough unique digits.");
            System.exit(0);
            //System.out.println("Please, enter the secret code's length:");
            //codeLength = scanner.nextInt();
        }

        /*****
         Input and input validation of length of characterset  needs to be integer in range 1-36
         * */

        System.out.println("Input the number of possible symbols in the code:");

        try {
            possibleChars = scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + possibleChars + "\" isn't a valid number.");
        }

        if(possibleChars < codeLength) {

            System.out.println("Error: it's not possible to generate a code with a length of " + codeLength + " with "
                   + possibleChars + " unique symbols.");
          System.exit(0);
            //System.out.println("Please, enter the secret code's length:");
            //possibleChars = scanner.nextInt();
        } else if (possibleChars > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }


        // generation of character set
        String characterSet = charSet(possibleChars);
        // generation of code
        code = codeGeneratorAlphaNumeric(codeLength, characterSet);
        //System.out.println(code);

        String preparedMessage = codeMessage(possibleChars, characterSet);

        System.out.println(preparedMessage);
        System.out.println("Okay, let's start a game!");

        gameLoop(code);

        System.out.println("Congratulations! You guessed the secret code.");


    }

    // First version of code generator used in earlier part of exercise left in for reference.

//    public static String codeGenerator (int len) {
//
//        StringBuilder code = new StringBuilder();
//        String partCode = "";
//        long pseudoRandom = System.nanoTime();
//        int charCount;
//
//        partCode = Long.toString(pseudoRandom);
//
//        for (int i = 0; i < partCode.length(); i++) {
//            if (partCode.charAt(i) != 0) {
//                code.append(partCode.charAt(i));
//                break;
//            }
//        }
//
//        while (code.length() < 10) {
//            //System.out.println("loop");
//            pseudoRandom = System.nanoTime();
//            partCode = Long.toString(pseudoRandom);
//
//            //System.out.println("Partcode" + partCode);
//
//            for (int i = 0; i < partCode.length(); i++) {
//                String finalPartCode = partCode;
//                int finalI = i;
//                charCount = (int) code.chars().filter(ch -> ch == finalPartCode.charAt(finalI)).count();
//                    if (charCount == 0) {
//                        code.append(partCode.charAt(i));
//                    }
//
//            }
//            //System.out.println("code" + code);
//        }
//        //String result = "The random secret number is " + code.substring(0,len);
//
//        return code.substring(0, len);
//    }

    //Second version of code generator left in for reference.

//    public static String codeGeneratorImproved (int len) {
//        Random random = new Random();
//        StringBuilder code = new StringBuilder();
//
//        while (code.length() < len) {
//            String num = Integer.toString(random.nextInt(9));
//            long chCount = code.chars().filter(ch -> ch == num.charAt(0)).count();
//                if (chCount == 0) {
//                    code.append(num.charAt(0));
//                }
//        }
//    return code.toString();
//    }

    /**
     * Method to generate list of charactors to be used in the code, includes digits 0-9 and lower case a-z, current
     * version selects numeric digits first so any code of length < 10 will be numeric, could be fixed by using random
     * to select some numeric digits and remainder alphabetical
     * @param possibleChars
     * @return String consisting of all acceptable characters for use in code.
     */

    public static String charSet(int possibleChars) {

        StringBuilder charSet = new StringBuilder();

        List<Character> allValid = new ArrayList<>();
        for ( int i = 0; i < 10; i++) {
            allValid.add(Character.forDigit(i, 10));
        }

        for (int j = 97; j < 123; j++) {
            allValid.add(((char) j));
        }

        for (int k = 0; k < possibleChars; k++) {
            charSet.append(allValid.get(k));
        }

        return charSet.toString();
    }

    /**
     * Method that takes max number of possible characters in code and character set, assumes character set is ordered
     * starting with numeric digits then alphabetic characters. message is in the format:
     * "The secet code is prepared *'s representing code length (digit range, alphabetic range)
     * @param possibleChars
     * @param charSet
     * @return String message confirming code length and character sets to user.
     */
    public static String codeMessage(int possibleChars, String charSet) {

        String message = "The secret is prepared: ";
        String stars = "*";
        String lastDigit;
        String lastLetter;

        stars = stars.repeat(possibleChars);

        message = message + stars + " (0-";


        String lastChar =  String.valueOf(charSet.charAt(possibleChars -1));

        if (possibleChars <= 10) {
            lastDigit = lastChar;
            message = message + lastDigit + ").";
        }
        else {
            lastDigit = "9";
            lastLetter = lastChar;
            message = message + lastDigit + ", " + "a-" + lastLetter + ").";
        }


        return message;
    }

    /**
     * Method produces secret code from defined character set, randomly selects characters from the set, checks if they
     * are already present in the code and if not appends to end of code until required length is reached.
     * @param codeLength
     * @param charSet
     * @return secret code!
     */
    public static String codeGeneratorAlphaNumeric (int codeLength, String charSet) {

        StringBuilder code = new StringBuilder();


        while (code.length() < codeLength) {

            Random random = new Random();
            int position = random.nextInt(charSet.length() - 1);
            char nextChar = charSet.charAt(position);
            long chCount = code.chars().filter(ch -> ch == nextChar).count();
            if (chCount == 0) {
                code.append(nextChar);
            }
        }
        return code.toString();
    }

    /**
     * Method to grade users guess at code, checks if characters in the guess are present in code and if in correct
     * position. Called from gameloop.
     * @param code
     * @param guess
     * @return Message to user to indicate progress towards code solution, cows indicates number of correct
     * characters, bulls indicates number of correct characters in correct position.
     */
    public static String grader (String code, String guess) {

        int cows = 0;
        int bulls = 0;
        String result = "";

//        System.out.println("Guess: " + guess);
//        System.out.println("Code: " + code);

        for(int i = 0; i < code.length(); i++) {
            String x = Character.toString(code.charAt(i));
            String y = Character.toString(guess.charAt(i));
            if(x.equals(y)) {
                bulls += 1;
            } else if (code.contains(y)) {
                cows += 1;
            }
        }

        System.out.println("bulls: " + bulls);
        System.out.println("cows:" + cows);

        if(bulls == 0 && cows != 0) {
            result = "Grade: " + cows + " cow(s).";
        } else if (bulls != 0 && cows == 0) {
            result = "Grade: " + bulls + " bull(s) and "  + cows + " cow(s).";
        } else {
            result = "Grade: " + bulls + " bull(s) and "  + cows + " cow(s).";
        }

        if (bulls == code.length()) {
            result = "Grade: " + bulls + " bull(s).";
            System.out.println(result);
            result = "correct";
        }
        return result;
    }

    /**
     * Method to manage game turns, calls grader.
     * @param code
     */
    public static void gameLoop (String code) {

        int turn = 1;
        String guess;
        String result;
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Turn: " + turn);
            guess = scanner.next();

            while (guess.length() != code.length()) {
                System.out.println();
            }
            result = grader(code, guess);

            if (result.equals("correct")) {
                break;
            } else {
                System.out.println(result);
            }
            turn ++;

        }

    }
}

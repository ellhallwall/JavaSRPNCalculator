//Summary of code

//Tried to keep code as modular as possible, ensuring each action was encapsulated in a method and reduce repeat code where possible
//Imported libraires and defined constants before using methods to trim, split, and check the string before implementing any operators and trying to ensure I was checking under/overflow throughout.
//Printed error messages to guide the user on what operands/operators are unrecognised



import java.util.Stack; //imported for stack
import java.lang.Math; //imported for abs function on random num method
import java.util.Random; //imported for random num method

public class SRPN {
  
    //fields and constants
    private static final long MinNumber = -2147483648L;
    private static final long MaxNumber = 2147483647;
    private Stack<Long> CalculationStack;
    private Random randomNumberGenerator = new Random(); 
    private static final long MinNumberForRandom = 0; //min number for range
    private static final long MaxStackSize = 23;
    
  //construtor
    public SRPN() {
        CalculationStack = new Stack<>(); 
    }
    //method to process calculation
    public void processCommand(String command) {
        String trimmedCommand = command.trim(); //remove start and end white spaces
        String[] splitCommand = trimmedCommand.split(" "); //split by space delimiter

      //for each token in array
        for (String token : splitCommand) {
            if (checkPenultimateChar(token)) {
              continue; //use continue to exit loop, and stop processing other commands
            } //method explained below
            if (isNumber(token)) { //method explained below
                if (!StackOverflow()) { //if stack is not full
                    CalculationStack.push(Long.valueOf(token)); //push token to stack
                } else {
                    System.out.println("Stack overflow."); //else print out stack overflow
                }
              //process each type of operand - methods explained below
            } else if (token.equals("+")) {
                addNumbers();
            } else if (token.equals("*")) {
                multiplyNumbers();
            } else if (token.equals("^")) {
                ToPowersOfNumbers();
            } else if (token.equals("-")) {
                minusNumbers();
            } else if (token.equals("/")) {
                divideNumbers();
            } else if (token.equals("%")) {
                RemainderNumbers();
            } else if (token.equals("d")) {
                displayNumbers();
            } else if (token.equals("r")) {
                randomNumber(token);
            } else if (token.equals("=")) {
                if (!CalculationStack.isEmpty()) {
                    System.out.println(CalculationStack.peek());
                }
            } else { //error message for other operaands
                System.out.println("Unrecognised operator or operand " + token + ".");
            }
        }
    }

    //method for adding numbers and pushing to stack - checks for stack underflow before
    private void addNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        long answer = firstNumber + secondNumber;
        pushToStack(answer);
    }

    //method for multiplying numbers and pushing to stack - checks for stack underflow before
    private void multiplyNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        long answer = firstNumber * secondNumber;
        pushToStack(answer);
    }

    //method for finding power of two numbers and pushing to stack - checks for stack underflow before
    private void ToPowersOfNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        long answer = (int) Math.pow(secondNumber, firstNumber); //reverse order due to LIFO
        pushToStack(answer);
    }

    //method for minusing two numbers and pushing to stack - checks for stack underflow before
    private void minusNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        long answer = secondNumber - firstNumber; //reversed order due to LIFO data
        pushToStack(answer);
    }

    //method for dividing two numbers and pushing to stack - checks for stack underflow before
    private void divideNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        if (firstNumber == 0) {
            System.out.println("Divide by 0."); //error message 
        } else {
            long answer = secondNumber / firstNumber; //reversed order due to LIFO data
            pushToStack(answer);
        }
    }

    //method for finding the remainder two numbers and pushing to stack -  checks for stack underflow before
    private void RemainderNumbers() {
        if (StackUnderflow(2)) return;
        long firstNumber = CalculationStack.pop();
        long secondNumber = CalculationStack.pop();
        long answer = secondNumber % firstNumber; //reversed order due to LIFO data
        pushToStack(answer);
    }

    //method to work out if the token is a an integer
    private boolean isNumber(String token) {
        try { //try to parse string as int
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException exception) { //catch execption to stop runtime error
            return false;
        }
    }

    //method to display numbers in stack
    private void displayNumbers() {
        for (Long answer : CalculationStack) {
            System.out.println(answer);
        }
    }

    //method to generate a random number - and check if stack is full 
    private void randomNumber(String token) {
        if (token.equals("r")) {
            long range = MaxNumber - MinNumberForRandom; //range for the numbers
            long randomValue = (Math.abs(randomNumberGenerator.nextLong()) % range); //use math.abs to ensure number is positive and % range to keep the number in range
            StackOverflow(); 
            CalculationStack.push(randomValue);
        }
    }

    //method to push answers to the stack and ensure that saturation is applied
    private void pushToStack(long answer) {
        if (answer > MaxNumber) {
            answer = MaxNumber;
        } else if (answer < MinNumber) {
            answer = MinNumber;
        }
        CalculationStack.push(answer);
    }

  //method for when stack is empty or only had 1 entry
  private boolean StackUnderflow(int requiredNumbers) {
      if (CalculationStack.size() < requiredNumbers) {
          System.out.println("Stack underflow.");
          return true;
      }
      return false;
  }

    //method for when the stack is full and overflows
    private boolean StackOverflow() {
        if (CalculationStack.size() >= MaxStackSize) {
            System.out.println("Stack Overflow.");
            return true;
        }
        return false;
    }

    //method for checking the second last char is an operator and then peek the top result if true
    private boolean checkPenultimateChar(String token) {
        if (token.length() < 2) { 
            return false; //token length is too short to check
        }
        char penultimateCharacter = token.charAt(token.length() - 2); //checking the char at second last position

        // if the char is one of the listed operands
        boolean isOperator = penultimateCharacter == '+' ||
                penultimateCharacter == '*' ||
                penultimateCharacter == '^' ||
                penultimateCharacter == '-' ||
                penultimateCharacter == '/' ||
                penultimateCharacter == '%';

        //if isopeartor is true, and the stack is not emptyu
        if (isOperator && !CalculationStack.isEmpty()) {
            System.out.println(CalculationStack.peek()); // peek the last number 
            return true; // Penultimate character was an operator
        }
        return false; // Penultimate character was not an operator
    }
}


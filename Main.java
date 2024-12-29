package FOP_Team_Project;

import java.util.*;

public class Main {

    Sum sumInstance = new Sum();
    Factorial factorialInstance = new Factorial();
    GCD gcdInstance = new GCD();
    ReverseNumber reverseNumberInstance = new ReverseNumber();
    PrimeNumber primeNumberInstance = new PrimeNumber();
    PalindromeNumber palindromeNumberInstance = new PalindromeNumber();
    LargestDigit largestDigitInstance = new LargestDigit();
    SumOfDigits sumOfDigitsInstance = new SumOfDigits();
    MultiplicationTable multiplicationTableInstance = new MultiplicationTable(); // Added MultiplicationTable instance
    Fibonacci fibonacciInstance = new Fibonacci();  // Added Fibonacci instance
    static List<String> variableNames = new ArrayList<>();
    static List<String> variableValues = new ArrayList<>();
    boolean executeCurrentBlock = true;

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Python-like Interpreter. Type 'exit' to quit.");

        Stack<Boolean> executionStack = new Stack<>();
        Stack<Integer> indentStack = new Stack<>();

        while (scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine();

            // Exit condition
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Skip empty input
            if (input.trim().isEmpty()) continue;

            try {
                // Calculate indentation
                int indentLevel = input.length() - input.stripLeading().length();
                String line = input.trim();

                // Handle indentation change
                while (!indentStack.isEmpty() && indentStack.peek() > indentLevel) {
                    indentStack.pop();
                    interpreter.executeCurrentBlock = executionStack.pop();
                }

                // Handle Fibonacci calculation
                if (line.startsWith("fibonacci =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    int fibonacciResult = interpreter.fibonacciInstance.calculateFibonacci(number);
                    System.out.println("Fibonacci of " + number + " is: " + fibonacciResult);
                    continue;
                }

                // Handle IF statement
                if (line.startsWith("if ") && line.endsWith(":")) {
                    interpreter.handleIfStatement(line, indentLevel, executionStack, indentStack);
                    continue;
                }

                // Handle ELSE statement
                if (line.startsWith("else:")) {
                    interpreter.handleElseStatement(executionStack, indentStack, indentLevel);
                    continue;
                }

                // Handle WHILE statement
                if (line.startsWith("while ") && line.endsWith(":")) {
                    interpreter.handleWhileStatement(line, indentLevel, executionStack, indentStack, scanner);
                    continue;
                }

                // Handle sum calculation
                // Extract argument as String for sum
                if (line.startsWith("sum(")) {
                    String argument = extractArgumentAsString(line, "sum");
                    String value = interpreter.getValue(argument);
                    int number = Integer.parseInt(value);
                    interpreter.sumInstance.sumOfFirstNNumbers("sum = " + number);
                    continue;
                }



                // Handle factorial calculation
                if (line.startsWith("factorial =")) {
                    Factorial factorialInstance = new Factorial();
                    factorialInstance.calculateFactorial(line);
                    continue;
                }

                // Handle GCD calculation
                if (line.startsWith("gcd =")) {
                    String[] parts = line.split("=");
                    String[] numbers = parts[1].trim().split(",");
                    if (numbers.length == 2) {
                        int a = Integer.parseInt(numbers[0].trim());
                        int b = Integer.parseInt(numbers[1].trim());
                        int result = interpreter.gcdInstance.calculateGCD(a, b);
                        System.out.println("GCD: " + result);
                    } else {
                        throwError("Invalid GCD input. Provide two numbers.");
                    }
                    continue;
                }

                // Handle Reverse Number calculation
                if (line.startsWith("reverse =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    int reversedNumber = interpreter.reverseNumberInstance.reverse(number);
                    System.out.println("Reversed Number: " + reversedNumber);
                    continue;
                }

                // Handle Prime Number check
                if (line.startsWith("isPrime =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    boolean result = interpreter.primeNumberInstance.isPrime(number);
                    System.out.println("Is Prime: " + result);
                    continue;
                }

                // Handle Palindrome Number check
                if (line.startsWith("isPalindrome =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    boolean result = interpreter.palindromeNumberInstance.isPalindrome(number);
                    System.out.println("Is Palindrome: " + result);
                    continue;
                }

                // Handle Largest Digit calculation
                if (line.startsWith("largestDigit =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    int largestDigit = interpreter.largestDigitInstance.findLargestDigit(number);
                    System.out.println("Largest Digit: " + largestDigit);
                    continue;
                }

                // Handle Sum of Digits calculation
                if (line.startsWith("sumOfDigits =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    int sum = interpreter.sumOfDigitsInstance.sumOfDigits(number);
                    System.out.println("Sum of Digits: " + sum);
                    continue;
                }

                // Handle Multiplication Table calculation
                if (line.startsWith("multiTable =")) {
                    String[] parts = line.split("=");
                    String numberStr = parts[1].trim();
                    int number = Integer.parseInt(numberStr);
                    interpreter.multiplicationTableInstance.printTable(number);
                    continue;
                }

                // Skip lines in inactive block
                if (!interpreter.executeCurrentBlock) {
                    continue;
                }

                if (line.startsWith("print(") && line.endsWith(")")) {
                    String varName = line.substring(6, line.length() - 1).trim();
                    interpreter.printVariable(varName);
                    continue;
                }

                if (line.contains("=")) {
                    interpreter.handleAssignment(line);
                    continue;
                }

                throwError("Invalid command.");
            } catch (Exception e) {
                throwError(e.getMessage());
            }
        }

        scanner.close();
    }
    static int extractArgument(String line, String function) {
        try {
            String argument = line.substring(function.length() + 1, line.length() - 1).trim();
            return Integer.parseInt(argument);
        } catch (Exception e) {
            throwError("Invalid argument for " + function + " function.");
            return -1;
        }
    }
    static String extractArgumentAsString(String line, String function) {
        try {
            return line.substring(function.length() + 1, line.length() - 1).trim();
        } catch (Exception e) {
            throwError("Invalid argument for " + function + " function.");
            return "";
        }
    }



    // Handle IF statement logic
    void handleIfStatement(String line, int indentLevel, Stack<Boolean> executionStack, Stack<Integer> indentStack) {
        String condition = line.substring(3, line.length() - 1).trim();
        boolean conditionResult = evaluateCondition(condition);

        executionStack.push(executeCurrentBlock);
        executeCurrentBlock = executeCurrentBlock && conditionResult;
        indentStack.push(indentLevel);
    }

    // Handle ELSE statement logic
    void handleElseStatement(Stack<Boolean> executionStack, Stack<Integer> indentStack, int indentLevel) {
        if (executionStack.isEmpty()) {
            throwError("Unexpected else.");
        }

        boolean previousState = executionStack.pop();
        executionStack.push(previousState);

        executeCurrentBlock = !previousState && executeCurrentBlock;
        indentStack.push(indentLevel);
    }

    // Handle WHILE statement logic
    void handleWhileStatement(String line, int indentLevel, Stack<Boolean> executionStack, Stack<Integer> indentStack, Scanner scanner) {
        String condition = line.substring(6, line.length() - 1).trim();

        executionStack.push(executeCurrentBlock);
        boolean conditionResult = evaluateCondition(condition);

        while (conditionResult && executeCurrentBlock) {
            String nextLine = scanner.nextLine();
            int nextIndentLevel = nextLine.length() - nextLine.stripLeading().length();

            if (nextIndentLevel > indentLevel) {
                if (nextLine.startsWith("print(") && nextLine.endsWith(")")) {
                    String varName = nextLine.substring(6, nextLine.length() - 1).trim();
                    printVariable(varName);
                } else if (nextLine.contains("=")) {
                    handleAssignment(nextLine);
                } else {
                    throwError("Invalid command inside while loop.");
                }
            } else {
                break;
            }

            conditionResult = evaluateCondition(condition);
        }
    }

    boolean evaluateCondition(String condition) {
        String[] parts;
        if (condition.contains("==")) {
            parts = condition.split("==");
            String left = getValue(parts[0].trim());
            String right = getValue(parts[1].trim());
            return left.equals(right);
        } else if (condition.contains("<")) {
            parts = condition.split("<");
            int left = Integer.parseInt(getValue(parts[0].trim()));
            int right = Integer.parseInt(getValue(parts[1].trim()));
            return left < right;
        } else if (condition.contains(">")) {
            parts = condition.split(">");
            int left = Integer.parseInt(getValue(parts[0].trim()));
            int right = Integer.parseInt(getValue(parts[1].trim()));
            return left > right;
        }
        throwError("Invalid condition.");
        return false;
    }

    void handleAssignment(String line) {
        String[] parts = line.split("=");
        if (parts.length != 2) {
            throwError("Invalid assignment.");
        }

        String varName = parts[0].trim();
        String value = parts[1].trim();

        assignVariable(varName, value);
    }

    static void assignVariable(String varName, String value) {
        if (!isInteger(value) && !(value.startsWith("\"") && value.endsWith("\""))) {
            throwError("Only integers or strings in double quotes are allowed.");
        }

        int index = variableNames.indexOf(varName);
        if (index != -1) {
            variableValues.set(index, value);
        } else {
            variableNames.add(varName);
            variableValues.add(value);
        }
    }

    void printVariable(String varName) {
        int index = variableNames.indexOf(varName);
        if (index != -1) {
            System.out.println(variableValues.get(index));
        } else {
            throwError("Variable not defined.");
        }
    }

    String getValue(String token) {
        int index = variableNames.indexOf(token);
        if (index != -1) {
            return variableValues.get(index);
        }

        if (isInteger(token)) {
            return token;
        }

        if (token.startsWith("\"") && token.endsWith("\"")) {
            return token.substring(1, token.length() - 1);
        }

        throwError("Invalid value.");
        return null;
    }

    static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static void throwError(String message) {
        System.out.println("Error: " + message);
        System.exit(1);
    }
}

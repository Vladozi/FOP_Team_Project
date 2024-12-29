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

                // Handle factorial calculation using factorial(x) syntax
                if (line.startsWith("factorial(") && line.endsWith(")")) {
                    // Extract argument inside parentheses (e.g., from factorial(x), it will get "x")
                    String argument = extractArgumentAsString(line, "factorial");

                    // Check if the argument is a variable (e.g., x)
                    String value = getValue(argument);

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Call the calculateFactorial method and get the result
                        int factorialResult = interpreter.factorialInstance.calculateFactorial(number);

                        // Print the result
                        System.out.println("Factorial of " + number + " is: " + factorialResult);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for factorial.");
                    }
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





                // Handle GCD calculation using gcd(x, y) syntax
                if (line.startsWith("gcd(") && line.endsWith(")")) {
                    // Extract the arguments inside parentheses (e.g., from gcd(x, y), it will get "x, y")
                    String arguments = extractArgumentAsString(line, "gcd");

                    // Split arguments by comma (assuming two arguments for GCD)
                    String[] numbers = arguments.split(",");

                    if (numbers.length == 2) {
                        // Retrieve values of x and y
                        String valueX = getValue(numbers[0].trim());
                        String valueY = getValue(numbers[1].trim());

                        try {
                            // Parse both values as integers
                            int a = Integer.parseInt(valueX);
                            int b = Integer.parseInt(valueY);

                            // Call the calculateGCD method and get the result
                            int gcdResult = interpreter.gcdInstance.calculateGCD(a, b);

                            // Print the result
                            System.out.println("GCD of " + a + " and " + b + " is: " + gcdResult);

                        } catch (NumberFormatException e) {
                            throwError("Invalid number for GCD calculation.");
                        }
                    } else {
                        throwError("Invalid GCD input. Provide two numbers.");
                    }
                    continue;
                }

// Handle Reverse Number calculation using reverse(x) syntax
                if (line.startsWith("reverse(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from reverse(x), it will get "x")
                    String argument = extractArgumentAsString(line, "reverse");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Call the reverse function and get the reversed number
                        int reversedNumber = interpreter.reverseNumberInstance.reverse(number);

                        // Print the reversed number
                        System.out.println("Reversed Number: " + reversedNumber);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for reverse calculation.");
                    }
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
    // Extract argument for factorial function
    static String extractArgumentAsString(String line, String function) {
        try {
            // Extract the argument inside parentheses (e.g., from factorial(x), it will get "x")
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

    // Retrieve the value of a variable or directly return the number
    static String getValue(String token) {
        int index = variableNames.indexOf(token);  // Check if it's a variable
        if (index != -1) {
            return variableValues.get(index);  // Return the variable value
        }

        // If it's not a variable, just return the token (assuming it's a number)
        if (isInteger(token)) {
            return token;  // Return the number as a string
        }

        // If it's a string, return it
        if (token.startsWith("\"") && token.endsWith("\"")) {
            return token.substring(1, token.length() - 1);  // Return the string without quotes
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

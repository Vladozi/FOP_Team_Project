package FOP_Team_Project;

import java.util.*;

public class Main {

    //Creating instances so that it can work with input e.g sum(n) fibonacci(n) etc. (for instructions check Instruction.txt)
    Sum sumInstance = new Sum();
    Factorial factorialInstance = new Factorial();
    GCD gcdInstance = new GCD();
    ReverseNumber reverseNumberInstance = new ReverseNumber();
    PrimeNumber primeNumberInstance = new PrimeNumber();
    PalindromeNumber palindromeNumberInstance = new PalindromeNumber();
    LargestDigit largestDigitInstance = new LargestDigit();
    SumOfDigits sumOfDigitsInstance = new SumOfDigits();
    MultiplicationTable multiplicationTableInstance = new MultiplicationTable();
    Fibonacci fibonacciInstance = new Fibonacci();
    static List<String> variableNames = new ArrayList<>(); // Creating list for variable names (inputs)
    static List<String> variableValues = new ArrayList<>(); // List for values (inputs)
    boolean executeCurrentBlock = true; //if false then it means that program should stop working (case of errors)

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in); //scanner for input

        System.out.println("Welcome to Python interpreter. Type 'exit' to quit."); //Optionable message

        Stack<Boolean> executionStack = new Stack<>(); //Tracks execution.
        Stack<Integer> indentStack = new Stack<>(); //Tracks indentation.

        //This part checks for commands such as factorial(number), exit etc.
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

                // Handle Fibonacci calculation using fibonacci(x) syntax
                if (line.startsWith("fibonacci(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from fibonacci(x), it will get "x")
                    String argument = extractArgumentAsString(line, "fibonacci");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Calculate the Fibonacci number
                        int fibonacciResult = interpreter.fibonacciInstance.calculateFibonacci(number);

                        // Print the result
                        System.out.println("Fibonacci of " + number + " is: " + fibonacciResult);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for Fibonacci calculation.");
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

                // Handle sum calculation using sum(x) syntax
                if (line.startsWith("sum(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from sum(x), it will get "x")
                    String argument = extractArgumentAsString(line, "sum");

                    try {
                        // Retrieve the value of x
                        String value = getValue(argument);

                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Call the sumOfFirstNNumbers method and calculate the sum
                        int sumResult = interpreter.sumInstance.sumOfFirstNNumbers(number);

                        // Print the result
                        System.out.println("Sum of first " + number + " numbers is: " + sumResult);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for sum calculation.");
                    }
                    continue;
                }





                // Handle GCD calculation also works with variables such as x
                if (line.startsWith("gcd(") && line.endsWith(")")) {
                    // Extract the arguments inside parentheses (e.g. from gcd(x, y) it will get "x, y")
                    String arguments = extractArgumentAsString(line, "gcd");

                    // Split arguments by comma (two arguments for GCD)
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

                // Handle Reverse Number calculation also works with some variable x too(as well as straightly numbers).
                if (line.startsWith("reverse(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g. from reverse(x) it will get "x")
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
                if (line.startsWith("isPrime(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from isPrime(x), it will get "x")
                    String argument = extractArgumentAsString(line, "isPrime");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Check if the number is prime
                        boolean result = interpreter.primeNumberInstance.isPrime(number);

                        // Print the result
                        System.out.println("Is Prime: " + result);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for prime check.");
                    }
                    continue;
                }


                // Handle Palindrome Number check
                if (line.startsWith("isPalindrome(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from isPalindrome(x), it will get "x")
                    String argument = extractArgumentAsString(line, "isPalindrome");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Check if the number is a palindrome
                        boolean result = interpreter.palindromeNumberInstance.isPalindrome(number);

                        // Print the result
                        System.out.println("Is Palindrome: " + result);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for palindrome check.");
                    }
                    continue;
                }


                // Handle Largest Digit calculation
                if (line.startsWith("largestDigit(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from largestDigit(x), it will get "x")
                    String argument = extractArgumentAsString(line, "largestDigit");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Find the largest digit
                        int largestDigit = interpreter.largestDigitInstance.findLargestDigit(number);

                        // Print the result
                        System.out.println("Largest Digit: " + largestDigit);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for largest digit calculation.");
                    }
                    continue;
                }


                // Handle Sum of Digits calculation
                if (line.startsWith("sumDigits(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from sumOfDigits(x), it will get "x")
                    String argument = extractArgumentAsString(line, "sumDigits");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Calculate the sum of digits
                        int sum = interpreter.sumOfDigitsInstance.sumOfDigits(number);

                        // Print the result
                        System.out.println("Sum of Digits: " + sum);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for sum of digits calculation.");
                    }
                    continue;
                }


                // Handle Multiplication Table calculation
                if (line.startsWith("multiTable(") && line.endsWith(")")) {
                    // Extract the argument inside parentheses (e.g., from multiTable(x), it will get "x")
                    String argument = extractArgumentAsString(line, "multiTable");

                    // Retrieve the value of the variable x
                    String value = getValue(argument.trim());

                    try {
                        // Parse the value as an integer
                        int number = Integer.parseInt(value);

                        // Print the multiplication table
                        interpreter.multiplicationTableInstance.printTable(number);

                    } catch (NumberFormatException e) {
                        throwError("Invalid number for multiplication table.");
                    }
                    continue;
                }


                // Skip lines in inactive block for example if statements such as one case must be ignored.
                if (!interpreter.executeCurrentBlock) {
                    continue;
                }

                // this contains print statement
                if (line.startsWith("print(") && line.endsWith(")")) {
                    String varName = line.substring(6, line.length() - 1).trim();
                    interpreter.printVariable(varName);
                    continue;
                }

                if (line.contains("=")) {
                    interpreter.handleAssignment(line);
                    continue;
                }

                throwError("Invalid command."); // if any of these does not work then it is invalid command
            } catch (Exception e) {
                throwError(e.getMessage());
            }
        }

        scanner.close(); //program closes
    }
    //This block of code checks if passes argument is valid for example x = a is incorrect for int
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
            // Extract the argument inside parentheses (e.g. from factorial(x), it will get "x")
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

    //This one is for conditiopns for example in if block if x == 20
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

    //To parse, evaluate, and store variables and their values from an assignment command
    void handleAssignment(String line) {
        String[] parts = line.split("=");  // Split by assignment operator
        if (parts.length != 2) {
            throwError("Invalid assignment.");
        }

        String varName = parts[0].trim();  // Variable name
        String expression = parts[1].trim();  // The right-hand side expression

        // Evaluate the expression if needed
        String value = evaluateExpression(expression);

        // Assign the evaluated value to the variable
        assignVariable(varName, value);
    }


    //This code supports commands such as x = 10 + 2 or x = 10%3
    String evaluateExpression(String expression) {
        // Remove any spaces from the expression
        expression = expression.replace(" ", "");

        // Check for simple arithmetic operations
        if (expression.contains("+")) {
            String[] operands = expression.split("\\+");
            int left = Integer.parseInt(getValue(operands[0].trim()));
            int right = Integer.parseInt(getValue(operands[1].trim()));
            return Integer.toString(left + right);
        } else if (expression.contains("-")) {
            String[] operands = expression.split("-");
            int left = Integer.parseInt(getValue(operands[0].trim()));
            int right = Integer.parseInt(getValue(operands[1].trim()));
            return Integer.toString(left - right);
        } else if (expression.contains("*")) {
            String[] operands = expression.split("\\*");
            int left = Integer.parseInt(getValue(operands[0].trim()));
            int right = Integer.parseInt(getValue(operands[1].trim()));
            return Integer.toString(left * right);
        } else if (expression.contains("/")) {
            String[] operands = expression.split("/");
            int left = Integer.parseInt(getValue(operands[0].trim()));
            int right = Integer.parseInt(getValue(operands[1].trim()));
            if (right == 0) {
                throwError("Division by zero.");
            }
            return Integer.toString(left / right);
        } else if (expression.contains("%")) {
            String[] operands = expression.split("%");
            int left = Integer.parseInt(getValue(operands[0].trim()));
            int right = Integer.parseInt(getValue(operands[1].trim()));
            return Integer.toString(left % right);
        }

        // If no operators, just return the value (could be a variable or a number)
        return getValue(expression);
    }




    //ensure only valid values (integers or strings) are assigned and to store or update variables and their values
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

    //display the value of a specified variable
    void printVariable(String varName) {
        int index = variableNames.indexOf(varName);
        if (index != -1) {
            String value = variableValues.get(index);
            // Evaluate if it's an expression
            String evaluatedValue = evaluateExpression(value);
            System.out.println(evaluatedValue);
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
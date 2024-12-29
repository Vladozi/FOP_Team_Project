import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {

    // A map to store variables and their values
    private static final Map<String, Object> variables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Basic Python Interpreter");
        System.out.println("Supports variable assignment, print, if-else, and increment/decrement operations.");
        System.out.println("Type 'exit' to quit.");

        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            try {
                interpret(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void interpret(String input) throws Exception {
        input = input.trim();

        // Check for variable assignment
        if (input.contains("=") && !input.startsWith("if") && !input.startsWith("while")) {
            handleAssignment(input);
        }
        // Check for print statements
        else if (input.startsWith("print(") && input.endsWith(")")) {
            handlePrint(input);
        }
        // Check for `if-else` statements
        else if (input.startsWith("if")) {
            handleIfElse(input);
        }
        // Check for `while` loops
        else if (input.startsWith("while")) {
            handleWhile(input);
        }
        else if (input.contains("+=") || input.contains("-=")) {
            handleIncrementDecrement(input);
        }
        // Handle standalone arithmetic expressions
        else {
            handleArithmetic(input, false);
        }
    }


    private static void handleAssignment(String input) throws Exception {
        String[] parts = input.split("=", 2);
        if (parts.length != 2) {
            throw new Exception("Invalid assignment syntax");
        }

        String variableName = parts[0].trim();
        String valueExpression = parts[1].trim();

        // If input() is present, handle it as user input
        if (valueExpression.startsWith("input()")) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter value for " + variableName + ": ");
            String userInput = scanner.nextLine();
            variables.put(variableName, userInput);
        } else if (valueExpression.startsWith("\"") && valueExpression.endsWith("\"")) {
            variables.put(variableName, valueExpression.substring(1, valueExpression.length() - 1));
        } else {
            Object value = evaluateExpression(valueExpression);
            variables.put(variableName, value);
        }
    }

    private static void handleIncrementDecrement(String input) throws Exception {
        String[] parts;
        String operator = "";

        // Check for += or -= operators
        if (input.contains("+=")) {
            parts = input.split("\\+=", 2);
            operator = "+";
        } else if (input.contains("-=")) {
            parts = input.split("-=", 2);
            operator = "-";
        } else {
            throw new Exception("Invalid increment/decrement syntax");
        }

        // Ensure the input is split correctly
        if (parts.length != 2) {
            throw new Exception("Invalid increment/decrement syntax");
        }

        String variableName = parts[0].trim();
        String incrementValue = parts[1].trim();

        // Check if the variable exists
        if (!variables.containsKey(variableName)) {
            throw new Exception("Variable " + variableName + " not defined");
        }

        // Ensure the increment value is numeric
        if (!isNumeric(incrementValue)) {
            throw new Exception("Invalid increment/decrement value");
        }

        // Get the current value of the variable
        double currentValue = ((Number) variables.get(variableName)).doubleValue();
        double increment = Double.parseDouble(incrementValue);

        // Update the value based on the operator
        double result = operator.equals("+") ? currentValue + increment : currentValue - increment;

        // Store the updated result in the variables map
        variables.put(variableName, result);
    }



    private static void handleIfElse(String input) throws Exception {
        // Check if it's a one-liner (contains an inline `if` and actions)
        if (input.contains(" if ") && input.contains(":")) {
            // Split the input into the action and the condition
            String[] parts = input.split(" if ", 2);

            if (parts.length < 2) {
                throw new Exception("Invalid if-else syntax for one-liner.");
            }

            String actionPart = parts[0].trim(); // Action before "if"
            String conditionPart = parts[1].trim(); // Condition and statements after "if"

            // Split the condition and the block of statements
            String[] conditionParts = conditionPart.split(":", 2);
            if (conditionParts.length < 2) {
                throw new Exception("Missing ':' in if-else syntax.");
            }

            String condition = conditionParts[0].trim(); // Extract the condition
            String statements = conditionParts[1].trim(); // Extract the statements after ':'

            // Evaluate the condition
            boolean conditionResult = evaluateCondition(condition);

            if (conditionResult) {
                // Execute the action and statements if the condition is true
                interpret(actionPart);
                interpret(statements);
            }
        } else {
            // Fallback for regular `if-else` syntax
            String[] parts = input.split(":", 2);

            if (parts.length < 2) {
                throw new Exception("Invalid if-else syntax. Expected ':' to separate condition and statements.");
            }

            String condition = parts[0].trim(); // Extract the "if" condition part
            String statements = parts[1].trim(); // Extract the statements block

            if (!condition.startsWith("if")) {
                throw new Exception("Invalid if syntax. Expected 'if' keyword.");
            }

            // Remove "if" keyword and trim the condition
            condition = condition.substring(2).trim();

            // Evaluate the condition
            boolean conditionResult = evaluateCondition(condition);

            if (conditionResult) {
                // Execute the "if" block
                interpret(statements);
            } else {
                // Check for "else" block
                if (statements.contains("else:")) {
                    String[] elseParts = statements.split("else:", 2);
                    if (elseParts.length != 2) {
                        throw new Exception("Invalid else syntax.");
                    }

                    // Execute the "else" block
                    interpret(elseParts[1].trim());
                }
            }
        }
    }

    private static boolean evaluateCondition(String condition) throws Exception {
        // Preprocess the condition to ensure proper spacing around comparison operators
        condition = condition.replaceAll("\\s*(==|!=|<=|>=|>|<|&&|\\|\\|)\\s*", " $1 ");

        // Handle logical NOT operator (!)
        if (condition.startsWith("!")) {
            String innerCondition = condition.substring(1).trim();
            return !evaluateCondition(innerCondition);
        }

        // Handle logical AND (&&) and OR (||)
        if (condition.contains("&&")) {
            String[] parts = condition.split("&&");
            for (String part : parts) {
                if (!evaluateCondition(part.trim())) {
                    return false; // Return false if any condition in the AND chain is false
                }
            }
            return true; // All conditions are true
        } else if (condition.contains("||")) {
            String[] parts = condition.split("\\|\\|");
            for (String part : parts) {
                if (evaluateCondition(part.trim())) {
                    return true; // Return true if any condition in the OR chain is true
                }
            }
            return false; // All conditions are false
        }

        // For simple numeric comparisons (e.g., "x > 3")
        String[] tokens = condition.split("\\s+"); // Split by spaces
        if (tokens.length != 3) {
            throw new Exception("Invalid condition syntax: " + condition);
        }

        Object left = evaluateExpression(tokens[0]);
        String operator = tokens[1];
        Object right = evaluateExpression(tokens[2]);

        // Ensure both sides of the comparison are numeric
        if (!(left instanceof Number) || !(right instanceof Number)) {
            throw new Exception("Condition must compare numeric values");
        }

        double leftValue = ((Number) left).doubleValue();
        double rightValue = ((Number) right).doubleValue();

        // Perform the comparison based on the operator
        return switch (operator) {
            case "==" -> leftValue == rightValue;
            case "!=" -> leftValue != rightValue;
            case ">" -> leftValue > rightValue;
            case "<" -> leftValue < rightValue;
            case ">=" -> leftValue >= rightValue;
            case "<=" -> leftValue <= rightValue;
            default -> throw new Exception("Invalid comparison operator: " + operator);
        };
    }


    private static void handlePrint(String input) throws Exception {
        String content = input.substring(6, input.length() - 1).trim();

        Object result = evaluateExpression(content);

        if (result instanceof Double) {
            Double doubleResult = (Double) result;
            if (doubleResult == doubleResult.intValue()) {
                System.out.println(doubleResult.intValue());
            } else {
                System.out.println(doubleResult);
            }
        } else {
            System.out.println(result);
        }
    }

    private static Object evaluateExpression(String input) throws Exception {
        if (variables.containsKey(input)) {
            return variables.get(input);
        } else if (isNumeric(input)) {
            return Double.parseDouble(input);
        } else if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else {
            return handleArithmetic(input, true);
        }
    }

    private static Object handleArithmetic(String input, boolean evaluate) throws Exception {
        String[] tokens = input.split("(?=[-+*/])|(?<=[-+*/])");

        if (tokens.length == 1) {
            return evaluateExpression(tokens[0].trim());
        }

        String currentOperator = "+";
        Object result = evaluateExpression(tokens[0].trim());

        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (isOperator(token)) {
                currentOperator = token;
            } else {
                Object operand = evaluateExpression(token);

                if (result instanceof String && operand instanceof String) {
                    if (currentOperator.equals("+")) {
                        result = (String) result + (String) operand;
                    } else {
                        throw new Exception("Invalid operation between strings: " + currentOperator);
                    }
                } else if (result instanceof Number && operand instanceof Number) {
                    double num1 = ((Number) result).doubleValue();
                    double num2 = ((Number) operand).doubleValue();

                    switch (currentOperator) {
                        case "+" -> result = num1 + num2;
                        case "-" -> result = num1 - num2;
                        case "*" -> result = num1 * num2;
                        case "/" -> {
                            if (num2 == 0) throw new Exception("Division by zero is not allowed");
                            result = num1 / num2;
                        }
                        default -> throw new Exception("Unknown operator: " + currentOperator);
                    }
                } else {
                    throw new Exception("Invalid operation between types");
                }
            }
        }

        return result;
    }
    private static void handleWhile(String input) throws Exception {
        // Ensure the syntax is correct (contains ':')
        if (!input.contains(":")) {
            throw new Exception("Invalid while syntax. Expected ':' to separate condition and statements.");
        }

        // Split the condition and the block of statements
        String[] parts = input.split(":", 2);
        if (parts.length < 2) {
            throw new Exception("Invalid while syntax. Missing statements after ':'.");
        }

        String condition = parts[0].substring(5).trim(); // Extract the condition (remove "while")
        String statements = parts[1].trim(); // Extract the block of statements

        // Limit the number of iterations to prevent infinite loops
        int iterationLimit = 1000;
        int iterations = 0;

        // Loop until the condition evaluates to false
        while (true) {
            // Dynamically evaluate the condition before each iteration
            boolean conditionResult = evaluateCondition(condition);

            if (!conditionResult) {
                break; // Exit the loop if the condition becomes false
            }

            // Split the statements by semicolon and interpret each one
            String[] commands = statements.split(";");
            for (String command : commands) {
                interpret(command.trim());
            }

            // Increment the iteration count and check for infinite loops
            iterations++;
            if (iterations > iterationLimit) {
                throw new Exception("Infinite loop detected. Exceeded iteration limit of " + iterationLimit +
                        ". Ensure the condition is changing within the loop.");
            }

            // Re-evaluate the condition for the next iteration
            // No need to reassign condition here, as it will be evaluated again in the next loop
        }
    }








    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}









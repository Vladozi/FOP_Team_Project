import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {

    // A map to store variables and their values
    private static final Map<String, Object> variables = new HashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Basic Python Interpreter");
        System.out.println("Supports variable assignment, print, and basic arithmetic operations (+, -, *, /).");
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

    // Interpret a single line of input
    private static void interpret(String input) throws Exception {
        input = input.trim();

        // Check for variable assignment (e.g., x = 10 or z = x + y)
        if (input.contains("=")) {
            handleAssignment(input);
        }
        // Check for print statements (e.g., print(x), print("Hello"), or print(x + y))
        else if (input.startsWith("print(") && input.endsWith(")")) {
            handlePrint(input);
        }
        // Check for standalone arithmetic expressions (e.g., x + y)
        else {
            handleArithmetic(input, false);
        }
    }

    // Handle variable assignment
    private static void handleAssignment(String input) throws Exception {
        String[] parts = input.split("=", 2);

        if (parts.length != 2) {
            throw new Exception("Invalid assignment syntax");
        }

        String variableName = parts[0].trim();
        String valueExpression = parts[1].trim();

        // Check if the value is a string literal
        if (valueExpression.startsWith("\"") && valueExpression.endsWith("\"")) {
            variables.put(variableName, valueExpression.substring(1, valueExpression.length() - 1)); // Store as string
        } else {
            // Evaluate the right-hand side (could be a literal, variable, or arithmetic expression)
            Object value = evaluateExpression(valueExpression);
            variables.put(variableName, value); // Store the result as either a string or number
        }
    }

    // Handle print statements
    private static void handlePrint(String input) throws Exception {
        String content = input.substring(6, input.length() - 1).trim(); // Extract content inside print()

        // Evaluate the expression (could be a string or number)
        Object result = evaluateExpression(content);

        // Check if the result is a whole number and print it as an integer if possible
        if (result instanceof Double) {
            Double doubleResult = (Double) result;
            if (doubleResult == doubleResult.intValue()) {
                // Print as integer if no decimal part
                System.out.println(doubleResult.intValue());
            } else {
                System.out.println(doubleResult);
            }
        } else {
            System.out.println(result); // Print as is for other values
        }
    }

    // Evaluate an expression (could be numeric or string)
    private static Object evaluateExpression(String input) throws Exception {
        if (variables.containsKey(input)) {
            return variables.get(input); // Return the stored variable value
        } else if (isNumeric(input)) {
            return Double.parseDouble(input); // Return number if it's a literal number
        } else if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1); // Return the string literal
        } else {
            // It's an arithmetic or concatenation expression
            return handleArithmetic(input, true);
        }
    }

    // Handle arithmetic operations (e.g., x + y - z or string concatenation)
    private static Object handleArithmetic(String input, boolean evaluate) throws Exception {
        // Remove extra spaces and split by operator (+, -, *, /)
        String[] tokens = input.split("(?=[-+*/])|(?<=[-+*/])");

        // If the expression is a single token (no operator), evaluate directly
        if (tokens.length == 1) {
            return evaluateExpression(tokens[0].trim());
        }

        // Process the expression, handle addition as string concatenation or numeric addition
        String currentOperator = "+";
        Object result = evaluateExpression(tokens[0].trim()); // Start with the first operand

        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (isOperator(token)) {
                currentOperator = token;  // Update operator
            } else {
                Object operand = evaluateExpression(token);  // Evaluate the next operand

                if (result instanceof String && operand instanceof String) {
                    // String concatenation
                    if (currentOperator.equals("+")) {
                        result = (String) result + (String) operand;
                    } else {
                        throw new Exception("Invalid operation between strings: " + currentOperator);
                    }
                } else if (result instanceof String && operand instanceof Number && currentOperator.equals("+")) {
                    // String and Number concatenation
                    result = (String) result + operand.toString();
                } else if (result instanceof Number && operand instanceof Number) {
                    // Numeric operation
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
                    throw new Exception("Invalid operation between types: " + result.getClass() + " and " + operand.getClass());
                }
            }
        }

        return result;
    }

    // Check if the token is a number (including decimals)
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); // Try to parse as a number
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if the token is an operator
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}









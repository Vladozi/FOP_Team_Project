package FOP_Team_Project;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    private Map<String, Object> variables;

    public Main() {
        variables = new HashMap<>();
    }

    public void assignVariable(String varName, Object value) {
        variables.put(varName, value);
    }

    public Object getVariableValue(String varName) {
        return variables.getOrDefault(varName, null); // Default to null if variable not found
    }

    public Object evaluateExpression(String expression) {
        // Check for empty input
        if (expression.trim().isEmpty()) {
            return null; // Do nothing for empty input
        }

        // Check for print command
        if (expression.startsWith("print(") && expression.endsWith(")")) {
            String varName = expression.substring(6, expression.length() - 1).trim();
            return Methods.printVariable(varName, variables); // Use the print method from Methods class
        }

        // Check for variable assignment
        if (expression.contains("=")) {
            String[] parts = expression.split("=");
            String varName = parts[0].trim();
            Object value = evaluateExpression(parts[1].trim());
            assignVariable(varName, value);
            return null; // Do not return anything for assignments
        }

        // Check for addition
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            Object left = getValue(parts[0].trim());
            Object right = getValue(parts[1].trim());
            return addValues(left, right);
        }

        // If it's just a number or variable, return its value
        return getValue(expression.trim());
    }

    private Object getValue(String token) {
        // Check if the token is a variable
        if (variables.containsKey(token)) {
            return getVariableValue(token);
        }
        // Try to parse as Integer
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e1) {
            // Try to parse as Float
            try {
                return Float.parseFloat(token);
            } catch (NumberFormatException e2) {
                // Check if the token is a string (must be in quotes)
                if (token.startsWith("\"") && token.endsWith("\"")) {
                    return token.substring(1, token.length() - 1); // Remove quotes
                } else {
                    // Throw an error if the token is not a valid string
                    throw new RuntimeException("String values must be enclosed in double quotes.");
                }
            }
        }
    }

    private Object addValues(Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) {
            return (Integer) left + (Integer) right;
        } else if (left instanceof Float && right instanceof Float) {
            return (Float) left + (Float) right;
        } else if (left instanceof Integer && right instanceof Float) {
            return (Integer) left + (Float) right;
        } else if (left instanceof Float && right instanceof Integer) {
            return (Float) left + (Integer) right;
        } else {
            throw new RuntimeException("Unsupported operation for types: " + left.getClass() + " and " + right.getClass());
        }
    }

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simple Interpreter. Type 'exit' to quit.");
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                Object result = interpreter.evaluateExpression(command);
                if (result != null) {
                    System.out.println(result); // Only print results that are not null
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1); // Exit the program on error
            }
        }

        scanner.close();
    }
}
package FOP_Team_Project;

import java.util.*;

public class Main {

    List<String> variableNames = new ArrayList<>();
    List<String> variableValues = new ArrayList<>();

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Python-like Interpreter. Type 'exit' to quit.");

        Stack<Boolean> executionStack = new Stack<>();
        boolean executeCurrentBlock = true; // Global execution flag
        int currentIndent = 0;             // Track current indentation level
        Stack<Integer> indentStack = new Stack<>();

        while (true) {
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
                    executeCurrentBlock = executionStack.pop();
                }

                // IF statement
                if (line.startsWith("if ") && line.endsWith(":")) {
                    String condition = line.substring(3, line.length() - 1).trim(); // Remove 'if ' and ':'
                    String[] parts = condition.split("==");

                    if (parts.length == 2) {
                        String left = interpreter.getValue(parts[0].trim());
                        String right = interpreter.getValue(parts[1].trim());

                        boolean conditionResult = left.equals(right);

                        // Push block state
                        executionStack.push(executeCurrentBlock);
                        executeCurrentBlock = executeCurrentBlock && conditionResult;
                        indentStack.push(indentLevel);
                    } else {
                        throwError("Invalid if condition.");
                    }
                    continue;
                }

                // ELSE statement
                if (line.startsWith("else:")) {
                    if (executionStack.isEmpty()) {
                        throwError("Unexpected else.");
                    }

                    boolean previousState = executionStack.pop();
                    executionStack.push(previousState); // Push it back

                    // Execute ELSE only if previous IF failed
                    executeCurrentBlock = !previousState && executeCurrentBlock;
                    indentStack.push(indentLevel);
                    continue;
                }

                // Skip lines in inactive block
                if (!executeCurrentBlock) {
                    continue;
                }

                // Print statement
                if (line.startsWith("print(") && line.endsWith(")")) {
                    String varName = line.substring(6, line.length() - 1).trim();
                    interpreter.printVariable(varName);
                    continue;
                }

                // Assignment
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    if (parts.length != 2) {
                        throwError("Invalid assignment.");
                    }

                    String varName = parts[0].trim();
                    String value = parts[1].trim();

                    interpreter.assignVariable(varName, value);
                    continue;
                }

                throwError("Invalid command.");

            } catch (Exception e) {
                throwError(e.getMessage());
            }
        }

        scanner.close();
    }

    void assignVariable(String varName, String value) {
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

    boolean isInteger(String value) {
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

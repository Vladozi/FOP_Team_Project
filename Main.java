package FOP_Team_Project;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(System.in);

        Stack<Boolean> executionStack = new Stack<>();
        boolean executeCurrentBlock = true; // Global execution flag
        Stack<Integer> indentStack = new Stack<>();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            // Skip empty input
            if (input.trim().isEmpty()) continue;

            try {
                // Process the line
                interpreter.processLine(input, executionStack, indentStack);
            } catch (Exception e) {
                Interpreter.throwError(e.getMessage());
            }
        }

        scanner.close();
    }
}

class Interpreter {

    private List<String> variableNames = new ArrayList<>();
    private List<String> variableValues = new ArrayList<>();
    private boolean executeCurrentBlock = true;

    void processLine(String input, Stack<Boolean> executionStack, Stack<Integer> indentStack) {
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
                String left = getValue(parts[0].trim());
                String right = getValue(parts[1].trim());

                boolean conditionResult = left.equals(right);

                // Push block state
                executionStack.push(executeCurrentBlock);
                executeCurrentBlock = executeCurrentBlock && conditionResult;
                indentStack.push(indentLevel);
            } else {
                throwError("Invalid if condition.");
            }
            return;
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
            return;
        }

        // Skip lines in inactive block
        if (!executeCurrentBlock) {
            return;
        }

        // Print statement
        if (line.startsWith("print(") && line.endsWith(")")) {
            String varName = line.substring(6, line.length() - 1).trim();
            printVariable(varName);
            return;
        }

        // Assignment
        if (line.contains("=")) {
            String[] parts = line.split("=");
            if (parts.length != 2) {
                throwError("Invalid assignment.");
            }

            String varName = parts[0].trim();
            String value = parts[1].trim();

            assignVariable(varName, value);
            return;
        }

        throwError("Invalid command.");
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

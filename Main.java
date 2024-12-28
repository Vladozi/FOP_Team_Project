package FOP_Team_Project;
import FOP_Team_Project.Sum;
import java.util.*;

public class Main {

    // Instance variables for managing variable names and values
    static List<String> variableNames = new ArrayList<>();
    static List<String> variableValues = new ArrayList<>();
    boolean executeCurrentBlock = true; // Control flow for block execution

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

                // Handle different statements
                if (line.startsWith("if ") && line.endsWith(":")) {
                    interpreter.handleIfStatement(line, indentLevel, executionStack, indentStack);
                    continue;
                }

                if (line.startsWith("else:")) {
                    interpreter.handleElseStatement(executionStack, indentStack, indentLevel);
                    continue;
                }

                if (line.startsWith("while ") && line.endsWith(":")) {
                    interpreter.handleWhileStatement(line, indentLevel, executionStack, indentStack, scanner);
                    continue;
                }

                // Handle sum calculation (e.g., "sum = 10")
                if (line.startsWith("sum =")) {
                    Sum sumInstance = new Sum();
                    sumInstance.sumOfFirstNNumbers(line);
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

    // Handle the IF statement logic
    void handleIfStatement(String line, int indentLevel, Stack<Boolean> executionStack, Stack<Integer> indentStack) {
        String condition = line.substring(3, line.length() - 1).trim(); // Remove 'if ' and ':'
        boolean conditionResult = evaluateCondition(condition);

        // Push block state
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
        executionStack.push(previousState); // Push it back

        // Execute ELSE only if previous IF failed
        executeCurrentBlock = !previousState && executeCurrentBlock;
        indentStack.push(indentLevel);
    }

    // Handle WHILE statement logic
    void handleWhileStatement(String line, int indentLevel, Stack<Boolean> executionStack, Stack<Integer> indentStack, Scanner scanner) {
        String condition = line.substring(6, line.length() - 1).trim(); // Remove 'while ' and ':'

        // Push block state for while loop
        executionStack.push(executeCurrentBlock);
        boolean conditionResult = evaluateCondition(condition);

        while (conditionResult && executeCurrentBlock) {
            // Read the next line of input
            String nextLine = scanner.nextLine();
            int nextIndentLevel = nextLine.length() - nextLine.stripLeading().length();

            // Check for indentation to ensure it's part of the while block
            if (nextIndentLevel > indentLevel) {
                // Execute the line inside the loop
                if (nextLine.startsWith("print(") && nextLine.endsWith(")")) {
                    String varName = nextLine.substring(6, nextLine.length() - 1).trim();
                    printVariable(varName);
                } else if (nextLine.contains("=")) {
                    handleAssignment(nextLine);
                } else {
                    throwError("Invalid command inside while loop.");
                }
            } else {
                // Break the loop if indentation level is not correct
                break;
            }

            // Re-evaluate the condition inside the loop
            conditionResult = evaluateCondition(condition);
        }
    }

    // Evaluate condition for loops and if statements
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

    // Handle assignment of variables
    void handleAssignment(String line) {
        String[] parts = line.split("=");
        if (parts.length != 2) {
            throwError("Invalid assignment.");
        }

        String varName = parts[0].trim();
        String value = parts[1].trim();

        assignVariable(varName, value);
    }

    // Static method to assign values to variables
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

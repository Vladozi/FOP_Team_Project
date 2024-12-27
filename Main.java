package FOP_Team_Project;

import java.util.Scanner;

public class Main {
    // Node class to store variable name and value
    static class VariableNode {
        String name;
        String value;
        VariableNode next;

        VariableNode(String name, String value) {
            this.name = name;
            this.value = value;
            this.next = null;
        }
    }

    VariableNode head = null; // Head of the linked list

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simple Interpreter. Type 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            // Exit condition
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Skip empty input
            if (input.isEmpty()) continue;

            try {
                // Print statement
                if (input.startsWith("print(") && input.endsWith(")")) {
                    String varName = input.substring(6, input.length() - 1).trim();
                    interpreter.printVariable(varName);
                    continue;
                }

                // Assignment
                if (input.contains("=")) {
                    String[] parts = input.split("=");
                    if (parts.length != 2) {
                        throwError("Invalid assignment.");
                    }

                    String varName = parts[0].trim();
                    String value = parts[1].trim();

                    interpreter.assignVariable(varName, value);
                    continue;
                }

                // Addition
                if (input.contains("+")) {
                    String[] parts = input.split("\\+");
                    if (parts.length != 2) {
                        throwError("Invalid addition expression.");
                    }

                    String left = interpreter.getValue(parts[0].trim());
                    String right = interpreter.getValue(parts[1].trim());

                    // Perform addition but don't print directly
                    String result = interpreter.addValues(left, right);
                    interpreter.assignVariable("result", result); // Store the result for printing
                    continue;
                }

                // Invalid input
                throwError("Invalid command.");

            } catch (Exception e) {
                throwError(e.getMessage());
            }
        }

        scanner.close();
    }

    // Assigns a variable
    void assignVariable(String varName, String value) {
        // Enforce integer or strings with double quotes
        if (!isInteger(value) && !(value.startsWith("\"") && value.endsWith("\""))) {
            throwError("Only integers or strings in double quotes are allowed.");
        }

        VariableNode current = head;
        while (current != null) {
            if (current.name.equals(varName)) {
                current.value = value; // Update existing variable
                return;
            }
            current = current.next;
        }

        // Add new variable
        VariableNode newNode = new VariableNode(varName, value);
        newNode.next = head;
        head = newNode;
    }

    // Prints variable value
    void printVariable(String varName) {
        VariableNode current = head;
        while (current != null) {
            if (current.name.equals(varName)) {
                System.out.println(current.value);
                return;
            }
            current = current.next;
        }
        throwError("Variable not defined.");
    }

    // Evaluates and returns a value
    String getValue(String token) {
        // Check if token is a variable
        VariableNode current = head;
        while (current != null) {
            if (current.name.equals(token)) {
                return current.value;
            }
            current = current.next;
        }

        // Check if it's an integer
        if (isInteger(token)) {
            return token;
        }

        // Check if it's a string
        if (token.startsWith("\"") && token.endsWith("\"")) {
            return token.substring(1, token.length() - 1); // Remove quotes
        }

        throwError("Invalid value.");
        return null; // Unreachable, added for compiler
    }

    // Adds two values and returns the result instead of printing
    String addValues(String left, String right) {
        if (isInteger(left) && isInteger(right)) {
            int num1 = Integer.parseInt(left);
            int num2 = Integer.parseInt(right);
            return String.valueOf(num1 + num2);  // Return the sum as a string
        } else {
            throwError("Addition only supports integers.");
        }
        return null; // Unreachable
    }

    // Checks if string is an integer
    boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Throws an error and exits the program
    static void throwError(String message) {
        System.out.println("Error: " + message);
        System.exit(1); // Close program on error
    }
}

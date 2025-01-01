package FOP_Team_Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private Map<String, Integer> variables = new HashMap<>();

    public void interpret(String code) {
        String[] lines = code.split("\n");
        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();
            if (line.contains("=") && !line.contains("==")) {
                handleAssignment(line);
            } else if (line.startsWith("if")) {
                i = handleIfElse(lines, i);
            } else if (line.startsWith("while")) {
                i = handleWhileLoop(lines, i); // Handle while loop
            } else if (line.startsWith("print(") && line.endsWith(")")) {
                handlePrint(line);
            } else if (!line.isEmpty()) { // Ignore empty lines
                System.out.println("Error: Unknown command: " + line);
            }
            i++;
        }
    }

    private void handleAssignment(String line) {
        String[] parts = line.split("=");
        if (parts.length != 2) {
            System.out.println("Error: Invalid assignment syntax: " + line);
            return;
        }
        String varName = parts[0].trim();
        int value = evaluateExpression(parts[1].trim());
        variables.put(varName, value);
    }

    private void handlePrint(String line) {
        String content = line.substring(6, line.length() - 1).trim(); // Extract content inside parentheses
        if (content.startsWith("\"") && content.endsWith("\"")) {
            System.out.println(content.substring(1, content.length() - 1));
        } else if (variables.containsKey(content)) {
            System.out.println(variables.get(content));
        } else {
            try {
                System.out.println(Integer.parseInt(content));
            } catch (NumberFormatException e) {
                System.out.println("Error: Unknown variable or invalid value: " + content);
            }
        }
    }

    private int handleIfElse(String[] lines, int currentIndex) {
        String condition = lines[currentIndex].trim().substring(2).trim(); // Extract condition after "if"
        condition = cleanCondition(condition); // Remove the trailing colon
        boolean isConditionTrue = evaluateCondition(condition);

        // Process the true block
        int nextIndex = currentIndex + 1;
        while (nextIndex < lines.length && isIndented(lines[nextIndex])) {
            if (isConditionTrue) {
                interpret(lines[nextIndex].trim());
            }
            nextIndex++;
        }

        // Process the else block if exists, and make sure it's associated with the last `if`
        if (nextIndex < lines.length && lines[nextIndex].trim().startsWith("else")) {
            nextIndex++; // Skip "else:"
            while (nextIndex < lines.length && isIndented(lines[nextIndex])) {
                if (!isConditionTrue) {
                    interpret(lines[nextIndex].trim());
                }
                nextIndex++;
            }
        }

        return nextIndex - 1; // Adjust index to point to the last processed line
    }

    private int handleWhileLoop(String[] lines, int currentIndex) {
        String condition = lines[currentIndex].trim().substring(5).trim(); // Extract condition after "while"
        condition = cleanCondition(condition); // Remove the trailing colon

        // Evaluate the while loop condition
        boolean isConditionTrue = evaluateCondition(condition);

        int nextIndex = currentIndex + 1;
        while (nextIndex < lines.length && isIndented(lines[nextIndex])) {
            if (isConditionTrue) {
                interpret(lines[nextIndex].trim());
            } else {
                break; // Exit the loop if the condition is false
            }
            nextIndex++;
        }

        // Reevaluate the condition and repeat if it's still true
        while (isConditionTrue) {
            isConditionTrue = evaluateCondition(condition);
            if (isConditionTrue) {
                nextIndex = currentIndex + 1;
                while (nextIndex < lines.length && isIndented(lines[nextIndex])) {
                    interpret(lines[nextIndex].trim());
                    nextIndex++;
                }
            }
        }

        return nextIndex - 1; // Return adjusted index to prevent skipping lines
    }

    private String cleanCondition(String condition) {
        // Remove any trailing colons and unnecessary spaces
        return condition.trim().replace(":", "").trim();
    }

    private boolean evaluateCondition(String condition) {
        String[] tokens = condition.split(" ");
        if (tokens.length == 2 && tokens[0].equals("not")) {
            return !evaluateCondition(tokens[1]);
        } else if (tokens.length == 3) {
            int left = getValue(tokens[0]);
            int right = getValue(tokens[2]);
            switch (tokens[1]) {
                case ">":
                    return left > right;
                case "<":
                    return left < right;
                case "==":
                    return left == right;
                case "!=":
                    return left != right;
                case ">=": // Handle greater than or equal to
                    return left >= right;
                case "<=": // Handle less than or equal to
                    return left <= right;
                default:
                    System.out.println("Error: Unknown operator in condition: " + tokens[1]);
                    return false;
            }
        }
        System.out.println("Error: Invalid condition syntax: " + condition);
        return false;
    }

    private boolean isIndented(String line) {
        return line.startsWith("    "); // 4 spaces indicate indentation
    }

    private int evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        int result = getValue(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            int nextValue = getValue(tokens[i + 1]);
            switch (operator) {
                case "+":
                    result += nextValue;
                    break;
                case "-":
                    result -= nextValue;
                    break;
                case "*":
                    result *= nextValue;
                    break;
                case "/":
                    result /= nextValue;
                    break;
                case "%":
                    result %= nextValue;
                    break;
                default:
                    System.out.println("Error: Unknown operator: " + operator);
                    return 0;
            }
        }
        return result;
    }

    private int getValue(String token) {
        if (variables.containsKey(token)) {
            return variables.get(token);
        }
        return Integer.parseInt(token);
    }

    public static void main(String[] args) {
        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);
        StringBuilder code = new StringBuilder();

        try {
            // Check if standard input is redirected (e.g., from a file)
            if (System.in.available() > 0) {
                // Input is redirected, read from System.in
                Scanner inputScanner = new Scanner(System.in);
                while (inputScanner.hasNextLine()) {
                    code.append(inputScanner.nextLine()).append("\n");
                }
                inputScanner.close();
                interpreter.interpret(code.toString());  // Interpret the code from the file or input stream
            } else if (args.length > 0) {
                // File input if passed in args
                try {
                    File file = new File(args[0]);
                    Scanner fileScanner = new Scanner(file);
                    while (fileScanner.hasNextLine()) {
                        code.append(fileScanner.nextLine()).append("\n");
                    }
                    fileScanner.close();
                    interpreter.interpret(code.toString());
                } catch (FileNotFoundException e) {
                    System.out.println("Error: File not found: " + args[0]);
                }
            } else {
                // Interactive input if no file or redirected input is found
                System.out.println("Enter your code (type 'exit' to quit):");
                String input;
                while (!(input = scanner.nextLine()).equals("exit")) {
                    code.append(input).append("\n");
                }
                interpreter.interpret(code.toString());
            }
        } catch (IOException e) {
            System.out.println("Error: IOException occurred - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Unexpected error occurred - " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
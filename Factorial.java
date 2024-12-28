package FOP_Team_Project;

public class Factorial {

    // Method to calculate factorial
    public void calculateFactorial(String line) {
        // Extract the value of N from the input
        String[] parts = line.split("="); // Assuming input is like: factorial = 5
        if (parts.length != 2) {
            Main.throwError("Invalid assignment for factorial calculation.");
        }

        String varName = parts[0].trim(); // Extract variable name
        String valueStr = parts[1].trim(); // Extract the value

        // Parse the integer value of N
        int N;
        try {
            N = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            Main.throwError("Invalid number for factorial calculation.");
            return;
        }

        // Compute factorial
        int result = 1;
        for (int i = 1; i <= N; i++) {
            result *= i;
        }

        // Store result in the variable and print
        Main.assignVariable(varName, String.valueOf(result));
        System.out.println("Factorial of " + N + " is: " + result);
    }
}

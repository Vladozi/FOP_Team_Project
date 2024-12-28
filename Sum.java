package FOP_Team_Project;

public class Sum {
    void sumOfFirstNNumbers(String line) {
        // Extract the value of N from the input
        String[] parts = line.split("="); // Assuming input is like: sum = 10
        if (parts.length != 2) {
            Main.throwError("Invalid assignment for sum calculation.");
        }

        String varName = parts[0].trim(); // Extract variable name
        String valueStr = parts[1].trim(); // Extract the value

        int N = Integer.parseInt(valueStr); // Parse the integer value of N
        int sum = 0;

        // Loop to calculate the sum of the first N natural numbers
        for (int i = 1; i <= N; i++) {
            sum += i;
        }

        // Print the result (if the variable name is "sum", output it directly)
        Main.assignVariable(varName, String.valueOf(sum));  // Call Main's static method for variable assignment
        System.out.println("Sum of first " + N + " numbers is: " + sum);
    }
}

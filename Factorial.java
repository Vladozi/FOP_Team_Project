package FOP_Team_Project;

public class Factorial {

    // Method to calculate factorial
    public int calculateFactorial(int N) {
        // Compute factorial
        int result = 1;
        for (int i = 1; i <= N; i++) {
            result *= i;
        }
        return result;
    }
}

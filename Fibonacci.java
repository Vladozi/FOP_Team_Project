package FOP_Team_Project;

public class Fibonacci {

    // Method to calculate the Nth Fibonacci number using iteration
    public int calculateFibonacci(int n) {
        if (n <= 1) {
            return n;
        }

        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }
}

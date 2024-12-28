package FOP_Team_Project;

public class GCD {

    // Method to compute the GCD using the Euclidean algorithm
    public int calculateGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}

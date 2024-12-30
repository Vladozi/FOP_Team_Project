package FOP_Team_Project;

public class Sum {
    int sumOfFirstNNumbers(int N) {
        int sum = 0;

        // Loop to calculate the sum of the first N natural numbers
        for (int i = 1; i <= N; i++) {
            sum += i;
        }

        return sum;
    }

    public static void main(String[] args) {

    }
}
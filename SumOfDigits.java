package FOP_Team_Project;

public class SumOfDigits {

    // Method to calculate the sum of digits in a number
    public int sumOfDigits(int number) {
        number = Math.abs(number);  // Ignore the negative sign by using absolute value
        int sum = 0;

        while (number > 0) {
            sum += number % 10;  // Add the last digit to the sum
            number /= 10;         // Remove the last digit
        }

        return sum;
    }
}

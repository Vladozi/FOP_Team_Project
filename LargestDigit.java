package FOP_Team_Project;

public class LargestDigit {

    // Method to find the largest digit in a number
    public int findLargestDigit(int number) {
        number = Math.abs(number);  // Ignore the negative sign by using absolute value
        int largestDigit = 0;

        while (number > 0) {
            int digit = number % 10;
            if (digit > largestDigit) {
                largestDigit = digit;
            }
            number /= 10;
        }
        return largestDigit;
    }
}

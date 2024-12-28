package FOP_Team_Project;

public class ReverseNumber {

    // Method to reverse the digits of a number
    public int reverse(int number) {
        int reversed = 0;
        while (number != 0) {
            int digit = number % 10;
            reversed = reversed * 10 + digit;
            number /= 10;
        }
        return reversed;
    }
}
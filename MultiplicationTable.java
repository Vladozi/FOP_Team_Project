package FOP_Team_Project;

public class MultiplicationTable {

    // Method to print the multiplication table up to 10 for a given number
    public void printTable(int number) {
        for (int i = 1; i <= 10; i++) {
            System.out.println(number + " x " + i + " = " + (number * i));
        }
    }
}

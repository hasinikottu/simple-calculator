import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("==============================");
        System.out.println("   Simple Console Calculator   ");
        System.out.println("==============================\n");

        while (true) {
            printMenu();
            int choice = readInt(sc, "Choose an option (1-6): ");

            if (choice == 6) {
                System.out.println("Goodbye! 👋");
                break;
            }

            double a = readDouble(sc, "Enter first number: ");
            double b = readDouble(sc, "Enter second number: ");

            switch (choice) {
                case 1:
                    System.out.println("Result: " + (a + b));
                    break;
                case 2:
                    System.out.println("Result: " + (a - b));
                    break;
                case 3:
                    System.out.println("Result: " + (a * b));
                    break;
                case 4:
                    if (b == 0) {
                        System.out.println("Error: Division by zero is not allowed.");
                    } else {
                        System.out.println("Result: " + (a / b));
                    }
                    break;
                case 5:
                    if (b == 0) {
                        System.out.println("Error: Modulo by zero is not allowed.");
                    } else {
                        System.out.println("Result: " + (a % b));
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please pick 1-6.");
            }

            System.out.println();
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("1) Add");
        System.out.println("2) Subtract");
        System.out.println("3) Multiply");
        System.out.println("4) Divide");
        System.out.println("5) Modulo");
        System.out.println("6) Exit");
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a whole number.");
                sc.next(); // clear invalid token
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number (e.g., 12 or 3.5).");
                sc.next(); // clear invalid token
            }
        }
    }
}

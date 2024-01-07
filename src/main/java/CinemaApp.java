import java.util.Scanner;

public class CinemaApp {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    private static final int NUM_HALLS = 5;
    private static final int ROWS = 10;
    private static final int SEATS_PER_ROW = 20;
    private static final int[][][] cinemaSeats = new int[NUM_HALLS][ROWS][SEATS_PER_ROW];

    public static void initializeSeats() {
        for (int i = 0; i < NUM_HALLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                for (int k = 0; k < SEATS_PER_ROW; k++) {
                    cinemaSeats[i][j][k] = 0; // 0 represents an available seat
                }
            }
        }
    }

    public static void bookSeats(int hallNumber, int row, int[] seats) {
        for (int seat : seats) {
            if (cinemaSeats[hallNumber][row][seat] == 0) {
                cinemaSeats[hallNumber][row][seat] = 1; // 1 represents a booked seat
            } else {
                System.out.println("Seat " + seat + " in row " + row + " of hall " + hallNumber + " is already booked.");
            }
        }
    }

    public static void cancelBooking(int hallNumber, int row, int[] seats) {
        boolean bookingExists = false;
        for (int seat : seats) {
            if (seat >= 0 && seat < SEATS_PER_ROW && cinemaSeats[hallNumber][row][seat] == 1) {
                cinemaSeats[hallNumber][row][seat] = 0;
                bookingExists = true;
            }
        }

        if (bookingExists) {
            System.out.println("All seats in row " + (row + 1) + " of hall " + (hallNumber + 1) + " are now available.");
        } else {
            System.out.println("No booking found in the specified seats.");
        }
    }

    public static boolean checkAvailability(int hallNumber, int numSeats) {
        for (int i = 0; i < ROWS; i++) {
            int count = 0;
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (cinemaSeats[hallNumber][i][j] == 0) {
                    count++;
                    if (count == numSeats) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }

    public static void printSeatingArrangement(int hallNumber) {
        System.out.println("Seating Arrangement for Hall " + (hallNumber + 1) + ":");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (cinemaSeats[hallNumber][i][j] == 0) {
                    System.out.print(GREEN + "0 " + RESET); // 0 represents an available seat
                } else {
                    System.out.print(RED + "1 " + RESET); // 1 represents a booked seat
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        initializeSeats();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Доступні зали: 1, 2, 3, 4, 5");
            System.out.print("Виберіть номер залу для бронювання (або 0 для виходу): ");
            int selectedHall = scanner.nextInt();

            if (selectedHall == 0) {
                break;
            }

            if (selectedHall >= 1 && selectedHall <= NUM_HALLS) {
                System.out.println("Ви обрали зал номер " + selectedHall);

                System.out.print("Введіть номер ряду для бронювання: ");
                int selectedRow = scanner.nextInt();

                if (selectedRow >= 1 && selectedRow <= ROWS) {
                    System.out.println("Ви обрали ряд номер " + selectedRow);

                    System.out.print("Введіть номери місць для бронювання (розділені пробілами): ");
                    scanner.nextLine(); // Очистити буфер після введення чисел
                    String input = scanner.nextLine();
                    String[] seatNumbers = input.split(" ");

                    int[] seatsToBook = new int[seatNumbers.length];
                    for (int i = 0; i < seatNumbers.length; i++) {
                        seatsToBook[i] = Integer.parseInt(seatNumbers[i]) - 1; // -1 тому що масиви індексуються з 0
                    }

                    bookSeats(selectedHall - 1, selectedRow - 1, seatsToBook); // -1 тому що масиви індексуються з 0
                    printSeatingArrangement(selectedHall - 1); // Вивід схеми розміщення для обраного залу
                } else {
                    System.out.println("Невірний номер ряду.");
                }
            } else {
                System.out.println("Невірний номер залу.");
            }

            System.out.print("Хочете перевірити доступність місць? (Так/Ні): ");
            String checkAvailabilityChoice = scanner.next();
            if (checkAvailabilityChoice.equalsIgnoreCase("Так")) {
                System.out.print("Введіть кількість місць для перевірки: ");
                int numSeats = scanner.nextInt();
                if (checkAvailability(selectedHall - 1, numSeats)) {
                    System.out.println("Доступні " + numSeats + " послідовних місць у залі " + selectedHall);
                } else {
                    System.out.println("Доступних " + numSeats + " послідовних місць немає у залі " + selectedHall);
                }
            }

            System.out.print("Хочете скасувати бронювання? (Так/Ні): ");
            String cancelBookingChoice = scanner.next();
            if (cancelBookingChoice.equalsIgnoreCase("Так")) {
                System.out.print("Введіть номер ряду для скасування бронювання: ");
                int cancelRow = scanner.nextInt();

                System.out.print("Введіть номери місць для скасування бронювання (розділені пробілами): ");
                scanner.nextLine(); // Очистити буфер після введення чисел
                String cancelInput = scanner.nextLine();
                String[] cancelSeatNumbers = cancelInput.split(" ");

                int[] seatsToCancel = new int[cancelSeatNumbers.length];
                for (int i = 0; i < cancelSeatNumbers.length; i++) {
                    seatsToCancel[i] = Integer.parseInt(cancelSeatNumbers[i]) - 1; // -1 тому що масиви індексуються з 0
                }

                cancelBooking(selectedHall - 1, cancelRow - 1, seatsToCancel); // -1 тому що масиви індексуються з 0
                printSeatingArrangement(selectedHall - 1); // Вивід оновленої схеми розміщення для обраного залу
            }
        }

        scanner.close();
    }
}

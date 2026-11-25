import java.util.*;

class Passenger {
    int id;
    String name;
    int age;

    public Passenger(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Passenger ID: " + id +
                ", Name: " + name +
                ", Age: " + age;
    }
}

class Train {
    int trainNo;
    String trainName;
    String source;
    String destination;
    int fare;
    int availableSeats;

    public Train(int trainNo, String trainName, String source,
                 String destination, int fare, int availableSeats) {

        this.trainNo = trainNo;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Train No: " + trainNo +
                ", Name: " + trainName +
                ", Route: " + source + " -> " + destination +
                ", Fare: " + fare +
                ", Seats: " + availableSeats;
    }
}

class Ticket {
    String pnr;
    Passenger passenger;
    Train train;

    public Ticket(String pnr, Passenger passenger, Train train) {
        this.pnr = pnr;
        this.passenger = passenger;
        this.train = train;
    }

    @Override
    public String toString() {
        return "\nPNR: " + pnr +
                "\nPassenger: " + passenger.name +
                "\nTrain: " + train.trainName +
                "\nRoute: " + train.source +
                " -> " + train.destination;
    }
}

public class RailwayReservationSystem {

    static HashMap<Integer, Train> trainMap = new HashMap<>();
    static HashMap<Integer, Passenger> passengerMap = new HashMap<>();
    static HashMap<String, Ticket> ticketMap = new HashMap<>();

    static Queue<Passenger> waitingList = new LinkedList<>();

    static HashMap<String, List<String>> graph = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    // ---------------- Graph ----------------

    static void addRoute(String src, String dest) {
        graph.putIfAbsent(src, new ArrayList<>());
        graph.putIfAbsent(dest, new ArrayList<>());

        graph.get(src).add(dest);
        graph.get(dest).add(src);
    }

    static void displayNetwork() {

        System.out.println("\nRailway Network:");

        for (String station : graph.keySet()) {
            System.out.println(station + " -> " + graph.get(station));
        }
    }

    // --------------- Train -----------------

    static void addTrain() {

        System.out.print("Train Number: ");
        int no = sc.nextInt();
        sc.nextLine();

        System.out.print("Train Name: ");
        String name = sc.nextLine();

        System.out.print("Source: ");
        String source = sc.nextLine();

        System.out.print("Destination: ");
        String dest = sc.nextLine();

        System.out.print("Fare: ");
        int fare = sc.nextInt();

        System.out.print("Available Seats: ");
        int seats = sc.nextInt();

        Train train =
                new Train(no, name, source, dest, fare, seats);

        trainMap.put(no, train);

        addRoute(source, dest);

        System.out.println("Train Added Successfully.");
    }

    static void displayTrains() {

        if (trainMap.isEmpty()) {
            System.out.println("No Trains Available.");
            return;
        }

        for (Train t : trainMap.values()) {
            System.out.println(t);
        }
    }

    // --------------- Sorting ----------------

    static void sortByFare() {

        List<Train> trains =
                new ArrayList<>(trainMap.values());

        trains.sort((a, b) ->
                a.fare - b.fare);

        System.out.println("\nSorted By Fare:");

        for (Train t : trains) {
            System.out.println(t);
        }
    }

    // --------------- Booking ----------------

    static void bookTicket() {

        System.out.print("Passenger ID: ");
        int pid = sc.nextInt();
        sc.nextLine();

        System.out.print("Passenger Name: ");
        String name = sc.nextLine();

        System.out.print("Age: ");
        int age = sc.nextInt();

        System.out.print("Train Number: ");
        int trainNo = sc.nextInt();

        Passenger passenger =
                new Passenger(pid, name, age);

        passengerMap.put(pid, passenger);

        Train train = trainMap.get(trainNo);

        if (train == null) {
            System.out.println("Train Not Found.");
            return;
        }

        if (train.availableSeats > 0) {

            train.availableSeats--;

            String pnr =
                    "PNR" + (1000 + ticketMap.size());

            Ticket ticket =
                    new Ticket(pnr,
                            passenger,
                            train);

            ticketMap.put(pnr, ticket);

            System.out.println("\nTicket Confirmed");
            System.out.println(ticket);

        } else {

            waitingList.offer(passenger);

            System.out.println(
                    "Seat Full! Added To Waiting List.");
        }
    }

    // --------------- Cancel ----------------

    static void cancelTicket() {

        System.out.print("Enter PNR: ");
        String pnr = sc.next();

        Ticket ticket = ticketMap.remove(pnr);

        if (ticket == null) {
            System.out.println("PNR Not Found.");
            return;
        }

        ticket.train.availableSeats++;

        System.out.println("Ticket Cancelled.");

        if (!waitingList.isEmpty()) {

            Passenger next =
                    waitingList.poll();

            ticket.train.availableSeats--;

            String newPNR =
                    "PNR" + (1000 + ticketMap.size());

            Ticket newTicket =
                    new Ticket(newPNR,
                            next,
                            ticket.train);

            ticketMap.put(newPNR, newTicket);

            System.out.println(
                    next.name +
                            " moved from waiting list.");
        }
    }

    // --------------- Search ----------------

    static void searchPassenger() {

        System.out.print("Passenger ID: ");
        int id = sc.nextInt();

        Passenger p = passengerMap.get(id);

        if (p == null)
            System.out.println("Passenger Not Found");
        else
            System.out.println(p);
    }

    static void searchTrain() {

        System.out.print("Train Number: ");
        int no = sc.nextInt();

        Train t = trainMap.get(no);

        if (t == null)
            System.out.println("Train Not Found");
        else
            System.out.println(t);
    }

    // --------------- Ticket List ----------------

    static void displayTickets() {

        for (Ticket ticket : ticketMap.values()) {
            System.out.println(ticket);
        }
    }

    // ---------------- Main ----------------

    public static void main(String[] args) {

        trainMap.put(101,
                new Train(101,
                        "Rajdhani",
                        "Delhi",
                        "Mumbai",
                        1500,
                        2));

        trainMap.put(102,
                new Train(102,
                        "Shatabdi",
                        "Delhi",
                        "Jaipur",
                        900,
                        1));

        addRoute("Delhi", "Mumbai");
        addRoute("Delhi", "Jaipur");
        addRoute("Jaipur", "Kota");

        while (true) {

            System.out.println("\n===== Railway Reservation System =====");

            System.out.println("1. Add Train");
            System.out.println("2. Display Trains");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Search Passenger");
            System.out.println("6. Search Train");
            System.out.println("7. Sort Train By Fare");
            System.out.println("8. Show Railway Network");
            System.out.println("9. Display Tickets");
            System.out.println("10. Exit");

            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addTrain();
                    break;

                case 2:
                    displayTrains();
                    break;

                case 3:
                    bookTicket();
                    break;

                case 4:
                    cancelTicket();
                    break;

                case 5:
                    searchPassenger();
                    break;

                case 6:
                    searchTrain();
                    break;

                case 7:
                    sortByFare();
                    break;

                case 8:
                    displayNetwork();
                    break;

                case 9:
                    displayTickets();
                    break;

                case 10:
                    System.out.println("Thank You");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}
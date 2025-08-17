package JDBC.PostgreSQL;

import java.sql.*;
import java.util.*;

class Train {


    public static void registration(Connection con , Scanner sc,int passengers) {
        String query = "INSERT INTO registration(startlocation,destination,phone,seats,passenger_name)VALUES(?,?,?,?,?)";
        try {
            try(PreparedStatement p = con.prepareStatement(query)){

                String name;
                Long phone_number;
                int seats;
                int notduplicate;
                int duplicate;
                Set<Integer> s = new HashSet<>();
                for (int i = 1; i <=passengers; i++) {
                    System.out.println("Please enter your name: ");
                    name = sc.nextLine();
                    Random r = new Random();
                    seats = r.nextInt(99) + 1;
                    notduplicate = seats;
                    duplicate = seats;
                    seats = 0;
                    if (notduplicate != duplicate) {
                        s.add(notduplicate);
                    } else {
                        seats = r.nextInt(99) + 1;
                    }
                    System.out.println("seat for passenger " + i + " : " + seats);
                    System.out.println("Please enter your phone number");
                    phone_number = sc.nextLong();
                    sc.nextLine();
                    System.out.println("Please enter your start location : ");
                    String start = sc.nextLine();

                    System.out.println("Please enter your destination : ");
                    String destination = sc.nextLine();

                    System.out.println("Number of passengers: " + passengers);
                    System.out.println("Your start location : " + start);
                    System.out.println("Your destination : " + destination);
                    p.setString(1,start);
                    p.setString(2,destination);
                    if (phone_number >= 1_000_000_000L && phone_number <= 9_999_999_999L) {
                        p.setLong(3,phone_number);
                    }else{
                        System.out.println("Enter valid phone number");
                    }
                    p.setInt(4,seats);
                    p.setString(5,name);
                    int rowsaffected = p.executeUpdate();
                    if(rowsaffected>0){
                        System.out.println("Data saved successfully");
                    }else{
                        System.out.println("Something went wrong");
                    }
                }

            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void payment(Connection con, Scanner sc, int passengers){
        String query1 = "INSERT INTO payments(payment,gate_no)VALUES(?,?)";
        String query = "SELECT gate_no, COUNT(*) as count FROM payments GROUP BY gate_no ORDER BY count ASC LIMIT 1";

        try{
            try(PreparedStatement ps = con.prepareStatement(query1)) {
                try (Statement stmt = con.createStatement()) {
                    int amount = 0;
                    Stack<String> tickets = new Stack<>();
                    Random r = new Random();
                    for (int i = 1; i <= passengers; i++) {
                        tickets.add("Ticket " + i);
                    }
                    int addAmount = r.nextInt(121) + 80;
                    amount += addAmount * passengers;
                    System.out.println("Online Cancellation unavailable");
                    System.out.println("If you want to cancel your ride, Please enter y right now ");
                    String cancel = sc.next();
                    if (cancel.equalsIgnoreCase("y")) {
                        tickets.clear();
                    }
                    System.out.println("Please proceed with the payment: ");
                    System.out.println("Please pay : " + amount);
                    int payment = sc.nextInt();
                    con.setAutoCommit(false);
                    if (payment == amount) {
                        int gatenumber =  1;
                        ResultSet res = stmt.executeQuery(query);
                        List<Integer> leastCrowdedGates = new ArrayList<>();
                        int minCount = Integer.MAX_VALUE;

                        while (res.next()) {
                            int gate = res.getInt("gate_no");
                            int count = res.getInt("count");

                            if (count < minCount) {
                                minCount = count;
                                leastCrowdedGates.clear();
                                leastCrowdedGates.add(gate);
                            } else if (count == minCount) {
                                leastCrowdedGates.add(gate);
                            }
                        }
                        if(leastCrowdedGates.isEmpty()){
                            gatenumber = r.nextInt(8)+1;
                        }else {
                            gatenumber = leastCrowdedGates.get(r.nextInt(leastCrowdedGates.size()));
                        }

                        ps.setInt(1, payment);
                        ps.setInt(2,gatenumber);
                        int rowsaffected = ps.executeUpdate();
                        if (rowsaffected > 0) {
                            System.out.println("Data saved successfully");
                        } else {
                            System.out.println("Something went wrong");
                        }
                        con.commit();
                        System.out.println("Payment successfull");
                        System.out.println("Please follow the guidelines");
                        System.out.println("You have to enter the train from the alloted gate number");
                        System.out.println("You have to enter the train from gate number " + gatenumber);
                        System.out.println("Your tickets");
                        tickets.stream().forEachOrdered(System.out::println);
                        System.out.println("Enjoy your ride ");

                    } else {
                        con.rollback();
                        System.out.println("Booking Cnacelled");
                    }


                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }



}

public class TrainQueue  {
    private static final String url = "jdbc:postgresql://localhost:5432/train";
    private static final String user = "postgres";
    private static final String password = "PIYUSH@111WORD016";
    public static void main(String[] args)throws ClassNotFoundException,SQLException {
        try{
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e);
        }
        Connection con = DriverManager.getConnection(url,user,password);
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to train tickets booking site");
            System.out.println("Please enter the number of passengers ");
            int passengers =sc.nextInt();
            System.out.println("Please enter 1 to register and book ");
            int option = sc.nextInt();
            sc.nextLine();
            switch (option){
                case 1:
                    Train.registration(con,sc,passengers);
                    Train.payment(con,sc,passengers);
                    break;
                default:
                    System.out.println("Enter valid option");


            }

        }


    }
}
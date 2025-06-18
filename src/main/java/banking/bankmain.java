package banking;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Scanner;

public class bankmain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        MongoClient client = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = client.getDatabase("project");
        MongoCollection<Document> collection = database.getCollection("bankstatement");

        do {
            System.out.println("1.Create account\n 2.Deposit money\n 3.Withdraw money\n 4.Check Balance\n");
            System.out.println("enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    try {
                        System.out.println("Enter your name: \n");
                        String name = sc.nextLine();
                        System.out.println("Enter Account Number: \n");
                        String ac = sc.nextLine();
                        if (ac.isEmpty() || name.isEmpty()) {
                            throw new IllegalArgumentException("Account number and name cannot be empty.");
                        }

                        if (collection.find(new Document("accNo", ac)).first() != null) {
                            System.out.println("Account already exists.");
                        } else {
                            Document doc = new Document("accNo", ac)
                                    .append("name", name)
                                    .append("balance", 0.0);
                            collection.insertOne(doc);
                            System.out.println("Account created and stored in DB.");
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to create account: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        System.out.print("\n Enter account number: ");
                        String accNo = sc.nextLine();

                        Document acc = collection.find(new Document("accNo", accNo)).first();
                        if (acc == null) {
                            throw new IllegalArgumentException("Account not found.");
                        }

                        System.out.print("\n Enter deposit amount: ");
                        double amount = sc.nextDouble();
                        if (amount <= 0) {
                            throw new IllegalArgumentException("Deposit amount must be positive.");
                        }

                        double newBalance = acc.getDouble("balance") + amount;
                        collection.updateOne(new Document("accNo", accNo),
                                new Document("$set", new Document("balance", newBalance)));

                        System.out.println("Deposit successful. New Balance: " + newBalance);

                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid amount. Please enter a valid number.");
                    } catch (IllegalArgumentException iae) {
                        System.out.println(iae.getMessage());
                    } catch (Exception e) {
                        System.out.println("Deposit failed: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("\n Enter account number: ");
                        String acNo = sc.nextLine();
                        Document acc = collection.find(new Document("accNo", acNo)).first();

                        if (acc == null) {
                            throw new IllegalArgumentException("Account not found.");
                        }
                        System.out.print("\n Enter withdraw amount: ");
                        double wdamount = sc.nextDouble();

                        double balance = acc.getDouble("balance");
                        if (wdamount > balance) {
                            throw new IllegalArgumentException("\n Their should be sufficient bank balance.");
                        }
                        double Balance = balance - wdamount;
                        collection.updateOne(new Document("accNo", acNo),
                                new Document("$set", new Document("balance", Balance)));

                        System.out.println("Withdrawal successful. New Balance: " + Balance);

                    } catch (NumberFormatException ni) {
                        System.out.println("\n Invalid amount. Please enter a valid number.");
                    } catch (IllegalArgumentException iae) {
                        System.out.println(iae.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n Withdrawal failed: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        System.out.print("\n Enter account number: ");
                        String accNo = sc.nextLine();

                        Document acc = collection.find(new Document("accNo", accNo)).first();
                        if (acc == null) {
                            throw new IllegalArgumentException("Account not found.");
                        }
                        System.out.println("\n Account Balance : " + acc.getDouble("balance"));

                    } catch (IllegalArgumentException iae) {
                        System.out.println(iae.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n Balance enquire failed: " + e.getMessage());
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }while (choice != 4);
    }
}

package library;

public class BookMain {
        public static void main(String[] args) {
            Fiction fiction = new Fiction("Harry Potter", "J.K. Rowling", "123456789", "Fantasy");
            NonFiction nonFiction = new NonFiction("Sapiens", "Yuval Noah Harari", "987654321", "History");

            fiction.displayInfo();
            System.out.println();
            nonFiction.displayInfo();
        }
    }


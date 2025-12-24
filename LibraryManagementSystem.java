import java.io.*;
import java.util.*;

// Enum for Book Categories
enum BookCategory {
    FICTION, SCIENCE, HISTORY, TECHNOLOGY, EDUCATION
}

// Book class
class Book implements Serializable {
    private Integer id; // Wrapper class
    private String title;
    private String author;
    private Double price; // Wrapper class
    private BookCategory category;
    private boolean isBorrowed;

    public Book(Integer id, String title, String author, Double price, BookCategory category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.isBorrowed = false;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return isBorrowed; }

    public void borrowBook() { this.isBorrowed = true; }
    public void returnBook() { this.isBorrowed = false; }

    @Override
    public String toString() {
        return String.format("\n\nID: %d | %s by %s | %.2f | %s | %s",
                id, title, author, price, category, (isBorrowed ? "Borrowed" : "Available"));
    }
}

// Generic Library Class
class Library<T extends Book> {
    private ArrayList<T> books = new ArrayList<>();

    // Add a new book
    public void addBook(T book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }

    // View all books using Iterator
    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        Iterator<T> itr = books.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    // Borrow a book
    public void borrowBook(int id) {
        for (T book : books) {
            if (book.getId() == id && !book.isBorrowed()) {
                book.borrowBook();
                System.out.println("📘 You borrowed: " + book.getTitle());
                return;
            }
        }
        System.out.println("❌ Book not available or already borrowed!");
    }

    // Return a book
    public void returnBook(int id) {
        for (T book : books) {
            if (book.getId() == id && book.isBorrowed()) {
                book.returnBook();
                System.out.println("✅ Book returned successfully!");
                return;
            }
        }
        System.out.println("❌ Book not found or not borrowed!");
    }

    // Save books to file
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(books);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load books from file
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            books = (ArrayList<T>) ois.readObject();
            System.out.println("📂 Data loaded successfully!");
        } catch (Exception e) {
            System.out.println("⚠️ No saved data found, starting fresh.");
        }
    }
}

// Main Class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library<Book> library = new Library<>();
        library.loadFromFile("library.dat");

        while (true) {
            System.out.println("\n=====  Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Save & Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    System.out.print("Enter Category (FICTION/SCIENCE/HISTORY/TECHNOLOGY/EDUCATION): ");
                    BookCategory category = BookCategory.valueOf(sc.next().toUpperCase());
                    library.addBook(new Book(id, title, author, price, category));
                    break;

                case 2:
                    library.viewBooks();
                    break;

                case 3:
                    System.out.print("Enter Book ID to borrow: ");
                    library.borrowBook(sc.nextInt());
                    break;

                case 4:
                    System.out.print("Enter Book ID to return: ");
                    library.returnBook(sc.nextInt());
                    break;

                case 5:
                    library.saveToFile("library.dat");
                    System.out.println("Exiting... Have a good day!");
                    sc.close();
                    return;

                default:
                    System.out.println("⚠️ Invalid choice!");
            }
        }
    }
}

import java.util.*;
import java.io.*;

public class Main {
    private static ProductCatalog productCatalog;
    private static Client currentClient;
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientDatabase clientDatabase = ClientDatabase.instance();

    public static void main(String[] args) {
        // Initialize product catalog
        productCatalog = new ProductCatalog();
        
        // Load products from file
        loadProducts();
        
        // Main application loop
        boolean running = true;
        while (running) {
            System.out.println("\n========== Main Menu ==========");
            System.out.println("1. Product Management");
            System.out.println("2. Client Management");
            System.out.println("3. Wishlist Management");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        productManagement();
                        break;
                    case 2:
                        clientManagement();
                        break;
                    case 3:
                        if (currentClient == null) {
                            System.out.println("Please log in or register first.");
                            clientManagement();
                        } else {
                            wishlistManagement();
                        }
                        break;
                    case 4:
                        System.out.println("Thank you for using the system. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        scanner.close();
    }
    
    private static void loadProducts() {
        System.out.print("Enter product catalog file name (e.g., productcatalog.txt): ");
        String fileName = scanner.nextLine();
        File inputFile = new File(fileName);

        if (!inputFile.exists()) {
            System.out.println("Error: " + fileName + " not found!");
            return;
        }
        
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            productCatalog.store(fileInputStream);
            System.out.println("Products loaded successfully!");
            productCatalog.getAllProductInfo(System.out);
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }
    
    private static void productManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. List all products");
            System.out.println("2. Search for a product");
            System.out.println("3. Add a new product");
            System.out.println("4. Update product quantity");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        productCatalog.getAllProductInfo(System.out);
                        break;
                    case 2:
                        searchProduct();
                        break;
                    case 3:
                        addProduct();
                        break;
                    case 4:
                        updateProductQuantity();
                        break;
                    case 5:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static void clientManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Client Management ---");
            if (currentClient != null) {
                System.out.println("Logged in as: " + currentClient.getName() + " (ID: " + currentClient.getId() + ")");
                System.out.println("1. Logout");
                System.out.println("2. Back to main menu");
            } else {
                System.out.println("1. Register new client");
                System.out.println("2. Login");
                System.out.println("3. Back to main menu");
            }
            
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                if (currentClient != null) {
                    // Client is logged in
                    switch (choice) {
                        case 1:
                            System.out.println("Logging out " + currentClient.getName());
                            currentClient = null;
                            back = true;
                            break;
                        case 2:
                            back = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    // No client logged in
                    switch (choice) {
                        case 1:
                            registerClient();
                            break;
                        case 2:
                            loginClient();
                            break;
                        case 3:
                            back = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static void wishlistManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Wishlist Management ---");
            System.out.println("1. View my wishlists");
            System.out.println("2. Create new wishlist");
            System.out.println("3. Add product to wishlist");
            System.out.println("4. Remove product from wishlist");
            System.out.println("5. View wishlist contents");
            System.out.println("6. Back to main menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewWishlists();
                        break;
                    case 2:
                        createWishlist();
                        break;
                    case 3:
                        addToWishlist();
                        break;
                    case 4:
                        removeFromWishlist();
                        break;
                    case 5:
                        viewWishlistContents();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static void registerClient() {
        System.out.println("\n--- Register New Client ---");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        
        Client newClient = new Client(name, address, phone);
        clientDatabase.addClient(newClient);
        currentClient = newClient;
        
        System.out.println("Registration successful! Your client ID is: " + newClient.getId());
    }
    
    private static void loginClient() {
        System.out.println("\n--- Client Login ---");
        System.out.print("Enter your client ID: ");
        String clientId = scanner.nextLine();
        
        Client client = clientDatabase.search(clientId);
        if (client != null) {
            currentClient = client;
            System.out.println("Login successful! Welcome back, " + client.getName() + "!");
        } else {
            System.out.println("Client not found. Please check your ID or register as a new client.");
        }
    }
    
    private static void createWishlist() {
        System.out.println("\n--- Create New Wishlist ---");
        System.out.print("Enter a name for your wishlist: ");
        String name = scanner.nextLine();
        
        // In a real application, you would generate a unique ID for the wishlist
        int wishlistId = currentClient.getWishlists().size() + 1;
        Wishlist wishlist = new Wishlist(wishlistId, Integer.parseInt(currentClient.getId().substring(1)), 
                                       new java.util.Date().toString());
        currentClient.addWishlist(wishlist);
        System.out.println("Wishlist '" + name + "' created successfully!");
    }
    
    private static void viewWishlists() {
        System.out.println("\n--- Your Wishlists ---");
        List<Wishlist> wishlists = currentClient.getWishlists();
        if (wishlists.isEmpty()) {
            System.out.println("You don't have any wishlists yet.");
        } else {
            for (int i = 0; i < wishlists.size(); i++) {
                System.out.println((i + 1) + ". " + wishlists.get(i));
            }
        }
    }
    
    private static void addToWishlist() {
        System.out.println("\n--- Add to Wishlist ---");
        List<Wishlist> wishlists = currentClient.getWishlists();
        if (wishlists.isEmpty()) {
            System.out.println("You don't have any wishlists. Please create one first.");
            return;
        }
        
        // Show available wishlists
        System.out.println("Select a wishlist:");
        for (int i = 0; i < wishlists.size(); i++) {
            System.out.println((i + 1) + ". " + wishlists.get(i));
        }
        
        try {
            System.out.print("Enter wishlist number: ");
            int wishlistIndex = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (wishlistIndex < 0 || wishlistIndex >= wishlists.size()) {
                System.out.println("Invalid wishlist selection.");
                return;
            }
            
            Wishlist wishlist = wishlists.get(wishlistIndex);
            
            // Show available products
            System.out.println("\nAvailable Products:");
            // This is a simplified version - in a real app, you'd have a proper product listing
            System.out.println("1. Paper Towels (abc123)");
            System.out.println("2. Plastic Cups (pak1000)");
            System.out.println("3. Cast Iron Pans (Pan1r0n)");
            System.out.println("4. Cat Food (cat111)");
            
            System.out.print("Enter product number to add: ");
            int productChoice = Integer.parseInt(scanner.nextLine());
            
            // In a real app, you would look up the actual product from the catalog
            String sku;
            switch (productChoice) {
                case 1: sku = "abc123"; break;
                case 2: sku = "pak1000"; break;
                case 3: sku = "Pan1r0n"; break;
                case 4: sku = "cat111"; break;
                default:
                    System.out.println("Invalid product selection.");
                    return;
            }
            
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            
            // In a real app, you would get the actual product from the catalog
            // For now, we'll create a dummy product
            Product product = new Product("", sku, "", 0, 0.0);
            
            if (wishlist.addItem(product, quantity)) {
                System.out.println("Product added to wishlist successfully!");
            } else {
                System.out.println("Failed to add product to wishlist.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    
    private static void removeFromWishlist() {
        System.out.println("\n--- Remove from Wishlist ---");
        // Implementation would be similar to addToWishlist but for removing items
        System.out.println("This feature is not yet implemented.");
    }
    
    private static void viewWishlistContents() {
        System.out.println("\n--- View Wishlist Contents ---");
        List<Wishlist> wishlists = currentClient.getWishlists();
        if (wishlists.isEmpty()) {
            System.out.println("You don't have any wishlists.");
            return;
        }
        
        // Show available wishlists
        System.out.println("Select a wishlist to view:");
        for (int i = 0; i < wishlists.size(); i++) {
            System.out.println((i + 1) + ". " + wishlists.get(i));
        }
        
        try {
            System.out.print("Enter wishlist number: ");
            int wishlistIndex = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (wishlistIndex < 0 || wishlistIndex >= wishlists.size()) {
                System.out.println("Invalid wishlist selection.");
                return;
            }
            
            Wishlist wishlist = wishlists.get(wishlistIndex);
            System.out.println("\n--- Wishlist Contents ---");
            System.out.println(wishlist);
            
            List<WishlistItem> items = wishlist.getItems();
            if (items.isEmpty()) {
                System.out.println("This wishlist is empty.");
            } else {
                for (WishlistItem item : items) {
                    // In a real app, you would look up the product details
                    System.out.println("- Item ID: " + item.getWishlistItemID() + 
                                     ", Product SKU: " + item.getProductID() + 
                                     ", Quantity: " + item.getQuantity());
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    
    private static void searchProduct() {
        System.out.print("\nEnter SKU to search: ");
        String sku = scanner.nextLine();
        int index = productCatalog.search(sku);
        
        if (index != -1) {
            System.out.println("Product found:");
            System.out.println(productCatalog.getProductDetails(index));
        } else {
            System.out.println("Product with SKU " + sku + " not found.");
        }
    }
    
    private static void addProduct() {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter SKU: ");
        String sku = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter quantity: ");
        Object quantity = scanner.nextLine();
        System.out.print("Enter price: ");
        Object price = scanner.nextLine();
        
        productCatalog.addProduct(new Product(name, sku, description, quantity, price));
        System.out.println("Product added successfully!");
    }
    
    private static void updateProductQuantity() {
        System.out.print("\nEnter SKU to update quantity: ");
        String sku = scanner.nextLine();
        System.out.print("Enter new quantity: ");
        Object quantity = scanner.nextLine();
        
        if (productCatalog.updateProductQuantity(sku, quantity)) {
            System.out.println("Quantity updated successfully.");
        } else {
            System.out.println("Failed to update quantity. Product not found.");
        }
    }
}



import java.util.*;

// ================= 1-Interface =================

// Discountable Interface
public class interface Discountable {
    // Method to apply discount
    void applyDiscount(double percentage);
}
// ================= 2-BaseItem =================
// Abstract class representing a general item
// Contains id and name
// Any subclass must implement price calculation
abstract class BaseItem {
    protected int id;
    protected String name;

    public BaseItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    // Abstract method to calculate price
    public abstract double calculatePrice();
}

// ================= 3- Pizza =================
// General Pizza class
// Inherits from BaseItem and supports discount
class Pizza extends BaseItem implements Discountable {
    protected double basePrice;
    protected String size;

    public Pizza(int id, String name, double basePrice, String size) {
        super(id, name);
        this.basePrice = basePrice;
        this.size = size;
    }

    // Calculate price based on size
    
    public double calculatePrice() {
        if(size.equalsIgnoreCase("L")) return basePrice + 10;
        if(size.equalsIgnoreCase("M")) return basePrice + 5;
        return basePrice;
    }

    // Apply discount to base price
    
    public void applyDiscount(double percentage) {
        basePrice -= (basePrice * percentage / 100);
    }
}

// ================= 4-VeggiePizza =================
// Represents a vegetable pizza with extra cost
class VeggiePizza extends Pizza {
    private String vegetableType;

    public VeggiePizza(int id, String name, double basePrice, String size, String vegetableType) {
        super(id, name, basePrice, size);
        this.vegetableType = vegetableType;
    }

    // Add extra cost for veggie toppings
  
    public double calculatePrice() {
        return super.calculatePrice() + 5;
    }
}

// ================= 5-MeatPizza =================
// Represents a meat pizza with extra cost
class MeatPizza extends Pizza {
    private String meatType;

    public MeatPizza(int id, String name, double basePrice, String size, String meatType) {
        super(id, name, basePrice, size);
        this.meatType = meatType;
    }

    // Add extra cost for meat toppings

    public double calculatePrice() {
        return super.calculatePrice() + 10;
    }
}

// ================= 6 - Coupon =================

public class Coupon implements Discountable {
    private String code;
    private double value;
// Constructor 
    public Coupon(String code, double value) {
        this.code = code;
        this.value = value;
    }

   // Implementing the interface method 
    public void applyDiscount(double percentage) {
        this.value -= (this.value * percentage / 100);
    }
}

// ================= 7. PizzaShop =================

class PizzaShop {
    private String shopName;
    private Pizza[] menu;
    private int count;

    // Constructor
    public PizzaShop(String shopName, int maxMenuSize) {
        this.shopName = shopName;
        this.menu = new Pizza[maxMenuSize];
        this.count = 0;
    }

    // Add a pizza
    public void addPizza(Pizza p) {
        if (count < menu.length) {
            menu[count] = p;
            count++;
        }
    }

    // Remove a pizza based on ID
    public boolean removePizza(int id) {
        // Deletion logic goes here
        return false; 
    }

    // Search for a pizza by name
    public Pizza searchPizza(String name) {
        // Search logic goes here
        return null;
    }

    // Count pizzas recursively
    public int countPizzasRecursive(int index) {
        if (index >= count) {
            return 0;
        }
        return 1 + countPizzasRecursive(index + 1);
    }
}

// ================= 8-OrderDetails =================

class OrderDetail {
    private int quantity;
    private double subTotal;

    public OrderDetail(int quantity, double subTotal) {
        this.quantity = quantity;
        this.subTotal = subTotal;
    }
}
// ================= 9-Order =================

class Order {
    private int orderID;
    private double totalPrice;
    
    // Composition relationship with OrderDetail based on the diagram
    private List<OrderDetail> orderDetails;

    public Order(int orderID) {
        this.orderID = orderID;
        // Initialization (an order must contain at least one detail according to the 1..* relationship)
        this.orderDetails = new ArrayList<>(); 
    }

    // Add order details
    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
    }

    // Print invoice
    public void printInvoice() {
        System.out.println("Printing invoice for order ID: " + this.orderID);
        System.out.println("Total Price: " + this.totalPrice);
    }
}

// ================= 10-Main =================
// Main class to run the program
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create pizza shop
        PizzaShop shop = new PizzaShop("Slice of Heaven");

        // Create new order
        System.out.print("Enter Order ID: ");
        Order currentOrder = new Order(sc.nextInt());

        boolean run = true;

        // Menu loop
        while (run) {
            System.out.println("\n1.Add Pizza | 2.Order | 3.Discount | 4.Coupon | 5.Invoice | 6.Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // Add pizza
                case 1:
                    System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Name: "); String n = sc.nextLine();
                    System.out.print("Price: "); double p = sc.nextDouble();
                    System.out.print("Type (1 Veg / 2 Meat): "); int t = sc.nextInt();

                    if (t == 1)
                        shop.addPizza(new VeggiePizza(id, n, p, "L", "Mushroom"));
                    else
                        shop.addPizza(new MeatPizza(id, n, p, "L", "Pepperoni"));
                    break;

                // Order pizza
                case 2:
                    System.out.print("Pizza Name: ");
                    String sn = sc.nextLine();
                    Pizza found = shop.searchPizza(sn);

                    if (found != null) {
                        System.out.print("Quantity: ");
                        int q = sc.nextInt();
                        currentOrder.addDetail(q, found.calculatePrice());
                        System.out.println("Added!");
                    } else {
                        System.out.println("Not found");
                    }
                    break;

                // Apply discount
                case 3:
                    System.out.print("Pizza Name: ");
                    String dn = sc.nextLine();
                    Pizza dp = shop.searchPizza(dn);

                    if (dp != null) {
                        System.out.print("Discount %: ");
                        dp.applyDiscount(sc.nextDouble());
                    }
                    break;

                // Apply coupon
                case 4:
                    System.out.print("Coupon Code: ");
                    String code = sc.next();
                    System.out.print("Value: ");
                    double val = sc.nextDouble();

                    Coupon c = new Coupon(code, val);
                    currentOrder.applyCoupon(c.getValue());
                    break;

                // Print invoice
                case 5:
                    currentOrder.printInvoice();
                    break;

                // Exit
                case 6:
                    run = false;
                    break;
            }
        }
    }
}

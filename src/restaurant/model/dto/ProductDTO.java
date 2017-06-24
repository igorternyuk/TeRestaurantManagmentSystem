package restaurant.model.dto;

/**
 *
 * @author igor
 */

public class ProductDTO {
    private int id;
    private String name;
    private double price;
    private boolean available;

    public ProductDTO() {
    }

    public ProductDTO(String name, double price, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.available = isAvailable;
    }

    public ProductDTO(int id, String name, double price, boolean isAvailable) {
        this(name, price, isAvailable);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

//    @Override
//    public String toString() {
//        return name;
//        //return "ProductDTO{" + "id=" + id + ", name=" + name + ", price=" + price + ", available=" + available + '}';
//    }

}

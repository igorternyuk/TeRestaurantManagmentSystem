package restaurant.model.dto;

/**
 *
 * @author igor
 */
public class DrinkDTO extends ProductDTO {
    private boolean alcohol;

    public DrinkDTO() {
    }

    public DrinkDTO(String name, double price, boolean isAvailable,
            boolean isAlcohol) {
        super(name, price, isAvailable);
        this.alcohol = isAlcohol;
    }

    public DrinkDTO(int id, String name, double price, boolean isAvailable,
            boolean isAlcohol) {
        super(id, name, price, isAvailable);
        this.alcohol = isAlcohol;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

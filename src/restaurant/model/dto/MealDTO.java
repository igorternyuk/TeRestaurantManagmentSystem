package restaurant.model.dto;

import restaurant.model.MealType;

/**
 *
 * @author igor
 */
public class MealDTO extends ProductDTO {
    private MealType type;
    private boolean vegetarian, transgenic;

    public MealDTO() {
    }

    public MealDTO(String name, MealType type, double price,
            boolean isAvailable, boolean vegetarian, boolean transgenic) {
        super(name, price, isAvailable);
        this.type = type;
        this.vegetarian = vegetarian;
        this.transgenic = transgenic;

    }

    public MealDTO(int id, String name, MealType type, double price,
            boolean isAvailable, boolean vegetarian, boolean transgenic) {
        super(id, name, price, isAvailable);
        this.type = type;
        this.vegetarian = vegetarian;
        this.transgenic = transgenic;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isTransgenic() {
        return transgenic;
    }

    public void setTransgenic(boolean transgenic) {
        this.transgenic = transgenic;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return this.getName() + "(" + String.valueOf(this.getId()) + ")";
    }
    
    
}

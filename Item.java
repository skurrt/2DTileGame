import java.awt.image.BufferedImage;

public class Item {
    //init instance variables
    BufferedImage sprite;

    String itemName; 

    int speedIncrease;

    int strengthIncrease;

    int healthIncrease;

    int cost;
    /**
     * Author : Robert
     * Constructor for item object
     * Precondition : a string, and 3 ints must be passed
     * Postcondition : the item object is initialized
     * @param itemName - Name of the item
     * @param speedIncrease - The amount of speed increases youll get from this item
     * @param strengthIncrease - The amount of strength increase youll get from this item
     * @param healthIncrease - The amount of health increase youl l get from this item
     */
    public Item(String itemName, int speedIncrease, int strengthIncrease, int healthIncrease){

        //the instance variable are updated using the parameters
        this.itemName = itemName;

        this.speedIncrease = speedIncrease;

        this.strengthIncrease = strengthIncrease;

        this.healthIncrease = healthIncrease;

    }
}

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;

public class Chest {
    
    //initializes defualt value
    boolean hasBeenOpened = false;
    ArrayList<Item> inventory = new ArrayList<Item>();
    
    //init instance variables
    BufferedImage closedSprite; 

    BufferedImage openSprite;

    int chestCoins;

    int rowIndex;

    int colIndex;

    int yPos;

    int xPos;

    /**
     * Author :  Rob/Alp
     * constructor for Chest object, which helps print the chest
     * Precondition : 2 ints must be passed on 
     * Postcondition : the chest object is initialized 
     * @param rowIndex - The rowindex of the chest
     * @param colIndex - The colindex of the chest
     */
    public Chest(int rowIndex, int colIndex) {
        //the amount of coins is set to a random value between 5-16 not inclusive
        this.chestCoins = ThreadLocalRandom.current().nextInt(5,16);

        //tries to init the two different sprites for the chest
        try {

            this.closedSprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/Chest.png"));

            this.openSprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/Chest_Opened.png"));
        
        //used for error catching
        } catch (IOException e) {

            e.printStackTrace();
        }
        
        //the instance variables are set to the parameters
        this.rowIndex = rowIndex;

        this.colIndex = colIndex;
    }
    
    /**
     * Author : Robert / Alp
     * this method sets the cordinates for the chest
     * Precondition : none
     * Postcondition : the y and x pos of the chest object is updated
     */
    public void setCoordinates() {
        //the y and x pos of the chest is calculated using the block dimensions of the world
        this.yPos = this.rowIndex * Map.currentMap.blockHeight;

        this.xPos = this.colIndex * Map.currentMap.blockWidth;
    }

}


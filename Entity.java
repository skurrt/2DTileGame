import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    // initializes the entity's sprite image
    BufferedImage sprite;
// inits entity's height
    int height;
//init instance variables
    int width;

    String entityName;

    int health;

    int strength;

    int xPos;

    int yPos;

    int rowIndexInMap;

    int colIndexInMap;

    ArrayList<Item> inventory = new ArrayList<Item>();
    /**
     * Author Rob / Alp
     * Constructor method for an entity object
     * @param name - Name of the entity
     * Precondition : A string and 2 ints must be passed on
     * Postcondition An entity object is initializied
     * @param rowIndex - The rowindex of the entity
     * @param colIndex - The col index of the entity
     */
    public Entity(String name, int rowIndex, int colIndex) {
// sets the entity's name
        this.entityName = name;
// Sets the entity's strength relative to the level number for dynamic scaling of difficulty (they get strong fast)
        if (Map.levelNumber > 3) {
        
            this.strength = Map.levelNumber - 2;

            this.health = Map.levelNumber - 3;

        }
        else {
            //anything before level 3 will have 1 strength and 2 health

            this.strength = 1;

            this.health = 2;
        }

        //passes on the parameters of the constructor to the instance variables
        this.colIndexInMap = colIndex;

        this.rowIndexInMap = rowIndex;

        //initlaizes a  MAp object
        if (Map.currentMap != null) {

            Map currentLvl = Map.currentMap;

            //calculates the x and y pos of the entity and stores them in the instance variable
            this.xPos = (currentLvl.blockWidth * this.colIndexInMap);

            this.yPos = (currentLvl.blockHeight * this.rowIndexInMap);

        }
        //stores all the images for entities in an arraylist
        ArrayList<BufferedImage> possibleHostileImages = new ArrayList<BufferedImage>();

        try {
            //tries to initialize all the images for the entities

            BufferedImage demon = ImageIO.read(getClass().getResourceAsStream("/2dTiles/demon.png"));

            BufferedImage dog = ImageIO.read(getClass().getResourceAsStream("/2dTiles/hellhound.png"));

            BufferedImage zombie = ImageIO.read(getClass().getResourceAsStream("/2dTiles/zombie.png"));

            //adds the initalized images to the arraylist
            possibleHostileImages.add(demon);

            possibleHostileImages.add(dog);

            possibleHostileImages.add(zombie);

        //error catching
        } catch (IOException e) {
            
            e.printStackTrace();
        }

        //randomly selects an image from the array list of 3 images
        Random randInt = new Random();

        this.sprite = possibleHostileImages.get(randInt.nextInt(possibleHostileImages.size()));
    }

    /**
     * Author Rober/Alp
     * This method helps update the coordinates of the entity
     * Precondition : 2 ints must be passed on
     * Postcondition : the coordinates of the entity is updated
     * @param blockWidth - The width of the blocks in the map
     * @param blockHeight - Height of the blocks in the map
     */
    public void updateCoords(int blockWidth, int blockHeight) {

        this.xPos = (blockWidth * this.colIndexInMap);

        this.yPos = (blockHeight * this.rowIndexInMap);
    }

    /**
     * Author : Robert
     * This method updates the dimensions of the entity (hitbox purposes)
     * Precondition : None
     * Postconditon : The dimensions of the entity is updated
     */
    public void updateDimensions() {
        //sets the instance variables to the calculated dimensions
        this.width = (2*Map.currentMap.blockWidth/3);

        this.height = (2*Map.currentMap.blockHeight/3);
    }
    /**
     * Author : Rob
     * This method is used to draw the entities on the map
     * Precondition : A graphics2d Object must be passed on
     * Postcondition : the entity is drawn
     * @param g - graphics2d Object
     */
    public void drawEntity(Graphics2D g) {
            //draws the scaled instance at the current position of the entity
            g.drawImage(this.sprite.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0), this.xPos, this.yPos, null);
            
    }
    /**
     * Author Rob/ Alp
     * This method allows the attacking mechanic to happen between 2 entities
     * Precondition : An entity object and a graphics2d object must be passed on
     * Postcondition : An attack event happens
     * @param target - The target entity to attack
     * @param g - a Graphics2d object
     */
    public void attack(Entity target, Graphics2D g) {
// If the target is alive
        if (target.health > 0) {
// deals damage to the target based on this entity's strength
            target.health -= this.strength;
// setting font
            g.setFont(UI.normalFont);
// setting colour
            g.setColor(Color.yellow);
// Writing the damage done over top of the target
            g.drawString("" + this.strength, target.xPos, target.yPos);
        }
    }
}
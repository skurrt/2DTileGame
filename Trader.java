import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;

public class Trader extends Entity {
    //initializes starting variables
    boolean interactedWith = false;

    /**
     * Author : Alp/ Robert
     * constructor for trader object
     * Precondition : a string, and 2 ints must be passed on
     * Postcondition : Trader object is initialized
     * @param name - Name of the trader
     * @param rowIndex - Rowindex of the trader
     * @param colIndex - Colindex of the trader
     */
    public Trader(String name, int rowIndex, int colIndex) {
        //references the parent constructor
        super(name, rowIndex, colIndex);

        //intializes energy drink
        Item energyDrink = new Item("ENERGY DRINK", 1, 0, 0);

        //initiliazes dumbbell
        Item dumbell = new Item("DUMBBELL", 0, 1, 0);

        //sets the item's costs
        dumbell.cost = 15;
        energyDrink.cost = 15;

        //tries to initialize the sprites of the objects using their relative path
        try {
            
            energyDrink.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/energyDrink.png"));

            dumbell.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/dumbell.png"));

            this.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/RedHat_Trader.gif"));

        } catch (IOException e) {
            //used for finding errors
            e.printStackTrace();
        }
        //adds the items to the inventory
        this.inventory.add(energyDrink);

        this.inventory.add(dumbell);
    }

    /**
     * Author Alp/Robert
     * this method draws the shop that pops up when interacting with the trader
     * Precondition : a graphics2d object and a char must be passed on
     * Postcondition : The shop interface is printed
     * @param graphicsObj - A graphics2d Object
     * @param selectedItem - a char representing the selected item
     */
    public void drawShop(Graphics2D graphicsObj, char selectedItem) {
        //init and set color
        Color faintBlack = new Color(0, 0, 0, 210);

        graphicsObj.setColor(faintBlack);

        //sets up the dimensions for the shop window
        int shopWidth = GamePanel.windowWidth - (2 * Map.currentMap.blockWidth);

        int shopHeight = GamePanel.windowHeight - (2 * Map.currentMap.blockHeight);

        int xOffset = Map.currentMap.blockWidth;

        int yOffset = Map.currentMap.blockHeight;

        //background of shop
        graphicsObj.fillRect(xOffset, yOffset, shopWidth, shopHeight);

        //init the width for the sprites
        int imageWidth = shopWidth/5;

        int imageHeight = shopHeight/5;
        
        //init the images for the sprites
        Image energyDrinkScaled = this.inventory.get(0).sprite.getScaledInstance(imageWidth, imageHeight, 0);

        Image dumbellScaled = this.inventory.get(1).sprite.getScaledInstance(imageWidth, imageHeight, 0);

// Draws the sprites of the two items that the trader sells
        graphicsObj.drawImage(energyDrinkScaled, xOffset + imageWidth, yOffset + shopHeight/3, null);

        graphicsObj.drawImage(dumbellScaled, xOffset + (3 * shopWidth/5), yOffset + shopHeight/3, null);

        graphicsObj.setColor(Color.yellow);
        
        //init a new font
        Font font = new Font("Consolas", Font.BOLD, 24);

        graphicsObj.setFont(font);
// Drawing a string description of each item
        graphicsObj.drawString("" + this.inventory.get(0).itemName, xOffset + imageWidth, yOffset + (2 * shopHeight/3));

        graphicsObj.drawString("" + this.inventory.get(1).itemName, xOffset + (3 * imageWidth) + 30, yOffset + (2 * shopHeight/3));

        //if the hover effect is on the left (energy drink)
        if (selectedItem == 'L') {
            //draws the hover effect 
            graphicsObj.drawRect(xOffset + imageWidth, yOffset + shopHeight/3, imageWidth, imageHeight);
            
            //draws the cost and the description of the item
            graphicsObj.drawString("Costs " + this.inventory.get(0).cost + " Gold", xOffset + imageWidth, yOffset + (shopHeight/4));

            graphicsObj.drawString("Adds " + this.inventory.get(0).speedIncrease + " Speed", xOffset + imageWidth, yOffset + (shopHeight/5));
        }
        
        //if the hover effect is on the right (dumbell)
        if (selectedItem == 'R') {
            //draws hover effect
            graphicsObj.drawRect(xOffset + (3 * imageWidth), yOffset + shopHeight/3, imageWidth, imageHeight);

            //draws the cost and the description of the item
            graphicsObj.drawString("Costs " + this.inventory.get(1).cost + " Gold", xOffset + (3 * imageWidth), yOffset + (shopHeight/4));

            graphicsObj.drawString("Adds " + this.inventory.get(1).strengthIncrease + " Strength", xOffset + (3 * imageWidth), yOffset + (shopHeight/5));
        }
    }

    /**
     * Author : Robert
     * This method creates the flashing animation when buying an item
     * @param graphicsObj - A graphics2d object 
     * @param selectedItem - char represnting the selected item
     */
    public void drawBuyFlash(Graphics2D graphicsObj, char selectedItem) {
        //init the dimensions for the shop
        int shopWidth = GamePanel.windowWidth - (2 * Map.currentMap.blockWidth);

        int shopHeight = GamePanel.windowHeight - (2 * Map.currentMap.blockHeight);

        //init the offsets for the shop
        int xOffset = Map.currentMap.blockWidth;

        int yOffset = Map.currentMap.blockHeight;

        //init the width for the images
        int imageWidth = shopWidth/5;

        int imageHeight = shopHeight/5;

        graphicsObj.setColor(Color.yellow);

        //if the hover is on the left
        if (selectedItem == 'L') {
            //creates a filled rectangle on the item
            graphicsObj.fillRect(xOffset + imageWidth, yOffset + shopHeight/3, imageWidth, imageHeight);
        }
        
        //if the hover is on the right
        if (selectedItem == 'R') {
            //creates a filled rectangle on the item
            graphicsObj.fillRect(xOffset + (3 * imageWidth), yOffset + shopHeight/3, imageWidth, imageHeight);
        }

    }
    /**
     *  Author : Robert/Alp
     * This method sets the position for the trader
     * Precondition:  None
     * Postcondition : updates the instance variables
     */
    public void setCoordinates() {
        //the y and x pos of the trader is updates using the block dimensions
        this.yPos = this.rowIndexInMap * Map.currentMap.blockHeight;

        this.xPos = this.colIndexInMap * Map.currentMap.blockWidth;
    }
}


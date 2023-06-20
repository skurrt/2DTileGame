import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
// maximum speed the player can upgrade to
    int maxSpeed = 15;
// base speed
    int speed = 5;
// base coins, you start BROKE!
    int coins = 0;
// init width of the player
    int playerWidth;
// init height of the player
    int playerHeight;
// the actual detection range for collision is calculated with this xHitBox (xHitBox is top left corner of hitbox)
// representing the width of the hitbox, the height of the player and its vertical hitbox is the same
    int xHitBox;

/**
 * Precondition: There is an instance of Map
 * Constructor for Player class, instanciates the Entity class
 * Author: Robert
 * @param name
 * @param xCoord
 * @param yCoord
 */
    public Player(String name) {
// references the super class Entity
        super(name, Map.currentMap.startingPoint[0], Map.currentMap.startingPoint[1]);
// temporary map value, makes code more readable
        Map currentLvl = Map.currentMap;
// starting health set to 5
        this.health = 5;
// starting strength set to 1
        this.strength = 1;
// player height calculated with blockHeight
        this.playerHeight = 2 * currentLvl.blockHeight/3;
// player width calculated with blockwidth
        this.playerWidth = 2 * currentLvl.blockWidth/3;
// hitbox shaves off 1/2 of the player width
        xHitBox = this.xPos + this.playerWidth/4;
// loads player sprite
        try {

            this.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/skeletonPlayer.png"));

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


/**
 * Method to move an instance of the player class a certain amount, only moves the player
 * if the desired change in location is within the path
 * Author: Robert
 * @param xChange proposed change in x position
 * @param yChange proposed change in y position
 * @return none
 */
    public void movePlayer(int xChange, int yChange) {
// temporary Map instance, makes easier to read
        Map currentLvl = Map.currentMap;
// this is where collision detection happens for movement
// calculates the new position based off given change in x and y
        int newYPos = this.yPos + yChange;
// new hitbox position
        int newXHitBox = this.xHitBox + xChange;
// checks if the new coordinates are within range of the window size
        if (newXHitBox >= 0 && newXHitBox <= GamePanel.windowWidth && newYPos >= 0 && newYPos <= GamePanel.windowHeight) {
        // initially says the player can move
            boolean playerCanMove = true;
// TOP LEFT // checks if the coordinate at the top left corner of the hitbox at the proposed new location is in a path block
            if (!Map.checkIfPathBlock(newXHitBox, newYPos)) {
// if it isn't it sets playerCanMove to false, same for the rest of the checks
                playerCanMove = false;
            }
// TOP RIGHT// Checks the coordinate at the top right of the hitbox
            else if (!Map.checkIfPathBlock(newXHitBox + this.playerWidth/2, newYPos)) {

                playerCanMove = false;
            }
// BOTTOM LEFT// checks the coordinate at the bottom left of the hitbox
            else if (!Map.checkIfPathBlock(newXHitBox, newYPos + this.playerHeight)) {

                playerCanMove = false;
            }
// BOTTOM RIGHT// checks the coordinate at the bottom right
            else if (!Map.checkIfPathBlock(newXHitBox + this.playerWidth/2, newYPos + this.playerHeight)) {

                playerCanMove = false;
            }
// if the proposed movement passed all the checks
            if (playerCanMove) {
// sets the player's coordinates to the new ones, as well as updating the player's indices
                this.xHitBox = newXHitBox;

                this.xPos = this.xHitBox - this.playerWidth/4;

                this.yPos = newYPos;

                this.rowIndexInMap = (this.yPos / currentLvl.blockHeight);
            
                this.colIndexInMap = (this.xPos / currentLvl.blockWidth);
            }
        }
    }

    /**
     * Method to attack a given entity
     * Overrides the entity method
     * Author: Robert
     * @param target any entity
     * @param g any Graphics2D object
     */
@Override
    public void attack(Entity target, Graphics2D g) {
// if the target is alive
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

/**
 * Method to find whether the player's hitbox is in a certain block
 * Author: Robert
 * @return returns true if the player is currently in the exit block, false if not
 */
    public boolean playerAtBlock(int rowIndex, int colIndex) {
// calculates the current index of the top left corner of the hitbox
        int hitBoxColIndex = (this.xHitBox / Map.currentMap.blockWidth);
// calculates the index pair of the bottom right corner of the hitbox
        int bottomRowIndex = ((this.yPos + playerHeight) / Map.currentMap.blockHeight);

        int rightColIndex = ((this.xHitBox + playerWidth/4 ) / Map.currentMap.blockWidth);

        boolean atBlock = true;
// sets the amount of corners in the given block
        int cornersInBlock = 4;
// check if the bottom right corner is not in the given block
        if (bottomRowIndex != rowIndex || rightColIndex != colIndex) {
            // remove 1 from corners in block
            cornersInBlock--;
        }
// check top left
        if (this.rowIndexInMap != rowIndex || hitBoxColIndex != colIndex) {
// if not in the given block, remove 1
            cornersInBlock--;
        }
// check top right
        if (this.rowIndexInMap != rowIndex || rightColIndex != colIndex) {

            cornersInBlock--;
        }
// check bottom left
        if (bottomRowIndex != rowIndex || hitBoxColIndex != colIndex) {

            cornersInBlock--;
        }
// if there is more than 1 corner not in the block 
        if (cornersInBlock < 3) {
// set return value to false
            atBlock = false;
        }
// return return value
        return atBlock;
    }

/**
 * Method to check if a player is above, below, or beside a given block
 * Author: Robert
 * @param rowIndex row index of the block to be checked
 * @param colIndex col index of the block to be checked
 * @return true if the player is near the block, false if otherwise
 */
    public boolean playerNearBlock(int rowIndex, int colIndex) {
// sets return value to false by default
        boolean nearBlock = false;
// if the player it to the right of the block, this equals true
        boolean toTheRight = (this.rowIndexInMap == rowIndex && this.colIndexInMap == colIndex + 1);
// equals true if the player is to the left
        boolean toTheLeft = (this.rowIndexInMap == rowIndex && this.colIndexInMap == colIndex - 1);
// equals true if the player is above the block
        boolean above = (this.rowIndexInMap == rowIndex - 1 && this.colIndexInMap == colIndex);
// equals true if the player is below the bloc
        boolean below = (this.rowIndexInMap == rowIndex + 1 && this.colIndexInMap == colIndex);
// evaluates all, if any are true, the return value is set to true
        if (toTheRight || toTheLeft || above || below) {

            nearBlock = true;
        }
        
        return nearBlock;
    }

/**
 * Returns whethera player can buy a certain item from the trader
 * @param leftOrRight tells the method which item is being bought
 * @return true if the player can buy the item, false if not
 */
    public boolean buyItem(char leftOrRight) {
// defaults return value to false
        boolean canBuy = false;
// inits temp item object
        Item item;
// if the left item in the shop is selected
        if (leftOrRight == 'L') {
// returns the left item in the shop
            item = Map.currentMap.levelTrader.inventory.get(0);
// checks if the player has enough money for the item, and if the item would make the player's speed past the limit
            if (this.coins >= item.cost && this.speed + item.speedIncrease < maxSpeed) {
// sets return value to true
                canBuy = true;
// adds the item to the character's inventory
                this.inventory.add(item);
// adds the item's speed increase
                this.speed += item.speedIncrease;
// adds the strength increase
                this.strength += item.strengthIncrease;
// adds the health increase
                this.health += item.healthIncrease;
// takes away the proper amound of coins from the player
                this.coins -= item.cost;
// plays buying noise
                GamePanel.playSE(4);
            }
        }
// if the right item has been selected
        if (leftOrRight == 'R') {
// returns the right item in the shop
            item = Map.currentMap.levelTrader.inventory.get(1);
// checks if the player is brokies and if they aren't gonna go sonic after getting the item
            if (this.coins >= item.cost && this.speed + item.speedIncrease < maxSpeed) {
// if the player is rich, and not fast
// sets return value to true
                canBuy = true;
// adds item to the players inventory
                this.inventory.add(item);
// add item's speed increase
                this.speed += item.speedIncrease;
// adds the strength increase
                this.strength += item.strengthIncrease;
// adds the health increase
                this.health += item.healthIncrease;
// takes away the proper amound of coins from the player
                this.coins -= item.cost;
// plays the buying sound
                GamePanel.playSE(4);
            }

        }
// so that the player only buys one at a time
// if this wasn't here, the player would continue buying items in a loop until they go broke
        PlayerInput.fPressed = false;
// returns return value
        return canBuy;
    }


/**
 * Method to send a player to the center of a block at a certain index in the map
 * Author: Robert
 * @param rowIndex new row index
 * @param colIndex  new col index
 */
    public void sendPlayer(int rowIndex, int colIndex) {
// sets new column index
        this.colIndexInMap = colIndex;
// sets new row index
        this.rowIndexInMap = rowIndex;
// updates coordinate positions
        this.xPos = (colIndex * Map.currentMap.blockWidth);

        this.xHitBox = this.xPos + playerWidth/4;

        this.yPos = (rowIndex * Map.currentMap.blockHeight);

    }
}

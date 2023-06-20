import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Sub-Class of JPanel to make some minor adjustments as well as add a thread to the panel when instantiated
 */
public class GamePanel extends JPanel implements Runnable {

    
// Setting the desired program updates per second (amount of times the thread loops per second)
    int desiredUPS = 60;
//sets default values for the needed variables
    
    static int lengthOfMap = 10;
    
    char selectedItem = 'n';

    final static int menuState = 0;

    final static int playingState = 1;

    final static int pauseState = 2;

    final static int shopState = 3;

    final static int fightState = 4;

    final static int deathState = 5;

    static int windowWidth = 1000;

    static int windowHeight = 900;


    //init instance variables
    static int gameState;

    static Sound sound = new Sound();

    Thread gameCore;

    static BufferedImage coin;

    static BufferedImage heart;

    static boolean visitedTrader = false;
// keeps track of the amount of frames that have passed until 60
    public int loopCounter = 0;

/**
 * Constructor for GamePanel class, sets the dimensions of the window to the specified width and height.
 * Also instantiates the JPanel super class
 * Author: Robert
 */
    public GamePanel() {
// reference to JPanel super class
        super(null, true);
// sets the window size
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
// Sets the default background to black
        this.setBackground(Color.BLACK);
// plays the game music on loop ("End Of My Journey" - John Dreamer)
        playMusic(0);

        try {
// loads the coin image into a buffered image variable, same for heart
            coin = ImageIO.read(getClass().getResourceAsStream("/2dTiles/coin.png"));

            heart = ImageIO.read(getClass().getResourceAsStream("/2dTiles/heart.png"));

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

/**
 * Starts the game thread, when gameCore.start() is called, the method "run" will automatically be called as well
 * Author: Robert
 */
    public void startThread() {
// instantiates new thread
        gameCore = new Thread(this);
// starts the thread (calls the run() method)
        gameCore.start();
// sets default gamestate to menu
        gameState = menuState;

    }
// first instance of map
    static Map mapLevel = new Map(lengthOfMap);
// first instance of player
    static Player playerObject = new Player("Jorge");
// variable to check if a player has been rewarded with coins for killing a mob on a level
    boolean playerRewarded;

/**The main game loop, whatever is coded into this method is called when gameCore.start() is called
 * 
 */
    @Override
    public void run() {

// Creating set Frames Per Second
        double threadTime = 1000/desiredUPS;
// Main game loop
        while (gameCore != null) {
// takes a time stamp before the screen is updated and repainted
            double nextLoopTime = System.currentTimeMillis() + threadTime; 
// updates almost all dynamic variables
            updateAll();
// actually calls "paintComponent()"... not sure why, it just doesn't work easily any other way (that i know of)
            repaint();
            
// Calculating the amount of time that was taken to complete "updateAll" and "repaint" (in nanoseconds)
// using the time stamp taken before
            double timeTaken = nextLoopTime - System.currentTimeMillis();
// Thread.sleep() cannot be used without try and catch
// Waits the calculated amount of time to keep a consistent fps (divides by 1000000 to get milliseconds from nanoseconds)            
            try {    
                // forces the thread to wait before updating the screen again
                Thread.sleep((long)Math.abs(timeTaken));

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            // every 60 frames, reset loop counter (this acts as a timer for entities to attack)
            if (loopCounter == 60) {loopCounter = 0;}
            // otherwise increment by 1
            else {loopCounter++;}
        }
    }

/**
 * Method to reset the player, map, and progress
 * Author: Robert
 */
    public static void restart() {
// resets the map and all important static variables
        mapLevel = new Map(lengthOfMap);

        Map.levelNumber = 0;

        Map.firstLevelCreated = false;
// resets the player
        playerObject = new Player("Jorge");
// if the player has requested a restart it will send them into a new map, with a brand new character
        if (gameState == pauseState) {gameState = playingState;}
// if the player has died, it will send them to the menu
        else if (gameState == deathState) {gameState = playingState;}

    }


/**
 * Method to update the screen and player, if the user has pressed any movement keys it will call a method to check if the player 
 * can move in that direction
 * Also checks the player's location to see if any events need to be initiated
 * Author: Robert/Alp
 */
    public void updateAll() {

        if (playerObject.health <= 0) {
            // if the player loses all hp, the player has died and the game enters deathState
            gameState = deathState;
        }

// Checks if the player has reached the exit block
        if (playerObject.playerAtBlock(Map.currentMap.mapExitBlock[0], Map.currentMap.mapExitBlock[1])) {
// Plays door sound effect
                playSE(1);
// Creates the next level
                Map newMap = new Map(lengthOfMap);
// sends the player to the next level's starting point
                playerObject.sendPlayer(newMap.startingPoint[0], newMap.startingPoint[1]);
// resets the playerRewarded variable
                playerRewarded = false;
// Every three levels the player will gain one heart
                if (Map.levelNumber % 3 == 0) {
                        
                    playerObject.health++;
                }
// resets whether the player has visited the trader or not
                visitedTrader = false;
            }
// if the map has a chest
            if (Map.currentMap.levelChest != null) {
// checks if the player is at the chest block and the chest has not been opened
                if (playerObject.playerAtBlock(Map.currentMap.levelChest.rowIndex, Map.currentMap.levelChest.colIndex) && !Map.currentMap.levelChest.hasBeenOpened) {
// adds the coins from the chest to the player
                    playerObject.coins += Map.currentMap.levelChest.chestCoins;
// sets the chest to open
                    Map.currentMap.levelChest.hasBeenOpened = true;
// plays chest sound effect
                    playSE(2);
                }
            }
// if the map has a trader
            if (Map.currentMap.levelTrader != null) {
// checks if the player is in the same block and has met a certain interaction condition
                    if (playerObject.playerAtBlock(Map.currentMap.levelTrader.rowIndexInMap, Map.currentMap.levelTrader.colIndexInMap) && visitedTrader == false) {
// sets gamestate to shop, opens the trader shop
                        gameState = shopState;
// sets visitedTrader true
                        visitedTrader = true;
// plays "hello there"
                        playSE(3);
                    }   
// if the player leaves the block with the trader in it, the player can then re-enter the block to open the shop again
                if (playerObject.playerAtBlock(Map.currentMap.levelTrader.rowIndexInMap, Map.currentMap.levelTrader.colIndexInMap) == false) {
// sets visitedTrader to false
                    visitedTrader = false;
                }
            }
// if there is an entity instance in the map
            if (Map.currentMap.hostile != null) {

                if (playerObject.playerAtBlock(Map.currentMap.hostile.rowIndexInMap, Map.currentMap.hostile.colIndexInMap) && Map.currentMap.hostile.health > 0) {
// A hostile will attack every 60 frames (ideally = 1s) if the player is within its block
                    if (loopCounter == 60) {
// play attack sound
                        playSE(6);
// hostile entity attacks player
                        Map.currentMap.hostile.attack(playerObject, (Graphics2D)getGraphics());
                    }
                }
                // If the hostile is dead, the player is rewarded with some coins
                if (Map.currentMap.hostile.health <= 0 && !playerRewarded) {

                    playerObject.coins += 5;
// sets playerRewarded to true for this level
                    playerRewarded = true;
                }
            }

            

        // If the game is in normal playing state
        if (gameState == playingState) {
            // if the player clicks their mouse
            if (MouseInput.mousePressed) {
                // play attack sound
                playSE(6);
// if there is an entity in the current map
                if (Map.currentMap.hostile != null) {
// evaluates if the player is in one of the surrounding blocks of the entity
                    boolean playerNear = playerObject.playerNearBlock(Map.currentMap.hostile.rowIndexInMap, Map.currentMap.hostile.colIndexInMap);
// checks if the player is in the entity's block
                    boolean playerIn = playerObject.playerAtBlock(Map.currentMap.hostile.rowIndexInMap, Map.currentMap.hostile.colIndexInMap);
// if either are true, the player attacks the entity
                    if (playerIn || playerNear) {

                        playerObject.attack(Map.currentMap.hostile, (Graphics2D)getGraphics());
                    }
                }
// so that attacks are not repeated many times, sets MouseInput.mousePressed = false
                MouseInput.mousePressed = false;
            }

            try {
// if the user pressed 'w'
                if (PlayerInput.upPressed) {
// try to move player up
                    GamePanel.playerObject.movePlayer(0,-playerObject.speed);
                    
                } 
// if user presses 's' 
                if (PlayerInput.downPressed) {
// try to move down
                    GamePanel.playerObject.movePlayer(0, playerObject.speed);
                } 

                if (PlayerInput.leftPressed) {
// if user presses 'a'
                    GamePanel.playerObject.movePlayer(-playerObject.speed,0);
// try to move left and change the sprite to the left facing one
                    playerObject.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/skeletonPlayer.png"));
                } 

                if (PlayerInput.rightPressed) {
// if user presses 'd'
                    GamePanel.playerObject.movePlayer(playerObject.speed,0);
// try to move right and change the sprite to the right facing one
                    playerObject.sprite = ImageIO.read(getClass().getResourceAsStream("/2dTiles/SkeletonPlayerRight.png"));
                } 
// if the player presses esc while game state = playing
                if (PlayerInput.escapePressed) {
// pause the game
                    gameState = pauseState;
// set escapePressed to false to avoid loops
                    PlayerInput.escapePressed = false;
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        // If the game is paused
        else if (gameState == pauseState) {
// and player presses esc, unpause game
            if (PlayerInput.escapePressed) {

                gameState = playingState;

                PlayerInput.escapePressed = false;
            }
// return to menu if player presses 'f'
            if (PlayerInput.fPressed) {

                gameState = menuState;

                PlayerInput.fPressed = false;
            }
// reset game if player presses 'r'
            if (PlayerInput.rPressed) {
                
                restart();
            }
        }

        // If the user is in the trader's shop
        else if (gameState == shopState) { 

            if (PlayerInput.escapePressed) {
// leave the shop if the player presses esc
                gameState = playingState;

                PlayerInput.escapePressed = false;

                selectedItem = 'n';
            }

            // select the left shop item and play selection sound if player presses 'a'
            if (PlayerInput.leftPressed) {

                if (selectedItem == 'R') {playSE(5);}

                selectedItem = 'L';


                PlayerInput.leftPressed = false;
            }
            // select the right shop item and play selection sound if player presses 'd'
            if (PlayerInput.rightPressed) {

                if (selectedItem == 'L') {playSE(5);}
                
                selectedItem = 'R';

                PlayerInput.rightPressed = false;
            }
// if player presses 'f'
            if (PlayerInput.fPressed) {
// buy the selected item
                if (playerObject.buyItem(selectedItem)) {
// draw a special graphic notification when the transaction occurs
                    Map.currentMap.levelTrader.drawBuyFlash((Graphics2D)getGraphics(), selectedItem);
                }

            }
        }
        // if the player is in the menu and presses 'f' it will start the game
        else if (gameState == menuState) {
            if (PlayerInput.fPressed) {
                gameState = playingState;
            }
        }
// if the player died, and then press 'r', the game will reset with a fresh map and player
        else if (gameState == deathState) {
            if (PlayerInput.rPressed) {
                restart();
            }
        }

    }

/**
 * method to paint the screen with all the things that should be on it, automatically called where the "repaint" method is called
 * Author: Robert
 * @param graphicsObj graphics2d instance to draw the screen with
 */
    public void paintComponent(Graphics graphicsObj) {
// new UI instance to help draw certain gamestates
        UI tempUI = new UI();
// reference to super (this needs to be called for this method to work)
        super.paintComponent(graphicsObj);

        Graphics2D graphicsObjTwo = (Graphics2D)graphicsObj;
// Draws the level map screen
        if (gameState != menuState) {drawMap(graphicsObjTwo);}

// If the current level has a hostile, it is drawn to the screen
        if (Map.currentMap.hostile != null) {
// only renders the hostile if it is alive
            if (Map.currentMap.hostile.health > 0) {
// takes a scaled version of the hostile's sprite
                Image scaledHostile = Map.currentMap.hostile.sprite.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0);
// draws the scaled image at the hostiles coordinates
                graphicsObjTwo.drawImage(scaledHostile, Map.currentMap.hostile.xPos, Map.currentMap.hostile.yPos, null);
            }
        }

// Draws the player over the map, but under pause menu and shop
        Image playerSprite = playerObject.sprite.getScaledInstance(playerObject.playerWidth, playerObject.playerHeight, 0);
// Drawing player with the player
        graphicsObjTwo.drawImage(playerSprite, playerObject.xPos, playerObject.yPos, null);  

// Draws the pause screen if the gamestate specifies
        if (gameState == pauseState) {
            tempUI.drawPauseMenu(graphicsObjTwo);
        }

// Draws the shop screen if the gamestate specifies
        if (gameState == shopState && Map.currentMap.levelTrader != null) {
            Map.currentMap.levelTrader.drawShop(graphicsObjTwo, selectedItem);
        }
        
// Draws the coin counter in the top left using a method from the UI class
        tempUI.drawCoinCounterAndHearts(graphicsObjTwo);

        if (gameState == deathState) {
// if the gamestate is deathstate, draws the death screen
            tempUI.drawDeathScreen(graphicsObjTwo);

        }

        if (gameState == menuState) {
// if the gamestate is currently the menustate it draws the main menu
            tempUI.drawMainMenu(graphicsObjTwo);
        }
        // disposes of the graphics object to save resources (memory)
        graphicsObjTwo.dispose();

    }

    /**
     * Author: Alp
     * This method plays and loopsthe wanted music
     * Precondition: integer i must be passed on, sound object must be initalized
     * Postcondition : the wanted music file is played and looped
     * @param - index of the file which u want to play
     * 
     */
    public static void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    /**
     * Author: Alp
     * this method stops the initialized sound
     * Precondition : sound object must be initalized
     * Postcondition : sound is stopped
     */
    public static void stopMusic(){
        sound.stop();
    }
    /**
     * Author: Alp
     * this method only plays a sound effect and stops
     * Precondition : sound object must be initalized 
     * Postcondtion : sound effect is played
     * @param i - index of the sound effect
     */
    public static void playSE(int i){
        sound.setFile(i);
        sound.play();
    }


/**
 * Method to draw the map based on its 2d array
 * Author: Robert/Alp
 * @param graphicsObj graphics2d instance
 */
    public void drawMap(Graphics2D graphicsObj) {
// sets a new 2d array as the map array, makes easier to read
        int[][] currentMapPath = Map.currentMap.mapPathway;
// iterates for all rows
        for (int i = 0; i < currentMapPath.length; i++) {
        // iterates for all columns
            for (int j = 0; j < currentMapPath.length; j++) {
                // sets the top left coordinate pair of the current block
                int topLeftXCoor = (int) (i * Map.currentMap.blockWidth);
                int topLeftYCoor = (int) (j * Map.currentMap.blockHeight);
                // draws a lava tile if the current block has a value of 0
                if (currentMapPath[j][i] != 1) {
                        // NON PATH BLOCK
                        graphicsObj.drawImage(Map.nonPathImage.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0), topLeftXCoor, topLeftYCoor, null);
                }
    
                else {
// 2 boolean variables used to check whether the current block is a starting block or exit bloc
                    boolean atExit = (j == Map.currentMap.mapExitBlock[0] && i == Map.currentMap.mapExitBlock[1]);

                    boolean atStart = (j == Map.currentMap.startingPoint[0] && i == Map.currentMap.startingPoint[1]);

                    // EXIT BLOCK/STARTING BLOCK
                    if (atExit || atStart) {
                            // if the current block is a start or exit, draws a door tile
                            graphicsObj.drawImage(Map.exitBlockImage.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0), topLeftXCoor, topLeftYCoor, null);
                            
                            
                    }
                    // otherwise, draw a normal path block
                    else {
                        // NORMAL PATH BLOCK
                        graphicsObj.drawImage(Map.pathImage.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0), topLeftXCoor, topLeftYCoor, null);

                    } 
                }
            }
// if there is a chest in the current map
            if (Map.currentMap.levelChest != null) {
// init a image variable
                BufferedImage tempSprite;
// if the chest has been opened by a player, load the opened chest picture
                if (!Map.currentMap.levelChest.hasBeenOpened) {

                    tempSprite = Map.currentMap.levelChest.closedSprite;
                }
// if the chest is closed, load the closed picture
                else {
                    tempSprite = Map.currentMap.levelChest.openSprite;
                }
// draws the chest with whatever image it was evaluated to have
                graphicsObj.drawImage(tempSprite.getScaledInstance(Map.currentMap.blockWidth, Map.currentMap.blockHeight, 0), Map.currentMap.levelChest.xPos, Map.currentMap.levelChest.yPos, null);
            }
            // if the map has a trader
            if (Map.currentMap.levelTrader != null) {
                // load an image variable of the trader sprite
                BufferedImage TraderSprite = Map.currentMap.levelTrader.sprite;
// draw the scaled sprite at the trader's location
                graphicsObj.drawImage(TraderSprite.getScaledInstance(Map.currentMap.levelTrader.width, Map.currentMap.levelTrader.height, 0), Map.currentMap.levelTrader.xPos, Map.currentMap.levelTrader.yPos, null);
            }
        }
        // I SHOULD HAVE ADDED THE ENTITY DRAWING HERE, HOWEVER IT WAS ADDED SO LATE I FORGOT ABOUT THIS
    }
}
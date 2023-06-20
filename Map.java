import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * Class to represent one level of the game
 * Author: Robert
 */
public class Map {
// Keeps track of whether there has been a level created or not
    static boolean firstLevelCreated = false;
// The size of the map level's 2d array
    int size;
// the width of each block in pixels
    int blockWidth;
// the height of each block in pixels
    int blockHeight;
// keeps track of the amount of path blocks there are in each row
    int[] pathBlocksPerRow;
// keeps track of the amount of path blocks there are in each column 
    int[] pathBlocksPerColumn;
// 2d array to represent the map level
    int[][] mapPathway;
// an index pair (row, column) of the block the player needs to reach in order to go to the next level
    int[] mapExitBlock = new int[2];
// an index pair of the first path block in the level
    int[] startingPoint = new int[2];
// keeps track of the last block in the last level 
    static int[] lastBlockIndex = new int[2];
// keeps track of the level number
    static int levelNumber;
// keeps track of the current instance of the map class
    static Map currentMap;
// initializes the image variables for all the textures in the map
    public static BufferedImage pathImage, nonPathImage, exitBlockImage, exitBlockImage2;
// initalizes an instance of the chest class 
    Chest levelChest;
// initalizes an instance of the trader class which extends entity
    Trader levelTrader;
// initalizes an instance of the entity class
    Entity hostile;

/**
 * A method to remove the duplicates from an arraylist of integer arrays
 * Author: Robert
 * @param arr any given arraylist of 1d integer arrays
 */
    public void removeDupes(ArrayList<int[]> arr) {
// iterates through the list elements
        for (int i = 0; i < arr.size(); i++) {
// iterates through the list elements for every list element
            for (int j = 0; j < arr.size(); j++) {
// checks if it is a duplicate but not the same index
                if (Arrays.equals(arr.get(i), arr.get(j)) && i != j) {
// removes the element j
                    arr.remove(j);
                }
            }
        }
    }
        
    

/**
 * This method will check the values of each index that surrounds the given index and 
 * returns a list of the indices that have a value of 0 (the indices that are not path blocks)
 * Author: Robert
 * @param rowIndex an index indicating the row of the block
 * @param colIndex an index indicating the column of the block
 * @return a 2d list of all the possible indices that could become a path block
 */
    public ArrayList<int[]> checkCloseBlocks(int rowIndex, int colIndex, int[][] mapArray) {
// array list to be returned
        ArrayList<int[]> possiblePathBlocks = new ArrayList<int[]>();

// if the starting point is on the top or bottom of the map
        if (startingPoint[0] == mapArray.length - 1 || startingPoint[0] == 0) {
// calculates which direction a new possible path block is in
            int yDirection = -(((startingPoint[0] + mapArray.length/2) - mapArray.length) / Math.abs((startingPoint[0] + mapArray.length/2) - mapArray.length));
// creates an index pair
            int[] tempArray = {rowIndex + yDirection, colIndex};
// adds the index pair to the arraylist
            possiblePathBlocks.add(tempArray);
        }
// if the starting point is on one of the (left or right) sides of the map
        if (startingPoint[1] == mapArray.length - 1 || startingPoint[1] == 0) {
// calculates direction
            int xDirection = -(((startingPoint[1] + mapArray.length/2) - mapArray.length) / Math.abs((startingPoint[1] + mapArray.length/2) - mapArray.length));
// creates index pair
            int[] tempArray = {rowIndex, colIndex + xDirection};
// adds to arraylist
            possiblePathBlocks.add(tempArray);
        }
// if the block to the right of the current path block is within range of the map, and there aren't too many path blocks in that row
        if (colIndex + 1 < mapArray.length && pathBlocksPerRow[rowIndex] <= 3) {
// checks if it is not already a path block
            if (mapArray[rowIndex][colIndex + 1] == 0) {
// adds to arraylist
                possiblePathBlocks.add(new int[] {rowIndex, colIndex + 1});
            }
        }
// if the block to the left is within range and not too many path blocks in that row
        if (colIndex - 1 > 0 && pathBlocksPerRow[rowIndex] <= 3) {
// if it isn't already a path block
            if (mapArray[rowIndex][colIndex - 1] == 0) {
// adds to arraylist
                possiblePathBlocks.add(new int[] {rowIndex, colIndex - 1});
            }
        }
// checks block below current, and not too many blocks in current column
        if (rowIndex + 1 < mapArray.length && pathBlocksPerColumn[colIndex] <= 3) {
// if it isn't a path block
            if (mapArray[rowIndex + 1][colIndex] == 0) {
// adds to the arraylist
                possiblePathBlocks.add(new int[] {rowIndex + 1, colIndex});
            }
        }
// checks block above current and not too many in current column
        if (rowIndex - 1 > 0 && pathBlocksPerColumn[colIndex] <= 3) {
// if not a path block
            if (mapArray[rowIndex - 1][colIndex] == 0) {
// adds to arraylist
                possiblePathBlocks.add(new int[] {rowIndex - 1, colIndex});
            }
        }
// returns the arraylist of possible next path blocks
        return possiblePathBlocks;
    }


/**
 * Method to check if the current indexed block is the last on the path (if it is directly beside a wall)
 * Author: Robert
 * @param rowIndex The row index of the block to be checked
 * @param colIndex The column index of the block to be checked
 * @param mapArray The 2d array representing the current level path
 * @return a boolean, true if the current block can be the last in the path, false if otherwise
 */
    public boolean checkLastBlock(int rowIndex, int colIndex, int[][] mapArray, int amountPathBlocks) {
// uses pythagoras' theorem to calculate the distance from the starting point to the current point on the map 
        double distanceFromStart = Math.sqrt(Math.pow(Math.abs(rowIndex - startingPoint[0]), 2) + Math.pow(Math.abs(colIndex - startingPoint[1]), 2));
// if the generator has travelled at least half of the array length away from the start and there is a certain amount of path block
        if ((int)distanceFromStart >= (mapArray.length)/2 && amountPathBlocks > size - 2) {
// another check to see if the current block is against the roof, floor, or one of the sides of the map
            if (rowIndex == 0 || rowIndex + 1 == mapArray.length || colIndex == 0 || colIndex + 1 == mapArray.length) {
// returns true
                return true;
            }
        }
// otherwise returns false
        return false;
    }

/**
 * Method to return whether there is a path block at a certain coordinate point on the map
 * Author: Robert
 * @param xCoord an x coordinate within range of the windowWidth
 * @param yCoor a y coordinate within range of the windowHeight
 * @return true if there is a path block at the coordinates, false if not
 */
    public static boolean checkIfPathBlock(int xCoord, int yCoord) {
// sets a temporary map instance to the current map
        Map currentLvl = Map.currentMap;
// calculates row index with the y coordinate
        int rowIndex = (yCoord / currentLvl.blockHeight);
// calculates column index with x coordinate
        int colIndex = (xCoord / currentLvl.blockWidth);
// if rowIndex and colIndex are within range of the map
        if (rowIndex < currentLvl.size && rowIndex >= 0 && colIndex < currentLvl.size && colIndex >= 0) {
// if the value in the map array at those indices is one
            if (currentLvl.mapPathway[rowIndex][colIndex] == 1) {
// returns true
                return true;
            }
        }
        // other wise return false
        return false;
    }


/**
 * Constructor method that randomly generates a 2d array to represent the map/level
 * Author: Robert
 * @param size desired square dimensions of the returned array
 * @return a 2d array that has a randomly generated pathway through it
 */
    public Map(int size) {
// Each time a map is creates, adds one to the static levelnumber
        levelNumber += 1;
// sets pathblocks to default 0
        int amountPathBlocks = 0;
// instance variable size set to parameter
        this.size = size;
// instantiates an instance of "random" class for... "random" map generation
        Random randInt = new Random();
// calculates the width of each block
        blockWidth = GamePanel.windowWidth/size;
// calculates the height of each block
        blockHeight = GamePanel.windowHeight/size;
// init index pair to represent the current path block in the iteration
        int[] currentPathBlock = new int[2];
// init array to keep track of each row's path block amount
        pathBlocksPerRow = new int[size];
// init array to keep track of each column's path block amount
        pathBlocksPerColumn = new int[size];
// init 2d array to represent the map
        int[][] mapArray = new int[size][size];
// iterates through each row 
        for (int i = 0; i < size; i++) {
// iterates through each column
            for (int j = 0; j < size; j++) {
// creating a blank array of 0s
                mapArray[i][j] = 0;
            }
// sets the values at each index to 0 in both arrays
            pathBlocksPerRow[i] = 0;
            pathBlocksPerColumn[i] = 0;
        }

// If this is the first map in the program
        if (firstLevelCreated == false) {
// it chooses a random starting location on the ceiling, floor, left, or right side of the map
            int[][] possibleStarts = {{0, randInt.nextInt(size)}, {size - 1, randInt.nextInt(size)}, {randInt.nextInt(size), 0}, {randInt.nextInt(size), size - 1}};
// takes the index pair value at a random index in the possible starts array
            startingPoint = possibleStarts[randInt.nextInt(4)];
        }
// if this is not the first map
        else {
// Iterates twice, once for row, once for column
            for (int i = 0; i < 2; i++) {
// sets the new starting point based off where the exit was on the last map
// if the last map ended on the right or the bottom, the new map starts on the left or the top, respectively
                if (lastBlockIndex[i] == size - 1) {
                    startingPoint[i] = 0;
                }
// vise-versa here
                else if (lastBlockIndex[i] == 0) {
                    startingPoint[i] = size - 1;
                }
// if one of the indices is not 0 or the max, it keeps its old value
                else {
                    startingPoint[i] = lastBlockIndex[i];
                }
            }
        }
// sets the first current index pair to the starting point
        currentPathBlock[0] = startingPoint[0];

        currentPathBlock[1] = startingPoint[1];
// sets the first block as a path block
        mapArray[currentPathBlock[0]][currentPathBlock[1]] = 1;
// increases the amount of path blocks in the current row and column
        pathBlocksPerRow[currentPathBlock[0]] += 1;

        pathBlocksPerColumn[currentPathBlock[1]] += 1;

// keeps track of whether there is a chest, trader, and entity on the map already
        boolean chestCreated = false;
        boolean traderCreated = false;
        boolean hostileCreated = false;
// While loop to keep running the generation until it finds a suitable exit block
        while (true) {
// if it finds a suitable exit blocl
            if (checkLastBlock(currentPathBlock[0], currentPathBlock[1], mapArray, amountPathBlocks) == true) {
// updates static lastBlockIndex to the current index pair
                lastBlockIndex = currentPathBlock;
// sets this maps exit block to the current index pair
                mapExitBlock = currentPathBlock;
// breaks out of the while loop
                break;
            }

            else {
// Creates a temporary list of all the possible next path blocks moving from the current one
                ArrayList<int[]> tempArrayList = checkCloseBlocks(currentPathBlock[0], currentPathBlock[1], mapArray);
// removes any duplicates from the arraylist
                removeDupes(tempArrayList);
// takes a random index pair from the arraylist of possible path blocks
                currentPathBlock = (int[])tempArrayList.get(randInt.nextInt(tempArrayList.size()));
// updates the amount of pathblocks in the current row and column
                pathBlocksPerRow[currentPathBlock[0]]++;
                pathBlocksPerColumn[currentPathBlock[1]]++;
// sets the current index in the map to 1, representing a path block
                mapArray[currentPathBlock[0]][currentPathBlock[1]] = 1;
// increases the total amount of path blocks
                amountPathBlocks++;
// Redundant check, for some reason it only works well with this here
                if (checkLastBlock(currentPathBlock[0], currentPathBlock[1], mapArray, amountPathBlocks) == false) {
// If there isn't already a chest created, it will create a chest if a random number generator generates a 2 and the current block is not the exit block
                    if (chestCreated == false && randInt.nextInt(10) == 2 && Arrays.equals(currentPathBlock, mapExitBlock) == false) {
// instantiates a chest at the current indices
                        levelChest = new Chest(currentPathBlock[0], currentPathBlock[1]);
// tells the rest of the program a chest is in this map
                        chestCreated = true;
                    }
// if there isn't already a trader, its been 3 levels since the last trader, it is not the first level, and a random number condition is met
                    if (traderCreated == false && levelNumber % 3 == 0 && levelNumber != 0 && randInt.nextInt(10) == 2) {
// checks if there is a chest created
                        if (chestCreated) {
// check where the chest is
                            int[] chestIndices = {this.levelChest.rowIndex, this.levelChest.colIndex};
// if the current block is where the chest is, the trader cannot spawn here
                            if (Arrays.equals(currentPathBlock, chestIndices) == false) {
// if the blocks are not the same, the trader will spawn
                                levelTrader = new Trader("Red Hat", currentPathBlock[0], currentPathBlock[1]);
// updates variable until next map is made
                                traderCreated = true;
                            }
                        }   
// if there is no chest, no need to worry about overlap
                        else {

                            levelTrader = new Trader("Red Hat", currentPathBlock[0], currentPathBlock[1]);

                            traderCreated = true;
                        }
                    }
// if there isn't already a hostile entity
                    if (hostileCreated == false) {
// init 2 arrays to house the index pairs of the level's chest/trader
                        int[] chestIndices;

                        int[] traderIndices;
// if this random number condition is met, the entity can spawn (CAN spawn not WILL spawn)
                        boolean spawn = (randInt.nextInt(8) == 2);

                        if (spawn) {
// if there is only a chest
                            if (chestCreated && !traderCreated) {
// gets chest indices pair
                                chestIndices = new int[] {this.levelChest.rowIndex, this.levelChest.colIndex};
// compares the current block indices with the chest indices
                                if (Arrays.equals(currentPathBlock, chestIndices) == false) {
// if they are unequal, the entity is instantiated at this block
                                    this.hostile = new Entity("Jeremy", currentPathBlock[0], currentPathBlock[1]);

                                    hostileCreated = true;
                                }
                            }
// if there is only a trader
                            else if (!chestCreated && traderCreated) {
// gets the indices of the trader
                                traderIndices = new int[] {this.levelTrader.rowIndexInMap, this.levelTrader.colIndexInMap};
// compares trader indices to current block
                                if (Arrays.equals(currentPathBlock, traderIndices) == false) {
// if the trader is not at this block, the entity is instantiated at this block 
                                    this.hostile = new Entity("Jeremy", currentPathBlock[0], currentPathBlock[1]);

                                    hostileCreated = true;
                                }

                            }
// if there is a chest and a trader
                            else if (chestCreated && traderCreated) {
// gets chest indices
                                chestIndices = new int[] {this.levelChest.rowIndex, this.levelChest.colIndex};
// gets trader indices
                                traderIndices = new int[] {this.levelTrader.rowIndexInMap, this.levelTrader.colIndexInMap};
// compares the current block to the chest indices and the trader indices
                                if (Arrays.equals(currentPathBlock, traderIndices) == false && Arrays.equals(currentPathBlock, chestIndices) == false) {
// only if neither the chest and trader are on this block can the entity be instantiated
                                    this.hostile = new Entity("Jeremy", currentPathBlock[0], currentPathBlock[1]);

                                    hostileCreated = true;
                                }
                            }

                            else {
// if there is no chest and no trader, there are no checks to be done
                                this.hostile = new Entity("Jeremy", currentPathBlock[0], currentPathBlock[1]);

                                hostileCreated = true;
                            }
                            // if there was an entity instantiated, its coordinates are updates
                            if (this.hostile != null) {

                                this.hostile.updateCoords(this.blockWidth, this.blockHeight);
                            }
                        }
                    }
                }
            }
        }

        // saving all block images
        try {

            pathImage = ImageIO.read(getClass().getResourceAsStream("/2dTiles/path_Tile.png"));

            exitBlockImage = ImageIO.read(getClass().getResourceAsStream("/2dTiles/pathExitTile.png"));

            exitBlockImage2 = ImageIO.read(getClass().getResourceAsStream("/2dTiles/pathExitTile2.png"));

            nonPathImage = ImageIO.read(getClass().getResourceAsStream("/2dTiles/lava_Tile.png"));

        } catch (IOException e) {

            System.out.println("Resource Packs Not Found");
        }
        // sets this map instance's array to the one generated
        this.mapPathway = mapArray;
        // sets the static variable currentMap to the newly created map
        currentMap = this;
        // makes sure the firstLevelCreated is true
        firstLevelCreated = true;

        // Ensures chest is in proper location
        if (chestCreated) {

            this.levelChest.setCoordinates();
        }   
        // Ensures trader is in proper location
        if (traderCreated) {

            this.levelTrader.setCoordinates();
            
            this.levelTrader.updateDimensions();

        }
    }
}
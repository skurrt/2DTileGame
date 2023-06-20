import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class UI {
    //initializes the instance variables
//png image of the coin sprite
    BufferedImage coinImage;
// init png image of the heart sprite
    BufferedImage heartImage;
// initializing four fonts to be specified in the constructor
    static Font counterFont;

    static Font titleFont;

    static Font normalFont;
    
    static Font smallFont;

    static Font subTitleFont;
    /**
     * Author Rob/ Alp
     * constructor method for UI class
     * Precondition : None
     * Postcondition : the instance variables are updated
     */
    public UI() {   
        // takes the coin and heart sprites from the GamePanel class and sets them to their initalized variables
        coinImage = GamePanel.coin;
        heartImage = GamePanel.heart;

        //specifying 4 fonts
        counterFont = new Font("Constantia", Font.PLAIN, 40);

        titleFont = new Font("Constantia", Font.BOLD, 75);

        subTitleFont = new Font("Constantia", Font.PLAIN, 50);

        normalFont = new Font("Constantia", Font.PLAIN, 30);

        smallFont = new Font("Constantia", Font.PLAIN, 20);
    }
    /**
     * Author : Rob / Alp
     * draws the coin counter on the top left of the screen
     * Precondition : A graphics2d Object must be passed on
     * Postcondition : The coint counter is printed
     * @param graphicsObj - Takes in a Graphics2D object
     */
    public void drawCoinCounterAndHearts(Graphics2D graphicsObj) {
// using the counter font
        graphicsObj.setFont(counterFont);
// sets color to gold
        graphicsObj.setColor(new Color(212, 175, 55, 255));

        //scales the coin image, and displays it in top left of screen
        graphicsObj.drawImage(coinImage.getScaledInstance(70, 70, 0), 25, 25, null);

        //the amount of current coins is printed
        graphicsObj.drawString("" + GamePanel.playerObject.coins, 90, 74);
// changes color to white
        graphicsObj.setColor(Color.white);
// scales and displays the heart image in the top left of the screen
        graphicsObj.drawImage(heartImage.getScaledInstance(85, 85, 0), GamePanel.windowWidth - 110, 25, null);
// writes the amount of hearts left
        graphicsObj.drawString("" + GamePanel.playerObject.health, GamePanel.windowWidth - 125,  80);
        // set font to title
        graphicsObj.setFont(titleFont);
        // write the level number as a counter at the top of the screen
        graphicsObj.drawString("" + Map.levelNumber, GamePanel.windowWidth/2 - 15, 50);

    }
    /**
     * Author : Rob / Alp
     * draws the pause screen for the game
     * Precondition : A graphics2d object must be passed on
     * Postcondition : The pause screen is displayed
     * @param graphicsObj- Takes in a Graphics2D object
     */
    public void drawPauseMenu(Graphics2D graphicsObj) {
// sets font to title font
        graphicsObj.setFont(titleFont);
// makes the colour a translucent black
        graphicsObj.setColor(new Color(0, 0, 0, 170));
// creates a rectangle over the whole screen such that it darkens it 
        graphicsObj.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);
// changes colour to white for writing
        graphicsObj.setColor(Color.white);
// Displays "Game Pause" in the near center of the screen
        graphicsObj.drawString("Game Paused", GamePanel.windowWidth/4, (GamePanel.windowHeight/2) - 10);
// sets font to a smaller font
        graphicsObj.setFont(normalFont);
// creates a rectangle object to calculate the dimensions of the text
        Rectangle2D stringRect = normalFont.getStringBounds("Press 'f' To Return To Menu", new FontRenderContext(null, true, true));
// writes the text centered in the screen
        graphicsObj.drawString("Press 'f' To Return To Menu", GamePanel.windowWidth/2 - (int)stringRect.getWidth()/2, (2*GamePanel.windowHeight/3));
// another rectangle object for dimensions of different text
        stringRect = normalFont.getStringBounds("Press 'r' To Restart", new FontRenderContext(null, true, true));
// writes the other text also centered
        graphicsObj.drawString("Press 'r' To Restart", GamePanel.windowWidth/2 - (int)stringRect.getWidth( )/2, GamePanel.windowHeight/2 + (int)stringRect.getHeight());
    }
/**
 * Draws the main menu screen if the game state specifies it
 * Author: Robert
 * @param g any graphics2d object
 */
    public void drawMainMenu(Graphics2D g) {
// Sets colour to black
        g.setColor(Color.BLACK);
// Creates a black background
        g.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);
// Using title font
        g.setFont(titleFont);
// Using white text
        g.setColor(Color.white);
// Writes the first part of the title slightly offset from the left wall of the window
        g.drawString("Skeleton: ", GamePanel.windowWidth/10, GamePanel.windowHeight/4);
// Using a smaller font
        g.setFont(subTitleFont);
// Creating a rectangle object to calculate dimensions of text
        Rectangle2D stringRect = subTitleFont.getStringBounds("Venture Through ", new FontRenderContext(null, true, true));
// Writes the second part of the title under the first part
        g.drawString("Venture Through ", GamePanel.windowWidth/10, GamePanel.windowHeight/4 + (int)stringRect.getHeight());
// Using a dark-ish red
        g.setColor(new Color(255, 0, 63, 255));
// back to the title font
        g.setFont(titleFont);
// Writes the last part of the title under the second part
        g.drawString("H E L L", GamePanel.windowWidth/10, GamePanel.windowHeight/4 + 2*(int)stringRect.getHeight() + 10);
// Using normal font
        g.setFont(normalFont);
// Using white text
        g.setColor(Color.white);
// New rectangle object for dimensions
        stringRect = normalFont.getStringBounds("Press 'f' To Start", new FontRenderContext(null, true, true));
// Draws the last text in the near center of the screen
        g.drawString("Press 'f' To Start", GamePanel.windowWidth/2 - (int)stringRect.getWidth()/2, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2);

        g.setFont(smallFont);

        g.setColor(new Color(255, 0, 63, 255));

        g.drawString(" - PRESS ESC TO PAUSE THE GAME, PRESS 'r' TO RESET YOUR PROGRESS WHEN IN THE PAUSE MENU", 0, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2 + 100);
        
        g.drawString(" - MAKE SURE TO HAVE AUDIO ON FOR THE BEST EXPERIENCE!", 0, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2 + 150);

        g.drawString(" - COLLECT COINS BY RUNNING OVER CHESTS!", 0, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2 + 200);

        g.drawString(" - SPEND UR COINS BUYING UPGRADES USING THE 'f' KEY IN THE SHOP, PRESS ESC TO LEAVE THE SHOP", 0, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2 + 250);
        
        g.drawString(" - PRESS LEFT CLICK TO ATTACK MOBS, BE CAREFUL THEY WILL ATTACK YOU!", 0, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2 + 300);
    }

/**
 * Method to draw the death screen if the gamestate specifies it
 * Author: Robert
 * @param g any graphics2d object
 */
    public void drawDeathScreen(Graphics2D g) {
// sets color to a bright translucent red (so it looks like blood, cause... the player is dead)
        g.setColor(new Color(255, 0, 100, 160));
// covers the entire window in the red translucent rectangle
        g.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);
// 
        Rectangle2D stringRect = titleFont.getStringBounds("Death", new FontRenderContext(null, true, true));

        g.setFont(titleFont);

        g.setColor(Color.BLACK);

        g.drawString("Death", GamePanel.windowWidth/2 - (int)stringRect.getWidth()/2, GamePanel.windowHeight/2 - (int)stringRect.getHeight()/2);

        g.setFont(normalFont);

        stringRect = normalFont.getStringBounds("Press 'r' To Restart", new FontRenderContext(null, true, true));

        g.drawString("Press 'r' To Restart", GamePanel.windowWidth/2 - (int)stringRect.getWidth()/2, GamePanel.windowHeight/2 + (int)stringRect.getHeight());
        

    }
}

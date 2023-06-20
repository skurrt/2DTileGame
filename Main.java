public class Main {

    static GamePanel panelObject;
    
    /**
     * Author : Alp/Robert
     * This main method triggers all the components of the program and runs the main game
     * Precondition: None
     * Postcondition : The game loop is activated
     * @param args - N/A
     */
    public static void main(String[] args) {
       
        //initializes gameframe object
        GameFrame window = new GameFrame();

        //initliazes GamePanel object
        panelObject = new GamePanel();

        //calls the methods that are required to run the game loop
        window.add(panelObject);
        window.pack();
        window.setLocationRelativeTo(null);
        panelObject.startThread();
        
    }
}
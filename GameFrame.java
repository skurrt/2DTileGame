import javax.swing.JFrame;
import java.awt.*;  

public class GameFrame extends JFrame {
    /*
     * Author : Robert/ Alp
     * the constructor for the GameFrame class
     * Precondition : none
     * Postcondition : GameFrame object is initialized
     */
    public GameFrame() {
        //referencs parent file
        super();


        //sets the window name
        this.setTitle("Skeleton: Venture Through Hell");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);

        //initiliazes keylistener
        this.addKeyListener(new PlayerInput());

        this.addMouseListener(new MouseInput());

        this.setVisible(true);

        this.pack();
        
        //sets the icon for the window
        Image icon = Toolkit.getDefaultToolkit().getImage("/2dTiles/skeletonPlayer.png");  

        setIconImage(icon);    
       
    }
}

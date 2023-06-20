import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{
    //init instance variabl
    static boolean mousePressed;

    /**
     * Author : Robert
     * Precondition : java.awt.event.MouseEvent must be passed on
     * Postcondition : None
     * this method serves no purpose but must be set up to use the mouselistener
     * @param java.awt.event.MouseEvent - mouse event
     */
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    /**
     * Author : Rob
     * This method listens for a mouse click
     * Precondition : java.awt.event.MouseEvent must be passed on
     * Postcondition : Mousepressed becomes true
     * @param java.awt.event.MouseEvent - mouse event
     */
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        
        mousePressed = true;

    }
    /** 
     * Author : Rob
     * This method listens for the release of the mouse button
     * Precondition : java.awt.event.MouseEvent must be passed on 
     * Postcondition : mousepressed becomes false
     * @param java.awt.event.MouseEvent - mouse eevent
    */
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {

        mousePressed = false;

    }
    /**
     * This method serves no purpose but must be set up to use the listener
     * Author : Rob
     * Precondition : java.awt.event.MouseEvent must be passed on 
     * Postcondition : NOne
     * @param java.awt.event.MouseEvent - Mouse event
     */
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    
    /**
     * This method serves no purpose but must be set up to use the listener
     * Author : ROob
     * Precondition : java.awt.event.MouseEvent must be passed on 
     * Postcondition : None
     * @param java.awt.event.MouseEvent - Mousevenet
     */
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
    
    


}

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerInput implements KeyListener {
        //init instance variables to communicate which keys are being pressed in a time relative way
        static boolean upPressed, downPressed, rightPressed, leftPressed, escapePressed, fPressed, rPressed;

        /**
         * Author: Robert
         * Precondition : None
         * Postcondtion : None
         * overwrittes parent method, serves no actual purpose but is required for the object to work
         */
        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * Author : Rob/Alp
         * this method listens for pressed buttons
         * Precondition : Keyevent must be passed on
         * Postcondtion : Instance variables are updated
         */
        @Override
        public void keyPressed(KeyEvent e) {
            //if w is pressed uppressed becomes true
            if (e.getKeyChar() == 'w') {

                upPressed = true;

            } 
            //if s is pressed downPressed becomes true
            if (e.getKeyChar() == 's') {

                downPressed = true;

            } 
            //if a is pressed leftPressed becomes true
            if (e.getKeyChar() == 'a') {

                leftPressed = true;

            } 
            //if d is pressed rightPressed becomes true
            if (e.getKeyChar() == 'd') {

                rightPressed = true;

            } 
            //if e is pressed
            if (e.getKeyCode() == 27) {
                //if escape is already pressed switches boolean
                if (escapePressed) 
                {escapePressed = false;}
                
                else {escapePressed = true;}
                
            }
            //if f is pressed fpressed becomes true
            if (e.getKeyChar() == 'f') {

                fPressed = true;
            }

            if (e.getKeyChar() == 'r') {

                rPressed = true;
            }

        }

        /**
         * Author : Rob/Alp
         * this method listens for released buttons
         * Precondition : Keyevent must be passed on
         * Postcondtion : Instance variables are updated
         */
        @Override
        public void keyReleased(KeyEvent e) {
            //if w is released uppressed becomes false
            if (e.getKeyChar() == 'w') {

                upPressed = false;

            } 
            //if s is released downPressed becomes false
            if (e.getKeyChar() == 's') {

                downPressed = false;

            } 
            //if a is released leftpressed becomes false
            if (e.getKeyChar() == 'a') {

                leftPressed = false;

            } 
            //if d is released rightpressed becomes false
            if (e.getKeyChar() == 'd') {

                rightPressed = false;

            } 

            if (e.getKeyChar() == 'f') {

                fPressed = false;
            }

            if (e.getKeyChar() == 'r') {

                rPressed = false;
            }
        }
}

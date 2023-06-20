import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



public class Sound {
    Clip clip;
    URL soundURL [] = new URL[30];
    /**
     * Author : Alp
     * initializes a Sound object
     * Precondition : None
     * Postcondition : an array of urls are assigned to the instance variable array
     */
    public Sound(){
        soundURL[0] = getClass().getResource("/sounds/song.wav");
        soundURL[1] = getClass().getResource("/sounds/door.wav");
        soundURL[2] = getClass().getResource("/sounds/chest.wav");
        soundURL[3] = getClass().getResource("/sounds/npc.wav");
        soundURL[4] = getClass().getResource("/sounds/buy.wav");
        soundURL[5] = getClass().getResource("sounds/switch.wav");
        soundURL[6] = getClass().getResource("sounds/attack.wav");
    }
    /**
     * Author : Alp
     * sets the file that will play the audio
     * Precondition: And integer i must be passed on
     * Postcondition : the wanted file is establisehd as the current one
     * @param i - the index of the file, which you want to play
     * 
     */
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e){

        }
    }
    /**
     * Author : Alp
     * plays the initialized audio file
     * Precondition : The clip variable must be set up
     * Postcondition : The set file is played
     */
    public void play(){
        clip.start();
    }
    /**
     * Author : Alp
     * loops the initialized audio file
     * Precondition : The clip variable must be set up
     * Postcondition : The set file is looped
     */
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }
    /**
     * Author : Alp
     * stops the initialized audio file
     * Precondition : The clip variable must be set up
     * Postcondition : The set file is stopped
     */
    public void stop(){
        clip.stop();
    }
}   

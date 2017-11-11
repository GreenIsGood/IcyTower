package Main;

import Listener.Characters;
import Listener.Exit;
import Listener.HighScore;
import Listener.Home;
import Listener.Instructions;
import Listener.LevelOne;
import Listener.LevelThree;
import Listener.LevelTwo;
import Listener.Levels;
import Listener.Losing;
import Listener.Winning;
import Model.Blocks;
import Model.Boy;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame{
    
    public static final int MAX_WIDTH = 100, MAX_HEIGHT = 100;
    public static Boy boy;
    public static Blocks blocks;
    private static GLCanvas glcanvas;
    private Animator animator;
    private static HashMap listener;
    private static AudioInputStream audioStream;
    private static AudioFormat format;
    private static final DataLine.Info info = new DataLine.Info(Clip.class, format);
    private static Clip audioClip;
    private static final ClassLoader classloader = 
            Thread.currentThread().getContextClassLoader();
    private static URL soundURL;
    private static boolean mute = false;
    public static Home home;
    private static LevelOne levelOne;
    private static LevelTwo levelTwo;
    private static LevelThree levelThree;
    private static GLEventListener gLEventListener;
    
    public static void main(String[] args) throws
            LineUnavailableException, UnsupportedAudioFileException, IOException {
        try {
            Thread.sleep(2100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        Main main = new Main();
        SwingUtilities.invokeLater (
                new Runnable() {
                    public void run() {
                        main.setVisible(true);
                    }
                }
        );
    } 

    public Main() throws
            LineUnavailableException, UnsupportedAudioFileException, IOException {
        //init list of listener and add screens to it
        listener = new HashMap();   
        home = new Home();
        Levels levels = new Levels();
        Instructions instructions = new Instructions();
        HighScore highScore = new HighScore();
        Exit exit = new Exit();
        Characters characters = new Characters();
        Winning winning = new Winning();
        Losing losing = new Losing();
        listener.put("Home", home);
        listener.put("Characters", characters);
        listener.put("Instructions", instructions);
        listener.put("HighScore", highScore);
        listener.put("Exit", exit);
        listener.put("Levels", levels);
        listener.put("Winning", winning);
        listener.put("Losing", losing);
        
        //init jframe and glganvas
        glcanvas = new GLCanvas(new GLCapabilities());
        glcanvas.addGLEventListener((GLEventListener) listener.get("Home"));
        glcanvas.addKeyListener((KeyListener) listener.get("Home"));
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(40);
        animator.add(glcanvas);
        animator.start();
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
                
        //play music
        soundURL = classloader.getResource("Audio/background.wav"); 
        audioStream = AudioSystem.getAudioInputStream(soundURL);
        format = audioStream.getFormat();
        audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        audioClip.start();
        glcanvas.requestFocus();
    }
    
    public static void setListener(String from, String to){
        GLContext gLContext = glcanvas.getContext();
        gLContext.makeCurrent();
        if(from.equals("LevelOne") || from.equals("LevelTwo") || 
                from.equals("LevelThree")){
            glcanvas.removeGLEventListener(gLEventListener);
            glcanvas.removeKeyListener((KeyListener) gLEventListener);
        }
        else{
            glcanvas.removeGLEventListener((GLEventListener) listener.get(from));
            glcanvas.removeKeyListener((KeyListener) listener.get(from));
        }
        if(to.equals("LevelOne")){
            levelOne = null;
            levelOne = new LevelOne();
            gLEventListener = levelOne;
        }
        else if(to.equals("LevelTwo")){
            levelTwo = null;
            levelTwo = new LevelTwo();
            gLEventListener = levelTwo;
        }
        else if(to.equals("LevelThree")){
            levelThree = null;
            levelThree = new LevelThree();
            gLEventListener = levelThree;
        }
        else{
            gLEventListener = (GLEventListener) listener.get(to);
        }
        gLEventListener.init(glcanvas);
        gLEventListener.display(glcanvas);
        glcanvas.addGLEventListener(gLEventListener);
        glcanvas.addKeyListener((KeyListener) gLEventListener);
    }
    
    public static void setMute(){
        if(!mute){
            audioClip.stop();
            mute = true;
        }
        else{
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
            mute = false;
        }
    }
    
    public static boolean isMute(){
        return mute;
    }
    
    public static void changeMusic(boolean isMute) throws 
            UnsupportedAudioFileException, LineUnavailableException, IOException{
        if(!isMute){
         setMute();
        }
        if(soundURL.getFile().substring(soundURL.getFile().length()-11,
                soundURL.getFile().length()).equals("playing.wav")){
            soundURL = classloader.getResource("Audio/background.wav"); 
            audioStream = AudioSystem.getAudioInputStream(soundURL);
            format = audioStream.getFormat();
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
        }
        else{
            soundURL = classloader.getResource("Audio/playing.wav");
            audioStream = AudioSystem.getAudioInputStream(soundURL);
            format = audioStream.getFormat();
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
        }
        if(!isMute){
            setMute();
        }
    }

}

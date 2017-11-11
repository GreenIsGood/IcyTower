package Listener;

import Main.Main;
import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Winning implements GLEventListener, KeyListener {
    
    private final String TEXTURE_NAME[] = {"Images/winnig.png",
        "Images/no.png", "Images/no_clicked.png",
        "Images/yes.png", "Images/yes_clicked.png", "Images/winnig1.png", 
        "Images/ok.png"};
    private TextureReader.Texture texture[] = new 
            TextureReader.Texture[TEXTURE_NAME.length];
    private int textures[] = new int[TEXTURE_NAME.length];
    private int move = 1;
    private static String currentLevel;
    private static int score = 0;
    private int scores[] = new int[3];
    private TextRenderer textRenderer =
            new TextRenderer(new Font(Font.SANS_SERIF, Font.BOLD, 30));
  
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);   //This Will Clear The Background Color To Black
        
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
        gl.glGenTextures(TEXTURE_NAME.length, textures, 0);
        
        for(int i = 0; i < TEXTURE_NAME.length; i++){
            try {
                texture[i] = TextureReader.readTexture(TEXTURE_NAME[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA, // Internal Texel Format,
                    texture[i].getWidth(), texture[i].getHeight(),
                    GL.GL_RGBA, // External format from image,
                    GL.GL_UNSIGNED_BYTE,
                    texture[i].getPixels() // Imagedata
                    );
            } catch( IOException e ) {
              System.out.println(e);
              e.printStackTrace();
            }
        }
        
        //Read high scores from file
        try {
            File file = new File("Scores/Scores.txt");
            Scanner inputScanner = new Scanner(file);
            String next = inputScanner.nextLine();
            String[] s = next.split(" ");
            for(int i=0 ; i<3; i++){
                scores[i] = Integer.parseInt(s[i]);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        //Update the score 
        if(currentLevel.equals("LevelOne") && score>scores[0]){
            scores[0] = score;
        }
        else if(currentLevel.equals("LevelTwo") && score>scores[1]){
            scores[1] = score;
        }
        else if(currentLevel.equals("LevelThree") && score>scores[2]){
            scores[2] = score;
        }
        
        //Write the score on file
        try {
            PrintWriter output = new PrintWriter("Scores/Scores.txt");
            output.print(scores[0] + " " + scores[1] + " " + scores[2]);
            output.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    } 
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);//Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        
        //Draw background and the buttons
        if(currentLevel.equals("LevelThree")){
            DrawBackground(gl, 5);
            move = 0;
            DrawSprite(gl, 45, 25, 6, 2f); 
        }
        else{
            DrawBackground(gl, 0);
            move = move % 2;
            if(move == 1){
                DrawSprite(gl, 60, 25, 4, 2f); 
                DrawSprite(gl, 30, 25, 1, 2f); 
            }
            else{
                DrawSprite(gl, 60, 25, 3, 2f); 
                DrawSprite(gl, 30, 25, 2, 2f);
            }
        }
        
        //Write the score
        if(currentLevel.equals("LevelThree")){
            textRenderer.beginRendering(400, 400);
            textRenderer.draw(String.valueOf(score), 215, 190);
            textRenderer.endRendering();
        }
        else{
            textRenderer.beginRendering(400, 400);
            textRenderer.draw(String.valueOf(score), 230, 240);
            textRenderer.endRendering();
        }
    }

    public void DrawSprite(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);// Turn Blending On

        gl.glPushMatrix();
            gl.glTranslated( x/(Main.MAX_WIDTH/2.0) - 
                    0.9, y/(Main.MAX_HEIGHT/2.0) - 0.9, 0);
            gl.glScaled(0.1*scale, 0.1*scale, 1);
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    public void DrawBackground(GL gl, int index){
        gl.glEnable(GL.GL_BLEND);	
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);// Turn Blending On

        gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }  
    
    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT) {
            move++;
        } 
        else if (e.getKeyCode() == e.VK_LEFT) {
            move++;
        }
        else if((e.getKeyCode() == e.VK_SPACE) || (e.getKeyCode() == e.VK_ENTER)){
            if(move == 0){
                Main.setListener("Winning", "Home");
            }
            else{
                if(currentLevel.equals("LevelOne")){
                    Main.blocks.setImages("LevelTwo");
                    Main.setListener("Winning", "LevelTwo");
                }
                else if(currentLevel.equals("LevelTwo")){
                    Main.blocks.setImages("LevelThree");
                    Main.setListener("Winning", "LevelThree");
                }
            }
            System.out.println(move);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void setCurrentLevel(String currentLevel) {
        Winning.currentLevel = currentLevel;
    }

    public static void setScore(int score) {
        Winning.score = score;
    }
}
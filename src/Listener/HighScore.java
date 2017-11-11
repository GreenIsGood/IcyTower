package Listener;

import Main.Main;
import Model.Pointer;
import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class HighScore implements GLEventListener, KeyListener {
    
    private Pointer pointer = new Pointer();
    private final String TEXTURE_NAME[] = {"Images/high_score_background.png", 
        "Images/on.png", "Images/mute.png", "Images/ok.png"};
    private TextureReader.Texture texture[] = new 
            TextureReader.Texture[TEXTURE_NAME.length];
    private int textures[] = new int[TEXTURE_NAME.length];
    private TextRenderer textRenderer =
            new TextRenderer(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    private int scores[] = new int[3];
    
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        
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
        
    } 
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        
        //Draw the background
        DrawBackground(gl, 0);
        
        //Draw the ok button
        DrawSprite(gl, 46, 15, 3, 1.7f);  
        
        //Write the score
        textRenderer.beginRendering(400, 400);
        textRenderer.draw(String.valueOf(scores[0]), 210, 143);
        textRenderer.draw(String.valueOf(scores[1]), 210, 193);
        textRenderer.draw(String.valueOf(scores[2]), 210, 240);
        textRenderer.endRendering();
        
        //Draw the image of music
        if(Main.isMute()){
           DrawSprite(gl, 87, 87, 2, 1.7f);  
        }
        else{
            DrawSprite(gl, 87, 87, 1, 1.7f); 
        }
    }

    public void DrawSprite(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
            gl.glTranslated( x/(Main.MAX_WIDTH/2.0) - 0.9, y/(Main.MAX_HEIGHT/2.0) - 0.9, 0);
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
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

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
        if(e.getKeyCode() == e.VK_ESCAPE || (e.getKeyCode() == e.VK_SPACE) 
                || (e.getKeyCode() == e.VK_ENTER)){
            //Change lintener
            Main.setListener("HighScore", "Home");
        }
        else if(e.getKeyCode() == e.VK_F4){
            System.exit(0);
        }
        else if(e.getKeyCode() == e.VK_M){
            Main.setMute();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
package Listener;

import Main.Main;
import Texture.TextureReader;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Exit implements GLEventListener, KeyListener {
    
    private final String TEXTURE_NAME[] = {"Images/exit_background.png",
        "Images/no.png", "Images/no_clicked.png",
        "Images/yes.png", "Images/yes_clicked.png"};
    private TextureReader.Texture texture[] = new 
            TextureReader.Texture[TEXTURE_NAME.length];
    private int textures[] = new int[TEXTURE_NAME.length];
    private int move = 1;
  
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);//This Will Clear The Background Color To Black
        
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
    } 
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);//Clear The Screen And The Depth Buffer
        gl.glLoadIdentity(); 
        
        //Draw background
        DrawBackground(gl, 0);
        
        //Draw the image of yes, no and check if it selected or not
        move = move % 2;
        if(move == 0){
            DrawSprite(gl, 60, 35, 4, 2f); 
            DrawSprite(gl, 35, 35, 1, 2f); 
        }
        else{
            DrawSprite(gl, 60, 35, 3, 2f); 
            DrawSprite(gl, 35, 35, 2, 2f);
        }
    }

    public void DrawSprite(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);// Turn Blending On

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
        else if((e.getKeyCode()==e.VK_SPACE) || (e.getKeyCode()==e.VK_ENTER)){
            if(move == 0){
                System.exit(0);
            }
            else{
                //Change of listener
                Main.setListener("Exit", "Home");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
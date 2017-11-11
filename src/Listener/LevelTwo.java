package Listener;

import Main.Main;
import Model.Pair;
import Texture.TextureReader;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class LevelTwo implements GLEventListener, KeyListener {   
    
    private int index = 3, move = 1, count = 0, high = 100;
    private float XResistance = 0.1f,YResistance = 1;
    private final String TEXTURE_NAME[] = {"Images/background.png", 
        "Images/on.png", "Images/mute.png", Main.boy.getImage(0),
        Main.boy.getImage(1), Main.boy.getImage(2), Main.boy.getImage(3),
        Main.boy.getImage(4), Main.boy.getImage(5), Main.blocks.getImage(0),
        Main.blocks.getImage(1), Main.blocks.getImage(2), "Images/play.png", 
        "Images/close.png", "Images/no.png", "Images/no_clicked.png",
        "Images/yes.png", "Images/yes_clicked.png"};
    private TextureReader.Texture texture[] = new 
            TextureReader.Texture[TEXTURE_NAME.length];
    private int textures[] = new int[TEXTURE_NAME.length];
    private GLU glu;
    private double yPosition = -0.2, start, finish, pausing, s, f;
    private Pair[] pairs;
    private int[] yPosPair = new int[100];
    private boolean isClosing = false, isPause = false;
    
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        glu = new GLU();
        glu.gluPerspective(60.0, Main.home.getAspect(), 2.0, 20.0);
        
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
        
        //Change the music
        try {
            Main.changeMusic(Main.isMute());
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        //Random the places of blocks
        pairs = makePos(2, 5);
        pairs[0].setxPos(0);
        pairs[0].setLength(14);
        pairs[99].setxPos(0);
        pairs[99].setLength(14);
        int j = -5;
        for(int i=0 ; i<100 ; i++){
            yPosPair[i] = j;
            j += 15;
        }
        start = System.currentTimeMillis();
    } 
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        glu.gluLookAt(
                0, yPosition, 0,
                0, yPosition, 800,
                0, 1, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        //Check if losing or not if yes change the listener
        if((Main.boy.getYPos()/45)<=yPosition && yPosition!=-0.2){
            finish = System.currentTimeMillis();
            Losing.setScore((int)((Main.boy.getYPos()*10)/((finish-start)/1000)));
            Main.boy.setPos(6.7f, 0);
            Main.boy.setXAcceleration(0.2f);
            try {
                Main.changeMusic(Main.isMute());
            } catch (Exception ex) {
                System.err.println(ex);
            }
            Losing.setCurrentLevel("LevelTwo");
            Main.setListener("LevelTwo", "Losing");
        }
        
        
        //Draw the background
        DrawBackground(gl, 0);
        
        //Draw Blocks
        for(int i=0 ; i<pairs.length ; i++){
            pairs[i].setxFinal(drawBlocks(gl, 7+pairs[i].getxPos(),
                yPosPair[i]-3, pairs[i].getLength()));
        }
                
        //Change image of the boy if he is jumped
        if(Main.boy.isInTheAir()){
            index = 4;
        }
                
        //Draw the boy
        DrawSprite(gl, Main.boy.getXPos(), Main.boy.getYPos(), index, 2);
        
        //KeyPresses
        handleKeyPress();
        
        //Draw Play Button
        if(isPause){
            gl.glPushMatrix();
                gl.glTranslated(0, yPosition, 0);
                gl.glRotated(180, 0, 0, 1);
                DrawSprite(gl, 45,  50, 12, 7);
            gl.glPopMatrix();
        }
        
        //Draw Close Message
        else if(isClosing){
            gl.glPushMatrix();
                gl.glTranslated(0, yPosition, 0);
                DrawSprite(gl, 45, 45, 13, 7);
                gl.glPushMatrix();
                    gl.glRotated(180, 0, 1, 0);
                    move = move % 2;
                    if(move == 0){
                        DrawSprite(gl, 35, 35, 14, 2f); 
                        DrawSprite(gl, 60, 35, 17, 2f); 
                    }
                    else{
                        DrawSprite(gl, 35, 35, 15, 2f); 
                        DrawSprite(gl, 60, 35, 16, 2f); 
                    }
                gl.glPopMatrix();
            gl.glPopMatrix();
        }
        
        //run the game if is not paused or showing closing message
        if(!isPause && !isClosing){
            //Move the camera
            if(high-Main.boy.getYPos()>=1 && high-Main.boy.getYPos()<=40){
                yPosition += 0.50;
                count++;
                high += 30;
            }
            else if(count > 1){
                yPosition += 0.0035;
            }
            
            //Resistance
            resistance();

            //Gravity
            gravity();

            //Movement
            movement();
        }
        
        gl.glLoadIdentity(); 
        
        //Draw the icon of Play or Pause the music
        if(Main.isMute()){
           DrawSprite(gl, 87, 87, 2, 1.7f);  
        }
        else{
            DrawSprite(gl, 87, 87, 1, 1.7f); 
        }
        
        //Check if winnig or not if yes change the listener
        if(Main.boy.getYPos()+Main.boy.getYSpeed()>=1491){
            finish = System.currentTimeMillis();
            Winning.setScore((int)((Main.boy.getYPos()*10)/
                    ((finish-start+pausing)/1000)));
            Main.boy.setPos(6.7f, 0);
            Main.boy.setXAcceleration(0.2f);
            try {
                Main.changeMusic(Main.isMute());
            } catch (Exception ex) {
                System.err.println(ex);
            }
            Winning.setCurrentLevel("LevelTwo");
            Main.setListener("LevelTwo", "Winning");
        }  
    }
    
    public void resistance() {
        if( Main.boy.getXSpeed() > 0 ) {
            Main.boy.setXSpeed( Math.max( 0 ,
                    Main.boy.getXSpeed() - XResistance ) );
        }else if( Main.boy.getXSpeed() < 0 ) {
            Main.boy.setXSpeed( Math.min( 0 ,
                    Main.boy.getXSpeed() + XResistance ) );
        }
    }
    
    public void gravity() {
        int idxCurBlock = (int)Main.boy.getYPos()/15;
        if(!((((int)(Main.boy.getYPos()+Main.boy.getYSpeed())/15 != idxCurBlock) 
                || (Main.boy.getYPos()+Main.boy.getYSpeed() == idxCurBlock*15))
                && Main.boy.getYSpeed()<=0 && pairs[idxCurBlock].getxPos()+3<Main.boy.getXPos() 
                && Main.boy.getXPos()<pairs[idxCurBlock].getxFinal()-1.5)){
            Main.boy.setYSpeed(Main.boy.getYSpeed()-YResistance);
        }
        else {
            Main.boy.setPos(Main.boy.getXPos(), idxCurBlock*15);
            Main.boy.setYSpeed(Math.max(Main.boy.getYSpeed(),0));
            Main.boy.setInTheAir(false);
        }
    }
    
    public void movement() {
        if( Main.boy.getXSpeed() > 0 ) {
            if( Main.boy.getXPos() + Main.boy.getXSpeed() >
                    Main.MAX_WIDTH - 16.07f ) {
                float diff = Main.boy.getXPos() + Main.boy.getXSpeed() - 
                        ( Main.MAX_WIDTH - 16.07f );
                Main.boy.setPos( Main.MAX_WIDTH - 16.07f - diff , Math.max( 0 ,
                        Main.boy.getYPos() + Main.boy.getYSpeed() ) );
                Main.boy.setXSpeed( Main.boy.getXSpeed() * -1 );
            }else {
                Main.boy.setPos( Main.boy.getXPos() + Main.boy.getXSpeed() ,
                        Math.max(0, Main.boy.getYPos() + Main.boy.getYSpeed()));
            }
        }else if( Main.boy.getXSpeed() < 0 ) {
            if( Main.boy.getXPos() + Main.boy.getXSpeed() < 6.7f ) {
                float diff = Math.abs( Main.boy.getXPos() + Main.boy.getXSpeed()
                        - 6.7f);
                Main.boy.setPos( diff + 6.7f , Math.max( 0 , Main.boy.getYPos()
                        + Main.boy.getYSpeed() ) );
                Main.boy.setXSpeed( Main.boy.getXSpeed() * -1 );
            }else {
                Main.boy.setPos( Main.boy.getXPos() + Main.boy.getXSpeed() , 
                        Math.max(0, Main.boy.getYPos() + Main.boy.getYSpeed()));
            }
        }else {
            Main.boy.setPos( Main.boy.getXPos() , Math.max( 0 , 
                    Main.boy.getYPos() + Main.boy.getYSpeed() ) );
        }
    }
    
    public Pair[] makePos(int minLength,int maxLength) {
        Pair[] Block = new Pair[100];
        Random randomGenerator = new Random();
        for(int i = 0;i < 100; i++) {
            Block[i] = new Pair();
            Block[i].setxPos(randomGenerator.nextInt(44));
            Block[i].setLength(Math.min(minLength+randomGenerator.nextInt(
                    maxLength-minLength),100-Block[i].getLength()));
        }
        return Block;
    }

    public void DrawSprite(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);// Turn Blending On

        gl.glPushMatrix();
            gl.glTranslated( x/(Main.MAX_WIDTH/2.0) - 0.9, 
                    y/(Main.MAX_HEIGHT/2.0) - 0.9, 0);
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
                gl.glVertex3f(-1.0f, -1.5f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.5f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 30.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 30.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }  
    
    public int drawBlocks(GL gl, int xx, int yy, int rep)
    {
        DrawSprite(gl, xx, yy, 9, 0.7f);//the start
        xx += 5;
        while(rep>0)
        {
            DrawSprite(gl, xx, yy, 10, 0.7f);//the mid 
            rep--;
            xx += 5;
        }
        if(rep==0)
        {
            DrawSprite(gl, xx, yy, 11, 0.7f);//the end
        }
        return xx+5;
    }
        
    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }
    
    public void handleKeyPress() {
        //Handle key if is not paused or showing closing message
        if(!isPause && !isClosing){
            if ( isKeyPressed( KeyEvent.VK_RIGHT ) ) {
               if( Main.boy.getXSpeed() > 0 ) {
                   Main.boy.setXSpeed( Main.boy.getXSpeed() / 1.1f );
               }
               if( Main.boy.getXPos() > 6.7f) {
                   Main.boy.setXSpeed( Main.boy.getXSpeed() -
                           Main.boy.getXAcceleration() );
               }
               if(!Main.boy.isInTheAir()){
                   if(index == 5){
                       index = 6;
                   }
                   else{
                       index = 5;
                   }
               }
           }
           else if (isKeyPressed(KeyEvent.VK_LEFT)) {
              if(Main.boy.getXSpeed()<0) {
                  Main.boy.setXSpeed(Main.boy.getXSpeed()/2);
              }
              if( Main.boy.getXPos() < Main.MAX_WIDTH - 16.07f ) {
                  Main.boy.setXSpeed(Main.boy.getXSpeed()+
                          Main.boy.getXAcceleration());
              }
              if(!Main.boy.isInTheAir()){
                  if(index == 7){
                      index = 8;
                  }
                  else{
                      index = 7;
                  }
              }
           }
           //Change image of boy if he is do nothing
           else{
               if(!Main.boy.isInTheAir()){
                   index = 3;
               }
           }
           if (isKeyPressed(KeyEvent.VK_SPACE)) {
               if(!Main.boy.isInTheAir()) {
                   Main.boy.setInTheAir(true);
                   Main.boy.setYSpeed(Math.max(7,
                           Math.abs(Main.boy.getXSpeed()*3.5f)));
               }
           }
        }
        //Handle key for closing message
        else if(isClosing){
            if ( isKeyPressed( KeyEvent.VK_RIGHT ) ) {
            move++;
            } 
            else if (isKeyPressed(KeyEvent.VK_LEFT)) {
                move++;
            }
            else if (isKeyPressed(KeyEvent.VK_SPACE) ||
                    isKeyPressed(KeyEvent.VK_ENTER)){
                if(move == 1){
                    isClosing = false;
                }
                else{
                    //Change the music
                    try {
                        Main.changeMusic(Main.isMute());
                    } catch (Exception ex) {
                        System.err.println(ex);
                    }
                    Main.setListener("LevelTwo", "Home");
                }
            }
        }
        if(isKeyPressed(KeyEvent.VK_F4)){
            System.exit(0);
        }
        if(isKeyPressed(KeyEvent.VK_M)){
            Main.setMute();
        }
        //Handle key for pause
        if(isKeyPressed(KeyEvent.VK_P)){
            if(isPause){
                isPause = false;
                f = System.currentTimeMillis();
                pausing = f - s;
            }
            else{
                isPause = true;
                s = System.currentTimeMillis();
            }
            if(!Main.isMute()){
                Main.setMute();
            }
        }
        if(isKeyPressed(KeyEvent.VK_ESCAPE)){
            if(!isClosing){
                isClosing = true;
            }
        }
    }

    public BitSet keyBits = new BitSet(256);
 
    @Override 
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    } 
 
    @Override 
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    } 
 
    @Override 
    public void keyTyped(final KeyEvent event) {
        // don't care 
    } 
 
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
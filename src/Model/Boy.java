package Model;

public class Boy {
    
    private float x, y, xSpeed, ySpeed, xAcceleration;
    private boolean inTheAir;
    private String images[] = new String[6];
    
    public Boy() {
        xAcceleration = 0.2f;
        x = 6.7f;
    }
    
    public void setPos(float x,float y) {
        this.x = x;
        this.y = y;
    }
    
    public float getXPos() {
        return x;
    }
    
    public float getYPos() {
        return y;
    }
    
    public void setXSpeed(float speed) {
        xSpeed = speed;
    }
    
    public float getXSpeed() {
        return xSpeed;
    }
    
    public void setYSpeed(float speed) {
        ySpeed = speed;
    }
    
    public float getYSpeed() {
        return ySpeed;
    }
    
    public void setXAcceleration(float acceleration) {
        xAcceleration = acceleration;
    }
    
    public float getXAcceleration() {
        return xAcceleration;
    }
    
    public boolean isInTheAir() {
        return inTheAir;
    }
    
    public void setInTheAir(boolean inTheAir) {
        this.inTheAir = inTheAir;
    }

    public void setImages(String choosenCharacters) {
        if(choosenCharacters.equals("3d")){
            images[0] = "Images/3d.png";
            images[1] = "Images/3d_jump.png";
            images[2] = "Images/3d_left.png";
            images[3] = "Images/3d_left_walk.png";
            images[4] = "Images/3d_right.png";
            images[5] = "Images/3d_right_walk.png";
        }
        else if(choosenCharacters.equals("yasser")){
            images[0] = "Images/yasser.png";
            images[1] = "Images/yasser_jump.png";
            images[2] = "Images/yasser_left.png";
            images[3] = "Images/yasser_left_walk.png";
            images[4] = "Images/yasser_right.png";
            images[5] = "Images/yasser_right_walk.png";
        }
        else if(choosenCharacters.equals("classic")){
            images[0] = "Images/classic.png";
            images[1] = "Images/classic_jump.png";
            images[2] = "Images/classic_left.png";
            images[3] = "Images/classic_left_walk.png";
            images[4] = "Images/classic_right.png";
            images[5] = "Images/classic_right_walk.png";
        }
        else if(choosenCharacters.equals("cool")){
            images[0] = "Images/cool.png";
            images[1] = "Images/cool_jump.png";
            images[2] = "Images/cool_left.png";
            images[3] = "Images/cool_left_walk.png";
            images[4] = "Images/cool_right.png";
            images[5] = "Images/cool_right_walk.png";
        }
        else if(choosenCharacters.equals("batman")){
            images[0] = "Images/batman.png";
            images[1] = "Images/batman_jump.png";
            images[2] = "Images/batman_left.png";
            images[3] = "Images/batman_left_walk.png";
            images[4] = "Images/batman_right.png";
            images[5] = "Images/batman_right_walk.png";
        }
    }
    
    public String getImage(int index) {
        return images[index];
    }
}
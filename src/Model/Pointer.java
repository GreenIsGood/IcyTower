package Model;

public class Pointer {
    
    private final String TEXTURE_NAME = "Images/pointer.png";
    private int x, move;
    private final int[] Y = {7, 17, 27, 35};

    public Pointer() {
        this.x = 56;
        this.move = 3;
    }

    public String getTextureName() {
        return TEXTURE_NAME;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        move = move % 4;
        return Y[move];
    }
    
    public void move(String to){
        if(move == 0)
            move = 4;
        if (to.equals("Up")) {
            move++;
        } 
        else if (to.equals("Down")) {
            move--;
        }
    }
}

package Model;

public class Blocks {
    
    private String images[] = new String[3];
    
    public void setImages(String choosenLevel) {
        if(choosenLevel.equals("LevelOne")){
            images[0] = "Images/level_one_1.png";
            images[1] = "Images/level_one_2.png";
            images[2] = "Images/level_one_3.png";
        }
        else if(choosenLevel.equals("LevelTwo")){
            images[0] = "Images/level_two_1.png";
            images[1] = "Images/level_two_2.png";
            images[2] = "Images/level_two_3.png";
        }
        else if(choosenLevel.equals("LevelThree")){
            images[0] = "Images/level_three_1.png";
            images[1] = "Images/level_three_2.png";
            images[2] = "Images/level_three_3.png";
        }
    }
    
    public String getImage(int index) {
        return images[index];
    }
    
}

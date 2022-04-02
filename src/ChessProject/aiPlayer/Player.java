package ChessProject.aiPlayer;

import ChessProject.stone.Stone;

public class Player {

    private Stone stone;
    private boolean isMoving;

    public Player()
    {
        stone = new Stone();
        stone.setStoneColor(Stone.StoneColor.NONE);
        this.isMoving = false;
    }

    public Stone getStone()
    {
        return this.stone;
    }

    public void setStone(Stone.StoneColor stoneColor){
        this.stone.setStoneColor(stoneColor);
    }

    public boolean getIsMoving()
    {
        return this.isMoving;
    }

    public void setIsMoving(boolean isMoving)
    {
        this.isMoving = isMoving;
    }

}
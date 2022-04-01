package ChessProject.stone;

public class Stone {

    public enum StoneColor
    {
        BLACK,WHITE,NONE
    }

    private StoneColor stoneColor;

    public Stone()
    {
        this.stoneColor = StoneColor.NONE;
    }

    public void setStoneColor(StoneColor stoneColor)
    {
        this.stoneColor = stoneColor;
    }

    public StoneColor getStoneColor()
    {
        return  this.stoneColor;
    }
}
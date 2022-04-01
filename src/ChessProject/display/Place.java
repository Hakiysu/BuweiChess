package ChessProject.display;



import ChessProject.aiPlayer.Player;
import ChessProject.stone.Stone;

import java.awt.*;

public class Place extends Panel {

    ChessPad chessPad;

    public Place(ChessPad chessPad)
    {
        setSize(20,20);
        this.chessPad = chessPad;
    }

    // 落子
    public static void placeStone(Player player, int x, int y, Graphics graphics)
    {
        if(player.getStone().getStoneColor() == Stone.StoneColor.BLACK)
        {
            graphics.setColor(Color.BLACK);
            graphics.fillOval(x,y,20,20);
        }
        if(player.getStone().getStoneColor() == Stone.StoneColor.WHITE)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillOval(x,y,20,20);
        }
    }

    // 提子
    public static void takeStone(int x,int y,Graphics graphics)
    {
        graphics.clearRect(x,y,20,20);
    }

}

package BuweiChessProject.display;

import BuweiChessProject.player.Player;
import BuweiChessProject.chess.Chess;

import java.awt.*;

public class Place extends Panel {

    ChessPad chessPad;

    public Place(ChessPad chessPad)
    {
        setSize(40,40);//20x20
        this.chessPad = chessPad;
    }

    // 落子
    public static void placeStone(Player player, int x, int y, Graphics graphics)
    {

        if(player.getStone().getChessColor() == Chess.ChessColor.BLACK)
        {
            graphics.setColor(Color.BLACK);
            graphics.fillOval(x,y,40,40);//20x20
        }
        if(player.getStone().getChessColor() == Chess.ChessColor.WHITE)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillOval(x,y,40,40);//20x20
        }
    }

    // 提子
    public static void takeStone(int x,int y,Graphics graphics)
    {
        graphics.clearRect(x,y,40,40);//20x20
    }

}

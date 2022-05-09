package BuweiChessProject.player;

import BuweiChessProject.chess.Chess;

public class Player {

    private Chess stone;
    private boolean isMoving;

    public Player()
    {
        stone = new Chess();
        stone.setChessColor(Chess.ChessColor.NONE);
        this.isMoving = false;
    }

    public Chess getStone()
    {
        return this.stone;
    }

    public void setStone(Chess.ChessColor chessColor){
        this.stone.setChessColor(chessColor);
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
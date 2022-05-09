package BuweiChessProject.chess;

public class Chess {

    public enum ChessColor
    {
        BLACK,WHITE,NONE
    }

    private ChessColor chessColor;

    public Chess()
    {
        this.chessColor = ChessColor.NONE;
    }

    public void setChessColor(ChessColor chessColor)
    {
        this.chessColor = chessColor;
    }

    public ChessColor getChessColor()
    {
        return  this.chessColor;
    }
}
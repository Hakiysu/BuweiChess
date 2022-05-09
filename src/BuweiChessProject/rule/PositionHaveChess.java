package BuweiChessProject.rule;

import BuweiChessProject.chess.Chess;

public class PositionHaveChess {
    public static boolean ifAlreadyHadStone(Chess.ChessColor move[][], int coordinate_x, int coordinate_y)
    {

        if(move[coordinate_x][coordinate_y] == Chess.ChessColor.NONE)
        {
            return false;
        }
        return true;
    }
}

package ChessProject.rule;

import ChessProject.stone.Stone;

public class StoneAtHere {
    public static boolean ifAlreadyHadStone(Stone.StoneColor move[][], int coordinate_x, int coordinate_y)
    {

        if(move[coordinate_x][coordinate_y] == Stone.StoneColor.NONE)
        {
            return false;
        }
        return true;
    }
}

package ChessProject.rule;

public class PosInBoard {
    public static boolean ifInBoard(int coordinate_x, int coordinate_y)
    {
        if((coordinate_x>=0 && coordinate_x<=18) && (coordinate_y>=0 && coordinate_y<=18))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

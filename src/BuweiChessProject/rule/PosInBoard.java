package BuweiChessProject.rule;

public class PosInBoard {
    public static boolean ifInBoard(int coordinate_x, int coordinate_y)
    {
        return (coordinate_x >= 0 && coordinate_x <= 8) && (coordinate_y >= 0 && coordinate_y <= 8);
    }
}

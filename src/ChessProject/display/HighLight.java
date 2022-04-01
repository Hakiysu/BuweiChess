package ChessProject.display;


import ChessProject.stone.Stone;

import java.awt.*;

/**
 * 高亮最后一手
 */
public class HighLight{

    private static BasicStroke strokeLine = new BasicStroke(1.5f);

    // 给最后一手棋子加一圈红色边框
    public static void highLightLastStone(int coordinate_x, int coordinate_y,
                                          int last_coordinate_x, int last_coordinate_y,
                                          Stone.StoneColor move[][], int teNum, Graphics graphics)
    {
        graphics.setColor(Color.RED);
        int draw_x = (coordinate_x+1)*50 + 20;
        int draw_y = (coordinate_y+1)*50 + 20;
        //
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(strokeLine);
        g.drawOval(draw_x,draw_y,40,40);
        // 如果手数大于1，把倒数第二手的红色边框去除
        if(teNum > 1)
        {
            removeLastButOneLight(last_coordinate_x,last_coordinate_y,move,g);
        }
    }

    // 直接偷懒，用棋盘底色在原来的那一圈上面再画一圈
    public static void removeLastButOneLight(int last_coordinate_x, int last_coordinate_y, Stone.StoneColor move[][], Graphics g)
    {
        int draw_x = (last_coordinate_x + 1) * 50 + 20;
        int draw_y = (last_coordinate_y + 1) * 50 + 20;
        if (move[last_coordinate_x][last_coordinate_y] == Stone.StoneColor.BLACK) {
            g.setColor(Color.BLACK);
        }
        if (move[last_coordinate_x][last_coordinate_y] == Stone.StoneColor.WHITE) {
            g.setColor(Color.WHITE);
        }
        g.drawOval(draw_x, draw_y, 40, 40);
        g.setColor(Color.ORANGE);
        g.drawOval(draw_x,draw_y,40,40);
    }

}

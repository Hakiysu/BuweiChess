package ChessProject.display;

import ChessProject.stone.Stone;

import java.awt.*;

public class TeNum extends Panel {

    public static void drawTeNum(int place_x, int place_y, int teNum, Stone.StoneColor color, Graphics graphics)
    {
        if(color == Stone.StoneColor.BLACK)
        {
            graphics.setColor(Color.WHITE);
        }
        if(color == Stone.StoneColor.WHITE)
        {
            graphics.setColor(Color.BLACK);
        }
        Font font = graphics.getFont();
        FontMetrics metrics = graphics.getFontMetrics(font);
        // Determine the X coordinate for the text
        int teNum_x = place_x + (40 - metrics.stringWidth(String.valueOf(teNum))) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int teNum_y = place_y + ((40 - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        graphics.setFont(font);
        // Draw the String
        graphics.drawString(String.valueOf(teNum),teNum_x,teNum_y);
    }

}

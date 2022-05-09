package BuweiChessProject.display;

import BuweiChessProject.chess.Chess;

import java.awt.*;

public class TeNum extends Panel {

    public static void drawTeNum(int place_x, int place_y, int teNum, Chess.ChessColor color, Graphics graphics)
    {
        if(color == Chess.ChessColor.BLACK)
        {
            graphics.setColor(Color.WHITE);
        }
        if(color == Chess.ChessColor.WHITE)
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

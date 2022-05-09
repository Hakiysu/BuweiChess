package BuweiChessProject.rule;

import BuweiChessProject.chess.Chess;
import javax.swing.*;

public class Take {
    // 声明上下左右四个方向
    private static int[][] directions = {{0,1},{1,0},{-1,0},{0,-1}};
    // 记录上下左右四颗子的hasLiberty返回的长度
    private static int[][] take_length=new int[4][1];
    // 记录上下左右四颗子的hasLiberty返回的提子数组，这里感觉提的子不会很多，因此长度只有9
    // 4表示4个方向
    // 9表示第N个要提的子
    // 最后两位表示第N个要提的子的x、y坐标
    private static int[][][] take_takeStones=new int[4][9][2];

    // 初始化length
    private static void setUpLength()
    {
        for(int i=0;i<4;i++)
        {
            take_length[i][0] = 0;
        }
    }

    // 初始化takeStones
    private static void setUpTakeStones()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                for (int k = 0; k < 2; k++)
                {
                    take_takeStones[i][j][k] = 0;
                }
            }
        }
    }

    // 提子函数
    public static boolean takeStones(Chess.ChessColor move[][], int coordinate_x, int coordinate_y, int who) {
        // flag为1则有子可提
        int flag = 0;
        // 初始化记录数组
        setUpLength();
        setUpTakeStones();
        int direction_x,direction_y;
        // 获得当前局面最后一手棋的颜色
        Chess.ChessColor color = move[coordinate_x][coordinate_y];
        // 判断该手棋上下左右四个方向的相领棋子
        for(int i=0;i<4;i++)
        {
            direction_x = coordinate_x + directions[i][0];
            direction_y = coordinate_y + directions[i][1];
            // 如果不在棋盘内，继续下一个循环
            if(!(PosInBoard.ifInBoard(direction_x,direction_y)))
            {
                continue;
            }
            // 如果该方向上的有棋
            // 且棋子颜色与当前局面最后一手棋颜色不同
            else if(move[direction_x][direction_y] != color && move[direction_x][direction_y] != Chess.ChessColor.NONE)
            {
                // 如果该棋子所在的块有气，继续下一个循环
                if(ChessAir.hasLiberty(move,direction_x,direction_y))
                {
                    continue;
                }
                // 如果没气，flag为1
                else
                {
                    flag = 1;
                    if(who==0)
                        JOptionPane.showMessageDialog(null,"AI本次落子后吃掉了您的棋子，您赢了！");
                    else
                        JOptionPane.showMessageDialog(null,"用户本次落子后吃掉了AI的棋子，您输了！");
                    System.exit(0);

                }
            }
        }
        // flag不为0，可提子，返回true
        if(flag!=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static int[][][] getTakeStones()
    {
        return take_takeStones;
    }

    public static int[][] getLength()
    {
        return take_length;
    }
}

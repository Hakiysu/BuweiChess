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
    private static int[][][] take_takeStones=new int[4][256][2];

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


        //对弈禁止自杀，落子自杀一方判负
        //搁着玩自杀？？？
        System.out.println("NAV check start");
        int noAirValue=4;
        //center area,1.1~7.7
        if(coordinate_x<8&&coordinate_x>0&&coordinate_y<8&&coordinate_y>0)
        {
            System.out.println("CENTER！");
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //edge up
        //y=0,0<x<8
        if(coordinate_x<8&&coordinate_x>0&&coordinate_y==0)
        {
            System.out.println("u！");
            noAirValue=3;
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //edge down
        //y=8,0<x<8
        if(coordinate_x<8&&coordinate_x>0&&coordinate_y==8)
        {
            System.out.println("d！");
            noAirValue=3;
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //edge left
        //x=0,0<y<8
        if(coordinate_x==0&&coordinate_y>0&&coordinate_y<8)
        {
            System.out.println("l！");
            noAirValue=3;
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //edge right
        //x=8,0<y<8
        if(coordinate_x==8&&coordinate_y>0&&coordinate_y<8)
        {
            System.out.println("r！");
            noAirValue=3;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //at 00
        if(coordinate_x==0&&coordinate_y==0)
        {System.out.println("00！");
            noAirValue=2;
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //at 08
        if(coordinate_x==0&&coordinate_y==8)
        {System.out.println("08！");
            noAirValue=2;
            if(color!=move[coordinate_x+1][coordinate_y]&&move[coordinate_x+1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //at 80
        if(coordinate_x==8&&coordinate_y==0)
        {System.out.println("80！");
            noAirValue=2;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y+1]&&move[coordinate_x][coordinate_y+1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        //at 88
        if(coordinate_x==8&&coordinate_y==8)
        {System.out.println("88！");
            noAirValue=2;
            if(color!=move[coordinate_x-1][coordinate_y]&&move[coordinate_x-1][coordinate_y]!= Chess.ChessColor.NONE)noAirValue--;
            if(color!=move[coordinate_x][coordinate_y-1]&&move[coordinate_x][coordinate_y-1]!= Chess.ChessColor.NONE)noAirValue--;
        }
        if(noAirValue==0)flag=1;
        System.out.println("NAV check done,nva="+noAirValue);
        if(flag==1)
        {
            if(who==0)
                JOptionPane.showMessageDialog(null,"AI本次落子后吃掉了您的棋子，您赢了！");
            else
                JOptionPane.showMessageDialog(null,"用户本次落子后吃掉了AI的棋子，您输了！");
            System.exit(0);
        }
        // 判断该手棋上下左右四个方向的相领棋子
        if(flag==0)
        {
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
                    //System.out.println("different-"+i);
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

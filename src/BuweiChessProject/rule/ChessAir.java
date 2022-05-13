package BuweiChessProject.rule;

import BuweiChessProject.chess.Chess;

public class ChessAir {
    // 声明记录数组
    private static int[][] visited = new int[256][256];
    // 声明上下左右四个方向
    private static int[][] directions = {{0,1},{1,0},{-1,0},{0,-1}};
    // 声明记录提子的坐标的二维数组
    private static int[][] liberty_takeStones = new int[256][2];
    // 声明记录二维数组的长度
    private static int liberty_length;

    // 记录数组初始化函数
    private static void setUpVisited()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                visited[i][j] = 0;
            }
        }
    }

    private static void setUpTakeStones()
    {
        for (int i = 0; i < 9 ; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                liberty_takeStones[i][j] = 0;
            }
        }
    }

    private static boolean DFS(Chess.ChessColor[][] move, int coordinate_x, int coordinate_y)
    {
        int direction_x,direction_y;
        // 设置已访问标志1
        visited[coordinate_x][coordinate_y] = 1;
        // 将当前子的坐标存入提子数组，数组长度+1
        liberty_takeStones[liberty_length][0] = coordinate_x;
        liberty_takeStones[liberty_length][1] = coordinate_y;
        liberty_length++;
        // 遍历上下左右四个方向
        for(int i = 0;i < 4;i++)
        {
            direction_x = coordinate_x + directions[i][0];
            direction_y = coordinate_y + directions[i][1];
            // 判断是否在棋盘内
            if(!(PosInBoard.ifInBoard(direction_x,direction_y)))
            {
                // 不在棋盘内就遍历下一个点
                continue;
            }
            // 如果在棋盘内，且没访问过
            else if(visited[direction_x][direction_y] == 0)
            {
                // 如果该位置无子，则有气，返回true
                if(move[direction_x][direction_y] == Chess.ChessColor.NONE)
                {
                    // 这些输出是在debug的时候用的，可以删掉
                    System.out.println("有气： "+direction_x+" "+direction_y);
                    return true;
                }
                // 如果该位置有子，且子的颜色不同，就遍历下一个点
                if(move[direction_x][direction_y] != move[coordinate_x][coordinate_y])
                {
                    System.out.println("不同色： "+direction_x+" "+direction_y);
                    continue;
                }
                // 如果该位置有子，且颜色相同，递归遍历该子
                if(move[direction_x][direction_y] == move[coordinate_x][coordinate_y])
                {
                    System.out.println("同色： "+direction_x+" "+direction_y);
                    //如果下一个子返回true
                    if(DFS(move,direction_x,direction_y))
                    {
                        return true;
                    }
                }
            }
        }
        // 如果遍历完都没气，返回false
        return false;
    }

    // 判断是否有气函数
    public static boolean hasLiberty(Chess.ChessColor[][] move, int coordinate_x, int coordinate_y)
    {
        // 初始化遍历记录访问数组
        setUpVisited();
        setUpTakeStones();
        // 重置记录长度
        liberty_length = 0;
        System.out.println("hasLiberty开始: "+coordinate_x+" "+coordinate_y);
        if(DFS(move,coordinate_x,coordinate_y))
        {
            System.out.println("hasLiberty结束，返回true");
            return true;
        }
        else
        {
            System.out.println("hasLiberty结束，返回false");
            return false;
        }
    }

    public static int[][] getTakeStones()
    {
        return liberty_takeStones;
    }

    public static int getLength()
    {
        return liberty_length;
    }
}

package BuweiChessProject.display;

import BuweiChessProject.rule.Take;
import BuweiChessProject.chess.Chess;
import BuweiChessProject.player.Player;
import BuweiChessProject.rule.PositionHaveChess;;
import BuweiChessProject.rule.PosInBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ChessPad extends Panel implements MouseListener, ActionListener {

    /**
     * 声明Player类存储棋手下棋顺序
     * 声明落子绘图类用于绘制棋子
     * 声明teNum类用于绘制手数
     * 声明highLight高亮最后一手
     * 声明9*9 move数组，存储已落子的信息，road=路宽度，9
     * 声明teNum记录手数
     * 声明move_teNum 记录每一个坐标的棋子是第几手棋
     * 声明上一手的坐标last_coordinate_x，last_coordinate_y
     */
    Player BLACK_PLAYER;
    Player WHITE_PLAYER;
    Place BLACK_STONE;
    Place WHITE_STONE;
    TeNum class_teNum;
    HighLight highLight;
    Chess.ChessColor chessmap[][];
    chessStatus cs = new chessStatus();
    Random rdm = new Random();
    int teNum;
    int move_teNum[][][];
    int last_coordinate_x, last_coordinate_y;
    int whoFirst;
    int whoPlay;
    int value[][] = new int[9][9];//坐标权值
    int dx[] = {-1, 0, 1, 0};//行位移
    int dy[] = {0, -1, 0, 1};//列位移
    boolean visited_by_air_judge[][] = new boolean[9][9];

    int chessR = 20;
    int roadWidth = 50;//Normal:25
    int road = 9;
    boolean flagRun = true;

    /**
     * 构造棋盘大小、背景、鼠标监听器
     */
    ChessPad(int n) {

        // 初始化执黑棋手
        BLACK_PLAYER = new Player();
        BLACK_PLAYER.setIsMoving(true);
        BLACK_PLAYER.setStone(Chess.ChessColor.BLACK);
        // 初始化执白棋手
        WHITE_PLAYER = new Player();
        WHITE_PLAYER.setIsMoving(false);
        WHITE_PLAYER.setStone(Chess.ChessColor.WHITE);
        // 初始化手数类
        class_teNum = new TeNum();
        // 初始化高亮最后一手类
        highLight = new HighLight();
        // 初始化黑棋、白棋
        BLACK_STONE = new Place(this);
        WHITE_STONE = new Place(this);
        // 初始化棋谱数组、手数数组
        chessmap = new Chess.ChessColor[road][road];
        move_teNum = new int[road][road][1];
        for (int i = 0; i < road; i++)//init empty chesspad,full NONE
        {
            for (int j = 0; j < road; j++) {
                chessmap[i][j] = Chess.ChessColor.NONE;
                move_teNum[i][j][0] = -1;
                value[i][j] = 0;
                visited_by_air_judge[i][j] = false;
            }
        }
        //初始化手数、最后一手的坐标
        teNum = 1;
        last_coordinate_x = 0;
        last_coordinate_y = 0;

        this.add(BLACK_STONE);
        this.add(WHITE_STONE);
        this.add(class_teNum);
        this.setSize(600, 600);
        this.setLayout(null);
        this.setBackground(Color.ORANGE);
        this.addMouseListener(this);
        //n=0,pc first
        //n=1,user first
        //ALWAYS black is first
        whoFirst = n;
        whoPlay = n;

    }
    class chessStatus {
        JFrame frame = new JFrame("棋盘状态侦测");
        JPanel panel = new JPanel();
        JTextArea status = new JTextArea();
        JScrollPane jsp = new JScrollPane(status);

        {
            frame.setSize(360, 512);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.out.println("ACTIVE");
            panel.setLayout(null);
            status.setLineWrap(true);
            jsp.setBounds(20, 20, 300, 430);
            panel.add(jsp);
            frame.add(panel);
            frame.setVisible(true);

        }//init once
        void updateMsg(String msg)
        {
            status.setText(status.getText() + msg);
        }
    }

    /**
     * 画棋盘的线和点
     *
     * @param g
     */
    public void paint(Graphics g) {

        System.out.println("Painting chess pad");
        for (int i = 90; i <= roadWidth * 8 + 90; i += roadWidth) {
            g.drawLine(i, 90, i, roadWidth * 8 + 90);
        }
        for (int i = 90; i <= roadWidth * 8 + 90; i += roadWidth) {
            g.drawLine(90, i, roadWidth * 8 + 90, i);
        }
        int place_x = (4 + 1) * roadWidth + chessR;
        int place_y = (4 + 1) * roadWidth + chessR;
        if(whoFirst==0)//fix chess missing bug,not final version
        {
            Place.placeStone(this.BLACK_PLAYER, place_x, place_y, this.getGraphics());
            class_teNum.drawTeNum(place_x, place_y, 1, this.BLACK_PLAYER.getStone().getChessColor(), this.getGraphics());
            highLight.highLightLastStone(4, 4, 0, 0, chessmap, 0, this.getGraphics());
        }
    }

    public int opponent_color(int color) {
        if (color == 1)
            return -1;
        else
            return 1;
    }

    //判断点(x,y)是否在棋盘内
    public boolean inBoard_judge(int x, int y) {
        return 0 <= x && x < 9 && 0 <= y && y < 9;
    }

    //判断是否有气
    public boolean air_judge(int x, int y) {
        visited_by_air_judge[x][y] = true; //标记，表示这个位置已经搜过有无气了
        boolean flag = false;
        for (int dir = 0; dir < 4; dir++) {
            int x_dx = x + dx[dir], y_dy = y + dy[dir];
            if (inBoard_judge(x_dx, y_dy)) //界内
            {
                if (chessmap[x_dx][y_dy] == Chess.ChessColor.NONE) //旁边这个位置没有棋子
                    flag = true;
                if (chessmap[x_dx][y_dy] == chessmap[x][y] && !visited_by_air_judge[x_dx][y_dy]) //旁边这个位置是没被搜索过的同色棋
                    if (air_judge(x_dx, y_dy))
                        flag = true;
            }
        }
        return flag;
    }

    //判断能否下颜色为color的棋
    public boolean put_available(int x, int y, int color) {
        if (chessmap[x][y] != Chess.ChessColor.NONE) //如果这个点本来就有棋子
            return false;
        if (color == 1) {
            chessmap[x][y] = Chess.ChessColor.BLACK;
            for (int i = 0; i < road; i++)//reset value array
            {
                for (int j = 0; j < road; j++) {
                    value[i][j] = 0;
                }
            }
        } else {
            chessmap[x][y] = Chess.ChessColor.WHITE;
            for (int i = 0; i < road; i++)//reset value array
            {
                for (int j = 0; j < road; j++) {
                    value[i][j] = 0;
                }
            }
        }
        if (!air_judge(x, y)) //如果下完这步这个点没气了,说明是自杀步，不能下
        {
            chessmap[x][y] = Chess.ChessColor.NONE;
            return false;
        }
        for (int i = 0; i < 4; i++) //判断下完这步周围位置的棋子是否有气
        {
            int x_dx = x + dx[i], y_dy = y + dy[i];
            if (inBoard_judge(x_dx, y_dy)) //在棋盘内
            {
                //对于有棋子的位置（标记访问过避免死循环）
                if (chessmap[x_dx][y_dy] != Chess.ChessColor.NONE) {
                    if (!visited_by_air_judge[x_dx][y_dy]) {
                        if (!air_judge(x_dx, y_dy))//如果导致(x_dx,y_dy)没气了，则不能下
                        {
                            chessmap[x][y] = Chess.ChessColor.NONE; //回溯
                            return false;
                        }
                    }
                }
            }
        }
        chessmap[x][y] = Chess.ChessColor.NONE; //回溯
        return true;
    }

    //估值函数，对当前局面进行评估，计算颜色为color的一方比另一方可落子的位置数目多多少（权利值比较）
    public int evaluate(int color) {
        int right = 0;
        int op_color = opponent_color(color);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (put_available(x, y, color))
                    right++;
                if (put_available(x, y, op_color))
                    right--;
            }
        }
        return right;
    }

    public void greedyClicked() {
        int coordinate_x = 0;
        int coordinate_y = 0;
        int color;
        if (whoFirst == 0)
            color = 1;
        else
            color = -1;
        if (whoPlay == 0) {
            int max_value = Integer.MIN_VALUE;
            int best_i[] = new int[81];
            int best_j[] = new int[81];
            int best_num = 0;
            for (int i = 0; i < road; i++)//reset value array
            {
                for (int j = 0; j < road; j++) {
                    value[i][j] = 0;
                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (put_available(i, j, color)) {
                        if (color == 1) {
                            chessmap[i][j] = Chess.ChessColor.BLACK;
                            value[i][j] = evaluate(color);
                            if (value[i][j] > max_value)
                                max_value = value[i][j];
                            chessmap[i][j] = Chess.ChessColor.NONE;
                        }
                        else {
                            chessmap[i][j] = Chess.ChessColor.WHITE;
                            value[i][j] = evaluate(color);
                            if (value[i][j] > max_value)
                                max_value = value[i][j];
                            chessmap[i][j] = Chess.ChessColor.NONE;
                        }
                    }
                    else
                        value[i][j] = Integer.MIN_VALUE;
                }
            }
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    if (value[i][j] == max_value) {
                        best_i[best_num] = i;
                        best_j[best_num] = j;
                        best_num++;
                    }
            int randomNum = rdm.nextInt(best_num);//在所有最大value里面随机选
            coordinate_x = best_i[randomNum];
            coordinate_y = best_j[randomNum];
            //get click position & use virtual clicked

            // 得到的是落子类绘图方法需要的坐标
            int place_x = (coordinate_x + 1) * roadWidth + chessR;
            int place_y = (coordinate_y + 1) * roadWidth + chessR;
            // 判断点击位置是否在棋盘内
            if (!PositionHaveChess.ifAlreadyHadStone(chessmap, coordinate_x, coordinate_y)) {
                // 黑棋
                if (this.BLACK_PLAYER.getIsMoving()) {
                    // 落子、绘图
                    Place.placeStone(this.BLACK_PLAYER, place_x, place_y, this.getGraphics());
                    // 绘制手数
                    class_teNum.drawTeNum(place_x, place_y, teNum, this.BLACK_PLAYER.getStone().getChessColor(), this.getGraphics());
                    // 设置有子
                    chessmap[coordinate_x][coordinate_y] = this.BLACK_PLAYER.getStone().getChessColor();
                    cs.updateMsg("AI黑子下棋位置：" + coordinate_x + "," + coordinate_y + "\n");
                }

                // 白棋
                if (this.WHITE_PLAYER.getIsMoving()) {
                    // 落子、绘图
                    Place.placeStone(this.WHITE_PLAYER, place_x, place_y, this.getGraphics());
                    // 绘制手数
                    class_teNum.drawTeNum(place_x, place_y, teNum, this.WHITE_PLAYER.getStone().getChessColor(), this.getGraphics());
                    // 设置有子
                    chessmap[coordinate_x][coordinate_y] = this.WHITE_PLAYER.getStone().getChessColor();
                    cs.updateMsg("AI白子下棋位置：" + coordinate_x + "," + coordinate_y + "\n");
                }

                // 手数加1
                move_teNum[coordinate_x][coordinate_y][0] = teNum;
                teNum++;

                // 如果可以提子
                if (Take.takeStones(chessmap, coordinate_x, coordinate_y,0)) {
                    takeStones(this.getGraphics());
                    System.out.println("AI提子");
                    cs.updateMsg("");
                } else {
                    System.out.println("AI落子");
                }

                // 高亮最后一手，并将倒数第二手的高亮去除
                highLight.highLightLastStone(coordinate_x, coordinate_y, last_coordinate_x, last_coordinate_y, chessmap, teNum - 1, this.getGraphics());
                last_coordinate_x = coordinate_x;
                last_coordinate_y = coordinate_y;

                // 两级反转.表明包
                BLACK_PLAYER.setIsMoving(!(BLACK_PLAYER.getIsMoving()));
                WHITE_PLAYER.setIsMoving(!(WHITE_PLAYER.getIsMoving()));
                whoPlay = 1;
            }
            else {
                cs.updateMsg("AI处已有子，无法下子！！！\n");
                greedyClicked();//小小的递归一下，本次查询无结果则再来一次
            }
        }
    }

    // 提子
    public void takeStones(Graphics graphics) {
        int coordinate_x, coordinate_y, remove_x, remove_y;
        // 获得提子数量
        int length[][] = Take.getLength();
        // 获得提子坐标
        int takeStones[][][] = Take.getTakeStones();
        for (int i = 0; i < 4; i++) {
            // 如果记录的数量不为0，有子可提
            if (length[i][0] != 0) {
                for (int j = 0; j < length[i][0]; j++) {
                    // 获得要提的子的坐标
                    coordinate_x = takeStones[i][j][0];
                    coordinate_y = takeStones[i][j][1];
                    // 将坐标转换为绘图坐标
                    remove_x = (coordinate_x + 1) * roadWidth + chessR;
                    remove_y = (coordinate_y + 1) * roadWidth + chessR;
                    // 去除棋谱上该子
                    chessmap[coordinate_x][coordinate_y] = Chess.ChessColor.NONE;
                    // 提子
                    Place.takeStone(remove_x, remove_y, graphics);
                }
            }
        }
        // 重绘
        removeAll();
        paint(graphics);
        // 重绘仍在棋盘上的棋子
        for (int i = 0; i < road; i++) {
            for (int j = 0; j < road; j++) {
                if (chessmap[i][j] == Chess.ChessColor.BLACK) {
                    Place.placeStone(this.BLACK_PLAYER, ((i + 1) * roadWidth + chessR), ((j + 1) * roadWidth + chessR), this.getGraphics());
                    class_teNum.drawTeNum(((i + 1) * roadWidth + chessR), ((j + 1) * roadWidth + chessR), move_teNum[i][j][0], chessmap[i][j], this.getGraphics());
                }
                if (chessmap[i][j] == Chess.ChessColor.WHITE) {
                    Place.placeStone(this.WHITE_PLAYER, ((i + 1) * roadWidth + chessR), ((j + 1) * roadWidth + chessR), this.getGraphics());
                    class_teNum.drawTeNum(((i + 1) * roadWidth + chessR), ((j + 1) * roadWidth + chessR), move_teNum[i][j][0], chessmap[i][j], this.getGraphics());
                }
                if (move_teNum[i][j][0] == teNum) {
                    highLight.highLightLastStone(i, j, 0, 0, chessmap, 0, this.getGraphics());
                }
            }
        }
    }

    public void pcFirstInit()
    {
        System.out.println("Init now,set at 4,4");
        if(whoFirst==0)
        {
            int coordinate_x = 4;
            int coordinate_y = 4;
            //get click position & use virtual clicked

            // 得到的是落子类绘图方法需要的坐标
            int place_x = (coordinate_x + 1) * roadWidth + chessR;
            int place_y = (coordinate_y + 1) * roadWidth + chessR;
            // 判断是否在棋盘内
            if (!PositionHaveChess.ifAlreadyHadStone(chessmap, coordinate_x, coordinate_y)) {
                // 黑棋
                if (this.BLACK_PLAYER.getIsMoving()) {
                    // 设置有子
                    chessmap[coordinate_x][coordinate_y] = this.BLACK_PLAYER.getStone().getChessColor();
                    cs.updateMsg("AI黑子下棋位置：" + coordinate_x + "," + coordinate_y + "\n");
                }

                // 手数加1
                move_teNum[4][4][0] = teNum;
                teNum++;

                // 如果可以提子
                if (Take.takeStones(chessmap, 4, 4,0)) {
                    takeStones(this.getGraphics());
                    System.out.println("AI提子");
                    cs.updateMsg("");
                } else {
                    System.out.println("AI落子");
                }

                // 高亮最后一手，并将倒数第二手的高亮去除
                highLight.highLightLastStone(coordinate_x, coordinate_y, 0, 0, chessmap, 0, this.getGraphics());
                last_coordinate_x = coordinate_x;
                last_coordinate_y = coordinate_y;
                // 落子、绘图
                Place.placeStone(this.BLACK_PLAYER, place_x, place_y, this.getGraphics());
                // 绘制手数

                class_teNum.drawTeNum(place_x, place_y, 1, this.BLACK_PLAYER.getStone().getChessColor(), this.getGraphics());
                // 两级反转.表明包
                BLACK_PLAYER.setIsMoving(!(BLACK_PLAYER.getIsMoving()));
                WHITE_PLAYER.setIsMoving(!(WHITE_PLAYER.getIsMoving()));
                whoPlay = 1;
                System.out.println("FINISH");
            }
        }
    }
    /**
     * 按下鼠标，调用落子类绘图方法
     *
     * @param mouseEvent
     */

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(whoPlay==1)
        {
            if ((mouseEvent.getModifiers() == InputEvent.BUTTON1_MASK)) {
                // 这里减数是棋子的宽度、高度的一半 -- 10
                int x = (int) mouseEvent.getX() - chessR;
                int y = (int) mouseEvent.getY() - chessR;
                // 求余数和除数是棋盘每路之间的宽度 -- roadWidth
                // 得到的是棋盘坐标
                int coordinate_x = (x - (x % roadWidth)) / roadWidth - 1;
                int coordinate_y = (y - (y % roadWidth)) / roadWidth - 1;
                // 这里用棋盘坐标乘以棋盘每路之间的宽度 -- roadWidth
                // 再加上棋子的宽度、高度的一半 -- 10
                // 得到的是落子类绘图方法需要的坐标
                int place_x = (coordinate_x + 1) * roadWidth + chessR;
                int place_y = (coordinate_y + 1) * roadWidth + chessR;
                // 判断是否在棋盘内
                if (PosInBoard.ifInBoard(coordinate_x, coordinate_y)) {
                    if (!PositionHaveChess.ifAlreadyHadStone(chessmap, coordinate_x, coordinate_y)) {
                        //能进这里，则已经可以成功下子
                        // 黑棋
                        if (this.BLACK_PLAYER.getIsMoving()) {
                            // 落子、绘图
                            Place.placeStone(this.BLACK_PLAYER, place_x, place_y, this.getGraphics());
                            // 绘制手数
                            class_teNum.drawTeNum(place_x, place_y, teNum, this.BLACK_PLAYER.getStone().getChessColor(), this.getGraphics());
                            // 设置有子
                            chessmap[coordinate_x][coordinate_y] = this.BLACK_PLAYER.getStone().getChessColor();
                            cs.updateMsg("USER黑子下棋位置：" + coordinate_x + "," + coordinate_y + "\n");
                        }
                        // 白棋
                        if (this.WHITE_PLAYER.getIsMoving()) {
                            // 落子、绘图
                            Place.placeStone(this.WHITE_PLAYER, place_x, place_y, this.getGraphics());
                            // 绘制手数
                            class_teNum.drawTeNum(place_x, place_y, teNum, this.WHITE_PLAYER.getStone().getChessColor(), this.getGraphics());
                            // 设置有子
                            chessmap[coordinate_x][coordinate_y] = this.WHITE_PLAYER.getStone().getChessColor();
                            cs.updateMsg("USER白子下棋位置：" + coordinate_x + "," + coordinate_y + "\n");
                        }
                        // 手数加1
                        move_teNum[coordinate_x][coordinate_y][0] = teNum;
                        teNum++;
                        // 如果可以提子
                        if (Take.takeStones(chessmap, coordinate_x, coordinate_y,1)) {
                            takeStones(this.getGraphics());
                            System.out.println("USER提子");
                            cs.updateMsg("");
                        }
                        else {
                            System.out.println("USER落子");
                        }
                        // 高亮最后一手，并将倒数第二手的高亮去除
                        highLight.highLightLastStone(coordinate_x, coordinate_y, last_coordinate_x, last_coordinate_y, chessmap, teNum - 1, this.getGraphics());
                        last_coordinate_x = coordinate_x;
                        last_coordinate_y = coordinate_y;
                        // 两级反转.表明包
                        BLACK_PLAYER.setIsMoving(!(BLACK_PLAYER.getIsMoving()));
                        WHITE_PLAYER.setIsMoving(!(WHITE_PLAYER.getIsMoving()));
                        flagRun=false;
                        whoPlay=0;
                        greedyClicked();
                    }
                    else {
                        cs.updateMsg("USER此处已有子，无法下子！！！\n");
                        whoPlay=1;
                    }
                }
                else {
                    cs.updateMsg("USER鼠标点击位置处于棋盘外侧，无法下子！！！\n");
                    whoPlay=1;
                }

            }
            if ((mouseEvent.getModifiers() == InputEvent.BUTTON3_MASK)) {
                cs.updateMsg("USER点击鼠标左键下棋，不是右键！！！\n");
                whoPlay=1;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
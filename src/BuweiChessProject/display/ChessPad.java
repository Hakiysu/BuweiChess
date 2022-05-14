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
     * 声明9*9 chessmap数组，存储已落子的信息，road=路宽度，9
     * 声明teNum记录手数
     * 声明class_teNum 记录每一个坐标的棋子是第几手棋
     * 声明上一手的坐标last_coordinate_x，last_coordinate_y
     */
    Player BLACK_PLAYER;
    Player WHITE_PLAYER;
    Place BLACK_STONE;
    Place WHITE_STONE;
    TeNum class_teNum;
    HighLight highLight;
    Chess.ChessColor[][] chessmap;
    chessStatus cs = new chessStatus();
    Random rdm = new Random();
    int teNum;
    int[][][] move_teNum;
    int last_coordinate_x, last_coordinate_y;
    int whoFirst;
    int whoPlay;
    int[][] value = new int[9][9];//坐标权值
    int[] dx = {-1, 0, 1, 0};//行位移
    int[] dy = {0, -1, 0, 1};//列位移
    boolean[][] visited_by_air_judge = new boolean[9][9];

    int chessR = 20;
    int roadWidth = 50;//Normal:25
    int road = 9;
    boolean flagRun = true;
    JFrame frme;

    /**
     * 构造棋盘大小、背景、鼠标监听器
     */
    ChessPad(int n,JFrame frm) {
        frme=frm;
        if(n==-1)System.exit(0);
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
        void shutdownSeeker(){
            frame.dispose();
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
        if(whoFirst==0){
            Place.placeStone(this.BLACK_PLAYER, place_x, place_y, this.getGraphics());
            class_teNum.drawTeNum(place_x, place_y, 1, this.BLACK_PLAYER.getStone().getChessColor(), this.getGraphics());
            highLight.highLightLastStone(4, 4, 0, 0, chessmap, 0, this.getGraphics());
        }
    }

    public int opponent_color(int color) {//返回反的棋子颜色
        if (color == 1)
            return -1;
        else
            return 1;
    }

    //判断点(x,y)是否在棋盘内
    public boolean inBoard_judge(int x, int y) {
        return 0 <= x && x < 9 && 0 <= y && y < 9;
    }

    //判断下完此点(x,y)后是否有气
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

    public int getV(int x, int y) {
        visited_by_air_judge[x][y] = true; //标记，表示这个位置已经搜过有无气了
        int air=0;
        boolean flag = false;
        for (int dir = 0; dir < 4; dir++) {
            int x_dx = x + dx[dir], y_dy = y + dy[dir];
            if (inBoard_judge(x_dx, y_dy)) //界内
            {
                if (chessmap[x_dx][y_dy] == Chess.ChessColor.NONE) //旁边这个位置没有棋子
                    air+=2;
                if (chessmap[x_dx][y_dy] == chessmap[x][y] && !visited_by_air_judge[x_dx][y_dy]) //旁边这个位置是没被搜索过的同色棋
                    if (air_judge(x_dx, y_dy))
                        air++;
            }
        }
        return air;
    }
    //判断能否下颜色为color的棋
    //color:0 white/1 black
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
        }
        else {
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
        chessmap[x][y] = Chess.ChessColor.NONE; //回溯，棋子并没有下
        return true;
    }

    //估值函数，对当前棋面进行评估，
    // 计算颜色为color的一方比另一方可落子的位置数目多 多少（权值比较）
    //某个位置的气数可影响权值大小
    public int evaluate(int colorN) {
        int right = 0;
        int op_color = opponent_color(colorN);
        Chess.ChessColor color;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                //遍历整个棋盘
                if (put_available(x, y, colorN))//this color
                {
                    right+=getV(x,y);
                }
                if (put_available(x, y, op_color))//this color
                {
                    right=right-(int)getV(x,y)/2;
                }

            }
        }
        return right;
    }

    public void greedyClicked()throws NullPointerException {
        int coordinate_x;
        int coordinate_y;
        int color;
        if (whoFirst == 0)
            color = 1;
        else
            color = -1;
        //电脑先手就是1(黑色棋子)，否则就是-1(白色棋子)

        if (whoPlay == 0) {//只有操作权回归到电脑才触发以下贪婪操作
            int max_value = Integer.MIN_VALUE;//set to min
            int[] best_i = new int[81];
            int[] best_j = new int[81];
            int best_num = 0;
            //init

            for (int i = 0; i < road; i++)//reset value array
            {
                for (int j = 0; j < road; j++) {
                    value[i][j] = 0;
                }
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    //遍历棋盘
                    //颜色在上面已声明，且传入greedyClicked函数
                    if (put_available(i, j, color)) {
                        //能放棋子
                        if (color == 1) {
                            //黑子
                            chessmap[i][j] = Chess.ChessColor.BLACK;//先放个子
                            value[i][j] = evaluate(color);//判断本色棋子的局势
                            if (value[i][j] > max_value-3)//有优势则存，否则不存
                                max_value = value[i][j];
                            chessmap[i][j] = Chess.ChessColor.NONE;//回溯
                        }
                        else {
                            //白子
                            chessmap[i][j] = Chess.ChessColor.WHITE;
                            value[i][j] = evaluate(color);
                            if (value[i][j] > max_value-3)
                                max_value = value[i][j];
                            chessmap[i][j] = Chess.ChessColor.NONE;
                        }
                    }
                    else
                        //不能放棋子
                        value[i][j] = Integer.MIN_VALUE;
                }
            }

            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    if (value[i][j] >= max_value) {
                        //上面求得的所有权值存入best坐标数组
                        best_i[best_num] = i;
                        best_j[best_num] = j;
                        best_num++;
                    }
            System.out.println(best_num);
            int randomNum = rdm.nextInt(best_num);//在所有最大value里面随机选一个坐标
            coordinate_x = best_i[randomNum];
            coordinate_y = best_j[randomNum];
            //get click position
            //ai done work

            //start virtual clicked
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
                if (Take.takeStones(chessmap, coordinate_x, coordinate_y,0,frme)) {
                    System.out.println("AI提子");
                    cs.updateMsg("");
                    cs.shutdownSeeker();
                    frme.dispose();
                    flower.func();
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
                if (Take.takeStones(chessmap, 4, 4,0,frme)) {
                    //takeStones(this.getGraphics());
                    System.out.println("AI提子");
                    cs.updateMsg("");
                    cs.shutdownSeeker();
                    frme.dispose();
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
    public void mouseClicked(MouseEvent mouseEvent) throws NullPointerException {
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
                        if (Take.takeStones(chessmap, coordinate_x, coordinate_y,1,frme)) {
                            cs.shutdownSeeker();
                            System.out.println("USER提子");
                            cs.updateMsg("");
                            frme.dispose();
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
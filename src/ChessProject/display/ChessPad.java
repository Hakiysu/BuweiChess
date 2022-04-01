package ChessProject.display;

import ChessProject.display.Place;
import ChessProject.rule.Take;
import ChessProject.stone.Stone;
import ChessProject.aiPlayer.Player;
import ChessProject.rule.StoneAtHere;;
import ChessProject.rule.PosInBoard;


import java.awt.*;
import java.awt.event.*;

public class ChessPad extends Panel implements MouseListener, ActionListener {

    /**
     * 声明Player类存储棋手下棋顺序
     * 声明落子绘图类用于绘制棋子
     * 声明teNum类用于绘制手数
     * 声明highLight高亮最后一手
     * 声明19*19 move数组，存储已落子的信息
     * 声明teNum记录手数
     * 声明move_teNum 记录每一个坐标的棋子是第几手棋
     * 声明上一手的坐标last_coordinate_x，last_coordinate_y
     */
    Player BLACK_PLAYER ;
    Player WHITE_PLAYER ;
    Place BLACK_STONE;
    Place WHITE_STONE;
    TeNum class_teNum;
    HighLight highLight;
    Stone.StoneColor move[][] ;
    int teNum;
    int move_teNum[][][];
    int last_coordinate_x,last_coordinate_y;

    /**
     *构造棋盘大小、背景、鼠标监听器
     */
    ChessPad()
    {
        // 初始化执黑棋手
        BLACK_PLAYER = new Player();
        BLACK_PLAYER.setIsMoving(true);
        BLACK_PLAYER.setStone(Stone.StoneColor.BLACK);
        // 初始化执白棋手
        WHITE_PLAYER = new Player();
        WHITE_PLAYER.setIsMoving(false);
        WHITE_PLAYER.setStone(Stone.StoneColor.WHITE);
        // 初始化手数类
        class_teNum = new TeNum();
        // 初始化高亮最后一手类
        highLight = new HighLight();
        // 初始化黑棋、白棋
        BLACK_STONE = new Place(this);
        WHITE_STONE = new Place(this);
        // 初始化棋谱数组、手数数组
        move = new Stone.StoneColor[19][19];
        move_teNum = new int[19][19][1];
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                move[i][j] = Stone.StoneColor.NONE;
                move_teNum[i][j][0] = -1;
            }
        }
        //初始化手数、最后一手的坐标
        teNum = 1;
        last_coordinate_x = 0;
        last_coordinate_y = 0;
        this.add(BLACK_STONE);
        this.add(WHITE_STONE);
        this.add(class_teNum);
        this.setSize(600,600);
        this.setLayout(null);
        this.setBackground(Color.ORANGE);
        this.addMouseListener(this);
    }

    /**
     * 画棋盘的线和点
     * @param g
     */
    public void paint(Graphics g)
    {
        for (int i = 45; i <= 495; i += 25)
        {
            g.drawLine(i, 45, i, 495);
        }
        for (int i = 45; i <= 495; i += 25)
        {
            g.drawLine(45, i, 495, i);
        }
        //D16
        g.fillOval(116,116,8,8);
        //Q4
        g.fillOval(416,416,8,8);
        //D4
        g.fillOval(116,416,8,8);
        //Q16
        g.fillOval(416,116,8,8);
        //D10
        g.fillOval(116,266,8,8);
        //K16
        g.fillOval(266,116,8,8);
        //Q10
        g.fillOval(416,266,8,8);
        //K4
        g.fillOval(266,416,8,8);
        //天元
        g.fillOval(266,266,8,8);

    }

    /**
     * 按下鼠标，调用落子类绘图方法
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        if((mouseEvent.getModifiers() == InputEvent.BUTTON1_MASK))
        {
            // 这里减数是棋子的宽度、高度的一半 -- 10
            int x = (int)mouseEvent.getX()-10;
            int y = (int)mouseEvent.getY()-10;
            // 这里先求余、相减、再除
            // 求余数和除数是棋盘每路之间的宽度 -- 25
            // 得到的是棋盘坐标
            // -1 为了跟数组对应
            int coordinate_x = (x-(x%25))/25-1;
            int coordinate_y = (y-(y%25))/25-1;
            // 这里用棋盘坐标乘以棋盘每路之间的宽度 -- 25
            // 再加上棋子的宽度、高度的一半 -- 10
            // 得到的是落子类绘图方法需要的坐标
            int place_x = (coordinate_x+1)*25 + 10;
            int place_y = (coordinate_y+1)*25 + 10;
            // 判断是否在棋盘内
            if(PosInBoard.ifInBoard(coordinate_x,coordinate_y))
            {
                if(!StoneAtHere.ifAlreadyHadStone(move,coordinate_x,coordinate_y))
                {
                    // 黑棋
                    if(this.BLACK_PLAYER.getIsMoving())
                    {
                        // 落子、绘图
                        Place.placeStone(this.BLACK_PLAYER,place_x, place_y, this.getGraphics());
                        // 绘制手数
                        class_teNum.drawTeNum(place_x,place_y,teNum,this.BLACK_PLAYER.getStone().getStoneColor(),this.getGraphics());
                        // 设置有子
                        move[coordinate_x][coordinate_y] = this.BLACK_PLAYER.getStone().getStoneColor();
                    }
                    // 白棋
                    if(this.WHITE_PLAYER.getIsMoving())
                    {
                        // 落子、绘图
                        Place.placeStone(this.WHITE_PLAYER,place_x, place_y, this.getGraphics());
                        // 绘制手数
                        class_teNum.drawTeNum(place_x,place_y,teNum,this.WHITE_PLAYER.getStone().getStoneColor(),this.getGraphics());
                        // 设置有子
                        move[coordinate_x][coordinate_y] = this.WHITE_PLAYER.getStone().getStoneColor();
                    }
                    // 手数加1
                    move_teNum[coordinate_x][coordinate_y][0] = teNum;
                    teNum ++;
                    // 如果可以提子
                    if(Take.takeStones(move,coordinate_x,coordinate_y))
                    {
                        takeStones(this.getGraphics());
                        System.out.println("提子");
                    }
                    else
                    {
                        System.out.println("落子");
                    }
                    // 高亮最后一手，并将倒数第二手的高亮去除
                    highLight.highLightLastStone(coordinate_x,coordinate_y,last_coordinate_x,last_coordinate_y,move,teNum-1,this.getGraphics());
                    last_coordinate_x = coordinate_x;
                    last_coordinate_y = coordinate_y;
                    // 两级反转.表明包
                    BLACK_PLAYER.setIsMoving(!(BLACK_PLAYER.getIsMoving()));
                    WHITE_PLAYER.setIsMoving(!(WHITE_PLAYER.getIsMoving()));
                }
                else
                {
                    System.out.println("已有子");
                }
            }
            else
            {
                System.out.println("棋盘外");
            }
        }

        if((mouseEvent.getModifiers() == InputEvent.BUTTON3_MASK))
        {
            System.out.println("右键");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    // 提子
    public void takeStones(Graphics graphics)
    {
        int coordinate_x,coordinate_y,remove_x,remove_y;
        // 获得提子数量
        int length[][] = Take.getLength();
        // 获得提子坐标
        int takeStones[][][] = Take.getTakeStones();
        for(int i=0;i<4;i++)
        {
            // 如果记录的数量不为0，有子可提
            if(length[i][0] != 0)
            {
                for(int j=0;j<length[i][0];j++)
                {
                    // 获得要提的子的坐标
                    coordinate_x = takeStones[i][j][0];
                    coordinate_y = takeStones[i][j][1];
                    // 将坐标转换为绘图坐标
                    remove_x = (coordinate_x+1)*25 + 10;
                    remove_y = (coordinate_y+1)*25 + 10;
                    // 去除棋谱上该子
                    move[coordinate_x][coordinate_y] = Stone.StoneColor.NONE;
                    // 提子
                    Place.takeStone(remove_x,remove_y,graphics);
                }
            }
        }
        // 重绘
        removeAll();
        paint(graphics);
        // 重绘仍在棋盘上的棋子
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                if (move[i][j] == Stone.StoneColor.BLACK)
                {
                    Place.placeStone(this.BLACK_PLAYER,((i+1)*25 + 10),((j+1)*25 + 10),this.getGraphics());
                    class_teNum.drawTeNum(((i+1)*25 + 10),((j+1)*25 + 10),move_teNum[i][j][0],move[i][j],this.getGraphics());
                }
                if (move[i][j] == Stone.StoneColor.WHITE)
                {
                    Place.placeStone(this.WHITE_PLAYER,((i+1)*25 + 10),((j+1)*25 + 10),this.getGraphics());
                    class_teNum.drawTeNum(((i+1)*25 + 10),((j+1)*25 + 10),move_teNum[i][j][0],move[i][j],this.getGraphics());
                }
                if(move_teNum[i][j][0] == teNum)
                {
                    highLight.highLightLastStone(i,j,0,0,move,0,this.getGraphics());
                }
            }
        }
    }
}
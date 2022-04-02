package ChessProject.display;

import ChessProject.display.Place;
import ChessProject.rule.Take;
import ChessProject.stone.Stone;
import ChessProject.display.Status;
import ChessProject.aiPlayer.Player;
import ChessProject.rule.StoneAtHere;;
import ChessProject.rule.PosInBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;

public class ChessPad extends Panel implements MouseListener, ActionListener
{

        chessStatus cs=new chessStatus();

    boolean userStatus;
    /**
     * 声明Player类存储棋手下棋顺序
     * 声明落子绘图类用于绘制棋子
     * 声明teNum类用于绘制手数
     * 声明highLight高亮最后一手
     * 声明road*road move数组，存储已落子的信息
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

    int chessR=20;
    int chessD=chessR*2;
    int roadWidth=50;//Normal:25
    int road=9;

    /**
     *构造棋盘大小、背景、鼠标监听器
     */
    ChessPad(int n)
    {
        //0pc first,1user first
        //
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
        move = new Stone.StoneColor[road][road];
        move_teNum = new int[road][road][1];
        for (int i = 0; i < road; i++)
        {
            for (int j = 0; j < road; j++)
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

    class  chessStatus {
        String msg;
        JFrame frame =new JFrame("棋盘状态侦测");
        JPanel panel=new JPanel();
        JTextArea status = new JTextArea();
        JScrollPane jsp=new JScrollPane(status);
        {
            frame.setSize(360,512);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.out.println("ACTIVE");
            panel.setLayout(null);
            status.setLineWrap(true);
            jsp.setBounds(20,20,300,430);
            panel.add(jsp);
            frame.add(panel);
            frame.setVisible(true);
        }

        void updateMsg(String msg){
            status.setText(status.getText()+msg);
    }

    }

    /**
     * 画棋盘的线和点
     * @param g
     */
    public void paint(Graphics g)
    {
        for (int i = 90; i <= roadWidth*8+90; i += roadWidth)
        {
            g.drawLine(i, 90, i, roadWidth*8+90);
        }
        for (int i = 90; i <= roadWidth*8+90; i += roadWidth)
        {
            g.drawLine(90, i, roadWidth*8+90, i);
        }
    }

    /**
     * 按下鼠标，调用落子类绘图方法
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if((mouseEvent.getModifiers() == InputEvent.BUTTON1_MASK))
        {
            // 这里减数是棋子的宽度、高度的一半 -- 10
            int x = (int)mouseEvent.getX()-chessR;
            int y = (int)mouseEvent.getY()-chessR;
            // 这里先求余、相减、再除
            // 求余数和除数是棋盘每路之间的宽度 -- roadWidth
            // 得到的是棋盘坐标
            // -1 为了跟数组对应
            int coordinate_x = (x-(x%roadWidth))/roadWidth-1;
            int coordinate_y = (y-(y%roadWidth))/roadWidth-1;
            // 这里用棋盘坐标乘以棋盘每路之间的宽度 -- roadWidth
            // 再加上棋子的宽度、高度的一半 -- 10
            // 得到的是落子类绘图方法需要的坐标
            int place_x = (coordinate_x+1)*roadWidth + chessR;
            int place_y = (coordinate_y+1)*roadWidth + chessR;
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
                        cs.updateMsg("黑子下棋位置："+coordinate_x+","+coordinate_y+"\n");
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
                        cs.updateMsg("白子下棋位置："+coordinate_x+","+coordinate_y+"\n");
                    }

                    // 手数加1
                    move_teNum[coordinate_x][coordinate_y][0] = teNum;
                    teNum ++;

                    // 如果可以提子
                    if(Take.takeStones(move,coordinate_x,coordinate_y))
                    {
                        takeStones(this.getGraphics());
                        System.out.println("提子");
                        cs.updateMsg("");
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
                    cs.updateMsg("此处已有子，无法下子！！！\n");
                }
            }
            else
            {
                cs.updateMsg("鼠标点击位置处于棋盘外侧，无法下子！！！\n");
            }

        }

        if((mouseEvent.getModifiers() == InputEvent.BUTTON3_MASK))
        {
            cs.updateMsg("点击鼠标左键下棋，不是右键！！！\n");
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
                    remove_x = (coordinate_x+1)*roadWidth + chessR;
                    remove_y = (coordinate_y+1)*roadWidth + chessR;
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
        for (int i = 0; i < road; i++)
        {
            for (int j = 0; j < road; j++)
            {
                if (move[i][j] == Stone.StoneColor.BLACK)
                {
                    Place.placeStone(this.BLACK_PLAYER,((i+1)*roadWidth + chessR),((j+1)*roadWidth + chessR),this.getGraphics());
                    class_teNum.drawTeNum(((i+1)*roadWidth + chessR),((j+1)*roadWidth + chessR),move_teNum[i][j][0],move[i][j],this.getGraphics());
                }
                if (move[i][j] == Stone.StoneColor.WHITE)
                {
                    Place.placeStone(this.WHITE_PLAYER,((i+1)*roadWidth + chessR),((j+1)*roadWidth + chessR),this.getGraphics());
                    class_teNum.drawTeNum(((i+1)*roadWidth + chessR),((j+1)*roadWidth + chessR),move_teNum[i][j][0],move[i][j],this.getGraphics());
                }
                if(move_teNum[i][j][0] == teNum)
                {
                    highLight.highLightLastStone(i,j,0,0,move,0,this.getGraphics());
                }
            }
        }
    }
}
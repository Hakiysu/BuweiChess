package BuweiChessProject.main;

import BuweiChessProject.display.BackGround;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainFunction {
    public static void main(String[] args){
        JFrame frame = new JFrame("不围棋-MainMenu");
        ImageIcon img= new ImageIcon("D:/coding/wp.jpg");
        JLabel imgLabel=new JLabel(img);
        frame.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));//注意这里是关键，将背景标签添加到jfram的LayeredPane面板里。
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
        Container con=frame.getContentPane();
        con.setLayout(new GridLayout(16,9));
        JButton start=new JButton("开始游戏");//创建按钮
        start.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                new BackGround();
            }
        }
        );
        JButton exit=new JButton("退出游戏");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        con.add(start,"3");//将按钮添加入窗口的内容面板
        con.add(exit,"7");
        ((JPanel)con).setOpaque(false); //注意这里，将内容面板设为透明。这样LayeredPane面板中的背景才能显示出来。
        //为窗体设置一些参数：
        //显示窗体
        frame.setVisible(true);
        //调整窗体的大小
        frame.setSize(1280, 720);
        //设置窗体的位置
        frame.setLocation(0, 0);

        //窗体关闭时退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //调用创建棋盘

    }
}

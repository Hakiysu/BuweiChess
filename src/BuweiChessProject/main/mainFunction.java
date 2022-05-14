package BuweiChessProject.main;

import BuweiChessProject.display.BackGround;

import javax.swing.*;
import java.awt.*;

public class mainFunction extends JFrame{
    public static void main(String[] args){
        JFrame frame = new JFrame("不围棋-主菜单");
        frame.setLayout(null);
        ImageIcon imageIcon = new ImageIcon("resource/logo.jpg");
        frame.setIconImage(imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        ImageIcon img= new ImageIcon("resource/back2_blur.png");
        JLabel imgLabel=new JLabel(img);
        //imgLabel.setSize(1200,770);
        imgLabel.setBounds(0,0,1200,770);
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
        frame.getLayeredPane().setLayout(null);
        // 把背景图片添加到分层窗格的最底层作为背景
        frame.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));
        frame.setSize(1200,770);

        //3.之后把组件和面板添加到窗口面板就可以；
        JButton start=new JButton("开始游戏");//创建按钮
        start.addActionListener(e -> new BackGround());
        JButton exit=new JButton("退出游戏");
        exit.addActionListener(e -> System.exit(0));

        //设置尺寸位置
        start.setBounds(300,560,160,50);
        exit.setBounds(750,560,160,50);
        frame.add(start,BorderLayout.CENTER);
        frame.add(exit,BorderLayout.CENTER);

        JLabel TitleText=new JLabel();
        TitleText.setText("JAVA不围棋桌面客户端");
        Font titleFont=new Font("方正字迹-劲颜体 简", Font.PLAIN, 80);
        TitleText.setFont(titleFont);
        TitleText.setForeground(Color.red);
        TitleText.setBounds(220,150,1000,80);

        JLabel bodyText=new JLabel();
        bodyText.setText("<html><h1 style=\"color:yellow;text-align:center\">" +
                "团队成员：胡泽坤，李荣峥<br>孙峥，刘文荟，孔金燕，陈子健<br><br>版本号：" +
                "<b>V1.5.1</b></h1></html>");
        Font bodyFont=new Font("霞鹜文楷",Font.PLAIN,25);
        bodyText.setFont(bodyFont);
        bodyText.setBounds(430,300,500,200);

        frame.add(TitleText);
        frame.add(bodyText);
        frame.setBounds(300,200,1200,770);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

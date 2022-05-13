package BuweiChessProject.main;

import BuweiChessProject.display.BackGround;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainFunction {
    public static void main(String[] args){
        JFrame frame = new JFrame("不围棋-主菜单");
        ImageIcon imageIcon = new ImageIcon("./resource/logo.jpg");
        frame.setIconImage(imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        ImageIcon img= new ImageIcon("./resource/back2_blur.png");
        JLabel imgLabel=new JLabel(img);

        imgLabel.setSize(1200,770);
        frame.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));
        frame.setSize(1200,770);
        frame.setLayout(new GridLayout(16,9));

        //2.把窗口面板设为内容面板并设为透明、流动布局。
        JPanel panel=(JPanel)frame.getContentPane();
        panel.setOpaque(false);
        //3.之后把组件和面板添加到窗口面板就可以；
        JButton start=new JButton("开始游戏");//创建按钮
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                new BackGround();
            }
        });

        JButton exit=new JButton("退出游戏");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        start.setBounds(240, 500, 15, 5);
        exit.setBounds(560, 500, 15, 5);
        frame.add(start);
        frame.add(exit);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);




    }
}

package BuweiChessProject.display;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BackGround extends JFrame {

    ChessPad chessPad;

    public BackGround() {
        JFrame frame=new JFrame("不围棋");

        String [] options = {"电脑先手","电脑后手"};
        //先手为黑子
        int n= JOptionPane.showOptionDialog(null,"模式选择：","不围棋-游戏模式",
                JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        chessPad = new ChessPad(n,frame);
        frame.add(chessPad);
        frame.setSize(600,600);//不围棋主程序框-大小
        frame.setLocationRelativeTo(null);// 设置窗口默认中间
        //frame.setAlwaysOnTop(true);// 设置窗口永远显示在最前端//导致比赛结束对话框被遮挡
        frame.setResizable(false);// 设置窗口不能被修改
        frame.setVisible(true);
        chessPad.pcFirstInit();//if电脑先手，初始化天元位置下黑子
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }


}

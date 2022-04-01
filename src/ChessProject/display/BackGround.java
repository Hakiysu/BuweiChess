package ChessProject.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BackGround extends Frame {

    ChessPad chessPad;

    public BackGround()
    {
        String [] options = {"电脑先手","电脑后手"};
        int n= JOptionPane.showOptionDialog(null,"模式选择：","不围棋-游戏模式",
                JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        System.out.println(n+options[n]);
        chessPad = new ChessPad(n);
        this.add(chessPad);
        this.setSize(600,600);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}

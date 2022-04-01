package ChessProject.display;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BackGround extends Frame {

    ChessPad chessPad;

    public BackGround()
    {
        chessPad = new ChessPad();
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

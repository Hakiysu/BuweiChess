package ChessProject.display;

import javax.swing.*;
import java.awt.*;

public final class Status extends Panel {
    public void statusInfo(JPanel panel)
    {
        System.out.println("ACTIVE");
        panel.setLayout(null);
        JTextField status = new JTextField(200);
        status.setBounds(10,10,10,10);
        panel.add(status);
    }
}

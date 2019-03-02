package remoteokdesktop.gui;

import com.sun.javafx.embed.AbstractEvents;
import javafx.event.EventTarget;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SharePanel extends JPanel {

    public SharePanel() {
        this.setBackground(new Color(102, 102,102,50));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPanel panel = ((JPanel) e.getSource());
                panel.setBackground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JPanel panel = ((JPanel) e.getSource());
                panel.setBackground(new Color(102, 102,102,50));
            }
        });
    }
}

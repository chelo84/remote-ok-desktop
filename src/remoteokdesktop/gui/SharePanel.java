package remoteokdesktop.gui;

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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.WHITE);
                ((JPanel) e.getSource()).repaint();
                ((JPanel) e.getSource()).revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(new Color(102, 102,102,50));
                ((JPanel) e.getSource()).repaint();
                ((JPanel) e.getSource()).revalidate();
            }
        });

        IconFontSwing.register(FontAwesome.getIconFont());
        Icon shareIcon = IconFontSwing.buildIcon(FontAwesome.SHARE_ALT_SQUARE, 15, Color.BLACK);
        this.add(new JLabel(shareIcon));
    }
}

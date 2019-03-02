package remoteokdesktop.util;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.coobird.thumbnailator.Thumbnails;
import remoteokdesktop.gui.WhitePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ComponentUtils {
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        try {
            return Thumbnails.of(img).size(newW, newH).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JLabel unboldLabel(String text) {
        JLabel label = new JLabel(text);
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        return label;
    }

    public static Icon getOpenHeartLabel() {
        IconFontSwing.register(FontAwesome.getIconFont());
        return IconFontSwing.buildIcon(FontAwesome.HEART_O, 15, Color.BLACK);
    }

    public static Icon getClosedHeartLabel() {
        IconFontSwing.register(FontAwesome.getIconFont());
        return IconFontSwing.buildIcon(FontAwesome.HEART, 15, Color.RED);
    }
}

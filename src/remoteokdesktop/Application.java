package remoteokdesktop;

import remoteokdesktop.gui.ListFrame;
import remoteokdesktop.gui.LoginFrame;

import java.awt.*;

public class Application {

    public static void main(String[] args) {
        LoginFrame login = new LoginFrame();

        Runnable runner = () -> {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().getImage("imagem.jpeg");
                PopupMenu popup = new PopupMenu();
                MenuItem item = new MenuItem("Um Menu Item");
                popup.add(item);
                TrayIcon trayIcon = new TrayIcon(image, "Remote OK", popup);
                trayIcon.setImageAutoSize(true);

                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("Não pode adicionar a tray");
                }
            } else {
                System.err.println("Tray indisponível");
            }
        };

        EventQueue.invokeLater(runner);

    }
}

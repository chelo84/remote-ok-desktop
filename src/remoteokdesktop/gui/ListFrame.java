package remoteokdesktop.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.text.StyleConstants.Size;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class ListFrame extends JFrame {
    
    public ListFrame() {
        this.setTitle("RemoteOK");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(400, 500));
        createComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.pack();
    }
    
    public void createComponents() {
        this.setLayout(new MigLayout("fillx"));

        JPanel searchPanel  = new JPanel(new MigLayout("fillx"));
        
        JTextField searchField = new JTextField(25);
        searchField.setToolTipText("Search for a remote job!");
        searchPanel.add(searchField, "align center");
        this.add(searchPanel, "align center, wrap");
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel alllistPanel = new JPanel(new MigLayout("fillx"));
        JList allList = new JList();
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) allList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        allList.setBackground(Color.decode("#5D6D7E"));
        allList.setForeground(Color.WHITE);
        JScrollPane allListScroller = new JScrollPane();        
        allListScroller.setPreferredSize(new Dimension(300, 400));
        allListScroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        alllistPanel.add(allListScroller, "align center, wrap");
        
        tabbedPane.add("Todos", alllistPanel);
        
        JPanel favPanel = new JPanel(new MigLayout("fillx"));
        JList favList = new JList();
        favList.setBackground(Color.decode("#5D6D7E"));
        favList.setForeground(Color.WHITE);
        JScrollPane favListScroller = new JScrollPane();
        favListScroller.setPreferredSize(new Dimension(300, 400));
        favListScroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        favPanel.add(favListScroller, "align center, wrap");
        
        tabbedPane.add("Favoritos", favPanel);
        this.add(tabbedPane, "align center");
    }
}

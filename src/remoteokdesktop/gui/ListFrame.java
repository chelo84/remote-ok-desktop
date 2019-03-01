package remoteokdesktop.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.mashape.unirest.http.Unirest;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import remoteokdesktop.model.RemoteOkJob;
import remoteokdesktop.util.ComponentUtils;
import remoteokdesktop.util.RemoteOkUtils;

import static java.util.Objects.nonNull;
import static remoteokdesktop.util.ComponentUtils.*;
import static remoteokdesktop.util.RemoteOkUtils.getLikedJobs;
import static remoteokdesktop.util.StringUtils.formatDate;
import static remoteokdesktop.util.StringUtils.getDescription;

public class ListFrame extends JFrame {
	List<RemoteOkJob> jobs = null;
	JPanel allListPanel = new WhitePanel(new MigLayout());
    JPanel favPanel = new WhitePanel(new MigLayout("fillx"));
	
    public ListFrame() {
    	this.getJobs(null);
        this.setTitle("RemoteOK");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(500, 500));
        createComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);
        this.pack();
    }
    
    public void createComponents() {
        try {
            this.setLayout(new MigLayout("fillx"));

            JPanel searchPanel = new WhitePanel(new MigLayout("fillx"));

            JTextField searchField = new JTextField(25);
            searchField.addActionListener((ev) -> {
				getJobs(((JTextField) ev.getSource()).getText());
				paintJobs(allListPanel);
			});
            searchField.setToolTipText("Search for a remote job!");
            searchPanel.add(searchField, "align center");
            this.add(searchPanel, "align center, wrap");

            JTabbedPane tabbedPane = new JTabbedPane();

            JScrollPane allListScroller = new JScrollPane(allListPanel,
                                                            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            allListScroller.getVerticalScrollBar().setUnitIncrement(20);
            allListScroller.setBackground(Color.WHITE);

            this.paintJobs(allListPanel);

            tabbedPane.add("Todos", allListScroller);

//        JList favList = new JList();
//        favList.setBackground(Color.decode("#5D6D7E"));
//        favList.setForeground(Color.WHITE);
//        JScrollPane favListScroller = new JScrollPane();
//        favListScroller.setPreferredSize(new Dimension(300, 400));
//        favListScroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//
//        favPanel.add(favListScroller, "align center, wrap");
//
            tabbedPane.add("Favoritos", favPanel);
            
            tabbedPane.getComponentAt(0).setBackground(Color.BLACK);
            this.add(tabbedPane, "align center, growy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getJobs(String filter) {
        if(nonNull(filter)) {
            jobs = RemoteOkUtils.getJobs(filter);
        } else {
            jobs = RemoteOkUtils.getJobs();
        }
    }

    public void paintJobs(JPanel panelToAppend) {
    	try {
    	panelToAppend.removeAll();
            List<RemoteOkJob> likedJobs = getLikedJobs();
            jobs.forEach((job) -> {
            try {
                JPanel remoteJobPanel = new WhitePanel(new MigLayout("fillx"));
                JPanel jobPanel = new WhitePanel(new MigLayout("fillx"));
                jobPanel.setSize(new Dimension(100, 100));

                JPanel logoPanel = new WhitePanel(new MigLayout());
                if(nonNull(job.getLogoUrl())) {
                    BufferedImage image = ImageIO.read(Unirest.get(job.getLogoUrl()).asBinary().getBody());
                    if(nonNull(image)) {
                        image = ComponentUtils.resize(image, 30, 30);
                        JLabel imgLabel = new JLabel(new ImageIcon(image));

                        logoPanel.add(imgLabel);
                    }
                } else {
                	Icon icon = new EmptyIcon(30, 30);
                	JLabel imgLabel = new JLabel(icon);
                	logoPanel.add(imgLabel);
                }
                jobPanel.add(logoPanel, "");

                JPanel infoPanel = new WhitePanel(new MigLayout());
                JLabel companyLabel = new JLabel(job.getCompany());
                	
                infoPanel.add(companyLabel, "wrap");
                infoPanel.add(unboldLabel(formatDate(job.getDate())), "wrap");
                infoPanel.add(unboldLabel("<html>"+ getDescription(job.getDescription(), " <br> ", 60) +"</html>"));
                jobPanel.add(infoPanel, "wrap");
                
                JPanel othersPanel = new WhitePanel(new MigLayout());

                JPanel likePanel = new LikePanel(new MigLayout(), job, othersPanel);
                likePanel.setBackground(new Color(102, 102,102,50));
                likePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                Icon icon = getOpenHeartLabel();
                JLabel likeLabel = new JLabel(icon);
                if(RemoteOkUtils.isJobLiked(likedJobs, job)) {
                    likeLabel.setIcon(getClosedHeartLabel());
                    likeLabel.setName("closed");
                } else {
                    likeLabel.setName("open");
                }
                likePanel.add(likeLabel);
                othersPanel.add(likePanel);

                JPanel sharePanel = new SharePanel();

                othersPanel.add(sharePanel, "gapx 327");

                remoteJobPanel.add(jobPanel, "wrap");
                remoteJobPanel.add(othersPanel, "wrap");
                panelToAppend.add(remoteJobPanel, "wrap");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    	
    	panelToAppend.repaint();
    	panelToAppend.revalidate();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
}

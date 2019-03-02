package remoteokdesktop.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.mashape.unirest.http.Unirest;

import com.mashape.unirest.http.exceptions.UnirestException;
import net.miginfocom.swing.MigLayout;
import remoteokdesktop.model.RemoteOkJob;
import remoteokdesktop.util.ComponentUtils;
import remoteokdesktop.util.RemoteOkUtils;

import static java.util.Objects.nonNull;
import static remoteokdesktop.util.ComponentUtils.*;
import static remoteokdesktop.util.StringUtils.formatDate;
import static remoteokdesktop.util.StringUtils.getDescription;

public class ListFrame extends JFrame {
	JPanel allListPanel = new WhitePanel(new MigLayout("fillx"));
    JPanel likedListPanel = new WhitePanel(new MigLayout("fillx"));
    JPanel pagesPanel = new WhitePanel(new MigLayout("fillx"));
    JTabbedPane tabbedPane = new JTabbedPane();
	
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
                String searchText = ((JTextField) ev.getSource()).getText();
                int selectedIndex = tabbedPane.getSelectedIndex();

                switch(selectedIndex) {
                    case 0: paintJobs(getJobs(searchText), false); break;
                    case 1: paintJobs(getLikedJobs(searchText), true); break;
                }
			});
            searchField.setToolTipText("Search for a remote job!");
            searchPanel.add(searchField, "align center");
            this.add(searchPanel, "align center, wrap");

            JScrollPane allListScroller = new JScrollPane(allListPanel,
                                                            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            allListScroller.getVerticalScrollBar().setUnitIncrement(20);
            allListScroller.setBackground(Color.WHITE);

            this.paintJobs(getJobs(null), false);

            tabbedPane.add("Todos", allListScroller);

            JScrollPane likedListScroller = new JScrollPane(likedListPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            likedListScroller.getVerticalScrollBar().setUnitIncrement(20);
            likedListScroller.setBackground(Color.WHITE);

            tabbedPane.addChangeListener((e) -> {
                int selectedIndex = tabbedPane.getSelectedIndex();

                switch(selectedIndex) {
                    case 0: paintAllJobs(); break;
                    case 1: paintLikedJobs(); break;
                }
            });

            tabbedPane.add("Favoritos", likedListScroller);

            tabbedPane.getComponentAt(0).setBackground(Color.BLACK);
            this.add(tabbedPane, "align center, growy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void paintAllJobs() {
        paintJobs(getJobs(null), false);
    }

    private void paintLikedJobs() {
        paintJobs(getLikedJobs(null), true);
    }

    private List<RemoteOkJob> getJobs(String filter) {
        if(nonNull(filter)) {
            return RemoteOkUtils.getAll(filter);
        }
        return RemoteOkUtils.getAll();
    }

    private List<RemoteOkJob> getLikedJobs(String filter) {
        if(nonNull(filter)) {
            return RemoteOkUtils.getLiked(filter);
        }
        return RemoteOkUtils.getLiked();
    }

    private void paintJobs(List<RemoteOkJob> jobsToPaint, Boolean isLikedPanel) {
        try {
            JPanel panelToAppend = (isLikedPanel) ? likedListPanel : allListPanel;
            panelToAppend.removeAll();
            List<RemoteOkJob> likedJobs = getLikedJobs(null);
            for(int i = 0; i < jobsToPaint.size(); i++) {
                RemoteOkJob job = jobsToPaint.get(i);
                try {
                    JPanel remoteJobPanel = new WhitePanel(new MigLayout("fillx"));
                    JPanel jobPanel = new WhitePanel(new MigLayout("fillx"));
                    jobPanel.setSize(new Dimension(100, 100));

                    JPanel logoPanel = new WhitePanel(new MigLayout());
                    logoPanel.add(new JLabel(this.getLogo(job)));
                    jobPanel.add(logoPanel, "");

                    JPanel infoPanel = new WhitePanel(new MigLayout());
                    JLabel companyLabel = new JLabel(job.getCompany());

                    infoPanel.add(companyLabel, "wrap");
                    infoPanel.add(unboldLabel(formatDate(job.getDate())), "wrap");
                    infoPanel.add(unboldLabel("<html>" + getDescription(job.getDescription(), " <br> ", 60) + "</html>"));
                    jobPanel.add(infoPanel, "wrap");

                    JPanel othersPanel = new WhitePanel(new MigLayout());

                    JPanel likePanel = new LikePanel(new MigLayout(), job, othersPanel);
                    likePanel.setBackground(new Color(102, 102, 102, 50));
                    likePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                    Icon icon = getOpenHeartLabel();
                    JLabel likeLabel = new JLabel(icon);
                    if (RemoteOkUtils.isLiked(likedJobs, job)) {
                        likeLabel.setIcon(getClosedHeartLabel());
                        likeLabel.setName("closed");
                        if(isLikedPanel) {
                            likePanel.addMouseListener(new MouseAdapter() {
                                int i;

                                public MouseAdapter init(int i) {
                                    this.i = i;
                                    return this;
                                }

                                @Override
                                public void mouseReleased(MouseEvent ev) {
                                    panelToAppend.remove(i);
                                    panelToAppend.repaint();
                                    panelToAppend.revalidate();
                                }
                            }.init(i));
                        }
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
            }

            panelToAppend.repaint();
            panelToAppend.revalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Icon getLogo(RemoteOkJob job) throws IOException, UnirestException {
        if(nonNull(job.getLogoUrl())) {
            BufferedImage image = ImageIO.read(Unirest.get(job.getLogoUrl()).asBinary().getBody());
            if(nonNull(image)) {
                image = ComponentUtils.resize(image, 30, 30);
                return new ImageIcon(image);
            }
        }

        Icon icon = new EmptyIcon(30, 30);
        return icon;
    }
}

package remoteokdesktop.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	private JPanel allListPanel = new WhitePanel(new MigLayout("fillx"));
    private JPanel likedListPanel = new WhitePanel(new MigLayout("fillx"));
    private JTextField searchField = new JTextField(25);
    private JPanel pagesPanel = new WhitePanel(new MigLayout(""));
    private JPanel registryQtyPanel = new WhitePanel(new MigLayout(""));
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel footerPanel = new WhitePanel(new MigLayout("fillx"));
    private List<RemoteOkJob> currentShowingJobs = new ArrayList<>();
    private final Integer jobsPerPage = 50;
	
    public ListFrame() {
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

            searchField.addActionListener((ev) -> {
                String searchText = ((JTextField) ev.getSource()).getText();
                paintTab(searchText, 0);
            });
            searchField.setToolTipText("Search for a remote job!");
            searchPanel.add(searchField, "align center");
            this.add(searchPanel, "align center, wrap");

            JScrollPane allListScroller = new JScrollPane(allListPanel,
                                                            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            allListScroller.getVerticalScrollBar().setUnitIncrement(20);
            allListScroller.setBackground(Color.WHITE);

            this.paintAllJobs(0);

            tabbedPane.add("Todos", allListScroller);

            JScrollPane likedListScroller = new JScrollPane(likedListPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            likedListScroller.getVerticalScrollBar().setUnitIncrement(20);
            likedListScroller.setBackground(Color.WHITE);

            tabbedPane.addChangeListener((e) -> {
                paintTab(null, 0);
            });

            tabbedPane.add("Favoritos", likedListScroller);

            tabbedPane.getComponentAt(0).setBackground(Color.BLACK);
            this.add(tabbedPane, "align center, growy, wrap");

            this.add(footerPanel, "growx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void paintPageCountAndRegistryQty(Integer selected) {
        footerPanel.removeAll();
        pagesPanel.removeAll();
        registryQtyPanel.removeAll();
        for(int i = 0; i < Math.ceil(currentShowingJobs.size() / (double) jobsPerPage); i++) {
            JLabel pageLabel = unboldLabel(String.format("%s", i + 1));
            if(i == selected) bold(pageLabel);

            pageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pageLabel.setForeground(Color.BLUE);
            pageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    if(!isBold(label)) {
                        Arrays.asList(pagesPanel.getComponents()).forEach(ComponentUtils::unbold);
                        bold(label);
                        paintTab(searchField.getText(), Integer.parseInt(label.getText()) - 1);
                    }
                }
            });

            pagesPanel.add(pageLabel);

        }
        footerPanel.add(pagesPanel, "align center");

        registryQtyPanel.add(new JLabel(String.format("%s registros", currentShowingJobs.size())));
        footerPanel.add(registryQtyPanel, "dock east");
    }

    private void paintTab(String searchText, Integer page) {
        int selectedIndex = tabbedPane.getSelectedIndex();

        switch(selectedIndex) {
            case 0: paintJobs(getJobs(searchText), page, false); break;
            case 1: paintJobs(getLikedJobs(searchText), page, true); break;
        }
    }

    private void paintAllJobs(Integer page) {
        paintJobs(getJobs(null), page, false);
    }

    private void paintLikedJobs(Integer page) {
        paintJobs(getLikedJobs(null), page,true);
    }

    private List<RemoteOkJob> getJobs(String filter) {
        List<RemoteOkJob> jobs = null;
        if(nonNull(filter)) {
            currentShowingJobs = jobs = RemoteOkUtils.getAll(filter);
        } else {
            currentShowingJobs = jobs = RemoteOkUtils.getAll();
        }
        return jobs;
    }

    private List<RemoteOkJob> getLikedJobs(String filter) {
        List<RemoteOkJob> jobs = null;
        if(nonNull(filter) && !filter.isEmpty()) {
            currentShowingJobs = jobs = RemoteOkUtils.getLiked(filter);
        } else {
            currentShowingJobs = jobs = RemoteOkUtils.getLiked();
        }
        return jobs;
    }

    private void paintJobs(List<RemoteOkJob> jobsToPaint, Integer page, Boolean isLikedPanel) {
        try {
            JPanel panelToAppend = (isLikedPanel) ? likedListPanel : allListPanel;
            panelToAppend.removeAll();
            List<RemoteOkJob> likedJobs = RemoteOkUtils.getLiked();
            Integer startIndex, endIndex;
            startIndex = jobsPerPage * page;
            endIndex = (startIndex + jobsPerPage < jobsToPaint.size()) ? startIndex + jobsPerPage : jobsToPaint.size();

            for(int i = startIndex; i < endIndex; i++) {
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
                    if (isLikedPanel || RemoteOkUtils.isLiked(likedJobs, job)) {
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
                                    panelToAppend.remove(remoteJobPanel);
                                    panelToAppend.repaint();
                                    panelToAppend.revalidate();

                                    JLabel registryQtyLabel = (JLabel) registryQtyPanel.getComponents()[0];
                                    registryQtyLabel.setText(String.format("%s registros", Integer.parseInt(registryQtyLabel.getText()
                                            .substring(0, registryQtyLabel.getText().indexOf(' ')))-1));
                                    registryQtyPanel.repaint();
                                    registryQtyPanel.revalidate();
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

            paintPageCountAndRegistryQty(page);

            footerPanel.repaint();
            footerPanel.revalidate();
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

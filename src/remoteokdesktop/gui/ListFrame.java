package remoteokdesktop.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.coobird.thumbnailator.Thumbnails;
import net.miginfocom.swing.MigLayout;
import remoteokdesktop.model.RemoteOkJob;

public class ListFrame extends JFrame {
	List<RemoteOkJob> allJobs = null;
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
            allListScroller.setSize(new Dimension(300, 300));

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
            this.add(tabbedPane, "align center");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getJobs(String filter) {
        HttpResponse<JsonNode> remoteOkResponse = null;
        try {
            remoteOkResponse = Unirest.get("https://remoteok.io/api").asJson();
            JSONArray arr = remoteOkResponse.getBody().getArray();
            arr.remove(0);
            ObjectMapper mapper = new ObjectMapper();
            jobs = allJobs =  mapper.readValue(arr.toString(), mapper.getTypeFactory().constructCollectionType(List.class, RemoteOkJob.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(Objects.nonNull(filter) && !filter.isEmpty()) {
        	jobs = allJobs.stream()
        				.filter(job -> job.getCompany().contains(filter) || job.getSlug().contains(filter))
        				.collect(Collectors.toList());
        } else {
        	jobs = allJobs;
        }
        
        System.out.println(jobs.size());
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        try {
            return Thumbnails.of(img).size(newW, newH).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JLabel unboldLabel(String text) {
        JLabel label = new JLabel(text);
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        return label;
    }

    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/DD hh:mm:ss");
        return format.format(date);
    }

    public static String getDescription(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }
    
    public void paintJobs(JPanel panelToAppend) {
    	try {
    	panelToAppend.removeAll();
    	jobs.forEach((job) -> {
            try {
                JPanel remoteJobPanel = new WhitePanel(new MigLayout("fillx"));
                JPanel jobPanel = new WhitePanel(new MigLayout("fillx"));
                jobPanel.setSize(new Dimension(100, 100));

                JPanel logoPanel = new WhitePanel(new MigLayout());
                if(Objects.nonNull(job.getLogoUrl())) {
                    BufferedImage image = ImageIO.read(Unirest.get(job.getLogoUrl()).asBinary().getBody());
                    if(Objects.nonNull(image)) {
                        image = this.resize(image, 30, 30);
                        JLabel imgLabel = new JLabel(new ImageIcon(image));

                        logoPanel.add(imgLabel);
                    }
                } else {
                	Icon icon = new EmptyIcon(30, 30);
                	JLabel imgLabel = new JLabel(icon);
                	logoPanel.add(imgLabel);
                }
                jobPanel.add(logoPanel, "");

                JPanel infoPanel = new WhitePanel(new MigLayout("debug"));
                JLabel companyLabel = new JLabel(job.getCompany());
                	
                infoPanel.add(companyLabel, "wrap");
                infoPanel.add(this.unboldLabel(formatDate(job.getDate())), "wrap");
                infoPanel.add(this.unboldLabel("<html>"+ this.getDescription(job.getDescription(), " <br> ", 60) +"</html>"));
                jobPanel.add(infoPanel, "wrap");
                
                JPanel othersPanel = new WhitePanel(new MigLayout());

                IconFontSwing.register(FontAwesome.getIconFont());
                JPanel likePanel = new JPanel(new MigLayout());
                likePanel.setBackground(new Color(102, 102,102,50));
                likePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                Icon icon = IconFontSwing.buildIcon(FontAwesome.HEART_O, 15, Color.BLACK);
                likePanel.add(new JLabel(icon));
                othersPanel.add(likePanel, "width 10px");

                JPanel sharePanel = new SharePanel();

                othersPanel.add(sharePanel, "width 10px, gapx 327");

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

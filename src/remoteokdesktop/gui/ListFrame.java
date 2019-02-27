package remoteokdesktop.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import net.coobird.thumbnailator.Thumbnails;
import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import remoteokdesktop.model.RemoteOkJob;

import static java.util.Objects.nonNull;

public class ListFrame extends JFrame {
    
    public ListFrame() {
        this.setTitle("RemoteOK");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 600));
        createComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.pack();
    }
    
    public void createComponents() {
        try {
            this.setLayout(new MigLayout("fillx"));

            JPanel searchPanel = new JPanel(new MigLayout("fillx"));

            JTextField searchField = new JTextField(25);
            searchField.setToolTipText("Search for a remote job!");
            searchPanel.add(searchField, "align center");
            this.add(searchPanel, "align center, wrap");

            List<RemoteOkJob> jobs = this.getJobs();

            JTabbedPane tabbedPane = new JTabbedPane();

            JPanel allListPanel = new JPanel(new MigLayout());
            JScrollPane allListScroller = new JScrollPane(allListPanel,
                                                            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            allListScroller.setSize(new Dimension(100, 100));

            jobs.forEach((job) -> {
                try {
                    JPanel jobPanel = new JPanel(new MigLayout());
                    jobPanel.setSize(new Dimension(100, 100));

                    JPanel logoPanel = new JPanel(new MigLayout());
                    if(nonNull(job.getLogoUrl())) {
                        BufferedImage image = ImageIO.read(Unirest.get(job.getLogoUrl()).asBinary().getBody());
                        if(nonNull(image)) {
                            image = this.resize(image, 30, 30);
                            JLabel imgLabel = new JLabel(new ImageIcon(image));

                            logoPanel.add(imgLabel);
                        }
                    }
                    jobPanel.add(logoPanel, "growy");

                    JPanel infoPanel = new JPanel(new MigLayout("fillx"));
                    JLabel companyLabel = new JLabel("<html><strong>"+ job.getCompany() +"</html>");

                    infoPanel.add(companyLabel, "wrap");
                    infoPanel.add(new JLabel(formatDate(job.getDate())), "wrap");
                    infoPanel.add(new JLabel("<html>"+ this.getDescription(job.getDescription(), " <br> ", 80) +"</html>"));
                    jobPanel.add(infoPanel, "wrap, pushx");

                    allListPanel.add(jobPanel, "wrap");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            });

            tabbedPane.add("Todos", allListScroller);

//        JPanel favPanel = new JPanel(new MigLayout("fillx"));
//        JList favList = new JList();
//        favList.setBackground(Color.decode("#5D6D7E"));
//        favList.setForeground(Color.WHITE);
//        JScrollPane favListScroller = new JScrollPane();
//        favListScroller.setPreferredSize(new Dimension(300, 400));
//        favListScroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//
//        favPanel.add(favListScroller, "align center, wrap");
//
//        tabbedPane.add("Favoritos", favPanel);
            this.add(tabbedPane, "align center");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<RemoteOkJob> getJobs() {
        HttpResponse<JsonNode> remoteOkResponse = null;
        try {
            remoteOkResponse = Unirest.get("https://remoteok.io/api").asJson();
            /*HttpResponse<JsonNode> jsonResponse = Unirest.get("https://remoteok.io/api")
                    .asJson();*/
//            System.out.println(remoteOkResponse.getBody());
        } catch (UnirestException ex) {
            ex.printStackTrace();
        }

        JSONArray arr = remoteOkResponse.getBody().getArray();
        arr.remove(0);
        ObjectMapper mapper = new ObjectMapper();
        List<RemoteOkJob> list = null;
        try {
            list =  mapper.readValue(arr.toString(), mapper.getTypeFactory().constructCollectionType(List.class, RemoteOkJob.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nonNull(list) ? list : new ArrayList();
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        try {
            return Thumbnails.of(img).size(newW, newH).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        return label;
    }

    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        return format.format(date);
    }

    public static String getDescription(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }
}

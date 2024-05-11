package func;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ChallengeIcon extends JPanel {
    JLabel challengeLabel, recordLabel1, recordLabel2;

    public ChallengeIcon(int I, Runnable togglePanelsVisibility, Font customFont) {
        Font cFBig = customFont.deriveFont(Font.BOLD, 30);
        Font cFSmall = customFont.deriveFont(Font.PLAIN, 20);
        setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBorder(BorderFactory.createLineBorder(switch (I){
            case 0 -> Color.decode("#b17135");
            case 1 -> Color.decode("#51b234");
            default -> Color.decode("#d12415");},
            10));
        backgroundPanel.setBackground(switch (I){
            case 0 -> Color.decode("#9d642f");
            case 1 -> Color.decode("#489e2e");
            default -> Color.decode("#ba2012");});
        GridBagConstraints gbc = new GridBagConstraints();

        //Name label
        challengeLabel = new JLabel(Settings.messages.getString("levelTitle"+I), SwingConstants.CENTER);
        challengeLabel.setFont(cFBig);
        challengeLabel.setForeground(Color.decode("#1c1b1b"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(challengeLabel, gbc);

        // Normal image
        int how_many_ach=0;
        for (boolean ach : Settings.achievements)if(ach)how_many_ach++;
        String[] what_images;
        if(how_many_ach>=3)what_images= new String[]{"/challenges/Wisdom.png", "/challenges/Pepe.png", "/challenges/SunTzu.png"};
        else if (how_many_ach>=1)what_images= new String[]{"/challenges/Wisdom.png", "/challenges/Pepe.png", "/challenges/SunTzuL.png"};
        else what_images= new String[]{"/challenges/Wisdom.png", "/challenges/PepeL.png", "/challenges/SunTzuL.png"};
        //for(int i=0; i<what_images.length; i++)what_images[i] = Settings.getPath(what_images[i], "res");
        ImageIcon mainImageIcon = createImageIcon(what_images[I], 250, 300);

        // Brighter version of the image
        String[] ImagesBB = {"/challenges/WisdomBB.png", "/challenges/PepeBB.png", "/challenges/SunTzuBB.png"};
        //for(int i=0; i<ImagesBB.length; i++)ImagesBB[i] = Settings.getPath(ImagesBB[i], "res");
        ImageIcon highlightedImageIcon = createImageIcon(ImagesBB[I], 250, 300);

        JButton imageButton = new JButton(mainImageIcon);
        imageButton.setBorderPainted(false);
        imageButton.setContentAreaFilled(false);
        imageButton.setFocusPainted(false);
        gbc.gridy++;

        if((how_many_ach>=3) || (how_many_ach>=1 && I<=1) || (I==0)) {

            backgroundPanel.add(imageButton, gbc);

            imageButton.addActionListener(e -> {
                if (togglePanelsVisibility != null) {
                    togglePanelsVisibility.run();
                }
            });
            imageButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    imageButton.setIcon(highlightedImageIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    imageButton.setIcon(mainImageIcon);
                }
            });
        } else {
            imageButton.setToolTipText("<html>"
                    +"<style> h1{margin-bottom: 0.2em;} h3{margin-top: 0.2em;} </style>"+
                    "<body style='background-color: "+Settings.background+"; color: "+Settings.foreground+"; font-size: 30pt; font-family: "+Settings.font+";'>"
                    +"<h1>"+Settings.messages.getString("trophiesRequired")+
                    switch (I){
                        case 1 -> " 1";
                        default -> " 3";}
                    +"</h1></body></html>");
            backgroundPanel.add(imageButton, gbc);
        }
        // Trophy icons
        String[] TrophyIcons = {"/icons/trophy.png", "/icons/lackOfTrophy.png"};
        ToolTipManager.sharedInstance().setInitialDelay(0);
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        iconsPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            String has_trophy = TrophyIcons[0];
            if(!Settings.achievements[i+I*3])has_trophy=TrophyIcons[1];
            ImageIcon icon = createImageIcon(has_trophy, 50, 50);
            JLabel iconLabel = new JLabel(icon);

            iconLabel.setToolTipText("<html>"
                    +"<style> h1{margin-bottom: 0.2em;} h3{margin-top: 0.2em;} </style>"+
                    "<body style='background-color: "+Settings.background+"; color: "+Settings.foreground+"; font-size: 30pt; font-family: "+Settings.font+";'>"
                    +"<h1>"+
                    switch (i){
                        case 0 -> Settings.messages.getString("toolTipText0"+I);
                        case 1 -> Settings.messages.getString("toolTipText1");
                        default -> Settings.messages.getString("toolTipText2");}
                    +"</h1><h3>"+
                    switch (i){
                        case 0 -> Settings.messages.getString("toolTipSecText0") +": <"+ Settings.timesRequired[I]+" min";
                        case 1 -> Settings.messages.getString("toolTipSecText1");
                        default -> Settings.messages.getString("toolTipSecText2");}
                    +"</h3>"+
                    "</body></html>");
            iconsPanel.add(iconLabel);
        }
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        backgroundPanel.add(iconsPanel, gbc);

        // Record label
        recordLabel1 = new JLabel(Settings.messages.getString("bestTime") + ":", SwingConstants.CENTER);
        recordLabel1.setFont(cFSmall);
        recordLabel1.setForeground(Color.decode("#1c1b1b"));
        gbc.gridy++;
        backgroundPanel.add(recordLabel1, gbc);

        String czas;
        if(Settings.levelRecords[I] == 6969696969L) czas = Settings.messages.getString("notPlayedYet");
        else {
            long millis = Settings.levelRecords[I];
            long sec = millis/1000%60;
            long min = millis/(60*1000);
            czas = min+"m "+sec+"s";
        }
        recordLabel2 = new JLabel(czas, SwingConstants.CENTER);
        recordLabel2.setFont(cFBig);
        recordLabel2.setForeground(Color.decode("#1a1818"));
        gbc.gridy++;
        backgroundPanel.add(recordLabel2, gbc);

        add(backgroundPanel, BorderLayout.CENTER);
    }

    private ImageIcon createImageIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Couldn't find file: " + path);
            return new ImageIcon();
        }
    }
}
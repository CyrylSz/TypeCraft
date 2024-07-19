package func;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;

public class Menu extends Settings {
    private JFrame frame;
    public Menu() {
        SwingUtilities.invokeLater(() -> {
            frame = createMainFrame();
            applyFullscreen();

            panels = createPanels();

            initMainMenuPanel();
            initSettingsPanel();
            initChallengesMenuPanel();
            initChallengePanels();
            initYoda();

            updateUI(3);

            frame.add(panels[0]);
            if (!fullscreen) frame.setSize(frameSize);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    private JFrame createMainFrame() {
        JFrame frame = new JFrame("TypeCraft");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit(frame);
            }
        });
        return frame;
    }
    private JPanel[] createPanels() {
        JPanel[] panels = new JPanel[NoP];
        for (int i = 0; i < NoP; i++) {
            if(i == 0){
                panels[i] = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                            g.drawImage(bufferedBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
            } else {
                panels[i] = new JPanel(new GridBagLayout());
                panels[i].setOpaque(false);
                panels[i].setVisible(i == 1);
                panels[0].add(panels[i]);
            }
        }
        return panels;
    }
    private void initMainMenuPanel() {
        Font cFBig = new Font(font, Font.BOLD, 60);
        Font cFSmall = new Font(font, Font.PLAIN, 40);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 15, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        titleLabel = new JLabel(messages.getString("TypeCraft"));
        titleLabel.setFont(cFBig);
        panels[1].add(titleLabel, gbc);

        panels[1].add(Box.createVerticalStrut(300));

        for (int i = 0; i < NoMB; i++) {
            if(i==0 || i==1 || i==2){
                gbc.gridy = i + 1;
                menuButtons[i] = new JButton(messages.getString(i==0?"menuButtons"+0:i==1?"menuButtons"+1:"menuButtons"+2));
                menuButtons[i].setFont(cFSmall);
                menuButtons[i].setPreferredSize(new Dimension(350, 80));
                panels[1].add(menuButtons[i], gbc);
            }
            Toolbar = new JPanel();
            Toolbar.setLayout(new BoxLayout(Toolbar, BoxLayout.X_AXIS));
            Toolbar.setOpaque(false);
            if(i==3){
                gbc.gridy = i + 1;
                for(int j=3; j<=4; j++) {
                    menuButtons[j] = new JButton(j == 3 ? "⇤" : "⚙");
                    menuButtons[j].setFont(new Font("SansSerif", Font.BOLD, 45));
                    menuButtons[j].setPreferredSize(new Dimension(80, 80));
                    Toolbar.add(Box.createHorizontalStrut(20));
                    Toolbar.add(menuButtons[j]);
                    Toolbar.add(Box.createHorizontalStrut(20));
                }
                panels[1].add(Toolbar, gbc);
            }
            menuButtons[i].setFocusPainted(false);
            int finalI = i;
            menuButtons[i].addActionListener(e -> mainMenuButtons(finalI));
        }

        panels[0].setLayout(new GridBagLayout());
    }
    private void mainMenuButtons(int buttonIndex) {
        switch (buttonIndex) {
            case 0:
                togglePanelsVisibility(2);
                break;
            case 1, 2:
                yodaJumpScare();
                break;
            case 3:
                confirmExit(frame);
                break;
            case 4:
                togglePanelsVisibility(4);
                break;
            default:
                System.out.println("No such button! :o");
        }
    }
    private void initSettingsPanel() {
        Font cFBig = new Font(font, Font.BOLD, 60);
        Font cFSmall = new Font(font, Font.PLAIN, 40);

        GridBagConstraints gbc = new GridBagConstraints();

        BackButton(gbc, cFSmall, 4, 1);

        settingsLabel = new JLabel(messages.getString("settingsLabel"), SwingConstants.CENTER);
        settingsLabel.setFont(cFBig);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panels[4].add(settingsLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        Boolean[] bools = {fullscreen, infoBars, music};
        for (int i = 0; i <= 2; i++) {
            sToogles[i]= new JToggleButton(bools[i] ? messages.getString("sTSon") : messages.getString("sTSoff"), bools[i]);
            SC[i] = sToogles[i];
        }
        String[][] options = {
                {"English", "Polski"},
                getImageNames().toArray(new String[0]),
                {"Grey", "Pure Black", "Pure White", "Red v1", "Red v2", "Hacker", "Cyan-Gold", "Cyan"},
                loadFontOptions().toArray(new String[0])};

        for (int i = 0; i < NoS; i++) {
            getCustomizableSettings(i, cFSmall, options);
        }

        for (int i = 0; i < NoS; i++) {
            SL[i] = new JLabel(messages.getString("sText"+i));
            SL[i].setFont(cFSmall);
            SL[i].setOpaque(true);
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            panels[4].add(SL[i], gbc);

            gbc.gridx = 1;
            panels[4].add(SC[i], gbc);
            if(i==2 || i==5 || i==7){
                int j = i==2?0:i==5?1:2;
                gbc.gridx = 2;
                explorerButtons[j] = new JButton("\uD83D\uDCC2");
                explorerButtons[j].setFont(new Font("SansSerif", Font.BOLD, 32));
                explorerButtons[j].setFocusPainted(false);
                explorerButtons[j].addActionListener(e -> {
                    String path = switch (j) {
                        case 0 -> getPath("/music", "res");
                        case 1 -> getPath("/backgrounds", "res");
                        default -> getPath("/fonts", "res");
                    };
                    File file = new File(path);
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Path doesn't exist: " + path,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                panels[4].add(explorerButtons[j], gbc);
            }
        }
    }
    private void getCustomizableSettings(int i, Font cFSmall, String[][] options){
        // 0.FullScreen 1.InfoBars 2.Music (Toggle Switches)
        if(i <= 2) {
            JToggleButton toggleButton = (JToggleButton) SC[i];
            toggleButton.setFont(cFSmall);
            toggleButton.setFocusPainted(false);
            toggleButton.setContentAreaFilled(false);
            toggleButton.setOpaque(true);
            toggleButton.addActionListener(e -> {
                boolean isSelected = toggleButton.isSelected();
                toggleButton.setText(isSelected ? messages.getString("sTSon") : messages.getString("sTSoff"));
                if (i == 0) {fullscreen = isSelected;applyFullscreen();}
                else if (i == 1) infoBars = isSelected;
                else {music = isSelected; SoundManager.playMusic(music);}
                saveSettings();
            });
        }
        // 3.SoundEffects (Slider)
        if(i == 3){
            JSlider volumeSlider = new JSlider(0, 100, (int)(soundEffectsVolume*100));
            volumeSlider.setPreferredSize(new Dimension(135, 35));
            volumeSlider.setPaintLabels(true);
            volumeSlider.addChangeListener(e -> {
                soundEffectsVolume = volumeSlider.getValue()/100.0f;
                SoundManager.setVolume(soundEffectsVolume);
                saveSettings();
            });
            SC[i] = volumeSlider;
        }
        // 4.Languages 5.BackgroundImage 6.Colors 7.Font (ComboBoxes)
        if(i >= 4 && i <= 7) {
            JComboBox<String> comboBox = getCustomizationBoxes(i, cFSmall, options);
            SC[i] = comboBox;
        }
    }
    private JComboBox<String> getCustomizationBoxes(int i, Font cFSmall, String[][] options) {
        JComboBox<String> comboBox = new JComboBox<>(options[i - 4]);
        comboBox.setSelectedItem(i == 4 ? language : i == 5 ? backgroundImage : (i == 6 ? color : font));
        comboBox.addActionListener(e -> {
            if (i == 4){ language = (String)comboBox.getSelectedItem(); updateUI(1);}
            else if (i == 5){ backgroundImage = (String)comboBox.getSelectedItem(); updateUI(2);}
            else if (i == 6){ color = (String)comboBox.getSelectedItem(); updateUI(3);}
            else {font = (String)comboBox.getSelectedItem(); updateUI(4);}
            saveSettings();
        });
        comboBox.setFont(cFSmall.deriveFont(18f));
        comboBox.setPreferredSize(new Dimension(135, 45));
        comboBox.setRequestFocusEnabled(false);
        return comboBox;
    }
    public static void initChallengesMenuPanel() {
        Font cFBig = new Font(font, Font.BOLD, 60);
        Font cFSmall = new Font(font, Font.PLAIN, 40);

        GridBagConstraints gbc = new GridBagConstraints();

        BackButton(gbc, cFSmall, 2, 1);

        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.gridwidth = 3;
        InfoBarLabel = new JLabel();
        InfoBarLabel.setFont(cFSmall);
        InfoBarLabel.setOpaque(true);
        InfoBarLabel.setBackground(background);
        InfoBarLabel.setBorder(new LineBorder(additional==null?Color.decode("#346363"):additional, 5));
        int how_many_ach=0;
        for (boolean ach : achievements)if(ach)how_many_ach++;
        InfoBarLabel.setText("<html><div style='background-color:transparent;padding:5px;'>"
                +"\uD83C\uDFC6:"+how_many_ach+" | \uD83D\uDDF2:"+ DaysStreak +"</div></html>");
        panels[2].add(InfoBarLabel, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        challengesLabel = new JLabel(messages.getString("challengesLabel"), SwingConstants.CENTER);
        challengesLabel.setFont(cFBig);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        panels[2].add(challengesLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            ChallengeIcon level = new ChallengeIcon(i, () -> togglePanelsVisibility(5+finalI), cFSmall);
            gbc.gridx = i;
            panels[2].add(level, gbc);
        }
    }
    public static void initChallengePanels() {
        Font cFBig = new Font(font, Font.BOLD, 60);
        Font cFSmall = new Font(font, Font.PLAIN, 40);

        for (int i = 0; i < NoC; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            BackButton(gbc, cFSmall, 5+i, 2);

            challengesLabels[i] = new JLabel(messages.getString("levelTitle"+i), SwingConstants.CENTER);
            challengesLabels[i].setFont(cFBig);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panels[5+i].add(challengesLabels[i], gbc);

            new Challenge(i, gbc, cFSmall);
        }
    }
    private static void BackButton(GridBagConstraints gbc, Font cFSmall, int panels_index, int tPV_index) {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JButton backButton = getjButton(cFSmall, panels_index, tPV_index);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panels[panels_index].add(backButton, gbc);

        // Akcja wspólna dla wielu klawiszy
        AbstractAction returnToMenuAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePanelsVisibility(tPV_index);
            }
        };
        // Mapowanie
        panels[panels_index].getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        panels[panels_index].getActionMap().put("cancel", returnToMenuAction);
        panels[panels_index].getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        panels[panels_index].getActionMap().put("moveLeft", returnToMenuAction);
    }
    private static JButton getjButton(Font cFSmall, int panels_index, int tPV_index) {
        JButton backButton = new JButton("<<");
        backButton.setFocusPainted(false);
        backButton.setFont(cFSmall);
        backButton.addActionListener(e -> togglePanelsVisibility(tPV_index));
        if(panels_index >=4 && panels_index <=7)
            backButton.addActionListener(e -> {
                for (int i = 0; i < NoC; i++) {
                    panels[5 + i].removeAll();
                }
                initChallengePanels();
            });
        if(panels_index ==4)
            backButton.addActionListener(e -> {
                panels[2].removeAll();
                initChallengesMenuPanel();
        });
        return backButton;
    }
    private void yodaJumpScare() {
        if(yodaCount==0) {
            Timer timer1 = new Timer(500, e -> {
                yodaPanel.setVisible(true);
                togglePanelsVisibility(0);
                yodaCount++;
                ((Timer) e.getSource()).stop();
            });
            timer1.setRepeats(false);
            timer1.start();
        } else {
            yodaPanel.setVisible(true);
            togglePanelsVisibility(0);
        }
        Thread imageThread = new Thread(() -> {
            Timer timer2 = new Timer(2000, e -> {
                yodaPanel.setVisible(false);
                togglePanelsVisibility(1);
                ((Timer) e.getSource()).stop();
            });
            timer2.setRepeats(false);
            timer2.start();
        });
        Thread soundThread = new Thread(() -> {
            SoundManager.loadClip(getPath("/yodaJumpScare/Lego_Yoda_Death_Sound.wav", "res"), 3);
            SoundManager.playOneClip(3);
        });
        soundThread.start();
        imageThread.start();
    }
    private static int yodaCount = 0;
    private static final JPanel yodaPanel = new JPanel(new GridBagLayout());
    private void initYoda(){
        try {
            Font cFBig = new Font(font, Font.PLAIN, 60);

            File file = new File(getPath("/yodaJumpScare/yoda.png", "res"));
            BufferedImage image = ImageIO.read(file);
            JLabel labelYoda = new JLabel(new ImageIcon(image));
            labelYoda.setBounds(0, 0, panels[1].getWidth(), panels[1].getHeight());
            GridBagConstraints gbcImage = new GridBagConstraints();
            gbcImage.gridx = 0;
            gbcImage.gridy = 1;
            gbcImage.weightx = 1.0;
            gbcImage.weighty = 1.0;
            gbcImage.fill = GridBagConstraints.BOTH;
            gbcImage.anchor = GridBagConstraints.CENTER;

            JLabel textLabel = new JLabel(messages.getString("notYet"), SwingConstants.CENTER);
            textLabel.setFont(cFBig);
            textLabel.setForeground(Color.WHITE);
            GridBagConstraints gbcText = new GridBagConstraints();
            gbcText.gridx = 0;
            gbcText.gridy = 0;
            gbcText.weightx = 1.0;
            gbcText.weighty = 0;
            gbcText.fill = GridBagConstraints.HORIZONTAL;
            gbcText.anchor = GridBagConstraints.NORTH;

            yodaPanel.setOpaque(false);
            yodaPanel.add(textLabel, gbcText);
            yodaPanel.add(labelYoda, gbcImage);
            yodaPanel.setVisible(false);

            SoundManager.loadClip(getPath("/yodaJumpScare/Lego_Yoda_Death_Sound.wav", "res"), 3);

            panels[0].add(yodaPanel);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error during image display: ", ex);
        }
    }
    public void applyFullscreen() {
        frame.dispose();
        frame.setUndecorated(fullscreen);
        frame.setExtendedState(fullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
        frame.setSize(frameSize);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void confirmExit(JFrame frame) {
        int option = JOptionPane.showConfirmDialog(frame, "            u sure bro? :(", "Exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
package func;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.border.LineBorder;

public class Challenge extends Settings {
    private final ArrayList<Integer> sentenceIndexHistory = new ArrayList<>();
    private int currentSentenceIndex, currentWordIndex = 0, stumbles = 0, correctWordsCount = 0, correctSentencesCount = 0, timerActivationCount = 0;
    private final String[] sentences;
    private final JLabel displayArea, timerLabel;
    private JLabel feedbackLabel, progressLabel, errorLabel, speedLabel, accuracyLabel;
    private final JTextField typingField;
    public static JButton restartButton;
    private long startTime;
    private boolean timerStarted = false;
    private final Timer timer;
    private final int[] NoSLevel = {14, 15, 16};
    private final int I;

    public Challenge(int I, GridBagConstraints gbc, Font customFont) {
        this.I = I;
        this.sentences = loadSentenceArr(language, I);
        currentSentenceIndex = getNewRandomIndex(sentences, sentenceIndexHistory, 0);
        configureDaysStreakPopup(customFont);
        SoundManager.loadClip(getPath("/sound/correct.wav", "res"),0);
        SoundManager.loadClip(getPath("/sound/incorrect.wav", "res"),1);
        SoundManager.loadClip(getPath("/sound/win.wav", "res"),2);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        displayArea = new JLabel();
        displayArea.setFont(customFont.deriveFont(Font.PLAIN, 30));
        displayArea.setOpaque(true);
        displayArea.setBackground(background);
        displayArea.setBorder(new LineBorder(additional==null?Color.decode("#346363"):additional, 10));
        updateDisplayArea();
        gbc.gridy++;
        panels[5+I].add(displayArea, gbc);

        JPanel typingPanel = new JPanel();
        typingPanel.setLayout(new BoxLayout(typingPanel, BoxLayout.X_AXIS));
        typingPanel.setOpaque(false);

        restartButton = new JButton("↺");
        restartButton.setFont(new Font("SansSerif", Font.BOLD, 40));
        restartButton.setForeground(foreground);
        restartButton.setBackground(background);
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(e -> resetGame());

        typingPanel.add(Box.createHorizontalStrut(180));
        typingPanel.add(restartButton);
        typingPanel.add(Box.createHorizontalStrut(10));

        typingField = new JTextField(20);
        typingField.setFont(customFont.deriveFont(Font.PLAIN, 40));
        typingField.setBackground(background);
        typingField.setForeground(foreground);
        typingField.setCaretColor(foreground);
        typingField.setBorder(new LineBorder(additional==null?Color.decode("#346363"):additional, 4));
        typingPanel.add(typingField);

        typingPanel.add(Box.createHorizontalStrut(10));

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(customFont.deriveFont(Font.BOLD, 40));
        timerLabel.setForeground(foreground);
        typingPanel.add(timerLabel);
        typingPanel.add(Box.createHorizontalStrut(100));
        gbc.gridy++;
        panels[5+I].add(typingPanel, gbc);

        gbc.fill = GridBagConstraints.NONE;
        JLabel[] labels = new JLabel[5];
        for(int i=0; i<5; i++){
            labels[i] = new JLabel(
                i==0?" ":
                i==1?messages.getString("completed")+": 0/" + NoSLevel[I]:
                i==2?messages.getString("speed")+": 0.00":
                i==3?messages.getString("accuracy")+": 0%":
                messages.getString("stumbles")+": 0"
            );
            if(i!=0)labels[i].setOpaque(true);
            labels[i].setBackground(background);
            labels[i].setForeground(foreground);
            labels[i].setFont(customFont.deriveFont(Font.BOLD, 40));
            gbc.gridy++;
            if(infoBars)panels[5+I].add(
                switch (i){
                    case 0 -> feedbackLabel = labels[i];
                    case 1 -> progressLabel = labels[i];
                    case 2 -> speedLabel = labels[i];
                    case 3 -> accuracyLabel = labels[i];
                    default -> errorLabel = labels[i];
                },gbc);
        }

        timer = new Timer(1000, e -> updateTimer());
        typingField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!timerStarted && !typingField.getText().trim().isEmpty()) {
                    startTime = System.currentTimeMillis();
                    timerStarted = true;
                    timer.start();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    try {
                        handleTyping();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
    private void resetGame() {
        currentSentenceIndex = getNewRandomIndex(sentences, sentenceIndexHistory, 0);
        currentWordIndex = 0;
        correctSentencesCount = 0;
        correctWordsCount = 0;
        timerActivationCount = 0;
        stumbles = 0;
        updateProgress();
        updateDisplayArea();
        errorLabel.setText(messages.getString("stumbles")+": 0");
        speedLabel.setText(messages.getString("speed")+": 0.00");
        accuracyLabel.setText(messages.getString("accuracy")+": 0%");
        feedbackLabel.setText(" ");
        feedbackLabel.setOpaque(false);
        typingField.setEnabled(true);
        typingField.setText("");
        timerStarted = false;
        timer.stop();
        timerLabel.setText("00:00");
    }
    private void handleTyping() throws InterruptedException {
        String typedText = typingField.getText().trim();
        String[] currentWords = sentences[currentSentenceIndex].split("\\s+");
        if (currentWordIndex < currentWords.length && typedText.equalsIgnoreCase(currentWords[currentWordIndex])) {
            SoundManager.loadClip(getPath("/sound/correct.wav", "res"),0);
            SoundManager.playOneClip(0);
            currentWordIndex++;
            correctWordsCount++;
            updateDisplayArea();
            if (currentWordIndex >= currentWords.length) {
                currentSentenceIndex = getNewRandomIndex(sentences, sentenceIndexHistory, 0);
                currentWordIndex = 0;
                correctSentencesCount++;
                updateProgress();
                if (correctSentencesCount < NoSLevel[I]) {
                    updateDisplayArea();
                } else {
                    if(!LocalDate.now().equals(date) || DaysStreak ==0){
                        date = LocalDate.now();
                        DaysStreak++;
                        SoundManager.loadClip(getPath("/sound/win.wav", "res"),2);
                        SoundManager.playOneClip(2);
                        Timer timer = new Timer(3000, e -> {
                            togglePanelsVisibility(5+I);
                            ((Timer) e.getSource()).stop();
                        });
                        timer.setRepeats(false);
                        timer.start();
                        togglePanelsVisibility(8);
                    }
                    long endTime = System.currentTimeMillis();
                    long time= endTime - startTime;
                    feedbackLabel.setOpaque(true);
                    feedbackLabel.setBackground(background);
                    feedbackLabel.setForeground(foreground);
                    if(levelRecords[I]>time){
                        levelRecords[I]=time;
                        feedbackLabel.setText(messages.getString("finalTime")+": "+formatTime(time)+
                                " "+messages.getString("newRecord"));
                    }
                    else feedbackLabel.setText(messages.getString("finalTime")+": "+formatTime(time));
                    updateAchievements();
                    updateUI(5);
                    typingField.setEnabled(false);
                    timer.stop();
                }
            }
        } else {
            stumbles++;
            errorLabel.setText(messages.getString("stumbles")+": " + stumbles);
            SoundManager.loadClip(getPath("/sound/incorrect.wav", "res"),1);
            SoundManager.playOneClip(1);
        }
        typingField.setText("");
    }
    private void updateDisplayArea() {
        StringBuilder sb = new StringBuilder();
        String[] words = sentences[currentSentenceIndex].split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (i < currentWordIndex) {
                sb.append("<font color='green'>").append(words[i]).append("</font> ");
            } else if (i == (currentWordIndex)) {
                sb.append("<font color='blue'>").append(words[i]).append("</font> ");
            } else {
                sb.append(words[i]).append(" ");
            }
        }
        if (correctSentencesCount < NoSLevel[I]) {
            displayArea.setText("<html><div style='background-color:transparent;width:875px;height:132px;padding:15px;'>"
                    +sb+"</div></html>");
        }
    }
    private void updateProgress() {
        progressLabel.setText(messages.getString("completed")+": " + correctSentencesCount + "/" + NoSLevel[I]);
    }
    private void updateAchievements() {
        if(timesRequired[I]*60*1000 >= levelRecords[I])achievements[I*3] = true;
        if(timesRequired[I]*60*1000-30*1000 >= levelRecords[I])achievements[1+I*3] = true;
        if(stumbles ==0)achievements[2+I*3] = true;
    }
    private void updateTimer() {
        long elapsed = System.currentTimeMillis() - startTime;
        timerLabel.setText(formatTime(elapsed));
        timerActivationCount++;
        if(timerActivationCount%5==0){
            updateSpeed(elapsed);
            updateAccuracy();
        }
    }
    private String formatTime(long millis) {
        long sec = millis/1000%60;
        long min = millis/(60*1000);
        return String.format("%02d:%02d", min, sec);
    }
    private void updateSpeed(long elapsed) {
        double minutes = elapsed / 60000.0;
        double wordsPerMinute = correctWordsCount / minutes;
        speedLabel.setText(String.format(messages.getString("speed")+": %.2f", wordsPerMinute));
    }
    private void updateAccuracy() {
        double acc = (double)correctWordsCount/(correctWordsCount+stumbles);
        int accPercent = (int)(acc*100);
        accuracyLabel.setText(messages.getString("accuracy")+": "+accPercent+"%");
    }
    public static int getNewRandomIndex(String[] tab, ArrayList<Integer> historia, int repeat){
        boolean powtorzenie_slowa = true;
        int index = 0;
        while (powtorzenie_slowa) {
            index = (int)(Math.random()*tab.length);
            if (!historia.contains(index)) {
                historia.add(index);
                powtorzenie_slowa = false;
                if (historia.size() == tab.length - repeat)historia.clear();
            }
        }
        return index;
    }
    public static void configureDaysStreakPopup(Font customFont) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel DSPLabel1 = new JLabel(messages.getString("daysStreak"));
        DSPLabel1.setFont(customFont.deriveFont(Font.PLAIN, 60));
        DSPLabel1.setOpaque(true);
        DSPLabel1.setBackground(background);
        panels[8].add(DSPLabel1, gbc);
        gbc.gridy++;
        JLabel DSPLabel2 = new JLabel();
        DSPLabel2.setFont(customFont.deriveFont(Font.BOLD, 100));
        DSPLabel2.setOpaque(true);
        DSPLabel2.setBackground(background);
        DSPLabel2.setBorder(new LineBorder(additional==null?Color.decode("#346363"):additional, 5));
        DSPLabel2.setText("<html><div style='background-color:transparent;width:250px;height:60px;padding:5px;'>"
                +"+1 \uD83D\uDDF2"+"</div></html>");
        panels[8].add(DSPLabel2, gbc);
    }
    private static String[] txt_to_array(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
        String c = sb.toString();
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher m = pattern.matcher(c);
        sb = new StringBuilder();

        while (m.find()) {
            if (!sb.isEmpty()) {
                sb.append("\", \"");
            }
            sb.append(m.group(1));
        }

        return sb.toString().split("\"\\s*,\\s*\\n*\"");
    }
    public static String[] loadSentenceArr(String lang, int index) {
        String[] arr;
        String l;
        if (lang.equalsIgnoreCase("Polski"))l="pl";
        else l="en";
        switch (index) {
            case 0 -> arr = txt_to_array(getPath("/texts/Wisdom_"+l+".txt", "res"));
            case 1 -> arr = txt_to_array(getPath("/texts/Meme_"+l+".txt", "res"));
            default -> arr = txt_to_array(getPath("/texts/SunTzu_"+l+".txt", "res"));
            }
        return arr;
    }
    //Not used yet:
    public static String[] MonkeTyperArr(String lang) {
        String[] arr;
        if (lang.equalsIgnoreCase("polski")) {
            arr = txt_to_array(getPath("/texts/MonkeTyper_pl.txt", "res"));
        } else {
            arr = txt_to_array(getPath("/texts/MonkeTyper_en.txt", "res"));
        }
        return arr;
    }
}

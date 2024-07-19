package func;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Settings extends JFrame {
    // customizable in-game
    static boolean fullscreen;
    static boolean infoBars;
    static boolean music;
    static float soundEffectsVolume;
    static String language;
    static String backgroundImage;
    static String color;
    static String font;

    // not customizable in-game
    static JPanel Toolbar;
    static byte NoP = 9, NoMB = 5, NoS = 8, NoC = 3;
    // Number of: Panels | Menu Buttons | Settings | Challenges
    static JPanel[] panels = new JPanel[NoP];
    // [0]: Background
    // [1]: Main Menu
    // [2]: ChallengesMenu
    // [3]: Sandbox
    // [4]: Settings
    // [5]: Challenge1
    // [6]: Challenge2
    // [7]: Challenge3
    // [8]: YodaJumpScare
    static JLabel titleLabel, challengesLabel, InfoBarLabel, settingsLabel;
    static JLabel[] SL = new JLabel[NoS];
    static JLabel[] challengesLabels = new JLabel[NoC];
    static JButton[] menuButtons = new JButton[NoMB], explorerButtons = new JButton[3];
    static JToggleButton[] sToogles = new JToggleButton[3];
    static JComponent[] SC = new JComponent[NoS];

    static byte[] timesRequired = {8, 6, 4};
    static long[] levelRecords = new long[NoC];
    static boolean[] achievements = new boolean[NoC *3];
    public static int DaysStreak;

    static Dimension frameSize = new Dimension(1200, 780);
    public static ResourceBundle messages;
    public static LocalDate date;
    public static BufferedImage bufferedBackgroundImage;
    public static Color foreground, background, additional;
    public static final Logger logger = Logger.getLogger("MyLog");

    //Paths
    private static final String settingsFILE = getPath("/settings.txt", "src");
    private static final String backgroundDIR = getPath("/backgrounds", "res");
    private static final String fontDIR = getPath("/fonts", "res");
    private static final String logFILES = getPath("/logs/log%g", "src");

    //Log Handler
    static {
        try {
            FileHandler fh = new FileHandler(logFILES, 1024 * 1024, 5, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Logger error: ", e);
        }
    }

    public Settings() {
        loadSettings();
    }
    public void loadSettings() {
        if (!(new File(settingsFILE)).exists()) {
            fullscreen = false;
            infoBars = true;
            music = false;
            soundEffectsVolume = 0.5f;
            language = "English";
            updateLanguage();
            backgroundImage = "Cyan Mountains";
            updateBackground();
            color = "Grey";
            updateColor();
            font = "Monocraft Nerd Font Complete";
            Arrays.fill(levelRecords, 6969696969L);
            Arrays.fill(achievements, false);
            date = LocalDate.now();
            DaysStreak = 0;
            saveSettings();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(settingsFILE))) {
                fullscreen = Boolean.parseBoolean(reader.readLine());
                infoBars = Boolean.parseBoolean(reader.readLine());
                music = Boolean.parseBoolean(reader.readLine());
                soundEffectsVolume = Float.parseFloat(reader.readLine());
                SoundManager.playMusic(music);
                language = reader.readLine();
                updateLanguage();
                backgroundImage = reader.readLine();
                updateBackground();
                color = reader.readLine();
                updateColor();
                font = reader.readLine();
                for (int i = 0; i < NoC; i++) levelRecords[i] = Long.parseLong(reader.readLine());
                for (int i = 0; i < NoC * 3; i++) achievements[i] = Boolean.parseBoolean(reader.readLine());
                date = LocalDate.parse(reader.readLine());
                if(ChronoUnit.DAYS.between(date, LocalDate.now()) > 1) DaysStreak =0;
                else DaysStreak = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException | NullPointerException e) {
                File settings = new File(settingsFILE);
                if (!settings.delete()) logger.log(Level.INFO, "Error while loading Settings: ", e);
                else loadSettings();
            }
        }
    }
    public static void saveSettings() {
        try (PrintWriter writer = new PrintWriter(settingsFILE)) {
            writer.println(fullscreen);
            writer.println(infoBars);
            writer.println(music);
            writer.println(soundEffectsVolume);
            writer.println(language);
            writer.println(backgroundImage);
            writer.println(color);
            writer.println(font);
            for(long record : levelRecords)writer.println(record);
            for(boolean achievement : achievements)writer.println(achievement);
            writer.println(date);
            writer.println(DaysStreak);
        } catch (IOException e) {
            logger.log(Level.INFO, "Error while writing Settings: ", e);
        }
    }
    public static void updateUI(int ID) {
        switch (ID) {
            case 1 -> updateLanguage();
            case 2 -> {updateBackground();panels[0].repaint();}
            case 3 -> updateColor();
            case 4 -> {for(JPanel panel : panels)updateFont(panel, font);}
            case 5 -> {panels[2].removeAll();Menu.initChallengesMenuPanel();}
        }
        saveSettings();
    }
    private static void updateLanguage() {
        Locale currentLocale;
        Locale.Builder builder = new Locale.Builder();
        switch (language){
            case "Polski"-> currentLocale = builder.setLanguage("pl").setRegion("PL").build();
            case null, default -> currentLocale = builder.setLanguage("en").setRegion("US").build();
        }
        messages = ResourceBundle.getBundle("Language", currentLocale);
        if(titleLabel!=null)updateTexts();
    }
    private static void updateTexts() {
        titleLabel.setText(messages.getString("TypeCraft"));
        for (int i = 0; i < 3; i++)menuButtons[i].setText(messages.getString("menuButtons"+i));

        settingsLabel.setText(messages.getString("settingsLabel"));
        for (int i = 0; i < NoS; i++){
            SL[i].setText(messages.getString("sText"+i));
        }

        Boolean[] bools = {fullscreen, infoBars, music};
        for (int i = 0; i <= 2; i++) {
            String key = bools[i] ? "sTSon" : "sTSoff";
            String text = messages.getString(key);
            sToogles[i].setText(text);
            SC[i] = sToogles[i];
        }
    }
    private static void updateBackground() {
        try {
            bufferedBackgroundImage = ImageIO.read(new File("res/backgrounds/"+backgroundImage+".png"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while loading background image: ", e);
        }
    }
    public static List<String> getImageNames() {
        File dir = new File(backgroundDIR);
        return Arrays.stream(Objects.requireNonNull(dir.list((dir1, name) -> name.toLowerCase().endsWith(".png"))))
                .map(name -> name.substring(0, name.length() - 4))
                .collect(Collectors.toList());
    }
    private static void updateColor() {
        foreground = null;
        background = null;
        additional = null;
        boolean normal = false;

        switch (color) {
            case "Pure Black" -> {foreground = Color.decode("#000000"); background = Color.decode("#FFFFFF");
                additional = Color.decode("#F0F0F0");}
            case "Pure White" -> {foreground = Color.decode("#FFFFFF"); background = Color.decode("#000000");
                additional = Color.decode("#171717");}
            case "Red v1" -> {foreground = Color.decode("#4C0D09"); background = Color.decode("#BA8884");
                additional = Color.decode("#DEA29E");}
            case "Red v2" -> {foreground = Color.decode("#F5271A"); background = Color.decode("#140302");
                additional = Color.decode("#4D0D08");}
            case "Hacker" -> {foreground = Color.decode("#19B319"); background = Color.decode("#041F04");
                additional = Color.decode("#062E06");}
            case "Cyan-Gold" -> {foreground = Color.decode("#8E7F42"); background = Color.decode("#094740");
                additional = Color.decode("#631E5F");}
            case "Cyan" -> {foreground = Color.decode("#6DE6D1"); background = Color.decode("#206F73");
                additional = Color.decode("#3D6F73");}
            case null, default -> normal = true;
        }
        if(normal)UIManager.put("Button.select", null);
        else UIManager.put("Button.select", additional);
        for (JPanel panel : panels) {
            if(panel!=null)for(Component comp : panel.getComponents()) {
                comp.setForeground(foreground);
                comp.setBackground(background);
            }
        }
        if(menuButtons[3]!=null){
            menuButtons[3].setBackground(background);
            menuButtons[3].setForeground(foreground);
            menuButtons[4].setBackground(background);
            menuButtons[4].setForeground(foreground);
        }
    }
    private static void updateFont(Component component, String fontFamily) {
        Font currentFont = component.getFont();
        if (currentFont != null) {
            Font newFont = new Font(fontFamily, currentFont.getStyle(), currentFont.getSize());
            component.setFont(newFont);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateFont(child, fontFamily);
            }
        }
        Challenge.restartButton.setFont(new Font("SansSerif", Font.BOLD, 40));
        for(int i=0; i<3; i++)explorerButtons[i].setFont(new Font("SansSerif", Font.BOLD, 40));
        menuButtons[3].setFont(new Font("SansSerif", Font.BOLD, 45));
        menuButtons[4].setFont(new Font("SansSerif", Font.BOLD, 45));
    }
    public static List<String> loadFontOptions() {
        List<String> customFonts = loadCustomFonts();
        List<String> systemFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        return Stream.concat(customFonts.stream(), systemFonts.stream().sorted()).distinct().collect(Collectors.toList());
    }
    private static List<String> loadCustomFonts() {
        try (Stream<Path> paths = Files.walk(new File(fontDIR).toPath())) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".ttf"))
                    .map(path -> tryLoadFont(path.toFile()))
                    .filter(Objects::nonNull)
                    .map(Font::getName)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.INFO, "Error while loading Fonts: ", e);
            return Collections.emptyList();
        }
    }
    private static Font tryLoadFont(File file) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, file);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            logger.log(Level.INFO, "Error while loading Fonts: ", e);
            return null;
        }
    }
    public static void togglePanelsVisibility(int visiblePanelIndex) {
        for (JPanel panel : panels) {
            if(panel!=panels[0])panel.setVisible(false);
        }
        panels[visiblePanelIndex].setVisible(true);
    }
    public static String getPath(String relPath, String type) {
        String userDir = System.getProperty("user.dir");
        if (userDir.contains("out/production")) return userDir + relPath;
        else if (type.equalsIgnoreCase("src")) return userDir + "/src" + relPath;
        else return userDir + "/res" + relPath;
    }
}
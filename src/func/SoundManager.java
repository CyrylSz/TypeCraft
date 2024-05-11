package func;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class SoundManager {
    private static float volume = Settings.soundEffectsVolume;
    private static final List<Clip> clips = new ArrayList<>();
    public static final Clip[] typingClips = new Clip[4];
    private static final File musicDirectory;
    private static File[] musicFiles;
    private static volatile boolean playMusic;
    private static final List<Clip> activeClips = new ArrayList<>();
    private static Thread musicThread;

    static {
        musicDirectory = new File(Settings.getPath("/music", "res"));
        if (!musicDirectory.exists() || !musicDirectory.isDirectory()) {
            throw new IllegalArgumentException("Invalid Path!");
        }
        loadMusicFiles();
    }
    public static void setVolume(float newVolume) {
        volume = newVolume;
        for (Clip clip : clips) {
            setClipVolume(clip, volume);
        }
    }
    private static void setClipVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
    private static void loadMusicFiles() {
        musicFiles = musicDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new IllegalStateException("No music .wav files found!");
        }
        Arrays.sort(musicFiles, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
    }
    public static void loadClip(String filePath, int index) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            setClipVolume(clip, volume);
            typingClips[index] = clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Settings.logger.log(Level.SEVERE, "Error: ", e);
        }
    }
    public static void playOneClip(int index) {
        try {
            typingClips[index].start();
        } catch (Exception e) {
            Settings.logger.log(Level.INFO, "Error: ", e);
        }
    }
    public static void playAddedClips() {
        try {
            for (Clip clip : clips) {
                if (clip != null) {
                    clip.start();
                    try {
                        Thread.sleep(clip.getMicrosecondLength() / 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        clip.stop();
                        continue;
                    }
                    clip.stop();
                    clip.close();
                }
            }
        } finally {
            clips.clear();
        }
    }
    public static void playMusic(boolean shouldPlay) {
        playMusic = shouldPlay;
        if (playMusic) {
            if (musicThread == null || !musicThread.isAlive()) {
                reloadClips();
                musicThread = new Thread(SoundManager::run);
                musicThread.start();
            }
        } else {
            stopAndClearAllClips();
            if (musicThread != null) {
                musicThread.interrupt();
                musicThread = null;
            }
        }
    }
    private static void run() {
        while (!Thread.currentThread().isInterrupted() && playMusic) {
            for (File file : musicFiles) {
                if (!playMusic) break;
                Clip music = SoundManager.addMusic(file.getAbsolutePath());
                if (music != null) {
                    synchronized (activeClips) {
                        activeClips.add(music);
                    }
                }
            }
            SoundManager.playAddedClips();
            SoundManager.clearAddedClips();
        }
    }
    public static Clip addMusic(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            setClipVolume(clip, volume);
            clips.add(clip);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Settings.logger.log(Level.SEVERE, "Error loading clip: " + filePath, e);
            return null;
        }
    }
    private static void reloadClips() {
        stopAndClearAllClips();
        loadMusicFiles();
    }
    public static void clearAddedClips() {
        clips.clear();
    }
    private static void stopAndClearAllClips() {
        synchronized (activeClips) {
            for (Clip clip : activeClips) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
            }
            activeClips.clear();
        }
    }
}
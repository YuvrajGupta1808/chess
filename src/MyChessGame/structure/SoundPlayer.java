package MyChessGame.structure;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    public static void playSound(String soundFile) {
        try {
            URL soundURL = SoundPlayer.class.getResource(soundFile);
            if (soundURL != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Increase volume by setting a positive gain value
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(6.0f); // Increase volume by 6 decibels

                clip.start();
            } else {
                System.out.println("Sound file not found: " + soundFile);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

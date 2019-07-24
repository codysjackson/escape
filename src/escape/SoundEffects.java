/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundEffects {
  public  void playCrushSound() {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundEffects.class.getResourceAsStream( "resources/Crush.wav" ));
          clip.open( inputStream );
          clip.start(); 
        } catch ( Exception e ) {
          System.err.println( e.getMessage());
        }
      }
    }).start();
  }
  
  public  void playSquishedSound() {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundEffects.class.getResourceAsStream( "resources/Squished.wav" ));
          clip.open( inputStream );
          clip.start(); 
        } catch ( Exception e ) {
          System.err.println( e.getMessage());
        }
      }
    }).start();
  }
}

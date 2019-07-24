/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.io.IOException;


public class StopButton extends Sprite {
  
  public StopButton( int x, int y ) throws IOException {
    super( x, y );
    initStopButton();
  }
  
  public void initStopButton() throws IOException {
    loadImage( "resources/Button.png" );  
    getImageDimensions();
  }
  
}

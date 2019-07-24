/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.io.IOException;


public class Wall extends Sprite {
  public Wall( int x, int y ) throws IOException {
    super( x, y );
    initWall();
  }
  
  private void initWall() throws IOException {
    loadImage( "resources/Wall.png" );  
    getImageDimensions();
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.scene.shape.Line;

class FallingBlock extends Wall {
  private int weight, height;
  private boolean jumped;
  
  public FallingBlock( int x, int y, int Type ) throws IOException {
    super( x, y );
    initWall( Type );
  }
  
  private void initWall( int Type ) throws IOException {
    jumped = false;
    
    switch( Type ) {
      case 1: 
        cardBox();
        break;
      
      case 2: 
        woodBox();
        break;
        
      case 3: 
        metalBox();
        break;
        
      case 4:
        stoneBox();
        break;
    } 
  }
  
  public void checkJump( Player player ) {
    if( jumped == false ) {
      Rectangle playerRect = new Rectangle(player.getX(), player.getY() + 40,
              player.getImage().getWidth(), player.getImage().getHeight());
      Rectangle fallenBlockRect = getBounds();
      if(playerRect.intersects( fallenBlockRect )) {
        player.setX( getX()+ 20 );
        player.setY( getY() - 80 );
        jumped = true;
      }
    }
  }
  
  private void cardBox() throws IOException {
    loadImage( "resources/CardBox.png" );
    getImageDimensions();
    weight = 1;
  }
  
  private void woodBox() throws IOException {
    loadImage( "resources/WoodBox.png" );
    getImageDimensions();
    weight = 2;
  }
  
  private void metalBox() throws IOException {
    loadImage( "resources/MetalBox.png" );
    getImageDimensions();
    weight = 3;
  }
  
  private void stoneBox() throws IOException {
    loadImage( "resources/StoneBox.png" );
    getImageDimensions();
    weight = 4;
  }
  
  public void updatePosition() {
    y = y + 5;
  }
  
  @Override
  public BufferedImage getImage() {
    return image;
  }
  
  protected int getWeight(){
    return weight;
  }
}

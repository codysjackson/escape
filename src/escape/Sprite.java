/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

  protected int x;
  protected int y;
  protected int frame;
  protected int width;
  protected int height;
  protected boolean isVisible;
  protected boolean hasCollided;
  protected BufferedImage image;

  public Sprite( int x, int y ) {
    this.frame = 0;
    this.x = x;
    this.y = y;
    isVisible = true;
    hasCollided = false;
  }

  protected void loadImage( String imageName ) throws IOException {
    image = ImageIO.read( Sprite.class.getResource( imageName ));
  }

  protected void getImageDimensions() {
    width = image.getWidth( null );
    height = image.getHeight( null );
  }    

  public Image getImage() {
    return image;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX( int x ) {
    this.x = x;
  }

  public void setY( int y ) {
    this.y = y;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible( Boolean visible ) {
    this.isVisible = visible;
  }

  public Rectangle getBounds() {
    return new Rectangle( x, y, width , height );
  }   
}
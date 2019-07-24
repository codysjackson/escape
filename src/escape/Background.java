/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class Background extends JComponent {
  BufferedImage background;
  
  public Background( BufferedImage background ) throws IOException {
    this.background = background;
    initBackground();
  }
  
  private void initBackground() throws IOException {
    loadImage( "resources/Background.png" );
  }
  
  protected void loadImage( String imageName ) throws IOException {
    background = ImageIO.read( Background.class.getResource( imageName ));
  }
  
  @Override
  protected void paintComponent( Graphics graphics ) {
    super.paintComponent( graphics );
    graphics.drawImage( background, 0, 0, this );
  }
}

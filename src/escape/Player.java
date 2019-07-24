/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Sprite {

  private int spawnX, spawnY;
  private int heightPos;
  private double dx, dy;
  private double prevX, prevY;
  private double gravity;
  private double maxY;
  private int direction;
  private boolean isFalling;
  private int[] rowPos = new int[13];
  BufferedImage[] playerImages;

  public Player( int x, int y ) throws IOException {
      super( x, y );
      setSpawn( x, y );
      initPlayer();
  }

  private void initPlayer() throws IOException {
    direction = 0;
    loadImage( "resources/Lazarus_right_strip7.png" );
    maxY = 320;
    gravity = 1;
    isFalling = false;
    getImageDimensions();
  }

  private int getDirection() {
    return direction;
  }
  
  public void resetIsFalling() {
    isFalling = true;
  }
  
  private void setDirection( int Direction ) {
    this.direction = Direction;
  }
  
  protected void loadImageStanding( String source ) throws IOException {
    super.loadImage(source);
    playerImages = new BufferedImage[ 1 ];
    for( int counter = 0; counter < 7; counter++ ) {
      playerImages[ counter ] = image;
    }
  }
  
  @Override
  protected void loadImage( String source ) throws IOException {
    super.loadImage( source );

    if( image == null ){
      System.out.println( "image is null" );
    }

    playerImages = new BufferedImage[ 7 ];

    int w = image.getWidth( null ) / 7;
    int h = image.getHeight( null );
    int xCoord = 0;
    int yCoord = 0;

    for( int counter = 0; counter < 7; counter++ ) {
      playerImages[ counter ] = image.getSubimage( xCoord, yCoord, w, h );
      xCoord +=w;
    }
  }
  
  @Override
  public BufferedImage getImage() {
    if( frame < 7 ) {
      return playerImages[ frame ];
    } else {
      return null;
    }
  }
  
  public void nextFrame() throws InterruptedException {
    if( frame < playerImages.length ) {
      frame++;
    }
    if( frame == playerImages.length - 1 ) {
      frame = 0;
    }
  }
  
  public void checkPlayerCollisionWall( Wall[][] wall ) {
    Rectangle playerLazarus = new Rectangle( getX(), getY(),
            getImage().getWidth( null ) - 50, getImage().getHeight( null ));
    Rectangle[][] wallHitbox = new Rectangle[ 100 ][ 100 ];
    
    for( int i = 0; i < wall.length; i++ ) {
      for( int j = 0; j < wall[ i ].length; j++ ) {
        if( wall[ i ][ j ] != null ) {
          wallHitbox[ i ][ j ] = wall[ i][ j ].getBounds();
          if( playerLazarus.intersects( wallHitbox[ i ][ j ] )) {
            x = ( int ) prevX;
            y = ( int ) prevY;
          }
        }
      }
    }
  }
  
  public void checkPlayerCollisionBox( ArrayList<FallingBlock> fallenBlocks, int[] column ) {
    for( int counter = 0; counter < fallenBlocks.size(); counter++ ) {
      FallingBlock box = fallenBlocks.get( counter );
    
      Rectangle playerLazarus = new Rectangle( getX(), getY(),
              getImage().getWidth( null ) - 50, getImage().getHeight( null ));
      Rectangle[][] wallHitbox = new Rectangle[ 100 ][ 100 ];
      
      for( int counterI = 0; counterI < 100; counterI++ ) {
        for( int counterJ = 0; counterJ < 100; counterJ++ ) {
          wallHitbox[ counterI ][ counterJ ] = box.getBounds();
          if( playerLazarus.intersectsLine( wallHitbox[ counterI ][ counterJ ].getMaxX() - 6, wallHitbox[ counterI ][ counterJ ].getMinY(),
                  wallHitbox[ counterI ][ counterJ ].getMaxX() - 6, wallHitbox[ counterI ][ counterJ ].getMaxY()) || 
                  playerLazarus.intersectsLine( wallHitbox[ counterI ][ counterJ ].getMinX() + 5, wallHitbox[ counterI ][ counterJ ].getMinY(),
                  wallHitbox[ counterI ][ counterJ ].getMinX() + 5, wallHitbox[ counterI ][ counterJ ].getMaxY())) {
            if( column[( int )wallHitbox[ counterI ][ counterJ  ].getX()/40 ] == heightPos ) {
//              System.out.println(column[( int )wallHitbox[ counterI ][ counterJ  ].getX()/40 ]);
              x = ( int ) prevX;
              dy = -5;
            } else if( column[( int )wallHitbox[ counterI ][ counterJ ].getX()/40 ] > heightPos ) {
              x = ( int ) prevX;
              y = ( int ) prevY;
            }
          } else if( playerLazarus.intersectsLine( wallHitbox[ counterI ][ counterJ ].getMinX() + 5, wallHitbox[ counterI ][ counterJ ].getMinY(),
                  wallHitbox[ counterI ][counterJ ].getMaxX() - 6, wallHitbox[ counterI ][ counterJ ].getMinY())) {
            y = ( int ) prevY;
          }
        }
      }
    }
  }
  
  public boolean checkCollisionButton(StopButton button1, StopButton button2) {
    Rectangle playerLazarus = new Rectangle( getX(), getY(),
              getImage().getWidth( null ) - 50, getImage().getHeight( null ));
    Rectangle firstButton = new Rectangle( button1.getBounds());
    Rectangle secondButton = new Rectangle( button2.getBounds());
    
    if( playerLazarus.intersects( firstButton ) || playerLazarus.intersects( secondButton )) {
      return true;
    } else {
      return false;
    }
  }
  
  private int getRow( int x ){
        int result = ( x / 40 );
        return result;
    }
  
  public void move() {
    prevX = x;
    prevY = y;
    
    x += dx;
    y += dy;  
    
    if ( x < 0 ) {
      x = 0;
    }
    if (y < 0) {
      y = 0;
    } else if ( y > maxY )
      y =(int) maxY;

    if( y > 0 && y < 40 ) {
      heightPos = 8;
    } else if( y > 40 && y < 80 ) {
      heightPos = 7;
    } else if( y > 80 && y < 120 ) {
      heightPos = 6;
    } else if( y > 120 && y < 160 ) {
      heightPos = 5;
    } else if( y > 160 && y < 200 ) {
      heightPos = 4;
    } else if( y > 200 && y < 240 ) {
      heightPos = 3;
    } else if( y > 240 && y < 280 ) {
      heightPos = 2;
    } else if( y > 280 && y < 320 ) {
      heightPos = 1;
    }
  }



  public void fall() {
    if( isFalling ) {
      dy += gravity; 
    } 
  }
  
  private void setSpawn( int x, int y ) {
    this.spawnX = x;
    this.spawnY = y;
  }

  public void resetGame() {
    this.x = spawnX;
    this.y = spawnY;
  }

  public void keyPressed( KeyEvent e ) throws IOException {
    int key = e.getKeyCode();
    
    if( key == KeyEvent.VK_LEFT ) {
      dx = -5;
      setDirection( 1 );
    }

    if( key == KeyEvent.VK_RIGHT ) {
      dx = 5;
      setDirection( 2 );
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_LEFT) {
      dx = 0;
      dy = 0;
    }

    if (key == KeyEvent.VK_RIGHT) {
      dx = 0;
      dy = 0;
    }
  }
}
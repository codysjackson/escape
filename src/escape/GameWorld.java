/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class GameWorld extends JPanel implements ActionListener {
  private final int DELAY = 30;
  private Timer timer;
  private final BufferedImage background;
  private Player player;
  private StopButton button1, button2;
  public boolean activeFallingBlock;
  private FallingBlock falling = null;
  private FallingBlock nextBlock = null;
  private boolean gameOver;
  private JLabel label;
  Wall[][] wall = new Wall[ 100 ][ 100 ];
  int[] column = new int[ 16 ];
  ArrayList< FallingBlock > fallenBlocks = new ArrayList<>();
  int blockWidth;

  public GameWorld() throws IOException, URISyntaxException {
    this.background = ImageIO.read( GameWorld.class.getResource( "resources/Background.png" ));
    initBoard();
  }

  private void initBoard() throws IOException, URISyntaxException {
    addKeyListener(( KeyListener ) new TAdapter());
    setFocusable( true );
    setBackground( Color.BLACK );
    setDoubleBuffered( true );

    timer = new Timer( DELAY, this );
    timer.start();
    gameOver = false;
    player = new Player( 350, 7 * 40 );
    button1 = new StopButton( 0, 4 * 40 );
    button2 = new StopButton( 15 * 40, 4 * 40 );

    setLayout( new BorderLayout());
    label = new JLabel( "" );
    add( label, BorderLayout.CENTER );
    
    URL resource = GameWorld.class.getResource( "resources/level1.txt" );
    File level = new File( resource.toURI());
    BufferedReader reader = new BufferedReader( new FileReader( level ));
    String inputStream;

    int i = 0;
    int j = 0;
    int index = 0;

    while(( inputStream = reader.readLine()) != null ) {
      while( index < inputStream.length()) {
        switch( inputStream.charAt( index )) {
          case '0':
            wall[ j ][ i ] = null;
            i++;
            break;
          case '1':
            wall[ j ][ i ] = new Wall( i * 40, j * 40 );
            blockWidth = wall[ j ][ i ].getImage().getWidth( null );
            i++;
            break;
          default:
            wall[ j ][ i ] = null;
            i++;
            break;
        }
        index++;
      }
      i = 0;
      index = 0;
      j++;
    }
  }

  @Override
  public void paintComponent( Graphics g ) {
    super.paintComponent( g );
    paintBackground( g );
    paintFallingBlock( g );
    doDrawing( g );
    paintFallenBlock( g );

    Toolkit.getDefaultToolkit().sync();
  }

  public void paintFallingBlock( Graphics g ) {
    Graphics2D g2d = ( Graphics2D ) g;
    if( activeFallingBlock )
      g2d.drawImage( falling.getImage(), falling.getX(), falling.getY(), this );
  }

  public void paintFallenBlock( Graphics g ) {
    Graphics2D g2d = ( Graphics2D ) g;
    for( FallingBlock fb : fallenBlocks ) {
      FallingBlock b = fb;
      g2d.drawImage( b.getImage(), b.getX(), b.getY(), this );
    }
  }

  private void doDrawing( Graphics g ) {
    Graphics2D g2d = ( Graphics2D ) g;
    g2d.drawImage( player.getImage(), player.getX(), player.getY(), this );
    g2d.drawImage( button1.getImage(), button1.getX(), button1.getY(), this );
    g2d.drawImage( button2.getImage(), button2.getX(), button2.getY(), this );
  }

  public void paintBackground( Graphics g ) {
    Graphics2D g2d = ( Graphics2D ) g;
    g2d.drawImage( background, 0, 0, this );

    for( int i = 0; i < wall.length; i++ ) {
      for( int j = 0; j < wall[ i ].length; j++ ) {
        if( wall[ i ][ j ] != null ) {
          Wall wall = this.wall[ i ][ j ];
          g2d.drawImage( wall.getImage(), wall.getX(), wall.getY(), this );
        }
      }
    }
  }


  @Override
  public void actionPerformed( ActionEvent e ) {
    if( player.checkCollisionButton(button1, button2)) {
      try {
        timer.wait( 10 );
      } catch ( Exception ex ) {}
      timer.stop();
      displayGameOver();
    }
    
    try {
      updatePlayer();

      checkBlock();
      if( activeFallingBlock ) {
        falling.updatePosition();
      }
      if( generateController()) {
        generateBlock();
      }
      if( checkFallingBlock())
        restartLevel();
    } catch( InterruptedException | IOException ex ) {
        Logger.getLogger( GameWorld.class.getName()).log( Level.SEVERE, null, ex );
    }
    repaint();
  }

  private void updatePlayer() throws InterruptedException {
    player.move();
    player.checkPlayerCollisionWall( wall );
    player.checkPlayerCollisionBox( fallenBlocks, column );
    player.resetIsFalling();
    player.fall();
  }

  protected boolean generateController() throws IOException {
    Random rand = new Random();
    int randomNum = rand.nextInt(( 10 - 1 ) + 1 ) + 1;

    if( activeFallingBlock ) {
      return false;
    } else if( randomNum < 3 )
      return true;
    else
      return false;
  }

  private void generateBlock() throws IOException {
    Random rand = new Random();

    int blockType = rand.nextInt(( 4 - 1 ) + 1 ) + 1;
    falling = new FallingBlock( getColumn( player.getX()), 0, blockType );
    column[ getColumn( player.getX()) / 40 ]++;
    activeFallingBlock = true;
  }

  private void checkBlock() {
    if(activeFallingBlock) {
      for(int i = 0; i < wall.length; i++) {
        for(int j = 0; j < wall[i].length; j++) {
          Wall wall = this.wall[i][j];

          if(wall != null) {
            Rectangle fR = falling.getBounds();
            fR.setBounds(( int ) fR.getX(), ( int ) fR.getY(), ( int ) fR.getWidth(), 
                    ( int ) fR.getHeight() + 5 );
            if( fR.intersectsLine( wall.getX() + 5, wall.getY()+2,
                    wall.getX() + wall.getImage().getWidth( null ) - 5, wall.getY() + 2 )) {
              fallenBlocks.add( falling );
              falling = null;
              activeFallingBlock = false;
              return;
            }
          }
        }
      }
      
      for( int i = 0; i < fallenBlocks.size(); i++ ) {
        FallingBlock box = fallenBlocks.get( i );
        if( box != null ) {
          Rectangle fb = box.getBounds();
          Rectangle fallenRect = falling.getBounds();
          fallenRect.setBounds(( int )fallenRect.getX(), ( int )fallenRect.getY(),
                  ( int )fallenRect.getWidth(), ( int )fallenRect.getHeight() + 5 );
          if( fb.intersects( fallenRect )) {
            if( falling.getWeight() > box.getWeight()) {
              fallenBlocks.remove( i );
              column[ getColumn( box.getX()) / 40 ]--;
              SoundEffects effect = new SoundEffects();
              effect.playCrushSound();
            } else {
              fallenBlocks.add( falling );
              falling = null;
              activeFallingBlock = false;
              return;
            }
          }
        }
      }
    }
  }

  public boolean checkFallingBlock() {
    if( activeFallingBlock ) {
      Rectangle fR = falling.getBounds();
      Rectangle pR = new Rectangle( player.getX(), player.getY() + 40,
              player.getImage().getWidth() - 50, player.getImage().getHeight());
      if( fR.intersects( pR )) {
        return true;
      }
    }
    return false;
  }

  private int getColumn( int x ){
    int result = ( x / blockWidth );
    return result * blockWidth;
  }

  private void displayGameOver(){
    label.setFont( new Font( "Impact", Font.BOLD, 100 ));
    label.setHorizontalAlignment( SwingConstants.CENTER );
    label.setForeground( Color.WHITE );
    label.setText( "YOU WIN" );
  }

  public void restartLevel(){
    SoundEffects effect = new SoundEffects();
    effect.playSquishedSound();
    player.resetGame();
    fallenBlocks.clear();
    falling = null;
    activeFallingBlock = false;
    for( int i = 0; i < column.length; i++ ) {
      column[ i ] = 0;
    }
  }

  private class TAdapter extends KeyAdapter {
    @Override
    public void keyReleased( KeyEvent e ) {
      player.keyReleased( e );
    }

    @Override
    public void keyPressed( KeyEvent e ) {
      try {
        player.keyPressed( e );
      } catch ( IOException ex ) {
        Logger.getLogger( GameWorld.class.getName()).log( Level.SEVERE, null, ex );
      }
    }
  }
}
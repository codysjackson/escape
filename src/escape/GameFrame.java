/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escape;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class GameFrame extends JFrame {

  public GameFrame() throws IOException, URISyntaxException {
    initUI();
  }

  private void initUI() throws IOException, URISyntaxException {
    add( new GameWorld());

    setSize( 656, 519 );
    setResizable( true );

    setTitle( "escape" );
    setLocationRelativeTo( null );
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
  }

  public static void main( String[] args ) {

    EventQueue.invokeLater( new Runnable() {
      @Override
      public void run() {
        GameFrame ex = null;
        try {
          ex = new GameFrame();
        } catch ( IOException ex1 ) {
          Logger.getLogger( GameFrame.class.getName()).log( Level.SEVERE, null, ex1 );
        } catch ( URISyntaxException ex1 ) {
          Logger.getLogger( GameFrame.class.getName()).log( Level.SEVERE, null, ex1 );
        }
          ex.setVisible( true );
        }
    });
  }
}
package jpkmn.exe.launcher;

import javax.swing.*;

import jpkmn.img.ImageFinder;

public class Launcher extends JFrame {
  public Launcher() {
    setTitle("JPokemon (ver 0.1)");
    setIconImage(ImageFinder.find("main-icon"));
    setSize(720, 457); // WIDTH, HEIGHT
    setUndecorated(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    pane = new JLayeredPane();

    JLabel img = new JLabel(new ImageIcon(ImageFinder.find("splash")));
    img.setBounds(10, 10, 700, 437);
    pane.add(img, new Integer(-1));

    // Load Button
    PlayButton play = new PlayButton(this);
    play.setBounds(550, 60, 110, 30); // 10px border on all sides
    pane.add(play, new Integer(0));

    // New Game Button
    ConnectButton connect = new ConnectButton(this);
    connect.setBounds(550, 100, 110, 30);
    pane.add(connect, new Integer(0));

    // Exit Game Button
    QuitButton quit = new QuitButton(this);
    quit.setBounds(550, 140, 110, 30);
    pane.add(quit, new Integer(0));

    add(pane);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JLayeredPane pane;
  private static final long serialVersionUID = 1L;
}
package com.jpokemon.launcher;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import com.jpokemon.util.ui.ImageLoader;

public class Launcher extends JFrame {
  public Launcher() {
    setTitle("JPokemon (dev build)");
    setIconImage(ImageLoader.find("main-icon").getImage());
    setSize(720, 457); // WIDTH, HEIGHT
    setUndecorated(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    pane = new JLayeredPane();

    JLabel img = new JLabel(ImageLoader.find("splash"));
    img.setBounds(10, 10, 700, 437);
    pane.add(img, -1);
    pane.add(new PlayButton(this), 0);
    pane.add(new ConnectButton(this), 0);
    pane.add(new QuitButton(this), 0);
    add(pane);

    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JLayeredPane pane;
  private static final long serialVersionUID = 1L;
}
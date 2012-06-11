package jpkmn.exe.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ConnectButton extends JButton implements ActionListener {
  private int a; // Flag to do work

  public ConnectButton(Launcher l) {
    super("Connect");

    _launcher = l;
    
    setFocusable(false);
    setBorderPainted(false);
    setBounds(550, 100, 110, 30);
    setBackground(new Color(77, 206, 77));

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    _launcher.dispose();
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}

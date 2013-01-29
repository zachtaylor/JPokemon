package jpkmn.exe.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ConnectButton extends JButton implements ActionListener {
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
    JOptionPane.showMessageDialog(_launcher,
        "Hello! I have not been implemented yet!", "Oops!",
        JOptionPane.PLAIN_MESSAGE);
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}
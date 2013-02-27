package com.jpokemon.inbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jpokemon.JPokemonButton;

public class MessagePanel extends JPanel {
  public MessagePanel(InboxMenu parent, String... message) {
    _parent = parent;

    JPanel p = new JPanel();
    for (int i = 0; i < message.length; ++i)
      p.add(new JLabel(message[i]));
    add(p);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

    add(new JPanel());

    JButton close = new JPokemonButton("X");
    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MessagePanel.this.remove();
      }
    });
    add(close);
  }

  private void remove() {
    _parent.remove(this);
  }

  private InboxMenu _parent;

  private static final long serialVersionUID = 1L;
}
package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import jpkmn.Driver;

public class MessageWindow extends JFrame {
  private JRootPane root;
  private ArrayList<Message> messages;

  public MessageWindow() {
    super("Messages");
    root = this.getRootPane();
    messages = new ArrayList<Message>();

    construct();

    setVisible(true);
  }

  private void construct() {
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);
    root.setLayout(new FlowLayout());
    setSize(300, 400);
  }

  /**
   * Adds a message to the message window
   * 
   * @param icon Icon to use
   * @param title Message to display
   * @param message Tooltip message to show
   */
  public void addMessage(Image icon, String title, String message) {
    addMessage(new Message(icon, title, message));
  }

  /**
   * Adds a message to the message window
   * 
   * @param title Message to display
   * @param message Tooltip message to show
   */
  public void addMessage(String title, String message) {
    addMessage(new Message(title, message));
  }

  private void addMessage(Message m) {
    root.removeAll();
    messages.add(m);
    for (Message cur : messages) 
      root.add(cur);
  }

  private class Message extends JPanel {
    public Message(Image icon, String title, String message) {
      this.setLayout(new FlowLayout());
      this.add(new JLabel(new ImageIcon(icon)));
      this.add(new JLabel(title));
      this.setToolTipText(message);
    }

    public Message(String title, String message) {
      this.setLayout(new FlowLayout());
      this.add(new JLabel(title));
      this.setToolTipText(message);
    }
  }
}
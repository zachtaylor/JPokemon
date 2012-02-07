package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import jpkmn.Driver;

public class MessageView extends JPanel {
  private static final long serialVersionUID = 1L;
  private static MyFrame frame;
  
  public MessageView() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    frame = new MyFrame();
    JScrollPane scroll = new JScrollPane(this);
    frame.getContentPane().add(scroll, BorderLayout.CENTER);
    frame.setSize(new Dimension(350, 200));
    frame.setResizable(false);
    frame.setIconImage(Tools.findImage("mail"));
    frame.setTitle("Message Center");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public void addMessage(Image image, String title, String message) {
    addMessage(new Message(image, title, message));
  }
  
  public void addMessage(String title, String message) {
    addMessage(new Message(title, message));
  }
  
  public void setLocationRelativeTo(Component c) {
    frame.setLocationRelativeTo(c);
  }
  
  public void destruct() {
    frame.dispose();
  }
  
  private void refresh() {
    setVisible(false);
    setVisible(true);
  }
  
  private void addMessage(Message m) {
    add(m);
    refresh();
  }
  
  private void removeMessage(Message m) {
    remove(m);
    refresh();
  }
  
  private class Message extends JPanel {
    private static final long serialVersionUID = 1L;

    public Message(String title, String message) {
      this(Tools.findImage("main-icon"), title, message);
    }
    
    public Message(Image image, String title, String message) {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      add(new JLabel(new ImageIcon(image)));
      JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(title));
        p.add(new JLabel(message));
      add(p);
      add(new JLabel("   "));
      add(new CloseButton(this));
    }
  }
  
  private class CloseButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;
    Message message;
    
    public CloseButton(Message m) {
      super(new ImageIcon(Tools.findImage("close")));
      message = m;
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      removeMessage(message);
    }
  }
  
  // I need this so I can overwrite dispose
  private class MyFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Overwriting dispose so I can turn off the message flag
    public void dispose() {
      Driver.message = false;
      super.dispose();
    }
  }
}
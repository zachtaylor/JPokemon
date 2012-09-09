package jpkmn.exe.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import jpkmn.img.ImageFinder;

public class MessageView extends JPanel {
  public MessageView() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    frame = new MyFrame(this);
    scroll = new JScrollPane(this);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    frame.getContentPane().add(scroll, BorderLayout.CENTER);
    frame.setSize(new Dimension(350, 200));
    frame.setResizable(false);
    frame.setIconImage(ImageFinder.find("mail").getImage());
    frame.setTitle("Message Center");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  public void addMessage(ImageIcon image, String... message) {
    addMessage(new Message(image, message));
  }

  public void addMessage(String... message) {
    addMessage(new Message(message));
  }

  public void setLocationRelativeTo(Component c) {
    frame.setLocationRelativeTo(c);
  }

  public void dispose() {
    frame.close();
  }

  public void destruct() {
    frame.dispose();
  }

  private void refresh() {
    setVisible(false);
    setVisible(true);

    JScrollBar bar = scroll.getVerticalScrollBar();
    bar.setValue(bar.getMaximum());
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
    public Message(String... message) {
      this(ImageFinder.find("err"), message);
    }

    public Message(ImageIcon image, String... message) {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      add(new JLabel(image));
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      for (int i = 0; i < message.length; ++i)
        p.add(new JLabel(message[i]));
      add(p);
      add(new JLabel("   "));
      add(new CloseButton(this));
    }

    private static final long serialVersionUID = 1L;
  }

  private class CloseButton extends JButton implements ActionListener {
    public CloseButton(Message m) {
      super(ImageFinder.find("close"));
      message = m;
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      removeMessage(message);
    }

    private Message message;
    private static final long serialVersionUID = 1L;
  }

  private class MyFrame extends JFrame {
    public MyFrame(MessageView messages) {
      _m = messages;
    }

    public void dispose() {
      _m.removeAll();
      _m.refresh();
    }

    public void close() {
      super.dispose();
    }

    private MessageView _m;
    private static final long serialVersionUID = 1L;
  }

  private MyFrame frame;
  private JScrollPane scroll;
  private static final long serialVersionUID = 1L;
}
package com.jpokemon.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.service.PlayerService;

import com.jpokemon.GameWindow;

public class EventButton extends JButton implements ActionListener {
  public EventButton(WorldView view, int eventID, String text) {
    super(text);

    _eventID = eventID;
    _window = view.parent();

    setFocusable(false);
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    try {
      PlayerService.triggerEvent(_window.playerID(), _eventID);
    } catch (ServiceException s) {
      _window.inbox().addMessage(s.getMessage());
    }
  }

  private int _eventID;
  private GameWindow _window;

  private static final long serialVersionUID = 1L;
}

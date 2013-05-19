package org.jpokemon.misc;

import org.jpokemon.action.StoreAction;
import org.jpokemon.service.LoadException;
import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;

import com.jpokemon.GameWindow;

/**
 * A simple, one-off main method to load a Store interaction. Useful when
 * developing Stores.
 */
public class LaunchStore {
  public static void main(String[] args) {
    try {
      String zachID = PlayerService.load("Zach");

      Player zach = PlayerFactory.get(zachID);

      new StoreAction("1").execute(zach);

      new GameWindow(zachID);
    } catch (ServiceException e) {
    } catch (LoadException e) {
    }
  }
}
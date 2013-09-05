package org.jpokemon.action;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.provider.FriendsDataProvider;
import org.jpokemon.server.Message;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsAction extends Action {
  private JSONObject json;

  public FriendsAction(JSONObject j) {
    json = j;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    try {
      if (json.has("add")) {
        addFriend(player);
      }
      else if (json.has("accept")) {
        acceptFriend(player);
      }
      else if (json.has("block")) {
        blockFriend(player);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    PlayerManager.pushJson(player, FriendsDataProvider.generate(player));
  }

  private void addFriend(Player player) throws JSONException {
    String otherPlayerName = json.getString("add");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (otherPlayer == null) {
      PlayerManager.pushMessage(player, new Message.Notification("'" + otherPlayerName + "' not found"));
      return;
    }
    if (otherPlayer.getBlocked().contains(player.getName())) { return; }

    FriendsDataProvider.addPendingRequest(player.id(), otherPlayerName);
  }

  private void acceptFriend(Player player) throws JSONException {
    String otherPlayerName = json.getString("accept");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (!FriendsDataProvider.hasPendingRequest(player.id(), otherPlayerName)) { return; }

    player.addFriend(otherPlayerName);
    otherPlayer.addFriend(player.getName());
    FriendsDataProvider.removePendingRequest(player.id(), otherPlayerName);
  }

  private void blockFriend(Player player) throws JSONException {
    String otherPlayerName = json.getString("block");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    player.addBlocked(otherPlayerName);
    otherPlayer.removeFriend(player.getName());

    PlayerManager.pushMessage(player, new Message.Notification("'" + otherPlayer.getName() + "' blocked"));
  }
}
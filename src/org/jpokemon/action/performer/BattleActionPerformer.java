package org.jpokemon.action.performer;

import java.io.File;
import java.io.FileNotFoundException;

import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.component.BattleActivity;
import org.jpokemon.server.JPokemonServer;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Trainer;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.jnodalxml.XmlParser;

public class BattleActionPerformer extends AbstractActionPerformer {
  public BattleActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) throws LoadException {
    String fileName = player.record().replaceMacros(getData());
    String filePath = JPokemonServer.scriptedbattlepath + fileName + ".jpkmn";

    XmlNode trainerData;

    try {
      trainerData = XmlParser.parse(new File(filePath)).get(0);
    } catch (FileNotFoundException e) {
      throw new LoadException("Trainer file not found: " + getData());
    }

    Trainer trainer = new Trainer(getData());
    trainer.loadXml(trainerData);

    if (!player.record().getTrainer(trainer.id())) {
      PlayerManager.setActivity(player, new BattleActivity(player, trainer));
    }
  }
}
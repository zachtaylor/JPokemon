package org.jpokemon.action.performer;

import java.io.File;
import java.io.FileNotFoundException;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.component.BattleActivity;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Trainer;
import org.zachtaylor.jnodalxml.XMLNode;
import org.zachtaylor.jnodalxml.XMLParser;

public class BattleActionPerformer extends AbstractActionPerformer {
  public BattleActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) throws LoadException {
    String fileName = player.record().replaceMacros(getData(), player.name());
    String filePath = JPokemonConstants.TRAINER_PATH + fileName + ".jpkmn";

    XMLNode trainerData;

    try {
      trainerData = XMLParser.parse(new File(filePath)).get(0);
    } catch (FileNotFoundException e) {
      throw new LoadException("Trainer file not found: " + getData());
    }

    Trainer trainer = new Trainer(getData());
    trainer.loadXML(trainerData);

    if (!player.record().getTrainer(trainer.id()) || JPokemonConstants.ALLOW_REPEAT_TRAINER_BATTLES) {
      PlayerManager.setActivity(player, new BattleActivity(player, trainer));
    }
  }
}
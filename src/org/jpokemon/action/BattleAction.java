package org.jpokemon.action;

import java.io.File;
import java.io.FileNotFoundException;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.battle.BattleActivity;
import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Trainer;
import org.zachtaylor.jnodalxml.XMLNode;
import org.zachtaylor.jnodalxml.XMLParser;

public class BattleAction extends Action {
  public BattleAction(String data) {
    super(data);
  }

  public void execute(Player player) throws LoadException {
    String fileName = player.record().replaceMacros(data(), player.name());
    String filePath = JPokemonConstants.TRAINER_PATH + fileName + ".jpkmn";

    XMLNode trainerData;

    try {
      trainerData = XMLParser.parse(new File(filePath)).get(0);
    } catch (FileNotFoundException e) {
      throw new LoadException("Trainer file not found: " + data());
    }

    Trainer trainer = new Trainer(data());
    trainer.loadXML(trainerData);

    if (!player.record().getTrainer(trainer.id()) || JPokemonConstants.ALLOW_REPEAT_TRAINER_BATTLES) {
      ActivityTracker.setActivity(player, new BattleActivity(player, trainer));
    }
  }
}
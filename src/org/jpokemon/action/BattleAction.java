package org.jpokemon.action;

import java.io.File;
import java.io.FileNotFoundException;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.BattleRegistry;
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
    XMLNode trainerData;

    try {
      trainerData = XMLParser.parse(new File(JPokemonConstants.TRAINER_PATH + data() + ".jpkmn")).get(0);
    } catch (FileNotFoundException e) {
      throw new LoadException("Trainer file not found: " + data());
    }

    Trainer trainer = new Trainer();
    trainer.loadXML(trainerData);

    BattleRegistry.start(player, trainer);
  }
}
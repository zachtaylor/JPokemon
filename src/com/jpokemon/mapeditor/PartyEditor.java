package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.RewardAction;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Trainer;
import org.zachtaylor.jnodalxml.XMLNode;
import org.zachtaylor.jnodalxml.XMLParser;

import com.jpokemon.mapeditor.widget.panel.ActionPanel;
import com.jpokemon.mapeditor.widget.selector.MoveInfoSelector;
import com.jpokemon.mapeditor.widget.selector.PokemonInfoSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class PartyEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Trainer Files";

  public PartyEditor() {
    JPanel northPanel = new JPanel();

    fileName.setPreferredSize(new Dimension(160, 20));
    fileName.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onFileNameEnter();
      }
    });
    northPanel.add(fileName);

    JPanel westPanel = new JPanel();
    westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

    pokemonContainer.setLayout(new BoxLayout(pokemonContainer, BoxLayout.Y_AXIS));
    westPanel.add(pokemonContainer);

    JButton addPokemon = new JButton("Add Pokemon");
    addPokemon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddPokemonClick();
      }
    });
    westPanel.add(addPokemon);

    JPanel eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

    rewardContainer.setLayout(new BoxLayout(rewardContainer, BoxLayout.Y_AXIS));
    eastPanel.add(rewardContainer);

    JButton addReward = new JButton("Add Reward Action");
    addReward.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onAddRewardClick();
      }
    });
    eastPanel.add(addReward);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(westPanel, BorderLayout.WEST);
    editorPanel.add(eastPanel, BorderLayout.EAST);
  }

  @Override
  public JPanel getEditor() {
    if (trainer != null) {
      readyToEdit = false;

      pokemonContainer.removeAll();
      for (Pokemon pokemon : trainer.party()) {
        pokemonContainer.add(new PokemonPanel(pokemon));
      }

      rewardContainer.removeAll();
      for (RewardAction rewardAction : RewardAction.get(trainer.id())) {
        rewardContainer.add(new ActionPanel(this, rewardAction));
      }
    }

    readyToEdit = true;
    editorPanel.validate();
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(960, 400);
  }

  private void onFileNameEnter() {
    if (!readyToEdit) {
      return;
    }

    // TODO ask about saving

    trainer = new Trainer(fileName.getText());

    try {
      String filePath = JPokemonConstants.TRAINER_PATH + fileName.getText() + ".jpkmn";
      XMLNode trainerData = XMLParser.parse(new File(filePath)).get(0);
      trainer.loadXML(trainerData);
    } catch (FileNotFoundException e) {
    }

    commitChange();
    getEditor();
  }

  private void onAddPokemonClick() {
    if (!readyToEdit) {
      return;
    }

    trainer.add(new Pokemon(1));

    commitChange();
    getEditor();
  }

  private void onAddRewardClick() {
    if (!readyToEdit) {
      return;
    }

    RewardAction rewardAction = new RewardAction();
    rewardAction.setTrainerid(trainer.id());
    rewardAction.setData("undefined");

    try {
      SqlStatement.insert(rewardAction).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private void commitChange() {
    Writer writer = null;

    try {
      String filePath = JPokemonConstants.TRAINER_PATH + trainer.id() + ".jpkmn";
      writer = new BufferedWriter(new FileWriter(new File(filePath)));
      writer.write(trainer.toXML().toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  private class PokemonPanel extends JPanel {
    public PokemonPanel(Pokemon p) {
      pokemon = p;

      JPanel topPanel = new JPanel();

      pokemonSelector.reload();
      pokemonSelector.setSelectedIndex(pokemon.number() - 1);
      pokemonSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onPokemonSelect();
        }
      });
      topPanel.add(pokemonSelector);

      pokemonLevel.setText(pokemon.level() + "");
      pokemonLevel.setPreferredSize(new Dimension(40, 20));
      pokemonLevel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onPokemonLevelEnter();
        }
      });
      topPanel.add(new JLabel("Level:"));
      topPanel.add(pokemonLevel);

      JPanel bottomPanel = new JPanel();

      bottomPanel.add(new JLabel("Moves:"));
      ActionListener onMoveSelectCaller = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onMoveSelect();
        }
      };
      for (int i = 0; i < JPokemonConstants.KNOWN_MOVE_COUNT; i++) {
        moveSelectors[i] = new MoveInfoSelector();
        moveSelectors[i].reload();

        if (i < pokemon.moveCount()) {
          moveSelectors[i].setSelectedIndex(pokemon.move(i).number() - 1);
        }
        else {
          moveSelectors[i].setSelectedIndex(-1);
        }

        moveSelectors[i].addActionListener(onMoveSelectCaller);
        bottomPanel.add(moveSelectors[i]);
      }

      setLayout(new BorderLayout());
      add(topPanel, BorderLayout.NORTH);
      add(bottomPanel, BorderLayout.CENTER);
      add(new JPanel(), BorderLayout.SOUTH);
    }

    private void onPokemonSelect() {
      pokemon.evolve(pokemonSelector.getCurrentElement().getNumber());

      commitChange();
      getEditor();
    }

    private void onPokemonLevelEnter() {
      int newLevel = Integer.parseInt(pokemonLevel.getText());
      pokemon.level(newLevel);

      commitChange();
      getEditor();
    }

    private void onMoveSelect() {
      pokemon.removeAllMoves();

      for (MoveInfoSelector moveSelector : moveSelectors) {
        if (moveSelector.getCurrentElement() != null) {
          pokemon.addMove(moveSelector.getCurrentElement().getNumber());
        }
      }

      commitChange();
      getEditor();
    }

    private Pokemon pokemon;
    private JTextField pokemonLevel = new JTextField();
    private PokemonInfoSelector pokemonSelector = new PokemonInfoSelector();
    private MoveInfoSelector[] moveSelectors = new MoveInfoSelector[JPokemonConstants.KNOWN_MOVE_COUNT];

    private static final long serialVersionUID = 1L;
  }

  private Trainer trainer;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JPanel rewardContainer = new JPanel();
  private JPanel pokemonContainer = new JPanel();
  private JTextField fileName = new JTextField();
}
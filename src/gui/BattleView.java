package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import jpkmn.*;
import battle.*;

public class BattleView extends JPanel {
  private Battle battle;
  private GameWindow game;

  private JProgressBar userhp, enemyhp, userxp;
  private JLabel userpic, enemypic, username, enemyname, userstatus,
      enemystatus;
  private JPanel uparty, oparty;

  public BattleView(GameWindow g, Battle b) {
    game = g;
    battle = b;
    construct();
    reload();
  }

  private void construct() {
    //@preformat


		// Root Panel
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.PAGE_AXIS));
			// Fighters panel
			JPanel f = new JPanel();
			f.setLayout(new FlowLayout());
				// User Panel
				JPanel userpane = new JPanel();
				userpane.setLayout(new FlowLayout());
					// Party Info
					uparty = new JPanel();
					uparty.setPreferredSize(new Dimension(40, 50));
					for (int i=0; i<6; i++) {
						if (battle.user.party.pkmn[i] != null && battle.user.party.pkmn[i].awake)
							uparty.add(new JLabel(new ImageIcon(Tools.findImage("active"))));
						else {
							uparty.add(new JLabel(new ImageIcon(Tools.findImage("inactive"))));
						}
					}
				userpane.add(uparty);
					// Picture
					userpic = new JLabel(new ImageIcon(Tools.findImage(battle.user.leader)));
				userpane.add(userpic);
					// Info
					JPanel userinfo = new JPanel();
					userinfo.setLayout(new BoxLayout(userinfo, BoxLayout.PAGE_AXIS));
						// Name
						username = new JLabel(battle.user.party.leader().name+" Lvl."+battle.user.leader.level);
					userinfo.add(username);
						// HP Bar
						userhp = new JProgressBar(0, battle.user.leader.health.max);
						userhp.setValue(battle.user.leader.health.cur);
						userhp.setForeground(Color.PINK);
						userhp.setBackground(Color.GRAY);
						userhp.setStringPainted(true);
						userhp.setBorderPainted(false);
					userinfo.add(userhp);
						// XP Bar
						userxp = new JProgressBar(0, battle.user.leader.xpNeeded());
						userxp.setValue(battle.user.leader.xp);
						userxp.setForeground(Color.CYAN);
						userxp.setBackground(Color.GRAY);
						userxp.setStringPainted(true);
						userxp.setBorderPainted(false);
					userinfo.add(userxp);
						// Status
						userstatus = new JLabel(battle.user.leader.status.toString());
					userinfo.add(userstatus);
				userpane.add(userinfo);
			f.add(userpane);
				// Enemy Panel
				JPanel enemypane = new JPanel();
				enemypane.setLayout(new FlowLayout());
					// Info
					JPanel enemyinfo = new JPanel();
					enemyinfo.setLayout(new BoxLayout(enemyinfo, BoxLayout.PAGE_AXIS));
						// Name
						enemyname = new JLabel(battle.enemy.leader.name+" Lvl."+battle.enemy.leader.level);
					enemyinfo.add(enemyname);
						// HP Bar
						enemyhp = new JProgressBar(0, battle.enemy.leader.health.max);
						enemyhp.setValue(battle.enemy.leader.health.cur);
						enemyhp.setForeground(Color.PINK);
						enemyhp.setBackground(Color.GRAY);
						enemyhp.setStringPainted(true);
						enemyhp.setBorderPainted(false);
					enemyinfo.add(enemyhp);
						// Status
						enemystatus = new JLabel(battle.enemy.leader.status.toString());
					enemyinfo.add(enemystatus);
				enemypane.add(enemyinfo);
					// Picture
					enemypic = new JLabel(new ImageIcon(Tools.findImage(battle.enemy.leader)));
				enemypane.add(enemypic);
					// Party Info
					oparty = new JPanel();
					oparty.setPreferredSize(new Dimension(40, 50));
					for (int i=0; i<6; i++) {
						if (battle.enemy.party.pkmn[i] != null && battle.enemy.party.pkmn[i].awake)
							oparty.add(new JLabel(new ImageIcon(Tools.findImage("active"))));
						else {
							oparty.add(new JLabel(new ImageIcon(Tools.findImage("inactive"))));
						}
					}
				enemypane.add(oparty);
			f.add(enemypane);
		root.add(f);
			// Buttons panel
			JPanel b = new JPanel();
			b.add(new FightButton());
			b.add(new ItemButton());
			b.add(new SwapButton());
			b.add(new RunButton());
		root.add(b);
	  add(root);
	  //@format
  }

  public void load(Battle b) {
    battle = b;
    reload();
  }

  public void reload() {
    userhp.setMaximum(battle.user.leader.health.max);
    userhp.setValue(battle.user.leader.health.cur);
    userhp.setToolTipText(battle.user.leader.health.cur + "/"+battle.user.leader.health.max);
    enemyhp.setMaximum(battle.enemy.leader.health.max);
    enemyhp.setValue(battle.enemy.leader.health.cur);
    enemyhp.setToolTipText(battle.enemy.leader.health.cur + "/"+battle.enemy.leader.health.max);
    userxp.setMaximum(battle.user.leader.xpNeeded());
    userxp.setValue(battle.user.leader.xp);
    userpic.setIcon(new ImageIcon(Tools.findImage(battle.user.leader)));
    enemypic.setIcon(new ImageIcon(Tools.findImage(battle.enemy.leader)));
    username.setText(battle.user.party.leader().name+" Lvl."+battle.user.leader.level);
    enemyname.setText(battle.enemy.leader.name+" Lvl."+battle.enemy.leader.level);
    userstatus.setText(battle.user.leader.status.toString());
    enemystatus.setText(battle.enemy.leader.status.toString());
    Tools.game.repaint();
  }

  private void exit(BattleEndException e) {
    Driver.log(BattleView.class, "Battle ended code=" + e.getExitStatus()
        + ". " + e.getExitDescription());
    game.showMain();
  }

  private class FightButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;

    public FightButton() {
      super("FIGHT");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      try {
        battle.fight();
      } catch (BattleEndException e) {
        exit(e);
      }
      reload();
    }
  }

  private class ItemButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;

    public ItemButton() {
      super("ITEM");
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      try {
        battle.item();
      } catch (BattleEndException e) {
        exit(e);
      }
      reload();
    }
  }

  private class SwapButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;

    public SwapButton() {
      super("SWAP");
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      try {
        battle.swap();
      } catch (BattleEndException e) {
        exit(e);
      }
      reload();
    }
  }

  private class RunButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;

    public RunButton() {
      super("RUN");
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      try {
        battle.run();
      } catch (BattleEndException e) {
        exit(e);
      }
      reload();
    }
  }

  private static final long serialVersionUID = 1L;
}
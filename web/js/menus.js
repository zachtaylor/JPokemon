(function($) {
  me.menu = me.menu || {};

  me.menu.Window = me.ui.Panel.extend({
    init : function(x, y) {
      this.parent({
        x : x,
        y : y,
        xlayout : 'fill',
        ylayout : 'fit',
        color : 'black',
        border : 'white',
        opacity : .7
      });

      this.frame = new me.ui.Panel({ xlayout : 'fit', ylayout : 'center' });
      this.icon = new me.ui.Button();
      this.frame.add(this.icon);
      this.title = new me.ui.Label({ fontSize : 12, fontName : 'verdana' });
      this.frame.add(this.title);
      this.add(this.frame);

      this.pane = new me.ui.Panel({ padding : 0, border : 'white', xlayout : 'fill', ylayout : 'fit' });
      this.add(this.pane);
    },
  });

  me.menu.StatefulWindow = me.menu.Window.extend({
    init : function(x, y) {
      this.parent(x, y);

      this.icon.onClick = this.chooseState.bind(this);
      this.states = {};
    },

    addState : function(name, icon, view) {
      this.states[name] = new me.ui.Toggle({
        xlayout : 'fit', ylayout : 'center', toggleColor : false, untoggleColor : false, padding : 0,
        onToggleChange : (function(name) {
          return function() { this.setState(name); };
        })(name).bind(this)
      });

      this.states[name].add(new me.ui.Icon({ image : icon }));
      this.states[name].add(new me.ui.Label({ text : name }));
      this.states[name].icon = icon;
      this.states[name].view = view;
    },

    chooseState : function() {
      this.title.setText('');
      this.icon.setImage('circle_gray');
      this.pane.clear();

      for (var state in this.states) {
        this.pane.add(this.states[state]);
      }
    },

    setState : function(name) {
      this.title.setText(name);
      this.icon.setImage(this.states[name].icon);
      this.pane.clear();
      this.pane.add(this.states[name].view);
    }
  });

  me.menu.FriendsLauncher = me.ui.Toggle.extend({
    init : function() {
      this.parent({
        x : 10,
        y : 512 - 20,
        border : 'white',
        toggleStyle : 'toggle',
        padding : 0,
        opacity : .7
      });
      this.add(new me.ui.Label({text : 'Friends'}));

      this.friendsWindow = new me.menu.FriendsWindow();

      game.subscribe('friends', this.friendsWindow);
    },

    onToggleChange : function(visible) {
      if (visible) {
        this.friendsWindow.show();
        this.friendsWindow.sendLoadRequest();
      }
      else {
        this.friendsWindow.hide();
      }
    }
  });

  me.menu.FriendsWindow = me.menu.Window.extend({
    init : function() {
      this.parent(10, 50);

      this.icon.onClick = this.showChooseList.bind(this);
      this.frame.add(new me.ui.Icon({ image : 'plus_gray', onClick : this.showInputWindow.bind(this) }));
      this.frame.add(new me.ui.Icon({ image : 'refresh_gray', onClick : this.sendLoadRequest }));
      this.scrollers = {};
      this.stateButtons = {};

      this.scrollers.friends = new me.ui.Scrollable({ padding : 0, height : 100, width: 130 });
      this.stateButtons.friends =  new me.ui.Toggle({
        xlayout : 'fit', ylayout : 'center',
        toggleColor : false, untoggleColor : false,
        padding : 0, onToggleChange : this.showFriends.bind(this)
      });
      this.stateButtons.friends.add(new me.ui.Icon({ image : 'circle_green' }));
      this.stateButtons.friends.add(new me.ui.Label({ text : 'Friends' }));

      this.scrollers.blocked = new me.ui.Scrollable({ padding : 0, height : 100, width: 130 });
      this.stateButtons.blocked =  new me.ui.Toggle({
        xlayout : 'fit', ylayout : 'center',
        toggleColor : false, untoggleColor : false,
        padding : 0, onToggleChange : this.showBlocked.bind(this)
      });
      this.stateButtons.blocked.add(new me.ui.Icon({ image : 'circle_red' }));
      this.stateButtons.blocked.add(new me.ui.Label({ text : 'Blocked' }));

      this.scrollers.pending = new me.ui.Scrollable({ padding : 0, height : 100, width: 130 });
      this.stateButtons.pending =  new me.ui.Toggle({
        xlayout : 'fit', ylayout : 'center',
        toggleColor : false, untoggleColor : false,
        padding : 0, onToggleChange : this.showPending.bind(this)
      });
      this.stateButtons.pending.add(new me.ui.Icon({ image : 'circle_blue' }));
      this.stateButtons.pending.add(new me.ui.Label({ text : 'Pending' }));

      this.inputWindow = new me.ui.Panel({ x : 200, y : 200, ylayout : 'fit', border : 'white', color : 'black', opacity : .7 });
      this.inputWindowLabel = new me.ui.Label({ text : 'Add Friend' });
      this.inputWindow.add(this.inputWindowLabel);
      this.inputWindowInputBox = new me.ui.InputBox({ width : 100, onEnter : this.onInputWindowEnter.bind(this) });
      this.inputWindow.add(this.inputWindowInputBox);

      this.showFriends(true);
    },

    hide : function() {
      this.parent();
      this.inputWindow.hide();
    },

    showInputWindow : function() {
      this.inputWindow.show();
    },

    showFriends : function(selected) {
      if (!selected) {
        return;
      }
      this.list = 'friends';

      this.icon.setImage('circle_green');
      this.title.setText('Friends List');
      this.pane.clear();
      this.pane.add(this.scrollers.friends);
    },

    showBlocked : function(selected) {
      if (!selected) {
        return;
      }
      this.list = 'blocked';

      this.icon.setImage('circle_red');
      this.title.setText('Blocked List');
      this.pane.clear();
      this.pane.add(this.scrollers.blocked);
    },

    showPending : function(selected) {
      if (!selected) {
        return;
      }
      this.list = 'pending';

      this.icon.setImage('circle_blue');
      this.title.setText('Pending List');
      this.pane.clear();
      this.pane.add(this.scrollers.pending);
    },

    showChooseList : function() {
      if (this.list === 'choose') {
        return;
      }
      this.list = 'choose';
      
      this.icon.setImage('circle_gray');
      this.title.setText('Choose List');
      this.pane.clear();
      this.pane.add(this.stateButtons.friends);
      this.pane.add(this.stateButtons.blocked);
      this.pane.add(this.stateButtons.pending);
    },

    friendsNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity : 0, padding : 0, xlayout:'fill' });
      namePanel.add(new me.ui.Label({ text : name }));
      return namePanel;
    },

    blockedNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity : 0, padding : 0, xlayout:'fill' });
      namePanel.add(new me.ui.Label({ text : name }));
      return namePanel;
    },

    pendingNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity : 0, padding : 0, xlayout:'fit', ylayout : 'center' });

      var acceptButton = new me.ui.Icon({ image : 'plus_green', onClick : this.acceptPendingRequest(name).bind(this) });
      namePanel.add(acceptButton);

      namePanel.add(new me.ui.Label({ text : name }));

      return namePanel;
    },

    sendLoadRequest : function() {
      game.send({
        load : 'friends'
      });
    },

    onInputWindowEnter : function() {
      var friendName = this.inputWindowInputBox.getText();
      
      if (friendName) {
        game.send({
          action : 'friends',
          add : friendName
        });
        this.inputWindowInputBox.setText('');
      }

      this.inputWindow.hide();
    },

    acceptPendingRequest : function(name) {
      return function() {
        game.send({
          action : 'friends',
          accept : name
        });
      }
    },

    dispatch : function(json) {
      this.data = json;
      
      this.scrollers.friends.clear();
      for (var i = 0; i < this.data.friends.length; i++) {
        this.scrollers.friends.add(this.friendsNamePanel(this.data.friends[i]));
      }

      this.scrollers.blocked.clear();
      for (var i = 0; i < this.data.blocked.length; i++) {
        this.scrollers.blocked.add(this.blockedNamePanel(this.data.blocked[i]));
      }

      this.scrollers.pending.clear();
      for (var i = 0; i < this.data.pending.length; i++) {
        this.scrollers.pending.add(this.pendingNamePanel(this.data.pending[i]));
      }
    }
  });

  me.menu.BattleSignupLauncher = me.ui.Toggle.extend({
    init : function() {
      this.parent({
        x : 75,
        y : 512 - 20,
        border : 'white',
        toggleStyle : 'toggle',
        padding : 0,
        opacity : .7
      });
      this.add(new me.ui.Label({text : 'Battle'}));

      this.battleSignupWindow = new me.menu.BattleSignupWindow();

      game.subscribe('battlesignup', this.battleSignupWindow);
    },

    onToggleChange : function(focus) {
      if (focus) {
        this.battleSignupWindow.show();
      }
      else {
        this.battleSignupWindow.hide();
      }
    }
  });

  me.menu.BattleSignupWindow = me.menu.Window.extend({
    init : function() {
      this.parent(75, 50);

      this.icon.setImage('circle_green');
      this.title.setText('Create Battle');
      this.frame.add(new me.ui.Button({ image : 'refresh_gray', onClick : this.reset.bind(this) }));
      this.frame.add(new me.ui.Button({ image : 'check_gray', onClick : this.send.bind(this) }));

      this.inputWindow = new me.ui.Panel({ x : 200, y : 200, ylayout : 'fit', border : 'white', color : 'black', opacity : .7 });
      this.inputWindow.label = new me.ui.Label({ text : 'Enter name' });
      this.inputWindow.add(this.inputWindow.label);
      this.inputWindow.inputBox = new me.ui.InputBox({ width : 200, onEnter : this.acceptPlayer.bind(this) });
      this.inputWindow.add(this.inputWindow.inputBox);
      this.inputWindow.radio = new me.ui.RadioGroup({ xlayout : 'fit' });
      this.inputWindow.add(this.inputWindow.radio);

      var createView = new me.ui.Panel({ ylayout : 'fit', height : 100, width: 130, padding : 0 });
      this.pane.add(createView);

      var controls = new me.ui.Panel({ xlayout : 'fit', ylayout : 'center', width : 130});
      controls.add(new me.ui.Button({ image : 'plus_green', text : 'Team', onClick : this.addTeam.bind(this) }));
      controls.add(new me.ui.Button({ image : 'plus_green', text : 'Player', onClick : this.show.bind(this.inputWindow) }));
      createView.add(controls);

      this.teams = [];
      this.teamsContainer = new me.ui.Panel({xlayout : 'fit', ylayout : 'fill' });
      createView.add(this.teamsContainer);

      this.reset();
    },

    hide : function() {
      this.parent();
      this.inputWindow.hide();
    },

    addTeam : function() {
      var newTeam = new me.ui.Panel({ xlayout : 'fill', ylayout : 'fit', padding : 0, width : 25, height : 100 });
      newTeam.add(new me.ui.Label({ text : 'Team '+this.teams.length }));
      newTeam.add(new me.ui.Panel({ border : 'white', opacity : .7 }));

      this.teams.push(newTeam);
      this.refresh();
    },

    addPlayer : function(name, team) {
      var teamPanel = this.teams[team];

      var teamMember = new me.ui.Button({ 
        image : 'minus_red', 
        text : name
      });
      teamMember.playerName = name;

      teamMember.onClick = (function() {
        this.removePlayer(team, teamMember);
      }).bind(this);

      this.teams[team].add(teamMember);
    },

    acceptPlayer : function() {
      var name = this.inputWindow.inputBox.getText().trim();
      var team = this.inputWindow.radio.indexOf(this.inputWindow.radio.getSelectedItem());

      this.addPlayer(name, team);
      this.inputWindow.inputBox.setText('');
    },

    removePlayer : function(team, teamMember) {
      var teamPanel = this.teams[team];
      teamPanel.remove(teamMember);
      this.refresh();
    },

    reset : function() {
      this.teams = [];
      this.addTeam();
      this.addPlayer(game.playerName, 0);
    },

    refresh : function() {
      this.teamsContainer.clear();
      this.inputWindow.radio.clear();

      for (var team = 0; team < this.teams.length; team++) {
        this.teamsContainer.add(this.teams[team]);
        this.inputWindow.radio.addLabel({ text : 'Team '+ team });
      }
    },

    send : function() {
      var team, teams = []; // new JSONObject(); :)

      for (var i = 0; i < this.teams.length; i++) {
        team = [];

        this.teams[i]._forEachChild(function(child) {
          if (child.playerName) {
            team.push(child.playerName);
          }
        });

        teams.push(team);
      }

      game.send({
        action : 'createbattle',
        teams : teams
      });

      this.hide();
    },

    dispatch : function(json) {
    }
  });
})(window);
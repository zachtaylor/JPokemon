(function($) {
  me.screen = me.screen || {};

  me.screen.LoginScreen = me.ScreenObject.extend({
    onResetEvent: function(config) {
      this.loginPanel = new me.ui.Panel({
        width : 512,
        height : 512,
        color : 'darkblue',
        xlayout : 'center',
        ylayout : 'relative'
      });

      var namePanel = new me.ui.Panel({
        y : 100,
        xlayout : 'fit'
      });
      this.loginPanel.add(namePanel);

      namePanel.add(new me.ui.Label({
        text : 'Name :',
      }));

      var sendLoginRequest = function() {
        game.send({
          login: this.nameBox.getText()
        });
        me.state.change(me.state.PLAY);
      };

      namePanel.add(this.nameBox = new me.ui.InputBox({
        width : 250,
        onEnter : sendLoginRequest.bind(this)
      }));

      this.loginPanel.add(new me.ui.Label({
        y : 150,
        color : 'white',
        opacity : .5,
        text : 'Login',
        onClick : sendLoginRequest.bind(this)
      }));

      this.loginPanel.show();
    },
    
    onDestroyEvent: function() {
      this.loginPanel.hide();
    }
  });

  var players = {};

  me.screen.PlayScreen = me.ScreenObject.extend({
    init : function(config) {
    },

    dispatch: function(json) {
      if (json.login) {
        me.levelDirector.loadLevel(json.map);

        players[json.login] = new me.entityPool.newInstanceOf('player', json);
        players[json.login].setName(json.login);
        game.playerName = json.login;
        me.game.add(players[json.login]);

        new me.menu.FriendsLauncher().show();
        new me.menu.BattleLobbyLauncher().show();
        new me.menu.MessagesArea().show();
      }
      else if (json.add) {
        players[json.add] = me.entityPool.newInstanceOf('trainer', json);
        players[json.add].setName(json.add);
        me.game.add(players[json.add]);
        me.game.sort();
      }
      else if (json.move) {
        players[json.name]['walk' + json.move](json.x, json.y);
      }
      else if (json.leave) {
        me.game.remove(players[json.leave]);
        me.entityPool.freeInstance(players[json.leave]);
      }
    },

    onResetEvent: function(config) {
      players = {};
    },
    
    onDestroyEvent: function() {
      for (var entity in players) {
        me.entityPool.freeInstance(players[entity]);
      }
      friendsLauncher.hide();
    }
  });
  game.subscribe('overworld', me.screen.PlayScreen);

})(window);
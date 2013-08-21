(function($) {
  me.screen = me.screen || {};

  me.screen.LoginScreen = me.ScreenObject.extend({
    onResetEvent: function(config) {
      var loginPanel = new me.ui.Panel({
        width : 512,
        height : 512,
        color : 'blue',
        xlayout : 'center',
        ylayout : 'relative'
      });

      var namePanel = new me.ui.Panel({
        y : 100,
        xlayout : 'fit'
      });
      loginPanel.add(namePanel);

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

      loginPanel.add(new me.ui.Label({
        y : 150,
        color : 'white',
        opacity : .5,
        text : 'Login',
        onClick : sendLoginRequest.bind(this)
      }));

      loginPanel.show();
    },
    
    onDestroyEvent: function() {
    }
  });

  var player = null;
  var players = {};

  me.screen.PlayScreen = me.ScreenObject.extend({
    init : function(config) {
    },

    dispatch: function(json) {
      if (json.login) {
        player = new me.entityPool.newInstanceOf('player', json);
        me.levelDirector.loadLevel(json.map);

        new me.menu.FriendsLauncher().show();
      }
      else if (json.add) {
        players[json.add] = me.entityPool.newInstanceOf('trainer', json);
        me.game.sort();
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
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
        action: 'login',
        name: this.nameBox.getText()
      });
      me.state.change(me.state.PLAY);
    };

    namePanel.add(this.nameBox = new me.ui.InputBox({
      width : 250,
      onEnter : sendLoginRequest.bind(this)
    }));

    loginPanel.add(new me.ui.Button({
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

me.screen.PlayScreen = me.ScreenObject.extend({
  dispatch: function(json) {
    // TODO : other loading things for the overworld screen
    me.levelDirector.loadLevel(json.map);
  },

  onResetEvent: function(config) {
  },
  
  onDestroyEvent: function() {
  }
});
game.subscribe('overworld', me.screen.PlayScreen);
game.LoginScreen = me.ScreenObject.extend({
  onResetEvent: function() {
      var loginScreen = new me.ui.Panel({
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
      loginScreen.add(namePanel);

      namePanel.add(new me.ui.Label({
        text : 'Name :',
      }));

      namePanel.add(this.nameBox = new me.ui.InputBox({
        width : 250
      }));

      var loginButton = new me.ui.Button({
        y : 150,
        color : 'white',
        opacity : .5,
        text : 'Login',
        onClick : this._loadGame.bind(this)
      });
      loginScreen.add(loginButton);

      loginScreen.show();
  },

  _loadGame : function() {
    // TODO : send login request
  },
  
  onDestroyEvent: function() {
  }
});

game.PlayScreen = me.ScreenObject.extend({
  onResetEvent: function(config) {
    // TODO add the things
  },
  
  onDestroyEvent: function() {
    // TODO Make leaving request to server
  }
});
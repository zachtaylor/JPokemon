(function($) {
  me.menu = me.menu || {};

  me.menu.FriendsLauncher = me.ui.Toggle.extend({
    init : function() {
      this.parent({
        xlayout : 'fit',
        ylayout : 'fit',
        border : 'white',
        toggleStyle : 'toggle',
        toggleColor : 'gray',
        untoggleColor : 'black',
        padding : 0,
        opacity : .7
      });
      this.add(new me.ui.Label({text : 'Friends'}));

      this.friendsWindow = new me.menu.FriendsWindow();

      game.subscribe('friends', this);
    },

    onToggleChange : function(visible) {
      if (visible) {
        game.send({
          load : 'friends'
        });

        this.friendsWindow.show();
      }
      else {
        this.friendsWindow.hide();
      }
    },

    dispatch : function(json) {
      this.friendsWindow.dispatch(json)
    }
  });

  me.menu.FriendsWindow = me.ui.Panel.extend({
    init : function() {
      this.parent({
        x : 100,
        y : 100,
        ylayout : 'fit',
        border : 'white',
        color : 'black',
        opacity : .7,
      });

      var headerPanel = new me.ui.Panel({ xlayout : 'fit', padding : 0 });
      headerPanel.add(new me.ui.Label({ text : 'Friends List' }));
      headerPanel.add(new me.ui.Button({ text : '+Friend', onClick : this.showInputWindow.bind(this) }));
      this.add(headerPanel);

      this.friendsList = new me.ui.Scrollable({ height : 100, width: 100, border : 'pink' });
      this.add(this.friendsList);

      this.inputWindow = new me.ui.Panel({ x : 200, y : 200, ylayout : 'fit', border : 'white', color : 'black', opacity : .7 });
      this.inputWindowLabel = new me.ui.Label({ text : 'Add Friend' });
      this.inputWindow.add(this.inputWindowLabel);
      this.inputWindowInputBox = new me.ui.InputBox({ width : 100, onEnter : this.sendFriendRequest.bind(this) });
      this.inputWindow.add(this.inputWindowInputBox);
    },

    hide : function() {
      this.parent();
      this.inputWindow.hide();
    },

    showInputWindow : function() {
      this.inputWindow.show();
    },

    sendFriendRequest : function() {
      var friendName = this.inputWindowInputBox.getText();
        
      if (friendName) {
        game.send({
          action:'friends',
          add:friendName
        });
        this.inputWindowLabel.setText('');
      }

      this.inputWindow.hide();
    },

    doLayout : function() {
      this.parent();
    },

    dispatch : function(json) {
      this.friendsList.children = [];

      for (var i = 0; i < json.friends.length; i++) {
        var namePanel = new me.ui.Panel({ opacity:0, padding:0, xlayout:'fit' });
        namePanel.add(new me.ui.Label({text:json.friends[i]}));
        this.friendsList.add(namePanel);
      }
    }
  });

  // me.menu.MessagePanel = me.ui.Scrollable.extend({
  //   init : function() {
  //     this.parent({
  //       x : 0,
  //       y : me.video.getHeight() - 97,
  //       width : me.video.getWidth(),
  //       height : 79,
  //       color : 'black',
  //       opacity : .5
  //     });
  //     this.GUID = "messagepanel-" + me.utils.createGUID();
      
  //     this.messages = [];
  //     this.opacity = .3;
  //     this.drawnMessages = 5;
  //     this.messagePointer = 0;
  //     this.maxMessages = 25;

  //     this.inputBox = new me.ui.InputBox({
  //       x : 0, 
  //       y : this.rect.height, 
  //       width : me.video.getWidth(),
  //       opacity : .5,
  //       focusEvent : 'mousedown',
  //       onEnter : this.submitMessage.bind(this)
  //     });
  //     this.add(this.inputBox);

  //     this.nameFont = new me.Font('courier', 12, 'yellow');
  //     this.messageFont = new me.Font('courier', 12, 'cyan');

  //     game.socket.on('message', this.onMessageReceived.bind(this));
  //   },

  //   scroll : function(delta) {
  //     this.messagePointer = Math.min(
  //       Math.max(this.messagePointer - delta, 0),
  //       this.messages.length - this.drawnMessages
  //     );
      
  //     me.game.repaint();
  //   },

  //   onMessageReceived : function(name, message) {
  //     this.messages.push({
  //       "name" : name + ':',
  //       "message" : message
  //     });

  //     if (this.messages.length > this.maxMessages) {
  //       this.messages.shift();
  //     }

  //     this.messagePointer = Math.max(this.messages.length - this.drawnMessages, 0);

  //     me.game.repaint();
  //   },

  //   submitMessage : function() {
  //     game.socket.emit('message', this.inputBox.getText());
  //     this.inputBox.setText('');
  //     me.game.repaint();
  //   },

  //   draw: function(context) {
  //     this.parent(context);

  //     context.save();

  //     context.globalAlpha = 1.0;
  //     var location = this.rect.top;

  //     for(var i = 0; i < this.drawnMessages && i + this.messagePointer < this.messages.length; i++) {
  //       var message = this.messages[i + this.messagePointer];
  //       var nameWidth = this.nameFont.measureText(context, message.name).width;
  //       this.nameFont.draw(context, message.name, 3, location);
  //       this.messageFont.draw(context, message.message, nameWidth + 6, location);
  //       location += 15;
  //     }

  //     context.restore();
  //   }
  // });
})(window);
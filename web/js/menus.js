(function($) {
  me.menu = me.menu || {};

  me.menu.Window = me.ui.Panel.extend({
    init : function() {
      this.parent({
        x : 10,
        y : 10,
        xlayout : 'fill',
        ylayout : 'fit',
        color : 'black',
        border : 'white',
        opacity : .7
      });

      this.frame = new me.ui.Panel({ padding : 0, xlayout : 'fit', ylayout : 'center' });
      this.icon = new me.ui.Icon();
      this.frame.add(this.icon);
      this.title = new me.ui.Label({ fontSize : 12, fontName : 'verdana' });
      this.frame.add(this.title);
      this.add(this.frame);

      this.pane = new me.ui.Panel({ padding : 0, ylayout : 'fit' });
      this.add(this.pane);
    },
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
      this.parent();

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
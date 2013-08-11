(function($) {
  me.menu = me.menu || {};

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

      game.subscribe('friends', this);
    },

    onToggleChange : function(visible) {
      if (visible) {
        this.friendsWindow.show();
        this.friendsWindow.sendLoadRequest();
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
        x : 20,
        y : 20,
        xlayout : 'fill',
        ylayout : 'fit',
        border : 'white',
        color : 'black',
        opacity : .7,
      });

      var headerPanel = new me.ui.Panel({ padding : 0, xlayout : 'fit', ylayout : 'center' });
      this.currentListImage = new me.ui.Icon({ image : 'circle_green', onClick : this.showChooseList.bind(this) });
      headerPanel.add(this.currentListImage);
      this.title = new me.ui.Label({ text : 'Friends List', fontSize : 12, fontName : 'verdana' });
      headerPanel.add(this.title);
      headerPanel.add(new me.ui.Icon({ image : 'plus_gray', onClick : this.showInputWindow.bind(this) }));
      headerPanel.add(new me.ui.Icon({ image : 'refresh_gray', onClick : this.sendLoadRequest }));
      this.add(headerPanel);

      this.friendsList = new me.ui.Scrollable({ padding : 0, height : 100, width: 130 });
      this.add(this.friendsList);

      this.showFriendsButton =  new me.ui.Toggle({
        xlayout : 'fit',
        ylayout : 'center',
        padding : 0,
        toggleColor : false,
        untoggleColor : false,
        onToggleChange : this.showFriends.bind(this)
      });
      this.showFriendsButton.add(new me.ui.Icon({ image : 'circle_green' }));
      this.showFriendsButton.add(new me.ui.Label({ text : 'Friends' }));
      this.showBlockedButton =  new me.ui.Toggle({
        xlayout : 'fit',
        ylayout : 'center',
        padding : 0,
        toggleColor : false,
        untoggleColor : false,
        onToggleChange : this.showBlocked.bind(this)
      });
      this.showBlockedButton.add(new me.ui.Icon({ image : 'circle_red' }));
      this.showBlockedButton.add(new me.ui.Label({ text : 'Blocked' }));
      this.showPendingButton =  new me.ui.Toggle({
        xlayout : 'fit',
        ylayout : 'center',
        padding : 0,
        toggleColor : false,
        untoggleColor : false,
        onToggleChange : this.showPending.bind(this)
      });
      this.showPendingButton.add(new me.ui.Icon({ image : 'circle_blue' }));
      this.showPendingButton.add(new me.ui.Label({ text : 'Pending' }));

      this.inputWindow = new me.ui.Panel({ x : 200, y : 200, ylayout : 'fit', border : 'white', color : 'black', opacity : .7 });
      this.inputWindowLabel = new me.ui.Label({ text : 'Add Friend' });
      this.inputWindow.add(this.inputWindowLabel);
      this.inputWindowInputBox = new me.ui.InputBox({ width : 100, onEnter : (function () { this[this.list + 'Request'] }).bind(this) });
      this.inputWindow.add(this.inputWindowInputBox);

      this.list = 'friends';
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

      this.currentListImage.setImage('circle_green');
      this.title.setText('Friends List');

      this.reload();
    },

    friendsRequest : function() {
      var friendName = this.inputWindowInputBox.getText();
        
      if (friendName) {
        game.send({
          action:'friends',
          add:friendName
        });
        this.inputWindowInputBox.setText('');
      }

      this.inputWindow.hide();
    },

    friendsNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity:0, padding:0, xlayout:'fill' });
      namePanel.add(new me.ui.Label({ text : name }));
      return namePanel;
    },

    showBlocked : function(selected) {
      if (!selected) {
        return;
      }
      this.list = 'blocked';

      this.currentListImage.setImage('circle_red');
      this.title.setText('Blocked List');

      this.reload();
    },

    blockedNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity:0, padding:0, xlayout:'fill' });
      namePanel.add(new me.ui.Label({ text : name }));
      return namePanel;
    },

    showPending : function(selected) {
      if (!selected) {
        return;
      }
      this.list = 'pending';

      this.currentListImage.setImage('circle_blue');
      this.title.setText('Pending List');

      this.reload();
    },

    pendingNamePanel : function(name) {
      var namePanel = new me.ui.Panel({ opacity:0, padding:0, xlayout:'fit', ylayout : 'center' });

      var acceptButton = new me.ui.Icon({ image : 'plus_green', onClick : this.pendingRequest(name).bind(this) });
      namePanel.add(acceptButton);

      namePanel.add(new me.ui.Label({ text : name }));

      return namePanel;
    },

    pendingRequest : function(name) {
      return function() {
        game.send({
          action:'friends',
          accept : name
        });
      }
    },

    showChooseList : function() {
      if (this.list === 'choose') {
        return;
      }
      this.list = 'choose';

      this.children.splice(1);
      this.friendsList.onHide();
      this.add(this.showFriendsButton);
      this.showFriendsButton.onShow();
      this.add(this.showBlockedButton);
      this.showBlockedButton.onShow();
      this.add(this.showPendingButton);
      this.showPendingButton.onShow();
      
      this.currentListImage.setImage('circle_gray');
      this.title.setText('Select List');
    },

    sendLoadRequest : function() {
      game.send({
        load : 'friends'
      });
    },

    dispatch : function(json) {
      this.data = json;
      this.reload();
    },

    reload : function() {
      if (this.list === 'choose') {
        return;
      }
      this.children.splice(1);
      this.showFriendsButton.onHide();
      this.showBlockedButton.onHide();
      this.showPendingButton.onHide();
      this.add(this.friendsList);
      this.friendsList.onShow();

      this.friendsList.children = [];

      var namePanel;
      for (var i = 0; i < this.data[this.list].length; i++) {
        // Function pointers, man
        namePanel = this[this.list + 'NamePanel'](this.data[this.list][i]);
        this.friendsList.add(namePanel);
        namePanel.onShow();
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
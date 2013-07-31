(function($) {
  me.menu = me.menu || {};

  me.menu.MessagePanel = me.ui.Scrollable.extend({
    init : function() {
      this.parent({
        x : 0,
        y : me.video.getHeight() - 97,
        width : me.video.getWidth(),
        height : 79,
        color : 'black',
        opacity : .5
      });
      this.GUID = "messagepanel-" + me.utils.createGUID();
      
      this.messages = [];
      this.opacity = .3;
      this.drawnMessages = 5;
      this.messagePointer = 0;
      this.maxMessages = 25;

      this.inputBox = new me.ui.InputBox({
        x : 0, 
        y : this.rect.height, 
        width : me.video.getWidth(),
        opacity : .5,
        focusEvent : 'mousedown',
        onEnter : this.submitMessage.bind(this)
      });
      this.add(this.inputBox);

      this.nameFont = new me.Font('courier', 12, 'yellow');
      this.messageFont = new me.Font('courier', 12, 'cyan');

      game.socket.on('message', this.onMessageReceived.bind(this));
    },

    scroll : function(delta) {
      this.messagePointer = Math.min(
        Math.max(this.messagePointer - delta, 0),
        this.messages.length - this.drawnMessages
      );
      
      me.game.repaint();
    },

    onMessageReceived : function(name, message) {
      this.messages.push({
        "name" : name + ':',
        "message" : message
      });

      if (this.messages.length > this.maxMessages) {
        this.messages.shift();
      }

      this.messagePointer = Math.max(this.messages.length - this.drawnMessages, 0);

      me.game.repaint();
    },

    submitMessage : function() {
      game.socket.emit('message', this.inputBox.getText());
      this.inputBox.setText('');
      me.game.repaint();
    },

    draw: function(context) {
      this.parent(context);

      context.save();

      context.globalAlpha = 1.0;
      var location = this.rect.top;

      for(var i = 0; i < this.drawnMessages && i + this.messagePointer < this.messages.length; i++) {
        var message = this.messages[i + this.messagePointer];
        var nameWidth = this.nameFont.measureText(context, message.name).width;
        this.nameFont.draw(context, message.name, 3, location);
        this.messageFont.draw(context, message.message, nameWidth + 6, location);
        location += 15;
      }

      context.restore();
    }
  });
})(window);
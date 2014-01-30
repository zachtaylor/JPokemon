game.control('overworld', {
  view: false,
  constructor: function() {
    this.players = {};
    this.waitingKey = false;
    this.waitingKeyIsShortPress = true;
    this.keys = [87, 65, 83, 68, 69];

    // Have to do it this way because melon screws up my selector
    this.canvas = $('#screen')[0].children[0];

    $(document).mouseup(this.onDocumentClick.bind(this));
    $(document).keydown(this.onKeydown.bind(this));
    $(document).keyup(this.onKeyup.bind(this));

    me.audio.init("mp3,ogg");
  },

  onDocumentClick: function(e) {
    if (this.canvas == e.target) {
      this.canvasIsFocused = true;
    }
    else {
      this.canvasIsFocused = false;
      this.waitingKey = false;
      window.clearTimeout(this.waitingKeyTimer);
    }
  },

  onMenuHide: function() {
    this.canvasIsFocused = true;
  },

  onKeydown: function(e) {
    if (this.waitingKey || !this.canvasIsFocused || this.keys.indexOf(e.keyCode) < 0) {
      return;
    }

    this.waitingKey = e.keyCode;
    this.waitingKeyIsShortPress = true;
    this.waitingKeyTimer = window.setTimeout(this.onKeyHold.bind(this), 100);
  },

  onKeyup: function(e) {
    var keyCode = e.keyCode;

    if (keyCode !== this.waitingKey || !this.canvasIsFocused) {
      return;
    }

    window.clearTimeout(this.waitingKeyTimer);

    if (this.waitingKeyIsShortPress) {
      switch (this.waitingKey) {
        case 87: this.sendLook('up'); break; //w
        case 65: this.sendLook('left'); break; //a
        case 83: this.sendLook('down'); break; //s
        case 68: this.sendLook('right'); break; //d
        case 69: this.sendInteract(); break; //e
      }
    }

    this.waitingKey = false;
  },

  onKeyHold: function() {
    this.waitingKeyIsShortPress = false;
    this.waitingKeyTimer = window.setTimeout(this.onKeyHold.bind(this), this.players[game.playerName].moveSpeed);
    
    if (this.players[game.playerName].nextMoves.length > 1) {
      return;
    }

    switch (this.waitingKey) {
      case 87: this.sendMove('up'); break; //w
      case 65: this.sendMove('left'); break; //a
      case 83: this.sendMove('down'); break; //s
      case 68: this.sendMove('right'); break; //d
      case 69: this.sendInteract(); break; //e
    }
  },

  sendMove: function(direction) {
    game.send({
      'service': 'overworld',
      'method' : 'move',
      'direction' : direction
    });
  },

  sendLook: function(direction) {
    game.send({
      'service' : 'overworld',
      'method' : 'look',
      'direction' : direction
    });
  },

  sendInteract: function() {
    game.send({
      'service' : 'overworld',
      'method' : 'interact'
    });
  },

  load: function(json) {
    var playerName = json.name;
        mapId = json.map,
        otherPlayers = json.otherPlayers,

    me.levelDirector.loadLevel(mapId);

    game.playerName = this._addTrainer(json).name;
    me.game.viewport.follow(this.players[game.playerName], me.game.viewport.AXIS.BOTH);

    for (var i = 0; i < otherPlayers.length; i++) {
      this._addTrainer(otherPlayers[i]);
    }
  },

  join: function(json) {
    this.players[json.name] = me.entityPool.newInstanceOf('trainer', json);
    me.game.add(this.players[json.name]);
    me.game.sort();
  },

  leave: function(json) {
    me.game.remove(this.players[json.name]);
    me.entityPool.freeInstance(this.players[json.name]);
  },

  _addTrainer: function(json) {
    var name = json.name,
        player = this.players[name] = new me.entityPool.newInstanceOf('trainer', json);

    if (false) console.log('player name: ' + player.name + ' expected: ' + name);
    player.name = name; // bug workaround
    me.game.add(player);
    return player;
  },

  move: function(json) {
    this.players[json.name]['walk' + json.direction](json.x, json.y);
  },

  look: function(json) {
    this.players[json.name]['look' + json.direction]();
  }
});
/* Game namespace */
var game = (function() {
  var game = {};
  var menus = {};

  var dispatch = function(json) {
    var action = json.action;

    if (menus[action]) {
      if (!menus[action].dispatch) {
        menus[action] = new menus[action]();
      }

      menus[action].dispatch(json);
    }
    else {
      console.error("No menu defined for action : " + action);
    }
  };

  var socket = new WebSocket('ws://'+document.location.hostname+':'+document.location.port+'/socket');
  socket.onmessage = function(message) {
    dispatch(JSON.parse(message.data));
  };

  game.playerName = null;

  game.subscribe = function(action, menu) {
    if (menus[action]) {
      console.error("Cannot redefine menu : "+action);
    }
    else {
      menus[action] = menu;
    }
  };

  game.send = function(json) {
    socket.send(JSON.stringify(json));
  };

  // Run on page load.
  game.onload = function () {
    // Initialize the video.
    if (!me.video.init("screen", 512, 512, true, 1)) {
        alert("Your browser does not support HTML5 canvas.");
        return;
    }

    me.loader.onload = this.loaded.bind(this);
    me.audio.init("mp3,ogg");
    me.loader.preload(game.resources);
    me.state.change(me.state.LOADING);
  };

  // Run on game resources loaded.
  game.loaded = function () {
    me.state.LOGIN = me.state.USER + 0;

    me.state.set(me.state.PLAY, new me.screen.PlayScreen());
    me.state.set(me.state.LOGIN, new me.screen.LoginScreen());

    // Add the entities
    me.entityPool.add('player', game.PlayerEntity);
    me.entityPool.add('trainer', game.TrainerEntity, true);

    // enable the keyboard
    me.sys.gravity = 0;
    me.input.bindKey(me.input.KEY.LEFT, "left");
    me.input.bindKey(me.input.KEY.RIGHT, "right");
    me.input.bindKey(me.input.KEY.UP, "up");
    me.input.bindKey(me.input.KEY.DOWN, "down");
    me.input.bindKey(me.input.KEY.ENTER, "enter");

    me.state.change(me.state.LOGIN);
  };

  return game;
})(window);
/* Game namespace */
var game = {
  player: null,
  players : {},
  menus : {},

  socket : (function() {
    // TODO
  })(),

  // Run on page load.
  "onload" : function () {
      // Initialize the video.
      if (!me.video.init("screen", 512, 512, true, 1)) {
          alert("Your browser does not support HTML5 canvas.");
          return;
      }

      // Turn off gravity
      me.sys.gravity = 0;

      // Initialize the audio.
      me.audio.init("mp3,ogg");

      // Set a callback to run when loading is complete.
      me.loader.onload = this.loaded.bind(this);
   
      // Load the resources.
      me.loader.preload(game.resources);

      // Initialize melonJS and display a loading screen.
      me.state.change(me.state.LOADING);
  },

  // Run on game resources loaded.
  "loaded" : function () {
    me.state.LOGIN = me.state.USER + 0;

    me.state.set(me.state.PLAY, new game.PlayScreen());
    me.state.set(me.state.LOGIN, new game.LoginScreen());

    // Add the entities
    me.entityPool.add('player', game.PlayerEntity);
    me.entityPool.add('trainer', game.TrainerEntity);

    // enable the keyboard
    me.input.bindKey(me.input.KEY.LEFT, "left");
    me.input.bindKey(me.input.KEY.RIGHT, "right");
    me.input.bindKey(me.input.KEY.UP, "up");
    me.input.bindKey(me.input.KEY.DOWN, "down");
    me.input.bindKey(me.input.KEY.ENTER, "enter");

    me.state.change(me.state.LOGIN);
  }
};
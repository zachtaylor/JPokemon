/* Game namespace */
var game = (function() {
  var game = {};
  var menus = {};
  var realmenus = {}; // will replace menus
  var controllers = {};

  var dispatch = function(json) {
    var action = json.action;

    if (menus[action]) {
      // Legacy style
      if (!menus[action].dispatch) {
        menus[action] = new menus[action]();
      }

      menus[action].dispatch(json);
    }
    else {
      // This is the new way to do it
      var method = 'update';

      if (action.indexOf(':') >= 0) {
        method = action.substr(action.indexOf(':') + 1);
        action = action.substr(0, action.indexOf(':'));
      }

      if (!realmenus[action]) {
        var controller = game.getController(action);
        realmenus[action] = new controller();
      }

      realmenus[action].show();
      realmenus[action][method](json);
    }
  };

  var socket = new WebSocket('ws://'+document.location.hostname+':'+document.location.port+'/socket');
  socket.onmessage = function(message) {
    dispatch(JSON.parse(message.data));
  };

  game.subscribe = function(action, menu) {
    if (menus[action]) {
      console.error("Cannot redefine menu : "+action);
    }
    else {
      menus[action] = menu;
    }
  };

  game.control = function(name, config) {
    var constructor = function Controller() {
      this.name = name;
      this.view = $(Handlebars.getTemplate(name)()).appendTo('body');

      for (var i = 0; i < config.refs.length; i++) {
        var ref = config.refs[i];
        this[ref] = $('.' + name + '-' + ref, this.view);
      }

      config.api.constructor.apply(this);
    }

    for (var methodName in config.api) {
      constructor.prototype[methodName] = config.api[methodName];
    }
    constructor.prototype.show = function() {
      this.view.appendTo('body');
      this.view.show();
    };
    constructor.prototype.hide = function() {
      this.view.hide();
    }

    controllers[name] = constructor;
  };

  game.getController = function(name) {
    if (!controllers[name]) {
      $.ajax({
        url : 'control/' + name + '.js',
        dataType: "script",
        async: false,
        error : function(jqXHR, textStatus, e) {
          console.error("Error loading controller : " + name);
        }
      });
    }

    return controllers[name];
  }

  game.send = function(json) {
    socket.send(JSON.stringify(json));
  };

  // Run on page load.
  game.onload = function () {
    // Initialize the video.
    if (!me.video.init("screen", 768, 512, true, 1)) {
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

Handlebars.getTemplate = function(name) {
  if (Handlebars.templates === undefined || Handlebars.templates[name] === undefined) {
    $.ajax({
      url : 'view/' + name + '.handlebars',
      success : function(data) {
        if (Handlebars.templates === undefined) {
          Handlebars.templates = {};
        }
        Handlebars.templates[name] = Handlebars.compile(data);
      },
      async : false
    });
  }

  return Handlebars.templates[name];
};
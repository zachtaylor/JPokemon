/* Game namespace */
var game = (function() {
  var game = {};
  var menus = {};
  var controllers = {};
  var views = {};

  game.getController = function(name) {
    return controllers[name] || game.loadController(name);
  };

  game.loadController = function(name) {
    $.ajax({
      url : 'mvc/' + name + '.js',
      dataType: 'script',
      async: false,
      error : function(jqXHR, textStatus, e) {
        console.error(e);
      }
    });

    return controllers[name];
  };

  game.getView = function(name) {
    return views[name] || game.loadView(name);
  };

  game.loadView = function(name) {
    $.ajax({
      url : 'mvc/' + name + '.html',
      success : function(data) {
        views[name] = data;
      },
      async : false
    });

    return views[name];
  };

  game.dispatch = function(json) {
    var action = json.action,
        method = 'update';

    if (action.indexOf(':') > 0) {
      var colonIndex = action.indexOf(':');
      action = action.substring(0, colonIndex);
      method = json.action.substring(colonIndex + 1);
    }

    var receiver = game.getController(action);

    if (receiver) {
      receiver.show();
      receiver[method](json);
    }
  };

  var socket = new WebSocket('ws://'+document.location.hostname+':'+document.location.port+'/socket');
  socket.onmessage = function(message) {
    game.dispatch(JSON.parse(message.data));
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
    var constructor = function Controller(view) {
      this.name = name;
      this.view = $(game.getView(name)).appendTo('body');

      for (var i = 0; i < config.refs.length; i++) {
        var ref = config.refs[i];
        this[ref] = $('.' + name + '-' + ref, this.view);
      }
      for (var i = 0; i < config.subcontrols.length; i++) {
        var subcontrol = config.subcontrols[i];
        var subcontroller = game.getController(subcontrol);
        this[subcontrol] = new subcontroller();

        $('.controller-' + config.subcontrols[i], this.view).replaceWith(this[subcontrol].view);
      }

      config.api.constructor.apply(this);
    }

    for (var methodName in config.api) {
      constructor.prototype[methodName] = config.api[methodName];
    }
    constructor.prototype.show = function() {
      game.getController('main').showController(config.nav);
      this.view.show();
    }
    constructor.prototype.hide = function() {
      game.getController('main').hideController(config.nav);
      this.view.hide();
    }
    constructor.prototype.close = function() {
      game.getController('main').closeController(config.nav);
      this.view.hide();
    }

    controllers[name] = new constructor();
    game.getController('main').addController(config.nav, controllers[name]);
  };

  game.send = function(json) {
    socket.send(JSON.stringify(json));
  };

  // Run on page load.
  game.onload = function () {
    if (!me.video.init('screen', 768, 512, true, 1)) {
      alert('Your browser does not support HTML5 canvas.');
      return;
    }

    // Add the entities
    me.entityPool.add('player', game.PlayerEntity);
    me.entityPool.add('trainer', game.TrainerEntity, true);

    me.state.set(me.state.PLAY, new me.screen.PlayScreen());
    me.state.change(me.state.PLAY);

    // enable the keyboard
    me.sys.gravity = 0;
    me.input.bindKey(me.input.KEY.LEFT, "left");
    me.input.bindKey(me.input.KEY.RIGHT, "right");
    me.input.bindKey(me.input.KEY.UP, "up");
    me.input.bindKey(me.input.KEY.DOWN, "down");
    me.input.bindKey(me.input.KEY.ENTER, "enter");
  };

  return game;
})(window);

// Courtesy of StackOverflow!
// http://stackoverflow.com/questions/210717/using-jquery-to-center-a-div-on-the-screen
jQuery.fn.center = function() {
  this.css("position","absolute");
  this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
  this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
}
jQuery.fn.hcenter = function () {
  this.css("position","absolute");
  this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
  return this;
};
jQuery.fn.vcenter = function() {
  this.css("position","absolute");
  this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
  return this;
};
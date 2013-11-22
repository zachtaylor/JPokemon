/* Game namespace */
var game = (function() {
  var game = {};
  var menus = {};
  var realmenus = {}; // will replace menus
  var controllers = {};

  game.dispatch = function(json) {
    var action = json.action,
        method = 'update';

    if (action.indexOf(':') > 0) {
      var colonIndex = action.indexOf(':');
      action = action.substring(0, colonIndex);
      method = json.action.substring(colonIndex + 1);
    }

    var receiver = menus[action];
    if (!(receiver && receiver[method])) {
      receiver = realmenus[action];
    }
    if (!(receiver && receiver[method])) {
      var controller = game.getController(action);
      receiver = realmenus[action] = new controller();
    }

    if (receiver) {
      if (receiver.show) {
        receiver.show();
      }
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
    var constructor = function Controller() {
      this.name = name;
      this.view = $(Handlebars.getTemplate(name)()).appendTo('body');

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
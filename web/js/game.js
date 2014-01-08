/* Game namespace */
var game = (function() {
  var game = {};
  var menus = {};
  var controllers = {};
  var views = {};

  game.getMenu = function(name) {
    if (!menus[name]) {
      var controller = game.getController(name);
      menus[name] = new controller();
    }
    return menus[name];
  };

  game.getController = function(name) {
    return controllers[name] || game.loadController(name);
  };

  game.loadController = function(name) {
    var url = 'mvc/' + name.replace('.', '/') + '.js';

    $.ajax({
      url : url,
      dataType: 'script',
      async: false,
      error : function(jqXHR, textStatus, e) {
        throw e;
      }
    });

    return controllers[name];
  };

  game.control = function(name, config) {
    var constructor = function Controller() {
      this.name = name;

      var viewTemplate;
      if (typeof config.view === 'string') {
        viewTemplate = config.view;
      }
      else if (config.view === false) {
        viewTemplate = null;
      }
      else {
        viewTemplate = name;
      }
      
      if (viewTemplate) {
        this.view = $(game.getView(viewTemplate)).appendTo('body'); 
      }

      $.each(config, (function(key, value) {
        this[key] = value; // base case is whatever you set it as

        if (typeof value === 'string') {
          if (value === 'view') {
            return true; //continue;
          }

          var selection = $(value, this.view);

          if (selection.length > 0) {
            this[key] = selection;
          }
        }
        if (typeof value === 'object') {
          if (value.controller) {
            var controllerName = value.controller,
                controller = game.getController(controllerName);
            
            this[key] = new controller();

            if (value.selector && this[key].view) {
              $(value.selector, this.view).replaceWith(this[key].view);
            }
          }
        }
      }).bind(this));

      config.constructor.apply(this, arguments);
    }

    for (var fn in Object.keys(config)) {
      if (typeof config[fn] === 'function' && fn !== 'constructor') {
        constructor.prototype[fn] = config[fn];
      }
    }

    controllers[name] = constructor;
  };

  game.getView = function(name) {
    return views[name] || game.loadView(name);
  };

  game.loadView = function(name) {
    var url = 'mvc/' + name.replace('.', '/') + '.html';

    $.ajax({
      url : url,
      success : function(data) {
        views[name] = data;
      },
      error: function(jqXHR, textStatus, e) {
        throw e;
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

    var receiver = game.getMenu(action);

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

  game.send = function(json) {
    socket.send(JSON.stringify(json));
  };

  // Run on page load.
  game.onload = function () {
    if (!me.video.init('screen', 768, 512, true, 1)) {
      alert('Your browser does not support HTML5 canvas.');
      return;
    }
    
    game.subscribe('notification', {
      update: function(stuff) {
        Messenger().post({
          'message': stuff.text,
          'type': 'info',
        });
      },
      error: function(json) {
        Messenger().post({
          message: json.text,
          type: 'error'
        });
      }
    });

    // Add the entities
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
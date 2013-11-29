game.control('main', {
  refs: [
    'jpokemonButton',
    'navItems'
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.controllers = {};

      this.view.insertBefore('#screen');
      game.dispatch.defer({
        'action':'login:show'
      });
    },

    getNameFromConfig: function(config) {
      if (!config || typeof config === 'string') {
        return config;
      }
      return config.name;
    },

    addController: function(config, controller) {
      var name = this.getNameFromConfig(config);
      if (!name) { return; }

      var navItem;
      if (name === config) {
        navItem = new game.getController('main-navItem');
      }
      else {
        navItem = new game.getController('main-navItems');
      }
      navItem.configure(config, controller);

      this.controllers[name] = navItem;
      navItem.view.appendTo(this.navItems);
    },

    showController: function(config) {
      var name = this.getNameFromConfig(config);
      if (!name) { return; }

      this.controllers[name].showController();
    },

    hideController : function(config) {
      var name = this.getNameFromConfig(config);
      if (!name) { return; }

      this.controllers[name].hideController();
    },

    closeController : function(config) {
      var name = this.getNameFromConfig(config);
      if (!name) { return; }

      this.controllers[name].closeController();
      delete this.controllers[name];
    }
  }
});
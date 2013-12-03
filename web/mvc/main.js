game.control('main', {
  refs: [
    'navItems'
  ],
  subcontrols: {
    'dropdown' : 'main-navItems'
  },
  api: {
    constructor: function() {
      this.controllers = {};

      this.view.insertBefore('#screen');
      game.getMenu.defer('login');

      var requestableThings = ['friends', 'party', 'lobby'];

      this.dropdown.setName('JPokemon');
      for (var i = 0; i < requestableThings.length; i++) {
        var item = requestableThings[i];
        this['onClick' + item] = (function(item) {
                                    return function() {
                                      game.send({
                                        load : item
                                      });
                                    };
        })(item);

        this.dropdown.addRow(item, this['onClick' + item]);
      }
    },

    addMenu: function(name, menu, options) {
      var navItem;
      if (options) {
        var navItemsController = game.getController('main-navItems');
        navItem = new navItemsController(name, menu, options);
      }
      else {
        var navItemController = game.getController('main-navItem');
        navItem = new navItemController(name, menu);
      }

      this.controllers[name] = navItem;
      navItem.view.appendTo(this.navItems);
    },

    showMenu: function(name) {
      this.controllers[name].showMenu();
    },

    hideMenu : function(name) {
      this.controllers[name].hideMenu();
    },

    closeMenu : function(name) {
      this.controllers[name].closeMenu();
      delete this.controllers[name];
    }
  }
});
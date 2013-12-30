game.control('main.navItems', {
  'link' : '.dropdown-toggle',
  'menu' : '.dropdown-menu',

  constructor: function(name, controller, options) {
    this.controller = controller;

    if (name) {
      this.setName(name);
    }

    if (options) {
      $.each(options, (function(key, method) {
        this.addRow(key, method, controller)
      }).bind(this));

      $('<li class="divider"></li>').appendTo(this.menu);
      var toggleLink = $('<a href="#">Show/Hide</a>');
      toggleLink.click(this.onClick.bind(this));
      var toggleLinkListItem = $('<li></li>');

      toggleLink.appendTo(toggleLinkListItem);
      toggleLinkListItem.appendTo(this.menu);
    }

    this.menu.dropdown();
  },
  setName : function(name) {
    this.link.html(name + ' <b class="caret"></b>');
  },
  addRow : function(name, callback, scope) {
    var listItem = $('<li></li>'),
        link = $('<a href="#">' + name + '</a>');

    link.click(callback.bind(scope));
    link.appendTo(listItem);
    listItem.appendTo(this.menu);
  },
  onClick : function() {
    if (this.controller.view.is(':visible')) {
      this.hideMenu();
    }
    else {
      this.showMenu();
    }
  },
  showMenu: function() {
    this.view.addClass('active');
    this.controller.view.show();
  },
  hideMenu: function() {
    this.view.removeClass('active');
    this.controller.view.hide();
  },
  closeMenu:function() {
    this.controller.view.hide();
    this.view.remove();
  }
});
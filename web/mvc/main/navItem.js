game.control('main.navItem', {
  'link': '.link',

  constructor: function(name, controller) {
    this.controller = controller;

    this.link.html(name);
    this.link.click(this.onClick.bind(this));
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
    game.getMenu('overworld').onMenuHide();
  },
  closeMenu:function() {
    this.controller.view.hide();
    this.view.remove();
  }
});
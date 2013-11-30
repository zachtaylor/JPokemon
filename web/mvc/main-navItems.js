game.control('main-navItems', {
  refs:[
    'link',
    'dropdown'
  ],
  subcontrols:[
  ],
  api: {
    constructor: function(name, controller, options) {
      this.controller = controller;

      this.link.html(name + ' <b class="caret"></b>');

      $.each(options, (function(key, method) {
        var listItem = $('<li></li>'),
            link = $('<a href="#">' + key + '</a>');

        link.click(method.bind(controller));
        link.appendTo(listItem);
        listItem.appendTo(this.dropdown);
      }).bind(this));

      $('<li class="divider"></li>').appendTo(this.dropdown);
      var toggleLink = $('<a href="#">Show/Hide</a>');
      toggleLink.click(this.onClick.bind(this));
      var toggleLinkListItem = $('<li></li>');
      toggleLink.appendTo(toggleLinkListItem);
      toggleLinkListItem.appendTo(this.dropdown);

      this.dropdown.dropdown();
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
  }
});
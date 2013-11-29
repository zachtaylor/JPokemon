game.control('main-navItems', {
  refs:[
    'link',
    'dropdown'
  ],
  subcontrols:[
  ],
  api: {
    constructor: function() {
    },
    configure: function(config, controller) {
      this.controller = controller;

      this.link.html(config.name + ' <b class="caret"></b>');

      $.each(config.options, (function(key, methodName) {
        var link = $('<a href="#">' + key + '</a>');
        link.click(this.controller[methodName].bind(this.controller));
        var listItem = $('<li></li>');
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
        this.hideController();
      }
      else {
        this.showController();
      }
    },
    showController: function() {
      this.view.addClass('active');
      this.controller.view.show();
    },
    hideController: function() {
      this.view.removeClass('active');
      this.controller.view.hide();
    },
    closeController:function() {
      this.controller.view.hide();
      this.view.remove();
    }
  }
})
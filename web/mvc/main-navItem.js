game.control('main-navItem', {
  refs:[
    'link'
  ],
  subcontrols: [
  ],
  api : {
    constructor: function() {
    },
    configure: function(name, controller) {
      this.controller = controller;

      this.link.html(name);
      this.link.click(this.onClick.bind(this));
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
});
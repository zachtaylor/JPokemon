(function($) {
  me.screen = me.screen || {};

  me.screen.PlayScreen = me.ScreenObject.extend({
    init : function(config) {
      this.parent(config);
    },

    onResetEvent: function(config) {
      this.parent(config);
      game.dispatch({
        'action':'overworld:onResetEvent'
      });
    },
    
    onDestroyEvent: function() {
      this.parent();
    }
  });
})(window);
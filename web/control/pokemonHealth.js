game.control('pokemonHealth', {
  refs: [
    'healthBar'
  ],
  subcontrols : [
  ],
  api: {
    constructor : function() {
    },

    update : function(json) {
      var nextColor = null;

      if (json.healthPercent > 50) {
        nextColor = 'progress-bar-success';
      }
      else if (json.healthPercent > 25) {
        nextColor = 'progress-bar-warning';
      }
      else {
        nextColor = 'progress-bar-danger';
      }

      if (this.lastColor && this.lastColor !== nextColor) {
        this.healthBar.removeClass(this.lastColor);
      }
      this.healthBar.addClass(this.lastColor = nextColor);
      this.healthBar.css('width', json.healthPercent + '%');
    }
  }
});
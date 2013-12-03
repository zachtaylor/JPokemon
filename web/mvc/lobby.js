game.control('lobby', {
  refs: [
    'title',
    'container',
    'inputGroup',
    'input',
    'button',
    'searchResults'
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.view.center();
      this.view.draggable();

      
    }
  }
});
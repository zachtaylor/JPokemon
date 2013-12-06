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

      this.input.keypress(this.monitorInput.bind(this));

      this.sendSearchRequest();

      game.getMenu('main').addMenu('Lobby', this, {
        'My Lobby': this.showMyLobby
      });
      game.getMenu('main').showMenu('Lobby');
    },
    monitorInput: function(e) {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }
      if (e.which === 13) {
        this.sendSearchRequest();
        return;
      }

      this.searchTimeout = setTimeout(this.sendSearchRequest.bind(this), 500);
    },
    sendSearchRequest: function() {
      if (this.searchTimeout) {
        delete this.searchTimeout;
      }

      console.log('sending search request');
      game.send({
        'service' : 'lobby',
        'search' : this.input.val()
      });
    },
    fillSearchResults: function(json) {
      var lobbySearchResultController = game.getController('lobby-searchResult');

      this.searchResults.html('');
      for (var i = 0; i < json.names.length; i++) {
        var lobbySearchResult = new lobbySearchResultController();
        lobbySearchResult.setName(json.names[i]);
        lobbySearchResult.setTeamCount(2);
        this.searchResults.append(lobbySearchResult.view);
      }
    },
    showMyLobby: function() {
      this.title.html('My Lobby');
    },
    update: function(json) {
      console.log('lobby update');
      console.log(json);
    }
  }
});
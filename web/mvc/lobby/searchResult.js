game.control('lobby.searchResult', {
  'name' : '.lobby-search-result',
  constructor : function() {
  },
  setName: function(name) {
    this.name.html(name);
  },
  setTeamCount: function(teamCount) {
    this.view.html('');
    this.view.append(this.name);

    var button;
    for (var i = 0; i < teamCount; i++) {
      button = $('<td class="plus-green"></td>');
      button.click(this._generateButtonCallback(i, this.name.html()));
      this.view.append(button);
      this.view.append($('<td>' + i + '</td>'));
    }
  },
  _generateButtonCallback: function(team, name) {
    return function() {
      game.send({
        'service' : 'lobby',
        'configure' : 'addplayer',
        'team' : team,
        'name' : name
      });
    }
  }
});
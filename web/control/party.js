game.control('party', {
  refs : [
    'box',
    'pokemonName',
    'pokemonLevel',
    'pokemonConditionEffects'
  ],
  subcontrols: [
    'pokemonHealth'
  ],
  api : {
    constructor : function() {
      this.view.draggable();
      this.box.sortable();

      this.box.on('sortstop', this.onSortPokemon.bind(this));
    },

    update : function(json) {
      this.data = json;
      this.updateBox();
    },

    updateBox : function() {
      this.box.html('');
      var pokemonspriteController = game.getController('pokemonsprite');

      for (var pokemonIndex = 0; pokemonIndex < this.data.pokemon.length; pokemonIndex++) {
        var pokemonJson = this.data.pokemon[pokemonIndex];

        var listItem = $('<li pokemonIndex="' + pokemonIndex + '" class="panel-body"></li>');
        listItem.on('click', this.onClickPokemon.bind(this));
        listItem.css({
          'border': '2px solid transparent',
          'border-radius': '2px'
        });
        
        var subcontrol = new pokemonspriteController();
        subcontrol.update({
          pokemonNumber: pokemonJson.number
        });

        subcontrol.view.appendTo(listItem);
        listItem.appendTo(this.box);
      }
    },

    updatePokemonInfo : function(pokemonIndex) {
      var pokemonJson = this.data.pokemon[pokemonIndex];

      this.pokemonHealth.update({
        healthPercent : Math.floor(pokemonJson.health / pokemonJson.maxhealth * 100)
      });

      this.pokemonName.html(pokemonJson.name);
      this.pokemonLevel.html(pokemonJson.level);
      this.pokemonConditionEffects.html(pokemonJson.condition.join());
    },

    onClickPokemon : function(e) {
      if (this.currentPokemonSelected) {
        $(this.currentPokemonSelected).css('border', '2px solid transparent');
      }
      this.currentPokemonSelected = e.currentTarget;
      $(this.currentPokemonSelected).css({
        'border': '2px solid black'
      });

      var pokemonIndex = parseInt(e.currentTarget.getAttribute("pokemonindex"));
      this.updatePokemonInfo(pokemonIndex);
    },

    onSortPokemon : function(event, ui) {
      var seeking = -1,
          indexOld = -1,
          indexNew = -1;

      this.box.children().each(function(i) {
        var expectedIndex = i;
        var actualIndex = parseInt(this.getAttribute('pokemonIndex'));

        if (seeking >= 0) {
          if (actualIndex == seeking) {
            indexOld = actualIndex;
            indexNew = expectedIndex;
            return false;
          }
          return true;
        }

        if (actualIndex > expectedIndex + 1) {
          // Greater than 1 difference detects only slide up
          indexOld = actualIndex;
          indexNew = expectedIndex;
          return false;
        }

        if (actualIndex == expectedIndex + 1) {
          // detect slide down or adjacent swap
          seeking = expectedIndex;
        }
      });

      if (indexOld >= 0 && indexNew >= 0) {
        game.send({
          'service': 'party',
          'method': 'slide',
          'indexOld': indexOld,
          'indexNew': indexNew,
        });
      }
    }
  }
});
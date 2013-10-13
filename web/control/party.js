game.control('party', {
  refs : [
    'box',
    'pokemonName',
    'pokemonLevel',
    'pokemonConditionEffects',
    'pokemonHealth'
  ],
  api : {
    constructor : function() {
      this.view.show();
      this.view.draggable();
      this.box.sortable();
      //this.box.disableSelection();
    },

    update : function(json) {
      this.data = json;
      this.updateBox();
      console.log(json);
    },

    updateBox : function() {
      this.box.html("");
      for (var pokemonIndex = 0; pokemonIndex < this.data.pokemon.length; pokemonIndex++) {
        var pokemonJson = this.data.pokemon[pokemonIndex];
        var pokemonPanel = $('<li class="panel-body"></li>');
        var pokemonDiv = $('<div id="selectpokemon-pokemon" pokemonIndex="' + pokemonIndex + '" class="pokemon-sprite" style="background-position: 0px -'+ (80 * (pokemonJson.number - 1)) +'px"></div>');
        pokemonDiv.on("click", this.onClickPokemon.bind(this));
        pokemonPanel.html($(pokemonDiv));
        pokemonPanel.appendTo(this.box);
      }
    },

    updatePokemonInfo : function(pokemonIndex) {
      var pokemonJson = this.data.pokemon[pokemonIndex];
      var healthPercentage = pokemonJson.health / pokemonJson.maxhealth * 100;
      var healthBarColorClass = 'progress-bar-danger';

      if (healthPercentage > 50) {
        healthBarColorClass = 'progress-bar-success';
      }
      else if (healthPercentage > 25) {
        healthBarColorClass = 'progress-bar-warning';
      }

      this.pokemonHealth.removeClass('progress-bar-sucess progress-bar-warning progress-bar-danger');
      this.pokemonHealth.addClass(healthBarColorClass);
      this.pokemonHealth.css('width', healthPercentage + '%');

      this.pokemonName.html(pokemonJson.name);
      this.pokemonLevel.html(pokemonJson.level);
      this.pokemonConditionEffects.html(pokemonJson.condition.join());
    },

    onClickPokemon : function(e) {
      console.log(e);
      if (this.currentPokemonSelected) {
        $(this.currentPokemonSelected).removeClass("highlighted");
      }
      this.currentPokemonSelected = e.currentTarget;
      $(this.currentPokemonSelected).addClass("highlighted");
      var pokemonIndex = parseInt(e.currentTarget.getAttribute("pokemonindex"));
      this.updatePokemonInfo(pokemonIndex);
    }
  }
});

//this.pokemon.css('background-position', '0 -' + (80 * (pokemonJson.pokemonNumber - 1)));
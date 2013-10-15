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
      this.pokemonSpriteControllers = [];
      this.view.draggable();
      this.box.sortable();

      this.box.on('sortstop', this.onSortPokemon.bind(this));
    },

    update : function(json) {
      this.data = json;
      this.updateBox();
    },

    updateBox : function() {
      var pokemonspriteController = game.getController('pokemonsprite');

      for (var pokemonIndex = 0; pokemonIndex < this.data.pokemon.length || pokemonIndex < this.pokemonSpriteControllers.length; pokemonIndex++) {
        if (pokemonIndex < this.data.pokemon.length) {
          var pokemonJson = this.data.pokemon[pokemonIndex];

          if (pokemonIndex == this.pokemonSpriteControllers.length) {
            this.pokemonSpriteControllers[pokemonIndex] = new pokemonspriteController();

            var listItem = $('<li pokemonIndex="' + pokemonIndex + '" class="panel-body"></li>');
            listItem.on('click', this.onClickPokemon.bind(this));
            this.pokemonSpriteControllers[pokemonIndex].view.appendTo(listItem);
            listItem.appendTo(this.box);
          }

          this.pokemonSpriteControllers[pokemonIndex].update({
            pokemonNumber: pokemonJson.number
          });
        }
        else {
          this.pokemonSpriteControllers[pokemonIndex].view.hide();
        }
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
    },

    onSortPokemon : function(event, ui) {
      console.log(event);
    }
  }
});

//this.pokemon.css('background-position', '0 -' + (80 * (pokemonJson.pokemonNumber - 1)));
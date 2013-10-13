game.control('selectpokemon', {
  refs : [
    'pokemonDropdown',
    'acceptButton',
    'cancelButton',
    'pokemon',
    'healthMeter'
  ],
  api : {
    constructor : function() {
      this.view.draggable();

      this.pokemonDropdown.selectpicker({
        style : 'btn btn-primary',
        menuStyle : 'dropdown-inverse'
      });

      this.pokemonDropdown.on('change', this.onPokemonSelect.bind(this));
      this.acceptButton.on('click', this.onClickAcceptButton.bind(this));
      this.cancelButton.on('click', this.onClickCancelButton.bind(this));
    },

    update : function(json) {
      this.data = json;

      this.selectedPokemon = 0;
      this.updatePokemonDropdown();
      this.updatePokemonDetail();
      this.view.show();
    },

    close : function() {
      this.view.hide();
    },

    updatePokemonDropdown: function() {
      var optionsHtml = "";
      for (var pokemonIndex = 0; pokemonIndex < this.data.pokemon.length; pokemonIndex++) {
        optionsHtml += '<option value="' + pokemonIndex + '">' + this.data.pokemon[pokemonIndex].name + '</option>'
      }
      optionsHtml = this.pokemonDropdown.html($(optionsHtml));
    },

    updatePokemonDetail: function() {
      var pokemonJson = this.data.pokemon[this.selectedPokemon];

      this.pokemon.css('background-position', '0 -' + (80 * (pokemonJson.pokemonNumber - 1)));

      var healthPercentage = pokemonJson.health / pokemonJson.healthMax * 100;
      var healthBarColorClass = 'progress-bar-danger';

      if (healthPercentage > 50) {
        healthBarColorClass = 'progress-bar-success';
      }
      else if (healthPercentage > 25) {
        healthBarColorClass = 'progress-bar-warning';
      }

      this.healthMeter.removeClass('progress-bar-sucess progress-bar-warning progress-bar-danger');
      this.healthMeter.addClass(healthBarColorClass);
      this.healthMeter.css('width', healthPercentage + '%');

      console.log('selectpokemon updatepokemondetail with data...');
      console.log(pokemonJson);
    },

    onPokemonSelect: function() {
      this.selectedPokemon = parseInt(this.pokemonDropdown.find(':selected').val());
      this.updatePokemonDetail();
    },

    onClickAcceptButton : function() {
      game.send({
        pokemonIndex : this.selectedPokemon
      });
    },

    onClickCancelButton : function() {
      game.send({
        pokemonIndex : -1
      });
    }
  }
});
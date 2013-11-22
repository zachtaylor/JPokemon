game.control('login', {
  refs: [
    'formElement', // flat ui already uses 'login-form' css selector
    'usernameInputField',
    'button'
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.view.center();
      this.view.draggable();

      this.formElement.submit(this.onFormSubmit.bind(this));
    },

    close: function() {
      this.view.hide();
    },

    onFormSubmit: function(e) {
      e.preventDefault();

      var username = this.usernameInputField.val();
      game.send({
        login: username
      });
    },

    update: function(json) {
    }
  }
});
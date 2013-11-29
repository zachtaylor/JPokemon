game.control('login', {
  nav: 'Login',
  refs: [
    'formElement',
    'usernameInputField',
    'button'
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.view.center();
      this.view.draggable();

      this.usernameInputField.keyup(this.onUsernameInputFieldKeyup.bind(this));
      this.formElement.submit(this.onFormSubmit.bind(this));
    },

    onUsernameInputFieldKeyup: function(e) {
      if (e.keyCode === 13) {
        this.onFormSubmit(e);
      }
    },

    onFormSubmit: function(e) {
      e.preventDefault();

      var username = this.usernameInputField.val();
      game.send({
        login: username
      });
    }
  }
});
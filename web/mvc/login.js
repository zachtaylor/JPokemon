game.control('login', {
  'formElement': '.formElement',
  'usernameInputField': '.username-input',
  'loginButton' : '.login-button',
  'signupButton': '.signup-button',

  constructor: function() {
    this.view.center();
    this.view.draggable();

    this.usernameInputField.keyup(this.onUsernameInputFieldKeyup.bind(this));
    this.formElement.submit(this.onFormSubmit.bind(this));

    game.getMenu('main').addMenu('Login', this);
    game.getMenu('main').showMenu('Login');
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
  },

  accept : function() {
    this.formElement.addClass('has-success');
    setTimeout(function() {
      game.getMenu('main').closeMenu('Login');
    }, 500);
  }
});
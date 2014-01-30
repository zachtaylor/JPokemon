game.control('dresser.Dresser', {
  'avatarName': '.dresser-avatar-name',
  'avatarPicture': '.dresser-avatar-picture',
  'avatarsList': '.dresser-avatar-list',
  'acceptButton': '.dresser-accept-button',

  constructor: function() {
    this.view.center();
    this.view.draggable();
    this.acceptButton.click(this.acceptAvatar.bind(this));
  },

  open: function(json) {
    this.avatars = json.avatars;
    this.avatar = json.avatar;
    this.avatarsList.html(' ');

    for (var i = 0; i < this.avatars.length; i++) {
      var avatar = this.avatars[i],
          option = $('<div>' + avatar + '</div>');

      option.appendTo(this.avatarsList);
      option.click(this.focusAvatar(avatar).bind(this));
    }

    game.getMenu('main').addMenu('Dresser', this);
    game.getMenu('main').showMenu('Dresser');
    this.focusAvatar(this.avatar).apply(this);
  },

  close: function() {
    game.getMenu('main').closeMenu('Dresser');
  },

  focusAvatar: function(avatar) {
    return function() {
      this.avatar = avatar;
      this.avatarName.html(avatar);
      this.avatarPicture.css('background-image', 'url("resource/sprite/' + avatar + '.png")');
    }
  },

  acceptAvatar: function() {
    game.send({
      'service': 'dresser',
      'avatar': this.avatar
    });
  }
});
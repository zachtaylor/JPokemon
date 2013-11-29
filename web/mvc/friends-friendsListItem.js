game.control('friends-friendsListItem', {
  refs: [
    'buttonx',
    'nameLabel'
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.friend = null;
    },

    setFriend: function(friend) {
      this.friend = friend;
      this.nameLabel.html(this.friend);
    }
  }
});
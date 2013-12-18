game.control('friends', {
  'title': '.panel-heading',
  'container' : '.friends-container',
  'inputGroup' : '.input-group',
  'button' : '.btn',

  constructor: function() {
    this.view.center();
    this.view.draggable();
    this.currentTab = 'friends';

    game.getMenu('main').addMenu('Friends', this, {
      'Friends List': this.showFriendsTab,
      'Pending List': this.showPendingTab,
      'Blocked List': this.showBlockedTab
    });
    game.getMenu('main').showMenu('Friends');
  },

  showFriendsTab: function() {
    this.title.html('Friends List');
    this.container.html('');
    this.inputGroup.show();
    this.button.html('Add Friend');

    var friendsListItemController = game.getController('friends.listItem');
    for (var friendIndex = 0; friendIndex < this.data.friends.length; friendIndex++) {
      var friendsListItem = new friendsListItemController();
      friendsListItem.setFriend(this.data.friends[friendIndex]);
      this.container.append(friendsListItem.view);
    }
  },

  showPendingTab: function() {
    this.title.html('Pending List');
    this.container.html('');
    this.inputGroup.hide();
  },

  showBlockedTab: function() {
    this.title.html('Blocked List');
    this.container.html('');
    this.inputGroup.show();
    this.button.html('Block Player');
  },

  update: function(json) {
    this.data = json;

    if (this.currentTab === 'friends') {
      this.showFriendsTab();
    }
    else if (this.currentTab === 'pending') {
      this.showPendingTab();
    }
    else if (this.currentTab === 'blocked') {
      this.showBlockedTab();
    }
  }
});
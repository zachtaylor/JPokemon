game.control('friends', {
  refs: [
    'title',
    'friendsTab',
    'pendingTab',
    'blockedTab',
    'subcontrolContainer',
    'friendsList',
    'pendingList',
    'blockedList',
  ],
  subcontrols: [
  ],
  api: {
    constructor: function() {
      this.view.draggable();

      this.friendsTab.on('click', this.showFriendsTab.bind(this));
      this.pendingTab.on('click', this.showPendingTab.bind(this));
      this.blockedTab.on('click', this.showBlockedTab.bind(this));

      this.currentTab = this.friendsTab;
    },

    // 

    showFriendsTab: function() {
      $('a', this.currentTab).css({
        'color':'black',
        'border': '2px transparent'
      });
      $('a', this.currentTab = this.friendsTab).css({
        'color': 'rgb(39, 174, 96)',
        'border': '2px solid rgb(39, 174, 96)',
        'border-radius': '2px'
      });
      this.subcontrolContainer.html('');

      var friendsListItemController = game.getController('friends-friendsListItem');
      for (var friendIndex = 0; friendIndex < this.data.friends.length; friendIndex++) {
        var friendsListItem = new friendsListItemController();
        friendsListItem.setFriend(this.data.friends[friendIndex]);
        this.subcontrolContainer.append(friendsListItem.view);
      }
    },

    showPendingTab: function() {
      $('a', this.currentTab).css({
        'color':'black',
        'border': '2px transparent'
      });
      $('a', this.currentTab = this.pendingTab).css({
        'color': 'rgb(41, 128, 185)',
        'border': '2px solid rgb(41, 128, 185)',
        'border-radius': '2px'
      });
      this.subcontrolContainer.html('');
    },

    showBlockedTab: function() {
      $('a', this.currentTab).css({
        'color':'black',
        'border': '2px transparent'
      });
      $('a', this.currentTab = this.blockedTab).css({
        'color': 'rgb(192, 57, 43)',
        'border': '2px solid rgb(192, 57, 43)',
        'border-radius': '2px'
      });
      this.subcontrolContainer.html('');
    },

    update: function(json) {
      this.data = json;

      if (this.currentTab === this.friendsTab) {
        this.showFriendsTab();
      }
      else if (this.currentTab === this.pendingTab) {
        this.showPendingTab();
      }
      else if (this.currentTab === this.blockedTab) {
        this.showBlockedTab();
      }
    }
  }
});
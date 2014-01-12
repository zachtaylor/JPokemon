(function($) {
  var tileSize = 32;
  var namePadding = 2;

  game.TrainerEntity = me.ObjectEntity.extend({
    init: function(config) {
      this.z = config.z;
      this.name = config.name;
      this.spritewidth = config.spritewidth;
      this.spriteheight = config.spriteheight;
      this.parent(this._leftEdgeForTile(config.x), this._topEdgeForTile(config.y), config);

      this.collidable = false;
      this.alwaysUpdate = true;

      this.moveSpeed = 650;
      this.nextMoves = [];
      this.moving = false;
      this.lastMove = new Date().getTime();

      this.font = new me.Font('courier', 12, 'yellow');

      // set animations
      this.renderable.addAnimation('lookdown', [1], 5);
      this.renderable.addAnimation('lookleft', [4], 5);
      this.renderable.addAnimation('lookup', [7], 5);
      this.renderable.addAnimation('lookright', [10], 5);
      this.renderable.addAnimation('walkup', [7,8,7,6], 5);
      this.renderable.addAnimation('walkdown', [1,2,1,0], 5);
      this.renderable.addAnimation('walkleft', [4,5,4,3], 5);
      this.renderable.addAnimation('walkright', [10,11,10,9], 5);
      
      // align to the grid
      this.updateColRect((this.spritewidth - tileSize) / 2, tileSize, this.spriteheight - tileSize, tileSize);
    },

    update: function() {
      if (this.moving) {
        this.moving();
      }
      else if (this.readyToMove() && this.nextMoves.length > 0) {
        this.moving = this.nextMoves.shift();
        this.lastMove = new Date().getTime();
        this.moving();
      }

      return this.parent();
    },

    draw: function(context) {
      this.parent(context);

      context.save();

      var nameWidth = this.font.measureText(context, this.name).width + namePadding * 2;
      var nameLeft = this.pos.x + ((this.spritewidth - nameWidth) / 2);

      context.globalAlpha = 0.75;
      context.fillStyle = 'black';
      context.fillRect(nameLeft, this.pos.y, nameWidth, 14);
      context.globalAlpha = 1.0;
      this.font.draw(context, this.name, nameLeft + namePadding, this.pos.y);

      context.restore();
    },

    readyToMove: function() {
      var timeNow = new Date().getTime();
      return timeNow > this.lastMove + this.moveSpeed;
    },

    setCoordinates: function(x, y) {
      this.pos.x = this._leftEdgeForTile(x);
      this.pos.y = this._topEdgeForTile(y);
    },

    changeAnimation: function(animation) {
      if (!this.renderable.isCurrentAnimation(animation)) {
        this.renderable.setCurrentAnimation(animation);
      }
    },

    walkleft : function(x, y) {
      this.nextMoves.push(function() {
        var percentMoved = this._percentMoved();

        if (percentMoved >= 1) {
          percentMoved = 1;
          this.moving = false;
          this.lookleft();
        }

        this.setCoordinates(x + 1 - percentMoved, y);
        this.changeAnimation('walkleft');
      });
    },

    walkright: function(x, y) {
      this.nextMoves.push(function() {
        var percentMoved = this._percentMoved();

        if (percentMoved >= 1) {
          percentMoved = 1;
          this.moving = false;
          this.lookright();
        }

        this.setCoordinates(x - 1 + percentMoved, y);
        this.changeAnimation('walkright');
      });
    },

    walkup: function(x, y) {
      this.nextMoves.push(function() {
        var percentMoved = this._percentMoved();

        if (percentMoved >= 1) {
          percentMoved = 1;
          this.moving = false;
          this.lookup();
        }

        
        this.setCoordinates(x, y + 1 - percentMoved);
        this.changeAnimation('walkup');
      });
    },

    walkdown: function(x, y) {
      this.nextMoves.push(function() {
        var percentMoved = this._percentMoved();

        if (percentMoved >= 1) {
          percentMoved = 1;
          this.moving = false;
          this.lookdown();
        }

        this.setCoordinates(x, y - 1 + percentMoved);
        this.changeAnimation('walkdown');
      });
    },

    lookleft: function() {
      this.nextMoves.push(function() {
        this.lastMove -= this.moveSpeed;
        this.changeAnimation('lookleft');
        this.moving = false;
      });
    },

    lookup: function() {
      this.nextMoves.push(function() {
        this.lastMove -= this.moveSpeed;
        this.changeAnimation('lookup');
        this.moving = false;
      });
    },

    lookright: function() {
      this.nextMoves.push(function() {
        this.lastMove -= this.moveSpeed;
        this.changeAnimation('lookright');
        this.moving = false;
      });
    },

    lookdown: function() {
      this.nextMoves.push(function() {
        this.lastMove -= this.moveSpeed;
        this.changeAnimation('lookdown');
        this.moving = false;
      });
    },

    _leftEdgeForTile: function(tilex) {
      return (tilex * tileSize) - ((this.spritewidth - tileSize) / 2);
    },

    _topEdgeForTile: function(tiley) {
      return ((tiley + 1) * tileSize) - this.spriteheight;
    },

    _percentMoved: function() {
      return (new Date().getTime() - this.lastMove) / this.moveSpeed;
    }
  });
})(window);
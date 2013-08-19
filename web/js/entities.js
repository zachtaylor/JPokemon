(function($) {
  game.TrainerEntity = me.ObjectEntity.extend({
    collidable: false,

    init: function(x, y, settings) {
      // call the constructor
      this.nextX = x;
      this.nextY = y;
      x = this._xTileCoordinateToPixel(x);
      y = this._yTileCoordinateToPixel(y);
      this.parent(x, y, settings);

      this.moveQueue = [];
      this.walkSpeed = 2.45;
      this.alwaysUpdate = true;
      this.movementInterval = 200;
      this.lastMoved = new Date().getTime();

      // set the default horizontal & vertical speed (accel vector)
      this.collidable = false;
      this.setVelocity(this.walkSpeed, this.walkSpeed);

      // set animations
      this.renderable.addAnimation('walkup', [7,8,7,6], this.walkSpeed * 2);
      this.renderable.addAnimation('walkdown', [1,2,1,0], this.walkSpeed * 2);
      this.renderable.addAnimation('walkleft', [4,5,4,3], this.walkSpeed * 2);
      this.renderable.addAnimation('walkright', [10,11,10,9], this.walkSpeed * 2);
      
      // align to the grid
      this.updateColRect(8, 32, 24, 32);
    },

    update: function() {
      if (this.readyToMove()) {
        if (this.moveQueue.length > 0) {
          this.lastMoved = new Date().getTime();
          var moveCommand = this.moveQueue.shift();
          moveCommand.fn.apply(this, moveCommand.args);
        }
        else {
          var update = this.vel.x != 0 || this.vel.y != 0;
          this.vel.x = 0;
          this.vel.y = 0;
          this._setCoordinates(this.nextX, this.nextY);

          return update;
        }
      }

      this.updateMovement();

      if (this.vel.x != 0 || this.vel.y != 0) {
        this.parent();
        return true;
      }

      return false;
    },

    draw: function(context) {
      this.parent(context);

      if (this.name) {
        context.save();

        var nameWidth = this.font.measureText(context, this.name).width;
        var nameLeft = this.pos.x + ((game.TrainerEntity.settings.spritewidth - nameWidth) / 2) - this.nameBuffer;

        context.globalAlpha = 0.75;
        context.fillStyle = "black";
        context.fillRect(nameLeft, this.pos.y, nameWidth + 2 * this.nameBuffer, 10 + 2 * this.nameBuffer);
        context.globalAlpha = 1.0;
        this.font.draw(context, this.name, nameLeft + this.nameBuffer, this.pos.y);

        context.restore();
      }
    },
    
    setName: function(name) {
      this.name = name;
      this.font = new me.Font('courier', 12, 'yellow');
      this.nameBuffer = 3;
    },

    readyToMove: function() {
      var timeNow = new Date().getTime();
      return timeNow > this.lastMoved + this.movementInterval;
    },

    walkleft: function(x, y) {
      this.moveQueue.push({
        'fn': this._walkleft, 
        'args': [x,y]
      });
    },

    _walkleft: function(nextx, nexty) {
      this._setCoordinates(nextx + 1, nexty);
      this.nextX = nextx;
      this.nextY = nexty;

      this.renderable.setCurrentAnimation('walkleft');
      this.vel.x = -this.accel.x;
      this.vel.y = 0;
    },

    walkright: function(x, y) {
      this.moveQueue.push({
        'fn': this._walkright, 
        'args': [x,y]
      });
    },

    _walkright: function(nextx, nexty) {
      this._setCoordinates(nextx - 1, nexty);
      this.nextX = nextx;
      this.nextY = nexty;

      this.renderable.setCurrentAnimation('walkright');
      this.vel.x = this.accel.x;
      this.vel.y = 0;
    },

    walkup: function(x, y) {
      this.moveQueue.push({
        'fn': this._walkup,
        'args': [x,y]
      });
    },

    _walkup: function(nextx, nexty) {
      this._setCoordinates(nextx, nexty + 1);
      this.nextX = nextx;
      this.nextY = nexty;

      this.renderable.setCurrentAnimation('walkup');
      this.vel.y = -this.accel.y;
      this.vel.x = 0;
    },

    walkdown: function(x, y) {
      this.moveQueue.push({
        'fn': this._walkdown,
        'args': [x,y]
      });
    },

    _walkdown: function(nextx, nexty) {
      this._setCoordinates(nextx, nexty - 1);
      this.nextX = nextx;
      this.nextY = nexty;

      this.renderable.setCurrentAnimation('walkdown');
      this.vel.y = this.accel.y;
      this.vel.x = 0;
    },

    _setCoordinates: function(x,y) {
      this.pos.x = this._xTileCoordinateToPixel(x);
      this.pos.y = this._yTileCoordinateToPixel(y);
    },

    _xTileCoordinateToPixel: function(x) {
      return (x * 32) - (game.TrainerEntity.settings.spritewidth - 32) / 2;
    },

    _yTileCoordinateToPixel: function(y) {
      return (y * 32) - (game.TrainerEntity.settings.spriteheight - 32);
    }
  });

  game.PlayerEntity = game.TrainerEntity.extend({
    collidable: true,

    init: function(x, y, settings) {
      this.parent(x, y, settings);

      this.lastInput = new Date().getTime();

      me.game.viewport.follow(this.pos, me.game.viewport.AXIS.BOTH);
    },

    update: function() {
      if (this.readyForInput() && this.readyToMove()) {
        if (me.input.isKeyPressed('left')) {
          this.lastInput = new Date().getTime();
          game.send({
            'move': 'left'
          });
        }
        else if (me.input.isKeyPressed('right')) {
          this.lastInput = new Date().getTime();
          game.send({
            'move': 'right'
          });
        }
        else if (me.input.isKeyPressed('up')) {
          this.lastInput = new Date().getTime();
          game.send({
            'move': 'up'
          });
        }
        else if (me.input.isKeyPressed('down')) {
          this.lastInput = new Date().getTime();
          game.send({
            'move': 'down'
          });
        }
      }

      return this.parent();
    },

    readyForInput: function() {
      var timeNow = new Date().getTime();
      return timeNow > this.lastInput + this.movementInterval;
    },
  });

  game.TrainerEntity.settings = game.PlayerEntity.settings = {
    "spriteheight" : 56,
    "spritewidth" : 48,
    "image" : "male_protagonist"
  };

})(window);
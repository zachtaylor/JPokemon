(function($) {
  me.ui = me.ui || {};

  ///////////////////////////////////////////////
  // me.ui.Component
  //
  // opacity : the alpha with which to draw this component
  // color : background color of this component
  // border : border color of this component
  // onShow : callback function when this becomes visible
  // onHide : callback function when this becomes invisible
  ///////////////////////////////////////////////

  me.ui.Component = me.plugin.Base.extend({
    GUID : null,
    version : "0.9.5",
    z : 10,
    isEntity : true,
    floating : true,
    visible : false,

    init : function(config) {
      this.parent();
      this.config = config || {};
      this.GUID = me.utils.createGUID();
      this.needsUpdate = false;

      this.config.x = ~~this.config.x;
      this.config.y = ~~this.config.y;
      this.config.width = ~~this.config.width;
      this.config.height = ~~this.config.height;
      this.opacity = this.config.opacity !== undefined ? this.config.opacity : 1;
      this.color = this.config.color || false;
      this.border = this.config.border || false;
      this.onShow = this.config.onShow || this.onShow;
      this.onHide = this.config.onHide || this.onHide;

      this.rect = new me.Rect(new me.Vector2d(this.config.x, this.config.y), this.config.width, this.config.height);
    },

    setLeft : function(left) {
      this.needsUpdate = this.rect.left != left;
      this.rect.pos.x = left;
    },

    setTop : function(top) {
      this.needsUpdate = this.rect.top != top;
      this.rect.pos.y = top;
    },

    show : function() {
      if (!this.visible) {
        if (!me.game.getEntityByGUID(this.GUID)) {
          me.game.add(this, this.z);
          me.game.sort();
        }

        this.visible = true;
        this.onShow();
        me.game.repaint();
      }
    },

    onShow : function() {
    },
  
    hide : function() {
      if (this.visible) {
        this.visible = false;
        this.onHide();
        me.game.repaint();
      }
    },

    onHide : function() {
    },

    update : function() {
      if (this.needsUpdate) {
        this.needsUpdate = false;
        return true;
      }

      return false;
    },

    draw : function(context) {
      context.save();

      context.globalAlpha = this.opacity;

      if (this.color) {
        context.fillStyle = this.color;
        context.fillRect(this.rect.left, this.rect.top, this.rect.width, this.rect.height);
      }

      if (this.border) {
        context.lineWidth = 1;
        context.strokeStyle = this.border;
        context.strokeRect(this.rect.left, this.rect.top, this.rect.width, this.rect.height);
      }

      context.restore();
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Panel
  ///////////////////////////////////////////////

  var layouts = {
    x : {
      fit : function() {
        var currentX = this.rect.left + this.padding;

        this._forEachChild(function(child) {
          child.setLeft(currentX);
          currentX = child.rect.left + child.rect.width + this.padding;
        });

        this.rect.width = currentX - this.rect.left;
      },
      center : function() {
        var centerX = this.rect.left + (this.rect.width / 2);

        this._forEachChild(function(child) {
          child.setLeft(centerX - (child.rect.width / 2));
        });
      },
      relative : function() {
        var largestX = this.rect.width;

        this._forEachChild(function(child) {
          if (child.rect.width + 2 * this.padding > largestX) {
            largestX = child.rect.width + 2 * this.padding;
          }

          child.setLeft(this.rect.left + child.config.x + this.padding);
        });

        this.rect.width = largestX;
      },
      fill : function() {
        var largestX = this.rect.width;

        this._forEachChild(function(child) {
          if (child.rect.width + 2 * this.padding > largestX) {
            largestX = child.rect.width + 2 * this.padding;
          }

          child.setLeft(this.rect.left + this.padding);
        });

        this.rect.width = largestX;
        this._forEachChild(function(child) {
          child.rect.width = largestX - 2 * this.padding;
        });
      }
    },
    y : {
      fit : function() {
        var currentY = this.rect.top + this.padding;

        this._forEachChild(function(child) {
          child.setTop(currentY);
          currentY = child.rect.top + child.rect.height + this.padding;
        });

        this.rect.height = currentY - this.rect.top;
      },
      center : function() {
        var largestY = this.rect.height;
        var centerY = this.rect.top + (this.rect.height / 2);

        this._forEachChild(function(child) {
          if (child.rect.height  + 2 * this.padding > largestY) {
            largestY = child.rect.height + 2 * this.padding;
          }

          child.setTop(centerY - (child.rect.height / 2));
        });

        this.rect.height = largestY;
      },
      relative : function() {
        var largestY = this.rect.height;

        this._forEachChild(function(child) {
          if (child.rect.height  + 2 * this.padding > largestY) {
            largestY = child.rect.height + 2 * this.padding;
          }

          child.setTop(this.rect.top + child.config.y + this.padding);
        });

        this.rect.height = largestY;
      },
      fill : function() {
        var largestY = this.rect.height;

        this._forEachChild(function(child) {
          if (child.rect.height  + 2 * this.padding > largestY) {
            largestY = child.rect.height + 2 * this.padding;
          }

          child.setTop(this.rect.top + this.padding);
        });

        this.rect.height = largestY;
        this._forEachChild(function(child) {
          child.rect.height = largestY - 2 * this.padding;
        });
      }
    }
  };

  me.ui.Panel = me.ui.Component.extend({
    init : function(config) {
      this.parent(config);

      this.children = [];
      this.padding = config.padding !== undefined ? config.padding : 4;

      this.xlayout = config.xlayout || 'relative';
      this.ylayout = config.ylayout || 'relative';
    },

    add : function(child) {
      this.children.push(child);
      this.needsUpdate = true;
      return this;
    },

    setLeft : function(left) {
      var delta = left - this.rect.left;
      this.parent(left);

      this._forEachChild(function (child) {
        child.setLeft(child.rect.left + delta);
      });
    },

    setTop : function(top) {
      var delta = top - this.rect.top;
      this.parent(top);

      this._forEachChild(function (child) {
        child.setTop(child.rect.top + delta);
      });
    },

    onShow : function() {
      this._forEachChild(function(child) {
        child.visible = true;
        child.onShow();
      });
    },

    onHide : function() {
      this._forEachChild(function(child) {
        child.visible = false;
        child.onHide();
      });
    },

    update : function() {
      this._forEachChild(function(child) {
        this.needsUpdate = child.update() || this.needsUpdate;
      });

      if (this.needsUpdate) {
        this.doLayout();
      }

      return this.parent();
    },

    draw : function(context) {
      this.parent(context);

      this._forEachChild(function(child) {
        child.draw(context);
      });
    },

    doLayout : function() {
      this._forEachChild(function(child) {
        if (child.doLayout) {
          child.doLayout();
        }
      });

      layouts.x[this.xlayout].call(this);
      layouts.y[this.ylayout].call(this);
    },

    _forEachChild : function(fn) {
      for (var c = 0; c < this.children.length; c++) {
        fn.call(this, this.children[c]);
      }
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Clickable
  //
  // onClick : Callback function for clicking
  ///////////////////////////////////////////////

  me.ui.Clickable = me.ui.Component.extend({
    init : function(config) {
      this.parent(config);

      this.onClick = this.config.onClick || false;
    },

    onShow : function() {
      me.input.registerPointerEvent('mousedown', this.rect, this._clickPropogator.bind(this), true);
    },

    onHide : function() {
      me.input.releasePointerEvent('mousedown', this.rect);
    },

    _clickPropogator : function() {
      if (this.onClick) {
        this.onClick();
      }
    }
  });
  
  ///////////////////////////////////////////////
  // me.ui.Icon
  //
  // image : URL of the image to draw
  ///////////////////////////////////////////////

  me.ui.Icon = me.ui.Clickable.extend({
    init : function(config) {
      this.parent(config);

      this.padding = this.config.padding !== undefined ? this.config.padding : 4;

      this.setImage(this.config.image);
    },

    setImage : function(src) {
      this.image = me.loader.getImage(src);
      this.needsUpdate = true;
    },

    onShow: function() {
      this.parent();
    },

    update : function() {
      if (this.needsUpdate) {
        this.rect.width = this.image.width + 2 * this.padding;
        this.rect.height = this.image.height + 2 * this.padding;
      }

      return this.parent();
    },

    draw : function(context) {
      this.parent(context);

      context.save();

      context.globalAlpha = 1.0;
      context.drawImage(this.image, this.rect.left + this.padding, this.rect.top + this.padding);

      context.restore();
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Label
  //
  // text : Text to display
  // padding : Pixels between the text and component edge
  // fontName : Name of the font to use
  // fontSize : Size of the font to use
  // fontColor : Color to draw the font
  ///////////////////////////////////////////////

  me.ui.Label = me.ui.Clickable.extend({
    init : function(config) {
      this.parent(config);

      this.padding = this.config.padding !== undefined ? this.config.padding : 4;
      this.fontName = this.config.fontName || 'courier';
      this.fontSize = this.config.fontSize || 12;
      this.fontColor = this.config.fontColor || 'white';
      this.font = new me.Font(this.fontName, this.fontSize, this.fontColor);

      this.setText(this.config.text || '');
    },

    getText : function() {
      return this.text;
    },

    setText : function(text) {
      this.needsUpdate = this.text !== text;
      this.text = text;
    },

    update : function() {
      if (this.needsUpdate) {
        var textSize = this.font.measureText(me.video.getScreenContext(), this.text);
        this.rect.height = textSize.height + 2 * this.padding - 4;
        this.rect.width = textSize.width + 2 * this.padding;
      }

      return this.parent();
    },

    draw : function(context) {
      this.parent(context);

      context.save();

      context.globalAlpha = 1.0;
      this.font.draw(context, this.text, this.rect.left + this.padding, this.rect.top + this.padding - 4);

      context.restore();
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Scrollable
  ///////////////////////////////////////////////

  me.ui.Scrollable = me.ui.Panel.extend({
    init : function(config) {
      this.parent(config);

      this.selectedIndex = 0;
      this.drawnItems = [];
    },

    scroll : function(e) {
      var evt = window.event || e;
      var delta = evt.detail ? -evt.detail : (evt.wheelDelta / -120);

      if (delta > 0.4  && this.selectedIndex + 1 < this.children.length) {
        this.selectedIndex++;
        this.needsUpdate = true;
      }
      else if (delta < -0.4 && this.selectedIndex > 0) {
        this.selectedIndex--;
        this.needsUpdate = true;
      }
    },

    onShow : function() {
      me.input.registerPointerEvent('mousewheel', this.rect, this.scroll.bind(this), true);
      this.parent();
    },

    onHide : function() {
      me.input.releasePointerEvent('mousewheel', this.rect);
      this.parent();
    },

    draw : function(context) {
      me.ui.Component.prototype.draw.call(this, context); // grandparent

      for (var i = 0; i < this.drawnItems.length; i++) {
        this.drawnItems[i].draw(context);
      }
    },

    doLayout : function() {
      var currentHeight = 0;
      this.drawnItems = [];

      for (var i = this.selectedIndex; i < this.children.length; i++) {
        if (currentHeight + this.children[i].rect.height <= this.rect.height) {
          this.children[i].setTop(this.rect.top + currentHeight);
          this.children[i].setLeft(this.rect.left + this.padding);
          this.drawnItems.push(this.children[i]);
          currentHeight += this.padding + this.children[i].rect.height;

          if (this.children[i].doLayout) this.children[i].doLayout();
        }
        else {
          break;
        }
      }
    }
  });

  ///////////////////////////////////////////////////
  // me.ui.Toggle
  //
  // toggle : If this component is currently toggled
  // toggleStyle : Style to use when toggling this component
  // onToggleChange : Callback function when toggle changes
  // toggleColor : Color to use when toggled
  // untoggleColor : Color to use when untoggle
  ///////////////////////////////////////////////////

  var toggleStyles = {
    select : function(containsPoint) {
      return containsPoint;
    },

    toggle : function(containsPoint) {
      if (containsPoint) {
        return !this.toggle;
      }
      return this.toggle;
    }
  };

  me.ui.Toggle = me.ui.Panel.extend({
    init : function(config) {
      this.parent(config);

      this.toggle = this.config.toggle || false;
      this.toggleStyle = this.config.toggleStyle || 'select';
      this.toggleColor = this.config.toggleColor !== undefined ? this.config.toggleColor : 'gray';
      this.color = this.untoggleColor = this.config.untoggleColor !== undefined ? this.config.untoggleColor : 'black';
      this.padding = config.padding !== undefined ? config.padding : 4;

      this.rect.containsPoint = this._containsPoint.bind(this);
      this.onToggleChange = config.onToggleChange || this.onToggleChange;
    },

    // Override for this.rect.containsPoint to be able to hear when 
    // this.rect DOES NOT contain the point!
    _containsPoint : function(x, y) {
      var containsPoint = me.Rect.prototype.containsPoint.call(this.rect, x, y);
      var oldToggle = this.toggle;
      this.toggle = toggleStyles[this.toggleStyle].call(this, containsPoint);
      var toggleChange = this.toggle !== oldToggle;

      if (toggleChange) {
        this.needsUpdate = this.visible;
        this.onToggleChange(this.toggle);
      }

      return containsPoint;
    },

    onShow : function() {
      // Crazy stuff for checking toggle
      me.input.registerPointerEvent('mousedown', this.rect, function() {}, true);
      this.parent();
    },

    onHide : function() {
      this.toggle = false;
      me.input.releasePointerEvent('mousedown', this.rect);
      this.parent();
    },

    update : function() {
      if (this.needsUpdate) {
        this.color = this.toggle ? this.toggleColor : this.untoggleColor;
      }

      return this.parent();
    },

    onToggleChange : function(toggle) {
    }
  });

  ///////////////////////////////////////////////
  // me.ui.InputBox
  //
  // onEnter : Callback function for pressing enter
  ///////////////////////////////////////////////

  me.ui.InputBox = me.ui.Toggle.extend({
    init : function(config) {
      this.parent(config);

      this.add(this.label = new me.ui.Label({
        fontSize : config.height - 2 * ~~config.padding,
        padding : config.padding
      }));

      this.padding = 0;
      
      window.addEventListener('keypress', this.onKeyPress.bind(this));
      window.addEventListener('keydown', this.onKeyDown.bind(this));

      this.onEnter = config.onEnter || this.onEnter;
    },

    getText : function() {
      return this.label.getText();
    },

    setText : function(text) {
      this.label.setText(text);
      this.needsUpdate = this.label.needsUpdate;
    },

    onKeyPress : function(e)  {
      if (!this.toggle) {
        return;
      }

      var newChar = String.fromCharCode(e.charCode);
      this.setText(this.getText() + newChar);

      me.game.repaint();
    },
    
    onKeyDown : function(e)  {
      if (!this.toggle) {
        return;
      }

      if (e.keyCode === 8) {
        var text = this.getText();
        text = text.substring(0, text.length - 1);
        this.setText(text);
        e.preventDefault();
      }
      else if (e.keyCode === me.input.KEY.ENTER) {
        this.onEnter();
      }
      else if (e.keyCode === me.input.KEY.SPACE) {
        this.setText(this.getText() + ' ');
        e.preventDefault();
      }

      me.game.repaint();
    },

    update : function() {
      if (this.needsUpdate) {
        this.rect.height = this.label.rect.height;
      }

      return this.parent();
    },

    onEnter : function() {
    }
  });
})(window);
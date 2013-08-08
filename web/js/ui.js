(function($) {
  me.ui = me.ui || {};

  ///////////////////////////////////////////////
  // me.ui.Component
  ///////////////////////////////////////////////

  me.ui.Component = me.plugin.Base.extend({
    GUID : null,
    version : "0.9.5",
    z : 100,
    floating : true,
    visible : false,

    init : function(config) {
      this.parent();
      this.config = config || {};

      this.config.x = ~~this.config.x;
      this.config.y = ~~this.config.y;
      this.config.width = ~~this.config.width;
      this.config.height = ~~this.config.height;
      this.opacity = this.config.opacity !== undefined ? this.config.opacity : 1;
      this.color = this.config.color || false;
      this.border = this.config.border || false;

      this.rect = new me.Rect(new me.Vector2d(this.config.x, this.config.y), this.config.width, this.config.height);
      this.GUID = 'component' + me.utils.createGUID();
    },

    show : function() {
      if (!this.visible) {
        if (!me.game.getEntityByGUID(this.GUID)) {
          me.game.add(this, this.z);
          me.game.sort();
        }

        this.visible = true;
        me.game.repaint();
      }
    },
  
    hide : function() {
      if (this.visible) {
        this.visible = false;
        me.game.repaint();
      }
    },

    update : function() {
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
          child.rect.pos = new me.Vector2d(child.config.x + currentX, child.rect.top);
          currentX += child.rect.width + this.padding;
        });

        this.rect.width = currentX;
      },
      center : function() {
        var centerX = this.rect.left + (this.rect.width / 2);

        this._forEachChild(function(child) {
          child.rect.pos = new me.Vector2d(centerX - (child.rect.width / 2), child.rect.top);
        });
      },
      relative : function() {
        this._forEachChild((function(child) {
          child.rect.pos = new me.Vector2d(this.rect.left + child.config.x + this.padding, child.rect.top);
        }).bind(this));
      }
    },
    y : {
      fit : function() {
        var currentY = this.rect.top + this.padding;

        this._forEachChild(function(child) {
          child.rect.pos = new me.Vector2d(child.rect.left, child.config.y + currentY);
          currentY = child.rect.top + child.rect.height + this.padding;
        });

        this.rect.height = currentY;
      },
      center : function() {
        var centerY = this.rect.top + (this.rect.height / 2);

        this._forEachChild(function(child) {
          child.rect.pos = new me.Vector2d(child.rect.left, centerY - (child.rect.top / 2));
        });
      },
      relative : function() {
        this._forEachChild((function(child) {
          child.rect.pos = new me.Vector2d(child.rect.left, this.rect.top + child.config.y + this.padding);
        }).bind(this));
      }
    }
  };

  me.ui.Panel = me.ui.Component.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = "panel" + me.utils.createGUID();

      this.children = [];
      this.padding = config.padding !== undefined ? config.padding : 4;
      
      this.xlayout = config.xlayout || 'relative';
      this.ylayout = config.ylayout || 'relative';
    },

    add : function(child) {
      this.children.push(child);
      return this;
    },

    show : function() {
      this.doLayout();
      this.parent();
    },

    update : function() {
      this._forEachChild(function(child) {
        child.update();
      });
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

      this._forEachChild(function(child) {
        if (child.doLayout) {
          child.doLayout();
        }
      });
    },

    _forEachChild : function(fn) {
      for (var c = 0; c < this.children.length; c++) {
        fn.call(this, this.children[c]);
      }
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Icon
  //
  // image : URL of the image to draw
  ///////////////////////////////////////////////

  me.ui.Icon = me.ui.Component.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = 'label' + me.utils.createGUID();

      this.setImage(this.config.image);
    },

    setImage : function(src) {
      this.image = me.loader.getImage(src);
    },

    draw : function(context) {
      this.parent(context);

      context.save();

      context.globalAlpha = 1.0;
      context.drawImage(this.image, this.rect.left, this.rect.top);

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

  me.ui.Label = me.ui.Component.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = 'label' + me.utils.createGUID();

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
      this.text = text;

      var textSize = this.font.measureText(me.video.getScreenContext(), this.text);
      this.rect.height = textSize.height + 2 * this.padding - 4;
      this.rect.width = textSize.width + 2 * this.padding;
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
  // me.ui.Button
  //
  // onClick : Callback function for click
  ///////////////////////////////////////////////

  me.ui.Button = me.ui.Label.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = "button" + me.utils.createGUID();

      this.onClick = this.config.onClick || function() {};
      me.input.registerPointerEvent('mousedown', this.rect, this._clickPropogator.bind(this), true);
    },

    _clickPropogator : function() {
      this.onClick();
    }
  });

  ///////////////////////////////////////////////
  // me.ui.Scrollable
  ///////////////////////////////////////////////

  me.ui.Scrollable = me.ui.Panel.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = "scrollable" + me.utils.createGUID();

      this.selectedIndex = 0;
      this.drawnItems = [];

      me.input.registerPointerEvent('mousewheel', this.rect, this.scroll.bind(this), true);
    },

    scroll : function(e) {
      var evt = window.event || e;
      var delta = evt.detail ? -evt.detail : (evt.wheelDelta / 120);

      if (delta > 1 && this.selectedIndex < this.children.length) {
        this.selectedIndex++;
        this._calculateDrawnChildren();
      }
      else if (delta < 0 && this.selectedIndex > 0) {
        this.selectedIndex--;
        this._calculateDrawnChildren();
      }
    },

    show : function() {
      this.parent();

      this._calculateDrawnChildren();
    },

    draw : function(context) {
      me.ui.Component.draw.apply(this, context); // grandparent

      this._forEachChild(function(child) {
        child.draw(context);
      });
    },

    _calculateDrawnChildren : function() {
      var currentHeight = 0;
      this.drawnItems = [];

      for (var i = this.selectedIndex; i < this.children.length; i++) {
        if (currentHeight + this.children[i].height <= this.height) {
          this.children[i].rect.top = currentHeight;
          this.drawnItems.push(this.children[i]);
          currentHeight += this.padding + this.children[i].height;
        }
        else {
          break;
        }
      }
    }
  });

  ///////////////////////////////////////////////////
  // me.ui.Focusable
  //
  // focus : If this component is currently focused
  // focusStyle : Style to use when focusing this component
  // onFocusChange : Callback function when focus changes
  // focusColor : Color to use when focus
  // unfocusColor : Color to use when unfocus
  ///////////////////////////////////////////////////

  var focusStyles = {
    select : function(containsPoint) {
      return containsPoint;
    },

    toggle : function(containsPoint) {
      if (containsPoint) {
        return !this.focus;
      }
      return this.focus;
    }
  };

  me.ui.Focusable = me.ui.Panel.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = "focusable" + me.utils.createGUID();

      this.focus = this.config.focus || false;
      this.focusStyle = this.config.focusStyle || 'select';
      this.focusColor = this.config.focusColor || 'gray';
      this.color = this.unfocusColor = this.config.unfocusColor || 'black';

      // Crazy stuff for checking focus
      this.rect.containsPoint = this._containsPoint.bind(this);
      me.input.registerPointerEvent('mousedown', this.rect, function() {}, true);

      this.onFocusChange = config.onFocusChange || me.ui.Focusable.prototype.onFocusChange;
    },

    // Override for this.rect.containsPoint to be able to hear when 
    // this.rect DOES NOT contain the point!
    _containsPoint : function(x, y) {
      var containsPoint = me.Rect.prototype.containsPoint.call(this.rect, x, y);
      var oldFocus = this.focus;
      this.focus = focusStyles[this.focusStyle].call(this, containsPoint);
      var focusChange = this.focus !== oldFocus;

      if (focusChange) {
        me.game.repaint();
        this.color = this.focus ? this.focusColor : this.unfocusColor;
        this.onFocusChange(this.focus);
      }

      return containsPoint;
    },

    onFocusChange : function(focus) {
    }
  });

  ///////////////////////////////////////////////
  // me.ui.InputBox
  //
  // onEnter : Callback function for pressing enter
  ///////////////////////////////////////////////

  me.ui.InputBox = me.ui.Focusable.extend({
    init : function(config) {
      this.parent(config);
      this.GUID = 'inputbox' + me.utils.createGUID();

      this.add(this.label = new me.ui.Label({
        fontSize : config.height - 2 * ~~config.padding,
        padding : config.padding
      }));

      this.padding = 0;
      this.rect.height = this.label.rect.height;
      
      window.addEventListener('keypress', this.onKeyPress.bind(this));
      window.addEventListener('keydown', this.onKeyDown.bind(this));

      this.onEnter = config.onEnter || me.ui.InputBox.prototype.onEnter;
    },

    getText : function() {
      return this.label.getText();
    },

    setText : function(text) {
      this.label.setText(text);
    },

    onKeyPress : function(e)  {
      if (!this.focus) {
        return;
      }

      var newChar = String.fromCharCode(e.charCode);
      this.setText(this.getText() + newChar);

      me.game.repaint();
    },
    
    onKeyDown : function(e)  {
      if (!this.focus) {
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

    onEnter : function() {
    }
  });
})(window);
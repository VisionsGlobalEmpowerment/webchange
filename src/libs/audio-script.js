(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define("AudioScript", [], factory);
	else if(typeof exports === 'object')
		exports["AudioScript"] = factory();
	else
		root["WaveSurfer"] = root["WaveSurfer"] || {}, root["WaveSurfer"]["AudioScript"] = factory();
})(this, function() {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/AudioScript.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/AudioScript.js":
/*!****************************!*\
  !*** ./src/AudioScript.js ***!
  \****************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _unsupportedIterableToArray(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

function _iterableToArrayLimit(arr, i) { if (typeof Symbol === "undefined" || !(Symbol.iterator in Object(arr))) return; var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

/**
 * @typedef {Object} AudioScriptPluginParams
 * @desc Extends the `WavesurferParams` wavesurfer was initialised with
 * @property {!string|HTMLElement} container CSS selector or HTML element where
 * the audioscript should be drawn. This is the only required parameter.
 * @property {number} notchPercentHeight=90 Height of notches in percent
 * @property {string} unlabeledNotchColor='#c0c0c0' The colour of the notches
 * that do not have labels
 * @property {string} primaryColor='#000' The colour of the main notches
 * @property {string} secondaryColor='#c0c0c0' The colour of the secondary
 * notches
 * @property {string} primaryFontColor='#000' The colour of the labels next to
 * the main notches
 * @property {string} secondaryFontColor='#000' The colour of the labels next to
 * the secondary notches
 * @property {number} labelPadding=5 The padding between the label and the notch
 * @property {?number} zoomDebounce A debounce timeout to increase rendering
 * performance for large files
 * @property {string} fontFamily='Arial'
 * @property {number} fontSize=10 Font size of labels in pixels
 * @property {?number} duration Length of the track in seconds. Overrides
 * getDuration() for setting length of audioscript
 * @property {function} formatTimeCallback (sec, pxPerSec) -> label
 * @property {function} timeInterval (pxPerSec) -> seconds between notches
 * @property {function} primaryLabelInterval (pxPerSec) -> cadence between
 * labels in primary color
 * @property {function} secondaryLabelInterval (pxPerSec) -> cadence between
 * labels in secondary color
 * @property {?number} offset Offset for the audioscript start in seconds. May also be
 * negative.
 * @property {?boolean} deferInit Set to true to manually call
 * `initPlugin('audioscript')`
 * @property {Object[]} timing
 */

/**
 * Adds a audioscript to the waveform.
 *
 * @implements {PluginClass}
 * @extends {Observer}
 * @example
 * // es6
 * import AudioScriptPlugin from 'wavesurfer.audioscript.js';
 *
 * // commonjs
 * var AudioScriptPlugin = require('wavesurfer.audioscript.js');
 *
 * // if you are using <script> tags
 * var AudioScriptPlugin = window.WaveSurfer.audioscript;
 *
 * // ... initialising wavesurfer with the plugin
 * var wavesurfer = WaveSurfer.create({
 *   // wavesurfer options ...
 *   plugins: [
 *     AudioScriptPlugin.create({
 *       // plugin options ...
 *     })
 *   ]
 * });
 */
var AudioScriptPlugin = /*#__PURE__*/function () {
  _createClass(AudioScriptPlugin, null, [{
    key: "create",

    /**
     * AudioScript plugin definition factory
     *
     * This function must be used to create a plugin definition which can be
     * used by wavesurfer to correctly instantiate the plugin.
     *
     * @param  {AudioScriptPluginParams} params parameters use to initialise the plugin
     * @return {PluginDefinition} an object representing the plugin
     */
    value: function create(params) {
      return {
        name: 'audioscript',
        deferInit: params && params.deferInit ? params.deferInit : false,
        params: params,
        instance: AudioScriptPlugin,
        staticProps: {
          setAudioScript: function setAudioScript(data) {
            if (!this.initialisedPluginList.audioscript) {
              this.initPlugin('audioscript');
            }

            if (this.audioscript) {
              this.audioscript.timing = data;
              this.audioscript.render();
            }
          }
        }
      };
    } // event handlers

  }]);

  /**
   * Creates an instance of AudioScriptPlugin.
   *
   * You probably want to use AudioScriptPlugin.create()
   *
   * @param {AudioScriptPluginParams} params Plugin parameters
   * @param {object} ws Wavesurfer instance
   */
  function AudioScriptPlugin(params, ws) {
    var _this = this;

    _classCallCheck(this, AudioScriptPlugin);

    _initialiseProps.call(this);

    this.container = 'string' == typeof params.container ? document.querySelector(params.container) : params.container;
    this.timing = params.timing || [];

    if (!this.container) {
      throw new Error('No container for wavesurfer audioscript');
    }

    this.wavesurfer = ws;
    this.util = ws.util;
    this.params = Object.assign({}, {
      height: 20,
      notchPercentHeight: 90,
      labelPadding: 5,
      unlabeledNotchColor: '#c0c0c0',
      primaryColor: '#000',
      secondaryColor: '#c0c0c0',
      primaryFontColor: '#000',
      secondaryFontColor: '#000',
      fontFamily: 'Arial',
      fontSize: 10,
      duration: null,
      zoomDebounce: false,
      offset: 0
    }, params);
    this.canvases = [];
    this.wrapper = null;
    this.drawer = null;
    this.pixelRatio = null;
    this.maxCanvasWidth = null;
    this.maxCanvasElementWidth = null;
    /**
     * This event handler has to be in the constructor function because it
     * relies on the debounce function which is only available after
     * instantiation
     *
     * Use a debounced function if `params.zoomDebounce` is defined
     *
     * @returns {void}
     */

    this._onZoom = this.params.zoomDebounce ? this.wavesurfer.util.debounce(function () {
      return _this.render();
    }, this.params.zoomDebounce) : function () {
      return _this.render();
    };
  }
  /**
   * Initialisation function used by the plugin API
   */


  _createClass(AudioScriptPlugin, [{
    key: "init",
    value: function init() {
      // Check if ws is ready
      if (this.wavesurfer.isReady) {
        this._onReady();
      } else {
        this.wavesurfer.once('ready', this._onReady);
      }
    }
    /**
     * Destroy function used by the plugin API
     */

  }, {
    key: "destroy",
    value: function destroy() {
      this.unAll();
      this.wavesurfer.un('redraw', this._onRedraw);
      this.wavesurfer.un('zoom', this._onZoom);
      this.wavesurfer.un('ready', this._onReady);
      this.wavesurfer.drawer.wrapper.removeEventListener('scroll', this._onScroll);

      if (this.wrapper && this.wrapper.parentNode) {
        this.wrapper.removeEventListener('click', this._onWrapperClick);
        this.wrapper.parentNode.removeChild(this.wrapper);
        this.wrapper = null;
      }
    }
    /**
     * Create a audioscript element to wrap the canvases drawn by this plugin
     *
     */

  }, {
    key: "createWrapper",
    value: function createWrapper() {
      var wsParams = this.wavesurfer.params;
      this.container.innerHTML = '';
      this.wrapper = this.container.appendChild(document.createElement('audioscript'));
      this.util.style(this.wrapper, {
        display: 'block',
        position: 'relative',
        userSelect: 'none',
        webkitUserSelect: 'none',
        height: "".concat(this.params.height, "px")
      });

      if (wsParams.fillParent || wsParams.scrollParent) {
        this.util.style(this.wrapper, {
          width: '100%',
          overflowX: 'hidden',
          overflowY: 'hidden'
        });
      }

      this.wrapper.addEventListener('click', this._onWrapperClick);
    }
    /**
     * Render the audioscript (also updates the already rendered audioscript)
     *
     */

  }, {
    key: "render",
    value: function render() {
      if (!this.wrapper) {
        this.createWrapper();
      }

      this.updateCanvases();
      this.updateCanvasesPositioning();
      this.renderCanvases();
    }
    /**
     * Add new audioscript canvas
     *
     */

  }, {
    key: "addCanvas",
    value: function addCanvas() {
      var canvas = this.wrapper.appendChild(document.createElement('canvas'));
      this.canvases.push(canvas);
      this.util.style(canvas, {
        position: 'absolute',
        zIndex: 4
      });
    }
    /**
     * Remove audioscript canvas
     *
     */

  }, {
    key: "removeCanvas",
    value: function removeCanvas() {
      var canvas = this.canvases.pop();
      canvas.parentElement.removeChild(canvas);
    }
    /**
     * Make sure the correct of audioscript canvas elements exist and are cached in
     * this.canvases
     *
     */

  }, {
    key: "updateCanvases",
    value: function updateCanvases() {
      var totalWidth = Math.round(this.drawer.wrapper.scrollWidth);
      var requiredCanvases = Math.ceil(totalWidth / this.maxCanvasElementWidth);

      while (this.canvases.length < requiredCanvases) {
        this.addCanvas();
      }

      while (this.canvases.length > requiredCanvases) {
        this.removeCanvas();
      }
    }
    /**
     * Update the dimensions and positioning style for all the audioscript canvases
     *
     */

  }, {
    key: "updateCanvasesPositioning",
    value: function updateCanvasesPositioning() {
      var _this2 = this;

      // cache length for performance
      var canvasesLength = this.canvases.length;
      this.canvases.forEach(function (canvas, i) {
        // canvas width is the max element width, or if it is the last the
        // required width
        var canvasWidth = i === canvasesLength - 1 ? _this2.drawer.wrapper.scrollWidth - _this2.maxCanvasElementWidth * (canvasesLength - 1) : _this2.maxCanvasElementWidth; // set dimensions and style

        canvas.width = canvasWidth * _this2.pixelRatio; // on certain pixel ratios the canvas appears cut off at the bottom,
        // therefore leave 1px extra

        canvas.height = (_this2.params.height + 1) * _this2.pixelRatio;

        _this2.util.style(canvas, {
          width: "".concat(canvasWidth, "px"),
          height: "".concat(_this2.params.height, "px"),
          left: "".concat(i * _this2.maxCanvasElementWidth, "px")
        });
      });
    }
    /**
     * Render the audioscript labels and notches
     *
     */

  }, {
    key: "renderCanvases",
    value: function renderCanvases() {
      var _this3 = this;

      var duration = this.params.duration || this.wavesurfer.backend.getDuration();

      if (duration <= 0) {
        return;
      }

      var wsParams = this.wavesurfer.params;
      var fontSize = this.params.fontSize * wsParams.pixelRatio;
      var width = wsParams.fillParent && !wsParams.scrollParent ? this.drawer.getWidth() : this.drawer.wrapper.scrollWidth * wsParams.pixelRatio;
      var height1 = this.params.height * this.pixelRatio;
      var height2 = this.params.height * (this.params.notchPercentHeight / 100) * this.pixelRatio;
      var pixelsPerSecond = width / duration;
      this.timing.forEach(function (_ref) {
        var start = _ref.start,
            end = _ref.end,
            word = _ref.word;
        var startPx = start * pixelsPerSecond;
        var endPx = end * pixelsPerSecond;

        _this3.setFillStyles(_this3.params.secondaryColor);

        _this3.setStrokeStyles(_this3.params.primaryColor);

        _this3.fillRect(startPx, 0, endPx - startPx, height1);

        _this3.setFonts("".concat(fontSize, "px ").concat(_this3.params.fontFamily));

        _this3.setFillStyles(_this3.params.primaryFontColor);

        _this3.fillText(word, startPx + _this3.params.labelPadding * _this3.pixelRatio, height2 - _this3.params.labelPadding, endPx - startPx - _this3.params.labelPadding * 2);
      });
    }
    /**
     * Set the canvas fill style
     *
     * @param {DOMString|CanvasGradient|CanvasPattern} fillStyle Fill style to
     * use
     */

  }, {
    key: "setFillStyles",
    value: function setFillStyles(fillStyle) {
      this.canvases.forEach(function (canvas) {
        canvas.getContext('2d').fillStyle = fillStyle;
      });
    }
  }, {
    key: "setStrokeStyles",
    value: function setStrokeStyles(strokeStyle) {
      this.canvases.forEach(function (canvas) {
        canvas.getContext('2d').strokeStyle = strokeStyle;
      });
    }
    /**
     * Set the canvas font
     *
     * @param {DOMString} font Font to use
     */

  }, {
    key: "setFonts",
    value: function setFonts(font) {
      this.canvases.forEach(function (canvas) {
        canvas.getContext('2d').font = font;
      });
    }
    /**
     * Draw a rectangle on the canvases
     *
     * (it figures out the offset for each canvas)
     *
     * @param {number} x X-position
     * @param {number} y Y-position
     * @param {number} width Width
     * @param {number} height Height
     */

  }, {
    key: "fillRect",
    value: function fillRect(x, y, width, height) {
      var _this4 = this;

      this.canvases.forEach(function (canvas, i) {
        var intersection = {
          x1: Math.max(x, i * _this4.maxCanvasWidth),
          y1: y,
          x2: Math.min(x + width, i * _this4.maxCanvasWidth + canvas.width),
          y2: y + height
        };
	  
        if (intersection.x1 < intersection.x2) {
          var ctx = canvas.getContext('2d');
          var x1 = intersection.x1 - i * _this4.maxCanvasWidth,
              y1 = intersection.y1,
              x2 = intersection.x2 - i * _this4.maxCanvasWidth,
              y2 = intersection.y2;

          var _map = [0, 1, 2, 3].map(function (i) {
            return i * Math.PI / 2;
          }),
              _map2 = _slicedToArray(_map, 4),
              angleRight = _map2[0],
              angleBottom = _map2[1],
              angleLeft = _map2[2],
              angleTop = _map2[3];

          var radius = Math.round(Math.min(20, (x2 - x1) / 2)); // ctx.fillStyle = "#c0c0c0";

          ctx.beginPath();
          ctx.moveTo(x1, y2);
          ctx.lineTo(x1, y1 + radius);
          ctx.arc(x1 + radius, y1 + radius, radius, angleLeft, angleTop);
          ctx.lineTo(x2 - radius, y1);
          ctx.arc(x2 - radius, y1 + radius, radius, angleTop, angleRight);
          ctx.lineTo(x2, y2); // ctx.fill();

          ctx.stroke();
        }
      });
    }
  }, {
    key: "truncateText",
    value: function truncateText(context, text, maxWidth) {
      var getWidth = function getWidth(t) {
        return context.measureText(t).width;
      };

      var width = getWidth(text);

      if (width <= maxWidth) {
        return text;
      }

      var ellipsis = '…';
      var ellipsisWidth = getWidth(ellipsis);

      if (ellipsisWidth > maxWidth) {
        return '';
      }

      var length = text.length;

      while (getWidth(text.substring(0, length)) >= maxWidth - ellipsisWidth && length > 0) {
        length -= 1;
      }

      return text.substring(0, length) + ellipsis;
    }
    /**
     * Fill a given text on the canvases
     *
     * @param {string} text Text to render
     * @param {number} x X-position
     * @param {number} y Y-position
     */

  }, {
    key: "fillText",
    value: function fillText(text, x, y, maxWidth) {
      var _this5 = this;

      var textWidth;
      var xOffset = 0;
      this.canvases.forEach(function (canvas) {
        var context = canvas.getContext('2d');
        var canvasWidth = context.canvas.width;

        if (xOffset > x + textWidth) {
          return;
        }

        if (xOffset + canvasWidth > x) {
          textWidth = context.measureText(text).width;
          context.fillText(_this5.truncateText(context, text, maxWidth), x - xOffset, y);
        }

        xOffset += canvasWidth;
      });
    }
  }]);

  return AudioScriptPlugin;
}();

exports.default = AudioScriptPlugin;

var _initialiseProps = function _initialiseProps() {
  var _this6 = this;

  this._onScroll = function () {
    if (_this6.wrapper && _this6.drawer.wrapper) {
      _this6.wrapper.scrollLeft = _this6.drawer.wrapper.scrollLeft;
    }
  };

  this._onRedraw = function () {
    return _this6.render();
  };

  this._onReady = function () {
    var ws = _this6.wavesurfer;
    _this6.drawer = ws.drawer;
    _this6.pixelRatio = ws.drawer.params.pixelRatio;
    _this6.maxCanvasWidth = ws.drawer.maxCanvasWidth || ws.drawer.width;
    _this6.maxCanvasElementWidth = ws.drawer.maxCanvasElementWidth || Math.round(_this6.maxCanvasWidth / _this6.pixelRatio); // add listeners

    ws.drawer.wrapper.addEventListener('scroll', _this6._onScroll);
    ws.on('redraw', _this6._onRedraw);
    ws.on('zoom', _this6._onZoom);

    _this6.render();
  };

  this._onWrapperClick = function (e) {
    e.preventDefault();
    var relX = 'offsetX' in e ? e.offsetX : e.layerX;

    _this6.fireEvent('click', relX / _this6.wrapper.scrollWidth || 0);
  };
};

module.exports = exports.default;

/***/ })

/******/ });
});
//# sourceMappingURL=audio-script.js.map

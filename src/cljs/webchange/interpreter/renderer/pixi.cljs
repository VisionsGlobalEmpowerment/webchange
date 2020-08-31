(ns webchange.interpreter.renderer.pixi
  (:require
    [pixi.js]
    [pixi-spine]
    [pixi-filters]))

(def Application (.. js/PIXI -Application))
(def Loader (.. js/PIXI -Loader))

(def Spine (.. js/PIXI -spine -Spine))

(def Container (.. js/PIXI -Container))
(def Graphics (.. js/PIXI -Graphics))
(def RegionAttachment (.. js/PIXI -spine -core -RegionAttachment))
(def Skin (.. js/PIXI -spine -core -Skin))
(def Sprite (.. js/PIXI -Sprite))
(def Text (.. js/PIXI -Text))
(def TextureAtlasRegion (.. js/PIXI -spine -core -TextureAtlasRegion))
(def WHITE (.. js/PIXI -Texture -WHITE))

(def ColorMatrixFilter (.. js/PIXI -filters -ColorMatrixFilter))
(def DropShadowFilter (.. js/PIXI -filters -DropShadowFilter))
(def GlowFilter (.. js/PIXI -filters -GlowFilter))
(def OutlineFilter (.. js/PIXI -filters -OutlineFilter))

(def shared-ticker (.. js/PIXI -Ticker -shared))

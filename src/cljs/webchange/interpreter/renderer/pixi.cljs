(ns webchange.interpreter.renderer.pixi
  (:require
    [pixi]))

(def Application (.. pixi -PIXI -Application))
(def Loader (.. pixi -PIXI -Loader))

(def Spine (.. pixi -PIXI -spine -Spine))

(def Container (.. pixi -PIXI -Container))
(def Graphics (.. pixi -PIXI -Graphics))
(def Rectangle (.. pixi -PIXI -Rectangle))
(def RegionAttachment (.. pixi -PIXI -spine -core -RegionAttachment))
(def Skin (.. pixi -PIXI -spine -core -Skin))
(def Sprite (.. pixi -PIXI -Sprite))
(def Text (.. pixi -PIXI -Text))
(def TextStyle (.. pixi -PIXI -TextStyle))
(def TextMetrics (.. pixi -PIXI -TextMetrics))
(def Texture (.. pixi -PIXI -Texture))
(def TextureAtlasRegion (.. pixi -PIXI -spine -core -TextureAtlasRegion))
(def TilingSprite (.. pixi -PIXI -TilingSprite))
(def WHITE (.. pixi -PIXI -Texture -WHITE))

(def ColorMatrixFilter (.. pixi -PIXI -filters -ColorMatrixFilter))
(def DropShadowFilter (.. pixi -PIXI -filters -DropShadowFilter))
(def GlowFilter (.. pixi -PIXI -filters -GlowFilter))
(def OutlineFilter (.. pixi -PIXI -filters -OutlineFilter))

(def shared-ticker (.. pixi -PIXI -Ticker -shared))

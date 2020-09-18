(ns webchange.interpreter.pixi
  (:require
    [pixi]))

(def PIXI (.. pixi -PIXI))


(def Application (.. PIXI -Application))
(def Loader (.. PIXI -Loader))
(def shared-ticker (.. PIXI -Ticker -shared))
(def clear-texture-cache (.. PIXI -utils -clearTextureCache))
(def rgb2hex (.. PIXI -utils -rgb2hex))
(def hex2rgb (.. PIXI -utils -hex2rgb))
(def string2hex (.. PIXI -utils -string2hex))

(def Spine (.. PIXI -spine -Spine))

(def Circle (.. PIXI -Circle))
(def Container (.. PIXI -Container))
(def Graphics (.. PIXI -Graphics))
(def Rectangle (.. PIXI -Rectangle))
(def RegionAttachment (.. PIXI -spine -core -RegionAttachment))
(def RenderTexture (.. PIXI -RenderTexture))
(def Skin (.. PIXI -spine -core -Skin))
(def Sprite (.. PIXI -Sprite))
(def Text (.. PIXI -Text))
(def TextStyle (.. PIXI -TextStyle))
(def TextMetrics (.. PIXI -TextMetrics))
(def Texture (.. PIXI -Texture))
(def TextureAtlasRegion (.. PIXI -spine -core -TextureAtlasRegion))
(def TilingSprite (.. PIXI -TilingSprite))
(def WHITE (.. PIXI -Texture -WHITE))

(def ColorMatrixFilter (.. PIXI -filters -ColorMatrixFilter))
(def DropShadowFilter (.. PIXI -filters -DropShadowFilter))
(def GlowFilter (.. PIXI -filters -GlowFilter))
(def OutlineFilter (.. PIXI -filters -OutlineFilter))

(def blend-mode-erase (.. PIXI -BLEND_MODES.ERASE))

(def sound (.. PIXI -sound))
(def SoundFilter (.. PIXI -sound -filters -Filter))

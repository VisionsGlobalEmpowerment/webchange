(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-add :as add]
    [webchange.ui-framework.components.icon.icon-add_box :as add-box]
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-audio :as audio]
    [webchange.ui-framework.components.icon.icon-background :as background]
    [webchange.ui-framework.components.icon.icon-bring-to-top :as bring-to-top]
    [webchange.ui-framework.components.icon.icon-clear :as clear]
    [webchange.ui-framework.components.icon.icon-close :as close]
    [webchange.ui-framework.components.icon.icon-drop-place :as drop-place]
    [webchange.ui-framework.components.icon.icon-edit :as edit]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]
    [webchange.ui-framework.components.icon.icon-image :as image]
    [webchange.ui-framework.components.icon.icon-link :as link]
    [webchange.ui-framework.components.icon.icon-menu :as menu]
    [webchange.ui-framework.components.icon.icon-menu-vertical :as menu-vertical]
    [webchange.ui-framework.components.icon.icon-mic :as mic]
    [webchange.ui-framework.components.icon.icon-music :as music]
    [webchange.ui-framework.components.icon.icon-music-off :as music-off]
    [webchange.ui-framework.components.icon.icon-play :as play]
    [webchange.ui-framework.components.icon.icon-remove :as remove]
    [webchange.ui-framework.components.icon.icon-settings :as settings]
    [webchange.ui-framework.components.icon.icon-stop :as stop]
    [webchange.ui-framework.components.icon.icon-sync :as sync]
    [webchange.ui-framework.components.icon.icon-undo :as undo]
    [webchange.ui-framework.components.icon.icon-volume :as volume]
    [webchange.ui-framework.components.icon.icon-warning :as warning]

    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def icons
  {"add"           add/data
   "add-box"       add-box/data
   "arrow-left"    arrow-left/data
   "arrow-right"   arrow-right/data
   "audio"         audio/data
   "background"    background/data
   "bring-to-top"  bring-to-top/data
   "clear"         clear/data
   "close"         close/data
   "drop-place"    drop-place/data
   "edit"          edit/data
   "font-family"   font-family/data
   "font-size"     font-size/data
   "image"         image/data
   "link"          link/data
   "menu"          menu/data
   "menu-vertical" menu-vertical/data
   "mic"           mic/data
   "music"         music/data
   "music-off"     music-off/data
   "play"          play/data
   "remove"        remove/data
   "settings"      settings/data
   "stop"          stop/data
   "sync"          sync/data
   "undo"          undo/data
   "volume"        volume/data
   "warning"       warning/data})

(defn component
  [{:keys [icon rotate? class-name]}]
  [:div {:class-name (get-class-name (cond-> {"wc-icon"          true
                                              (str "icon-" icon) true}
                                             (some? rotate?) (assoc "rotating" rotate?)
                                             (some? class-name) (assoc class-name true)))}
   (get icons icon)])

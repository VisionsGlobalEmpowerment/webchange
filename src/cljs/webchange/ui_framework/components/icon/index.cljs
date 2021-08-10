(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-add :as add]
    [webchange.ui-framework.components.icon.icon-add-box :as add-box]
    [webchange.ui-framework.components.icon.icon-arrow-first :as arrow-first]
    [webchange.ui-framework.components.icon.icon-arrow-last :as arrow-last]
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-audio :as audio]
    [webchange.ui-framework.components.icon.icon-background :as background]
    [webchange.ui-framework.components.icon.icon-bring-to-top :as bring-to-top]
    [webchange.ui-framework.components.icon.icon-cancel :as cancel]
    [webchange.ui-framework.components.icon.icon-check :as check]
    [webchange.ui-framework.components.icon.icon-clear :as clear]
    [webchange.ui-framework.components.icon.icon-close :as close]
    [webchange.ui-framework.components.icon.icon-delay :as delay]
    [webchange.ui-framework.components.icon.icon-drop-place :as drop-place]
    [webchange.ui-framework.components.icon.icon-edit :as edit]
    [webchange.ui-framework.components.icon.icon-effect :as effect]
    [webchange.ui-framework.components.icon.icon-expand-arrow-down :as expand-arrow-down]
    [webchange.ui-framework.components.icon.icon-expand-arrow-right :as expand-arrow-right]
    [webchange.ui-framework.components.icon.icon-expand :as expand]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]
    [webchange.ui-framework.components.icon.icon-image :as image]
    [webchange.ui-framework.components.icon.icon-insert-after :as insert-after]
    [webchange.ui-framework.components.icon.icon-insert-before :as insert-before]
    [webchange.ui-framework.components.icon.icon-insert-parallel :as insert-parallel]
    [webchange.ui-framework.components.icon.icon-link :as link]
    [webchange.ui-framework.components.icon.icon-match :as match]
    [webchange.ui-framework.components.icon.icon-menu :as menu]
    [webchange.ui-framework.components.icon.icon-menu-vertical :as menu-vertical]
    [webchange.ui-framework.components.icon.icon-mic :as mic]
    [webchange.ui-framework.components.icon.icon-mismatch :as mismatch]
    [webchange.ui-framework.components.icon.icon-music :as music]
    [webchange.ui-framework.components.icon.icon-music-off :as music-off]
    [webchange.ui-framework.components.icon.icon-play :as play]
    [webchange.ui-framework.components.icon.icon-preview :as preview]
    [webchange.ui-framework.components.icon.icon-remove :as remove]
    [webchange.ui-framework.components.icon.icon-restart :as restart]
    [webchange.ui-framework.components.icon.icon-settings :as settings]
    [webchange.ui-framework.components.icon.icon-stop :as stop]
    [webchange.ui-framework.components.icon.icon-swap :as swap]
    [webchange.ui-framework.components.icon.icon-sync :as sync]
    [webchange.ui-framework.components.icon.icon-text-animation :as text-animation]
    [webchange.ui-framework.components.icon.icon-user :as user]
    [webchange.ui-framework.components.icon.icon-undo :as undo]
    [webchange.ui-framework.components.icon.icon-visibility-off :as visibility-off]
    [webchange.ui-framework.components.icon.icon-visibility-on :as visibility-on]
    [webchange.ui-framework.components.icon.icon-volume :as volume]
    [webchange.ui-framework.components.icon.icon-warning :as warning]

    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def icons
  {"add"                add/data
   "add-box"            add-box/data
   "arrow-first"        arrow-first/data
   "arrow-last"         arrow-last/data
   "arrow-left"         arrow-left/data
   "arrow-right"        arrow-right/data
   "audio"              audio/data
   "background"         background/data
   "bring-to-top"       bring-to-top/data
   "cancel"             cancel/data
   "check"              check/data
   "clear"              clear/data
   "close"              close/data
   "delay"              delay/data
   "drop-place"         drop-place/data
   "edit"               edit/data
   "effect"             effect/data
   "expand-arrow-down"  expand-arrow-down/data
   "expand-arrow-right" expand-arrow-right/data
   "expand"             expand/data
   "font-family"        font-family/data
   "font-size"          font-size/data
   "image"              image/data
   "insert-after"       insert-after/data
   "insert-before"      insert-before/data
   "insert-parallel"    insert-parallel/data
   "link"               link/data
   "match"              match/data
   "menu"               menu/data
   "menu-vertical"      menu-vertical/data
   "mic"                mic/data
   "mismatch"           mismatch/data
   "music"              music/data
   "music-off"          music-off/data
   "play"               play/data
   "preview"            preview/data
   "remove"             remove/data
   "restart"            restart/data
   "settings"           settings/data
   "stop"               stop/data
   "swap"               swap/data
   "sync"               sync/data
   "text-animation"     text-animation/data
   "undo"               undo/data
   "user"               user/data
   "visibility-off"     visibility-off/data
   "visibility-on"      visibility-on/data
   "volume"             volume/data
   "warning"            warning/data})

(defn component
  [{:keys [icon on-click rotate? title class-name]}]
  [:div (cond-> {:class-name (get-class-name (cond-> {"wc-icon"          true
                                                      (str "icon-" icon) true}
                                                     (some? rotate?) (assoc "rotating" rotate?)
                                                     (some? class-name) (assoc class-name true)))}
                (fn? on-click) (assoc :on-click on-click)
                (some? title) (assoc :title title))
   (get icons icon)])

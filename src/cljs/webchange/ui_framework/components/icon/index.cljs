(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-add :as add]
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-audio :as audio]
    [webchange.ui-framework.components.icon.icon-close :as close]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]
    [webchange.ui-framework.components.icon.icon-image :as image]
    [webchange.ui-framework.components.icon.icon-link :as link]
    [webchange.ui-framework.components.icon.icon-mic :as mic]
    [webchange.ui-framework.components.icon.icon-play :as play]
    [webchange.ui-framework.components.icon.icon-remove :as remove]
    [webchange.ui-framework.components.icon.icon-settings :as settings]
    [webchange.ui-framework.components.icon.icon-stop :as stop]
    [webchange.ui-framework.components.icon.icon-sync :as sync]
    [webchange.ui-framework.components.icon.icon-undo :as undo]
    [webchange.ui-framework.components.icon.icon-volume :as volume]

    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def icons
  {"add"         add/data
   "arrow-left"  arrow-left/data
   "arrow-right" arrow-right/data
   "audio"       audio/data
   "close"       close/data
   "font-family" font-family/data
   "font-size"   font-size/data
   "image"       image/data
   "link"        link/data
   "mic"         mic/data
   "play"        play/data
   "remove"      remove/data
   "settings"    settings/data
   "stop"        stop/data
   "sync"        sync/data
   "undo"        undo/data
   "volume"      volume/data})

(defn component
  [{:keys [icon rotate? class-name]}]
  [:div {:class-name (get-class-name (cond-> {"wc-icon"          true
                                              (str "icon-" icon) true}
                                             (some? rotate?) (assoc "rotating" rotate?)
                                             (some? class-name) (assoc class-name true)))}
   (get icons icon)])

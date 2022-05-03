(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-add :as add]
    [webchange.ui-framework.components.icon.icon-add-box :as add-box]
    [webchange.ui-framework.components.icon.icon-align-center :as align-center]
    [webchange.ui-framework.components.icon.icon-align-justify :as align-justify]
    [webchange.ui-framework.components.icon.icon-align-left :as align-left]
    [webchange.ui-framework.components.icon.icon-align-right :as align-right]
    [webchange.ui-framework.components.icon.icon-animation :as animation]
    [webchange.ui-framework.components.icon.icon-animation-add :as animation-add]
    [webchange.ui-framework.components.icon.icon-animation-remove :as animation-remove]
    [webchange.ui-framework.components.icon.icon-arrow-first :as arrow-first]
    [webchange.ui-framework.components.icon.icon-arrow-last :as arrow-last]
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-audio :as audio]
    [webchange.ui-framework.components.icon.icon-back :as back]
    [webchange.ui-framework.components.icon.icon-background :as background]
    [webchange.ui-framework.components.icon.icon-book :as book]
    [webchange.ui-framework.components.icon.icon-book-library :as book-library]
    [webchange.ui-framework.components.icon.icon-bring-to-top :as bring-to-top]
    [webchange.ui-framework.components.icon.icon-cancel :as cancel]
    [webchange.ui-framework.components.icon.icon-check :as check]
    [webchange.ui-framework.components.icon.icon-chevron-down :as chevron-down]
    [webchange.ui-framework.components.icon.icon-chevron-left :as chevron-left]
    [webchange.ui-framework.components.icon.icon-chevron-right :as chevron-right]
    [webchange.ui-framework.components.icon.icon-chevron-up :as chevron-up]
    [webchange.ui-framework.components.icon.icon-clear :as clear]
    [webchange.ui-framework.components.icon.icon-close :as close]
    [webchange.ui-framework.components.icon.icon-presentation :as presentation]
    [webchange.ui-framework.components.icon.icon-create-book :as create-book]
    [webchange.ui-framework.components.icon.icon-create-game :as create-game]
    [webchange.ui-framework.components.icon.icon-dashboard :as dashboard]
    [webchange.ui-framework.components.icon.icon-delay :as delay]
    [webchange.ui-framework.components.icon.icon-drop-place :as drop-place]
    [webchange.ui-framework.components.icon.icon-edit :as edit]
    [webchange.ui-framework.components.icon.icon-effect :as effect]
    [webchange.ui-framework.components.icon.icon-expand-arrow-down :as expand-arrow-down]
    [webchange.ui-framework.components.icon.icon-expand-arrow-right :as expand-arrow-right]
    [webchange.ui-framework.components.icon.icon-expand :as expand]
    [webchange.ui-framework.components.icon.icon-flip :as flip]
    [webchange.ui-framework.components.icon.icon-font-color :as font-color]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]
    [webchange.ui-framework.components.icon.icon-game :as game]
    [webchange.ui-framework.components.icon.icon-game-library :as game-library]
    [webchange.ui-framework.components.icon.icon-group :as group]
    [webchange.ui-framework.components.icon.icon-image :as image]
    [webchange.ui-framework.components.icon.icon-image-contain :as image-contain]
    [webchange.ui-framework.components.icon.icon-image-cover :as image-cover]
    [webchange.ui-framework.components.icon.icon-image-no-size :as image-no-size]
    [webchange.ui-framework.components.icon.icon-insert-after :as insert-after]
    [webchange.ui-framework.components.icon.icon-insert-before :as insert-before]
    [webchange.ui-framework.components.icon.icon-insert-parallel :as insert-parallel]
    [webchange.ui-framework.components.icon.icon-link :as link]
    [webchange.ui-framework.components.icon.icon-logout :as logout]
    [webchange.ui-framework.components.icon.icon-match :as match]
    [webchange.ui-framework.components.icon.icon-menu :as menu]
    [webchange.ui-framework.components.icon.icon-menu-vertical :as menu-vertical]
    [webchange.ui-framework.components.icon.icon-mic :as mic]
    [webchange.ui-framework.components.icon.icon-mismatch :as mismatch]
    [webchange.ui-framework.components.icon.icon-movement :as movement]
    [webchange.ui-framework.components.icon.icon-music :as music]
    [webchange.ui-framework.components.icon.icon-music-off :as music-off]
    [webchange.ui-framework.components.icon.icon-play :as play]
    [webchange.ui-framework.components.icon.icon-preview :as preview]
    [webchange.ui-framework.components.icon.icon-remove :as remove]
    [webchange.ui-framework.components.icon.icon-restart :as restart]
    [webchange.ui-framework.components.icon.icon-school :as school]
    [webchange.ui-framework.components.icon.icon-settings :as settings]
    [webchange.ui-framework.components.icon.icon-slider :as slider]
    [webchange.ui-framework.components.icon.icon-stop :as stop]
    [webchange.ui-framework.components.icon.icon-students :as students]
    [webchange.ui-framework.components.icon.icon-swap :as swap]
    [webchange.ui-framework.components.icon.icon-sync :as sync]
    [webchange.ui-framework.components.icon.icon-text-animation :as text-animation]
    [webchange.ui-framework.components.icon.icon-text :as text]
    [webchange.ui-framework.components.icon.icon-user :as user]
    [webchange.ui-framework.components.icon.icon-users :as users]
    [webchange.ui-framework.components.icon.icon-undo :as undo]
    [webchange.ui-framework.components.icon.icon-visibility-off :as visibility-off]
    [webchange.ui-framework.components.icon.icon-visibility-on :as visibility-on]
    [webchange.ui-framework.components.icon.icon-volume :as volume]
    [webchange.ui-framework.components.icon.icon-warning :as warning]

    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def icons
  {"add"                add/data
   "add-box"            add-box/data
   "align-center"       align-center/data
   "align-justify"      align-justify/data
   "align-left"         align-left/data
   "align-right"        align-right/data
   "animation"          animation/data
   "animation-add"      animation-add/data
   "animation-remove"   animation-remove/data
   "arrow-first"        arrow-first/data
   "arrow-last"         arrow-last/data
   "arrow-left"         arrow-left/data
   "arrow-right"        arrow-right/data
   "audio"              audio/data
   "back"               back/data
   "background"         background/data
   "book"               book/data
   "book-library"       book-library/data
   "bring-to-top"       bring-to-top/data
   "cancel"             cancel/data
   "check"              check/data
   "chevron-down"       chevron-down/data
   "chevron-left"       chevron-left/data
   "chevron-right"      chevron-right/data
   "chevron-up"         chevron-up/data
   "clear"              clear/data
   "close"              close/data
   "presentation"       presentation/data
   "create-book"        create-book/data
   "create-game"        create-game/data
   "dashboard"          dashboard/data
   "delay"              delay/data
   "drop-place"         drop-place/data
   "edit"               edit/data
   "effect"             effect/data
   "expand-arrow-down"  expand-arrow-down/data
   "expand-arrow-right" expand-arrow-right/data
   "expand"             expand/data
   "flip"               flip/data
   "font-color"         font-color/data
   "font-family"        font-family/data
   "font-size"          font-size/data
   "game"               game/data
   "game-library"       game-library/data
   "group"              group/data
   "image"              image/data
   "image-contain"      image-contain/data
   "image-cover"        image-cover/data
   "image-no-size"      image-no-size/data
   "insert-after"       insert-after/data
   "insert-before"      insert-before/data
   "insert-parallel"    insert-parallel/data
   "link"               link/data
   "logout"             logout/data
   "match"              match/data
   "menu"               menu/data
   "menu-vertical"      menu-vertical/data
   "mic"                mic/data
   "mismatch"           mismatch/data
   "movement"           movement/data
   "music"              music/data
   "music-off"          music-off/data
   "play"               play/data
   "preview"            preview/data
   "remove"             remove/data
   "restart"            restart/data
   "school"             school/data
   "settings"           settings/data
   "slider"             slider/data
   "stop"               stop/data
   "students"           students/data
   "swap"               swap/data
   "sync"               sync/data
   "text-animation"     text-animation/data
   "text"               text/data
   "undo"               undo/data
   "user"               user/data
   "users"              users/data
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

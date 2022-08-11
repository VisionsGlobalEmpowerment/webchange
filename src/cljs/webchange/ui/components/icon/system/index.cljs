(ns webchange.ui.components.icon.system.index
  (:require
    [webchange.ui.components.icon.system.icon-account :as account]
    [webchange.ui.components.icon.system.icon-account-add :as account-add]
    [webchange.ui.components.icon.system.icon-account-remove :as account-remove]
    [webchange.ui.components.icon.system.icon-align-left :as align-left]
    [webchange.ui.components.icon.system.icon-align-right :as align-right]
    [webchange.ui.components.icon.system.icon-align-center :as align-center]
    [webchange.ui.components.icon.system.icon-archive :as archive]
    [webchange.ui.components.icon.system.icon-arrow-left :as arrow-left]
    [webchange.ui.components.icon.system.icon-arrow-right :as arrow-right]
    [webchange.ui.components.icon.system.icon-arrow-up :as arrow-up]
    [webchange.ui.components.icon.system.icon-book :as book]
    [webchange.ui.components.icon.system.icon-build :as build]
    [webchange.ui.components.icon.system.icon-calendar :as calendar]
    [webchange.ui.components.icon.system.icon-caret-down :as caret-down]
    [webchange.ui.components.icon.system.icon-caret-left :as caret-left]
    [webchange.ui.components.icon.system.icon-caret-right :as caret-right]
    [webchange.ui.components.icon.system.icon-caret-up :as caret-up]
    [webchange.ui.components.icon.system.icon-change-position :as change-position]
    [webchange.ui.components.icon.system.icon-character :as character]
    [webchange.ui.components.icon.system.icon-check :as check]
    [webchange.ui.components.icon.system.icon-close :as close]
    [webchange.ui.components.icon.system.icon-contain :as contain]
    [webchange.ui.components.icon.system.icon-cover :as cover]
    [webchange.ui.components.icon.system.icon-cup :as cup]
    [webchange.ui.components.icon.system.icon-dialogue :as dialogue]
    [webchange.ui.components.icon.system.icon-dnd :as dnd]
    [webchange.ui.components.icon.system.icon-download :as download]
    [webchange.ui.components.icon.system.icon-drop :as drop]
    [webchange.ui.components.icon.system.icon-duplicate :as duplicate]
    [webchange.ui.components.icon.system.icon-edit :as edit]
    [webchange.ui.components.icon.system.icon-edit-boxed :as edit-boxed]
    [webchange.ui.components.icon.system.icon-effects :as effects]
    [webchange.ui.components.icon.system.icon-emotion :as emotion]
    [webchange.ui.components.icon.system.icon-flip :as flip]
    [webchange.ui.components.icon.system.icon-image-broken :as image-broken]
    [webchange.ui.components.icon.system.icon-images :as images]
    [webchange.ui.components.icon.system.icon-info :as info]
    [webchange.ui.components.icon.system.icon-move :as move]
    [webchange.ui.components.icon.system.icon-movement :as movement]
    [webchange.ui.components.icon.system.icon-play :as play]
    [webchange.ui.components.icon.system.icon-plus :as plus]
    [webchange.ui.components.icon.system.icon-question :as question]
    [webchange.ui.components.icon.system.icon-record :as record]
    [webchange.ui.components.icon.system.icon-restore :as restore]
    [webchange.ui.components.icon.system.icon-rewind-backward :as rewind-backward]
    [webchange.ui.components.icon.system.icon-rewind-forward :as rewind-forward]
    [webchange.ui.components.icon.system.icon-search :as search]
    [webchange.ui.components.icon.system.icon-select :as select]
    [webchange.ui.components.icon.system.icon-statistics :as statistics]
    [webchange.ui.components.icon.system.icon-stop-recording :as stop-recording]
    [webchange.ui.components.icon.system.icon-template :as template]
    [webchange.ui.components.icon.system.icon-translate :as translate]
    [webchange.ui.components.icon.system.icon-trash :as trash]
    [webchange.ui.components.icon.system.icon-upload :as upload]
    [webchange.ui.components.icon.system.icon-visibility-off :as visibility-off]
    [webchange.ui.components.icon.system.icon-visibility-on :as visibility-on]
    [webchange.ui.components.icon.system.icon-zoom-in :as zoom-in]
    [webchange.ui.components.icon.system.icon-zoom-out :as zoom-out]
    [webchange.ui.components.icon.utils :refer [with-prefix]]))

(def data (with-prefix "system"
                       {"account"         account/data
                        "account-add"     account-add/data
                        "account-remove"  account-remove/data
                        "align-left"      align-left/data
                        "align-right"     align-right/data
                        "align-center"    align-center/data
                        "archive"         archive/data
                        "arrow-left"      arrow-left/data
                        "arrow-right"     arrow-right/data
                        "arrow-up"        arrow-up/data
                        "book"            book/data
                        "build"           build/data
                        "system-book"     book/data
                        "calendar"        calendar/data
                        "caret-down"      caret-down/data
                        "caret-left"      caret-left/data
                        "caret-right"     caret-right/data
                        "caret-up"        caret-up/data
                        "change-position" change-position/data
                        "character"       character/data
                        "check"           check/data
                        "close"           close/data
                        "contain"         contain/data
                        "cover"           cover/data
                        "cup"             cup/data
                        "dialogue"        dialogue/data
                        "dnd"             dnd/data
                        "download"        download/data
                        "drop"            drop/data
                        "duplicate"       duplicate/data
                        "edit"            edit/data
                        "edit-boxed"      edit-boxed/data
                        "effects"         effects/data
                        "emotion"         emotion/data
                        "flip"            flip/data
                        "image-broken"    image-broken/data
                        "images"          images/data
                        "info"            info/data
                        "move"            move/data
                        "movement"        movement/data
                        "play"            play/data
                        "plus"            plus/data
                        "question"        question/data
                        "record"          record/data
                        "restore"         restore/data
                        "rewind-backward" rewind-backward/data
                        "rewind-forward"  rewind-forward/data
                        "search"          search/data
                        "select"          select/data
                        "statistics"      statistics/data
                        "stop"            stop-recording/data
                        "template"        template/data
                        "translate"       translate/data
                        "trash"           trash/data
                        "upload"          upload/data
                        "visibility-off"  visibility-off/data
                        "visibility-on"   visibility-on/data
                        "zoom-in"         zoom-in/data
                        "zoom-out"        zoom-out/data}))

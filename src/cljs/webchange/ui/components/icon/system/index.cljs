(ns webchange.ui.components.icon.system.index
  (:require
    [webchange.ui.components.icon.system.icon-account :as account]
    [webchange.ui.components.icon.system.icon-account-add :as account-add]
    [webchange.ui.components.icon.system.icon-account-remove :as account-remove]
    [webchange.ui.components.icon.system.icon-archive :as archive]
    [webchange.ui.components.icon.system.icon-arrow-left :as arrow-left]
    [webchange.ui.components.icon.system.icon-arrow-right :as arrow-right]
    [webchange.ui.components.icon.system.icon-book :as book]
    [webchange.ui.components.icon.system.icon-calendar :as calendar]
    [webchange.ui.components.icon.system.icon-caret-down :as caret-down]
    [webchange.ui.components.icon.system.icon-caret-left :as caret-left]
    [webchange.ui.components.icon.system.icon-caret-right :as caret-right]
    [webchange.ui.components.icon.system.icon-caret-up :as caret-up]
    [webchange.ui.components.icon.system.icon-change-position :as change-position]
    [webchange.ui.components.icon.system.icon-check :as check]
    [webchange.ui.components.icon.system.icon-close :as close]
    [webchange.ui.components.icon.system.icon-cup :as cup]
    [webchange.ui.components.icon.system.icon-dnd :as dnd]
    [webchange.ui.components.icon.system.icon-download :as download]
    [webchange.ui.components.icon.system.icon-drop :as drop]
    [webchange.ui.components.icon.system.icon-duplicate :as duplicate]
    [webchange.ui.components.icon.system.icon-edit :as edit]
    [webchange.ui.components.icon.system.icon-edit-boxed :as edit-boxed]
    [webchange.ui.components.icon.system.icon-info :as info]
    [webchange.ui.components.icon.system.icon-play :as play]
    [webchange.ui.components.icon.system.icon-plus :as plus]
    [webchange.ui.components.icon.system.icon-restore :as restore]
    [webchange.ui.components.icon.system.icon-search :as search]
    [webchange.ui.components.icon.system.icon-statistics :as statistics]
    [webchange.ui.components.icon.system.icon-trash :as trash]
    [webchange.ui.components.icon.system.icon-upload :as upload]
    [webchange.ui.components.icon.system.icon-visibility-off :as visibility-off]
    [webchange.ui.components.icon.system.icon-visibility-on :as visibility-on]))

(def data {"account"         account/data
           "account-add"     account-add/data
           "account-remove"  account-remove/data
           "archive"         archive/data
           "arrow-left"      arrow-left/data
           "arrow-right"     arrow-right/data
           "book"            book/data
           "calendar"        calendar/data
           "caret-down"      caret-down/data
           "caret-left"      caret-left/data
           "caret-right"     caret-right/data
           "caret-up"        caret-up/data
           "change-position" change-position/data
           "check"           check/data
           "close"           close/data
           "cup"             cup/data
           "dnd"             dnd/data
           "download"        download/data
           "drop"            drop/data
           "duplicate"       duplicate/data
           "edit"            edit/data
           "edit-boxed"      edit-boxed/data
           "info"            info/data
           "play"            play/data
           "plus"            plus/data
           "restore"         restore/data
           "search"          search/data
           "statistics"      statistics/data
           "trash"           trash/data
           "upload"          upload/data
           "visibility-off"  visibility-off/data
           "visibility-on"   visibility-on/data})
(ns webchange.ui.index
  (:require
    [webchange.ui.components.audio-wave.views :as audio-wave-views]
    [webchange.ui.components.avatar.views :as avatar-views]
    [webchange.ui.components.breadcrumbs.views :as breadcrumbs-views]
    [webchange.ui.components.button.views :as button-views]
    [webchange.ui.components.card.views :as card-views]
    [webchange.ui.components.chip.views :as chip-views]
    [webchange.ui.components.complete-progress.views :as complete-progress-views]
    [webchange.ui.components.copy-link.views :as copy-link-views]
    [webchange.ui.components.date.views :as date-views]
    [webchange.ui.components.dialog.views :as dialog-views]
    [webchange.ui.components.file.views :as file-views]
    [webchange.ui.components.form.views :as form-views]
    [webchange.ui.components.icon.views :as icon-views]
    [webchange.ui.components.image.views :as image-views]
    [webchange.ui.components.info.views :as info-views]
    [webchange.ui.components.input.views :as input-views]
    [webchange.ui.components.input-error.views :as input-error-views]
    [webchange.ui.components.input-label.views :as input-label-views]
    [webchange.ui.components.link.views :as link-views]
    [webchange.ui.components.list.views :as list-views]
    [webchange.ui.components.logo.views :as logo-views]
    [webchange.ui.components.note.views :as note-views]
    [webchange.ui.components.overlay.views :as overlay-views]
    [webchange.ui.components.panel.views :as panel-views]
    [webchange.ui.components.password.views :as password-views]
    [webchange.ui.components.progress.views :as progress-views]
    [webchange.ui.components.range.views :as range-views]
    [webchange.ui.components.select.views :as select-views]
    [webchange.ui.components.switch.views :as switch-views]
    [webchange.ui.components.tab.views :as tab-views]
    [webchange.ui.components.text-area.views :as text-area-views]
    [webchange.ui.utils.get-class-name :as gcn]))

(def audio-wave audio-wave-views/audio-wave)
(def avatar avatar-views/avatar)
(def breadcrumbs breadcrumbs-views/breadcrumbs)
(def button button-views/button)
(def card card-views/card)
(def chip chip-views/chip)
(def circular-progress progress-views/circular-progress)
(def confirm dialog-views/confirm)
(def complete-progress complete-progress-views/complete-progress)
(def copy-link copy-link-views/copy-link)
(def date date-views/date)
(def dialog dialog-views/dialog)
(def file file-views/file)
(def flag icon-views/flag-icon)
(def form form-views/form)
(def icon icon-views/general-icon)
(def image image-views/image)
(def info info-views/info)
(def input input-views/input)
(def input-error input-error-views/input-error)
(def input-label input-label-views/input-label)
(def logo-with-name logo-views/logo-with-name)
(def link link-views/link)
(def list list-views/list)
(def list-item list-views/list-item)
(def loading-overlay overlay-views/loading-overlay)
(def navigation-icon icon-views/navigation-icon)
(def note note-views/note)
(def focus-overlay overlay-views/focus-overlay)
(def panel panel-views/panel)
(def password password-views/password)
(def range range-views/range)
(def select select-views/select)
(def social-icon icon-views/social-icon)
(def system-icon icon-views/system-icon)
(def tab tab-views/tab)
(def switch switch-views/switch)
(def text-area text-area-views/text-area)

(def get-class-name gcn/get-class-name)

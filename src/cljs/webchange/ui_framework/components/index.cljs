(ns webchange.ui-framework.components.index
  (:require
    [webchange.ui-framework.components.audio.index :as audio]
    [webchange.ui-framework.components.avatar.index :as avatar]
    [webchange.ui-framework.components.button.index :as button]
    [webchange.ui-framework.components.card.index :as card]
    [webchange.ui-framework.components.checkbox.index :as checkbox]
    [webchange.ui-framework.components.chip.index :as chip]
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]
    [webchange.ui-framework.components.confirm.index :as confirm]
    [webchange.ui-framework.components.date.index :as date]
    [webchange.ui-framework.components.date-str.index :as date-str]
    [webchange.ui-framework.components.dialog.index :as dialog]
    [webchange.ui-framework.components.file.index :as file]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.image.index :as image]
    [webchange.ui-framework.components.image-preview.index :as image-preview]
    [webchange.ui-framework.components.input.index :as input]
    [webchange.ui-framework.components.label.index :as label]
    [webchange.ui-framework.components.range-input.index :as range-input]
    [webchange.ui-framework.components.menu.index :as menu]
    [webchange.ui-framework.components.message.index :as message]
    [webchange.ui-framework.components.password.index :as password]
    [webchange.ui-framework.components.range-input.index :as range-input]
    [webchange.ui-framework.components.select.index :as select]
    [webchange.ui-framework.components.select-image.index :as select-image]
    [webchange.ui-framework.components.switcher.index :as switcher]
    [webchange.ui-framework.components.text-area.index :as text-area]
    [webchange.ui-framework.components.timeline.index :as timeline]
    [webchange.ui-framework.components.tooltip.index :as tooltip]
    [webchange.ui-framework.components.utils :as utils]))

(def audio audio/component)
(def avatar avatar/component)
(def button button/component)
(def card card/component)
(def checkbox checkbox/component)
(def chip chip/component)
(def circular-progress circular-progress/component)
(def date date/component)
(def date-str date-str/component)
(def dialog dialog/component)
(def file file/component)
(def icon icon/component)
(def icon-button icon-button/component)
(def image image/component)
(def image-preview image-preview/component)
(def input input/component)
(def label label/component)
(def menu menu/component)
(def message message/component)
(def password password/component)
(def range-input range-input/component)
(def select select/component)
(def select-image select-image/component)
(def switcher switcher/component)
(def text-area text-area/component)
(def timeline timeline/component)
(def tooltip tooltip/component)
(def with-confirmation confirm/with-confirmation)
(def with-custom-window confirm/with-custom-window)

(def get-class-name utils/get-class-name)

(defn confirm
  [message handler]
  (when (js/confirm message)
    (when (fn? handler)
      (handler))))

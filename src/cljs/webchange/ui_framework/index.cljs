(ns webchange.ui-framework.index
  (:require
    [webchange.ui-framework.components.button.index :as button]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.select.index :as select]
    [webchange.ui-framework.components.text-area.index :as text-area]))

(def button button/component)
(def icon icon/component)
(def icon-button icon-button/component)
(def select select/component)
(def text-area text-area/component)

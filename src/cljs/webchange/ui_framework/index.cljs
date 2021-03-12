(ns webchange.ui-framework.index
  (:require
    [webchange.ui-framework.components.button.index :as button]
    [webchange.ui-framework.components.select.index :as select]
    [webchange.ui-framework.components.text-area.index :as text-area]))

(def button button/component)
(def select select/component)
(def text-area text-area/component)

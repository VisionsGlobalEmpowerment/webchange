(ns webchange.ui-framework.components.dialog.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon-button.index :as icon-button]))

(defn component
  [{:keys [title on-close] :as props}]
  (print "props" props)
  (let [this (r/current-component)]
    [:div.dialog-wrapper
     [:div.dialog
      [:h1 title]
      [icon-button/component {:icon       "close"
                              :class-name "close-button"
                              :on-click   on-close}]
      (into [:div.dialog-content]
            (r/children this))]]))

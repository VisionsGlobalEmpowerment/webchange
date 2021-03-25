(ns webchange.ui-framework.components.dialog.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon-button.index :as icon-button]))

(defn component
  [{:keys [actions title on-enter on-exit on-close open?]
    :or   {title ""
           on-enter #()
           on-exit  #()
           on-close #()}}]
  (let [show-window? (or open? (nil? open?))
        this (r/current-component)]
    (when show-window?
      (on-enter)
      [:div.dialog-wrapper
       [:div.dialog
        [:h1 title]
        [icon-button/component {:icon       "close"
                                :class-name "close-button"
                                :on-click   #(do (on-exit)
                                                 (on-close))}]
        (into [:div.dialog-content]
              (r/children this))
        (when (some? actions)
          [:div.dialog-actions
           actions])]])))

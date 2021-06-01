(ns webchange.ui-framework.components.dialog.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [actions size title title-actions on-enter on-exit on-close open?]
    :or   {title    ""
           on-enter #()
           on-exit  #()
           on-close #()}}]
  (let [show-window? (or open? (nil? open?))
        this (r/current-component)]
    (when show-window?
      (on-enter)
      [:div.wc-dialog-wrapper
       [:div {:class-name (get-class-name (-> {"dialog" true}
                                              (assoc (str "size-" size) true)))}
        [:div.header
         [:h1 title]
         (when (some? title-actions)
           (into [:div.title-actions]
                 title-actions))
         [icon-button/component {:icon       "close"
                                 :class-name "close-button"
                                 :on-click   #(do (on-exit)
                                                  (on-close))}]]
        (into [:div.dialog-content]
              (r/children this))
        (when (some? actions)
          (into [:div.dialog-actions]
                actions))]])))

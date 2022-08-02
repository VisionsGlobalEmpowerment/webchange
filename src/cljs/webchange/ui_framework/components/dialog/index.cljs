(ns webchange.ui-framework.components.dialog.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [actions class-name close-button? content-align class-name-content full-screen? size title title-actions on-enter on-exit on-close open? width]
    :or   {full-screen?  false
           title         ""
           close-button? true
           content-align "left"
           on-enter      #()
           on-exit       #()
           on-close      #()}}]
  (r/with-let [this (r/current-component)]
    (let [show-window? (or open? (nil? open?))]
      (when show-window?
        (on-enter)
        [:div {:class-name (get-class-name {"wc-dialog-wrapper" true
                                            class-name          (some? class-name)})}
         [:div (cond-> {:class-name (get-class-name (cond-> {"dialog"      true
                                                             "full-screen" full-screen?}
                                                            (some? size) (assoc (str "size-" size) true)))}
                       (number? width) (assoc :style {:width     width
                                                      :min-width width}))
          [:div.header
           [:h1 title]
           (when (some? title-actions)
             (into [:div.title-actions]
                   title-actions))
           (when close-button?
             [icon-button/component {:icon       "close"
                                     :class-name "close-button"
                                     :on-click   #(do (on-exit)
                                                      (on-close))}])]
          (into [:div {:class-name (get-class-name (cond-> {"dialog-content"                     true
                                                            (str "content-align-" content-align) true}
                                                           (some? class-name-content) (assoc class-name-content true)))}]
                (r/children this))
          (when (some? actions)
            (into [:div.dialog-actions]
                  actions))]]))
    (finally
      (on-close))))

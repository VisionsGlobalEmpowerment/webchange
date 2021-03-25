(ns webchange.book-creator.text-form.views-font-size
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.ui-framework.components.index :refer [icon select]]))

(def font-sizes [8 9 10 11 12 14 18 24 30 36 48 60 72 96])

(defn font-size-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-size id])
        disabled? @(re-frame/subscribe [::state/loading? id])
        handle-change (fn [font-size]
                        (re-frame/dispatch [::state/set-current-font-size id (.parseInt js/Number font-size)]))
        handle-dec-click (fn [] (handle-change (dec value)))
        handle-inc-click (fn [] (handle-change (inc value)))
        options (->> (conj font-sizes value)
                     (distinct)
                     (sort)
                     (map (fn [size] {:text size :value size})))]
    [:div.control-block
     [icon {:icon "font-size"}]
     [select {:value               value
              :on-change           handle-change
              :options-text-suffix "pt"
              :options             options
              :show-buttons?       true
              :with-arrow?         false
              :on-arrow-up-click   handle-inc-click
              :on-arrow-down-click handle-dec-click
              :disabled?           disabled?
              :class-name          "font-size-selector"}]]))

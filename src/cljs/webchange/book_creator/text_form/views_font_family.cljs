(ns webchange.book-creator.text-form.views-font-family
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.ui-framework.index :refer [icon select]]))

(def font-families ["Roboto"])

(defn font-family-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-family id])
        handle-change (fn [font-family]
                        (re-frame/dispatch [::state/set-current-font-family id font-family]))
        options (->> (conj font-families value)
                     (remove nil?)
                     (distinct)
                     (sort)
                     (map (fn [size] {:text size :value size})))]
    [:div.control-block
     [icon {:icon "font-family"}]
     [select {:value               value
              :on-change           handle-change
              :options-text-suffix "pt"
              :options             options
              :show-buttons?       true
              :with-arrow?         false
              :class-name          "font-family-selector"}]]))

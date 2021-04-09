(ns webchange.book-creator.text-form.views-font-family
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.state.state-fonts :as fonts]
    [webchange.ui-framework.components.index :refer [icon select]]))

(defn font-family-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-family id])
        options @(re-frame/subscribe [::fonts/font-family-options])
        handle-change (fn [font-family]
                        (re-frame/dispatch [::state/set-current-font-family id font-family]))]
    [:div.control-block
     [icon {:icon "font-family"}]
     [select {:value               (or value "")
              :on-change           handle-change
              :options-text-suffix "pt"
              :options             options
              :show-buttons?       true
              :with-arrow?         false
              :class-name          "font-family-selector"}]]))

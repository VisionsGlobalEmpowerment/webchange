(ns webchange.book-creator.text-form.views-text
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.ui-framework.index :refer [text-area]]))

(defn text-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-text id])
        handle-change (fn [value] (re-frame/dispatch [::state/set-current-text id value]))]
    [text-area {:value     value
                :on-change handle-change}]))

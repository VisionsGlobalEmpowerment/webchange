(ns webchange.ui-framework.components.loading-overlay.index
  (:require
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]))

(defn component
  [{:keys []}]
  [:div.wc-loading-overlay
   [circular-progress/component]])

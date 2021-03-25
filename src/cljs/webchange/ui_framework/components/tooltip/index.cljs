(ns webchange.ui-framework.components.tooltip.index
  (:require
    [reagent.core :as r]))

(defn component
  [{:keys [open?]
    :or   {open? false}}]
  (let [this (r/current-component)]
    (when open?
      (into [:div.wc-tooltip]
            (r/children this)))))

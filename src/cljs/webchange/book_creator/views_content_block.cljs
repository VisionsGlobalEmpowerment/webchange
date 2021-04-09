(ns webchange.book-creator.views-content-block
  (:require
    [reagent.core :as r]))

(defn- block-header
  [{:keys [title left-controls right-controls]}]
  [:div.block-header
   [:span.title title]
   [:div.left-controls
    (when (some? left-controls)
      left-controls)]
   [:div.right-controls
    (when (some? right-controls)
      right-controls)]])

(defn content-block
  [{:keys [title left-controls right-controls style]
    :or   {style {}}}]
  (let [this (r/current-component)]
    (into [:div.content-block {:style style}
           [block-header {:title          title
                          :left-controls  left-controls
                          :right-controls right-controls}]]
          (r/children this))))

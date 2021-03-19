(ns webchange.book-creator.views-content-block
  (:require
    [reagent.core :as r]))

(defn- block-header
  [{:keys [title left-controls]}]
  [:div.block-header
   [:span.title title]
   (when (some? left-controls)
     [:div.left-controls
      left-controls])])

(defn content-block
  [{:keys [title left-controls]}]
  (let [this (r/current-component)]
    (into [:div.content-block
           [block-header {:title         title
                          :left-controls left-controls}]]
          (r/children this))))

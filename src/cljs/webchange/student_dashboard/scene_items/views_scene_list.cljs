(ns webchange.student-dashboard.scene-items.views-scene-list
  (:require
    [webchange.student-dashboard.scene-items.views-scene-list-item :refer [scene-list-item show-more-item]]))

(def content-block-styles
  {:display        "flex"
   :flex-direction "column"
   :max-height     "50%"
   :overflow       "hidden"
   :padding        "10px 0"})

(def content-header-styles
  {:font-size "24px"
   :flex      "0 0 auto"})

(def content-body-styles
  {:display    "flex"
   :flex       "1 1 auto"
   :flex-wrap  "wrap"
   :overflow-y "auto"})

(defn scene-list
  [items {:keys [title on-click show-more]}]
  [:div {:style content-block-styles}
   [:h1 {:style content-header-styles} title]
   [:div {:style content-body-styles}
    (when show-more
      [show-more-item {:on-click show-more}])
    (for [item items]
      ^{:key (str (:id item) "-" (:activity-id item))}
      [scene-list-item item {:on-click on-click}])]])

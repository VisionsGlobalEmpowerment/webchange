(ns webchange.student-dashboard.scene-items.views-scene-list
  (:require
    [webchange.student-dashboard.scene-items.views-scene-list-item :refer [scene-list-item]]))

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
  [items {:keys [title on-click]}]
  [:div {:style content-block-styles}
   [:h1 {:style content-header-styles} title]
   [:div {:style content-body-styles}
    (for [item items]
      (do
        (js/console.log "item-id " (:id item) item)

      ^{:key (:id item)}
      [scene-list-item item {:on-click on-click}]))]])

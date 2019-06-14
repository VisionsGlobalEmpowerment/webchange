(ns webchange.student-dashboard.related-content.views-related-content-list
  (:require
    [webchange.student-dashboard.related-content.views-related-content-list-item :refer [related-content-list-item]]
    [webchange.student-dashboard.related-content.views-related-content-styles :as styles]))

(def content-block-styles
  {:display        "flex"
   :flex-direction "column"
   :max-height     "50%"
   :padding        "10px 0"})

(def content-header-styles
  {:font-size "24px"
   :flex      "0 0 auto"})

(def content-body-styles
  {:display         "flex"
   :flex            "1 1 auto"
   :flex-wrap       "wrap"
   :justify-content "space-between"
   :overflow-y      "auto"
   :margin          (str "-" styles/margin "px")})

(defn related-content-list
  [related-content {:keys [title on-click]}]
  [:div {:style content-block-styles}
   [:h1 {:style content-header-styles} title]
   [:div {:style content-body-styles}
    (for [item related-content]
      ^{:key (:id item)}
      [related-content-list-item item {:on-click on-click}])]])

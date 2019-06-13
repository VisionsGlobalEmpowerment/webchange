(ns webchange.student-dashboard.life-skills.views-life-skill-list
  (:require
    [webchange.student-dashboard.life-skills.views-life-skill-list-item :refer [life-skill-list-item]]
    [webchange.student-dashboard.life-skills.views-life-skill-styles :as styles]))

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

(defn life-skill-list
  [life-skills {:keys [title on-click]}]
  [:div {:style content-block-styles}
   [:h1 {:style content-header-styles} title]
   [:div {:style content-body-styles}
    (for [life-skill life-skills]
      ^{:key (:id life-skill)}
      [life-skill-list-item life-skill {:on-click on-click}])]])

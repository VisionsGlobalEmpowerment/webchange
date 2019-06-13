(ns webchange.student-dashboard.views-additional-content
  (:require
    [webchange.student-dashboard.views-related-content-list :refer [related-content-list]]))

(def additional-content-styles
  {:background-color "#f7f7f7"
   :display          "flex"
   :flex-direction   "column"
   :flex             "0 0 auto"
   :padding          "50px"
   :width            410})


;(defn- life-skills-item
;  [{:keys [id name type link]}]
;  [:div name])
;
;(defn- life-skills-block
;  [life-skills]
;  [:div
;   [:h1 {:style content-header-styles} "Life skills / Extra credit"]
;   [:ul
;    (for [item life-skills]
;      ^{:key (:id item)}
;      [life-skills-item item])]])


(defn additional-content
  [{:keys [related-content on-related-content-click
           life-skills on-life-skill-click]}]
  [:div {:style additional-content-styles}
   [related-content-list related-content {:on-click on-related-content-click}]
   ;[life-skills-block life-skills]
   ])

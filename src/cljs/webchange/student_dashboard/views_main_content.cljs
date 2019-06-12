(ns webchange.student-dashboard.views-main-content
  (:require
    [webchange.student-dashboard.views-main-content-list-item :refer [content-list-item]]))

(def main-content-styles
  {:background-color "#ededed"
   :flex             "1 1 auto"})

(defn content-list
  [items {:keys [title on-click]}]
  [:div
   [:h1 title]
   [:div {:style {:display "flex"}}
    (for [item items]
      ^{:key (:id item)}
      [content-list-item item {:on-click on-click}])]])

(defn main-content
  [{:keys [stories assessments on-story-click on-assessment-click]}]
  [:div {:style main-content-styles}
   [content-list stories {:title    "Continue the story"
                          :on-click on-story-click}]
   [content-list assessments {:title    "Assessment"
                              :on-click on-assessment-click}]])

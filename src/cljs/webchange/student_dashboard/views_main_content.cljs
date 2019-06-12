(ns webchange.student-dashboard.views-main-content
  (:require
    [webchange.student-dashboard.views-main-content-list-item :refer [content-list-item]]))

(defn translate
  [path]
  (get-in {:story      {:title "Continue the story"}
           :assessment {:title "Assessment"}}
          path))

(def main-content-styles
  {:background-color "#ededed"
   :display          "flex"
   :flex             "1 1 auto"
   :flex-direction   "column"
   :padding          "50px"})

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

(defn content-list
  [items {:keys [title on-click]}]
  [:div {:style content-block-styles}
   [:h1 {:style content-header-styles} title]
   [:div {:style content-body-styles}
    (for [item items]
      ^{:key (:id item)}
      [content-list-item item {:on-click on-click}])]])

(defn main-content
  [{:keys [stories assessments on-story-click on-assessment-click]}]
  [:div {:style main-content-styles}
   [content-list (into [{:id :show-more}] stories) {:title    (translate [:story :title])
                                                  :on-click on-story-click}]
   [content-list assessments {:title    (translate [:assessment :title])
                              :on-click on-assessment-click}]])

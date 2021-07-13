(ns webchange.student-dashboard.toolbar.course-name.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]))

(def courses
  [{:course-id 4 :slug "english" :name "english"}
   {:course-id 2 :slug "spanish" :name "español"}])

(defn get-course-name
  [props]  (->> courses
                (filter #(= props (:slug %)))
                (first)
                (:name)))

(defn course-name
  []
  (let [current-course (get-course-name  @(re-frame/subscribe [::subs/current-course]))]
    [ui/typography {:variant "h6"
                    :class-name "main"}
     (str current-course)]))

(ns webchange.student-dashboard.toolbar.course-name.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]))

(defn- get-styles
  []
  {:main      {:width          "150px"
               :margin         "0 20px"
               :text-transform "capitalize"
               :font-weight    "bold"}})

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
  (let [current-course (get-course-name  @(re-frame/subscribe [::subs/current-course]))
        styles (get-styles)]
    [ui/typography {:variant "h6"
                    :style   (:main styles)}
     (str current-course)]))
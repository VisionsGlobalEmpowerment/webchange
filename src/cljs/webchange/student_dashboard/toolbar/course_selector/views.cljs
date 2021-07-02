(ns webchange.student-dashboard.toolbar.course.views
  (:require
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]))

(defn- get-styles
  []
  {:main {:width          "150px"
          :margin         "0 20px"
          :color          "black"
          :text-transform "capitalize"
          :font-family    "Roboto, Helvetica, Arial, sans-serif"}})

(defn course
  []
  (let [current-course @(re-frame/subscribe [::subs/current-course])
        styles (get-styles)]
    [:h3 {:style (:main styles)} current-course]))

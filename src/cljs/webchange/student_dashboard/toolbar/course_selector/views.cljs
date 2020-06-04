(ns webchange.student-dashboard.toolbar.course-selector.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.subs :as subs]
    [webchange.student-dashboard.toolbar.course-selector.flags.flag-en :as flag-en]
    [webchange.student-dashboard.toolbar.course-selector.flags.flag-es :as flag-es]))

;; ToDo: get courses list from API
(def courses
  [{:key "en" :value "english" :text "English"}
   {:key "es" :value "spanish" :text "Español"}])

(defn- get-styles
  []
  {:main {:width  "150px"
          :margin "0 20px"}
   :icon {:position "relative"
          :top      "2px"}})

(defn- flag
  [name]
  (case name
    "en" [flag-en/get-shape]
    "es" [flag-es/get-shape]))

(defn- menu-item
  [{:keys [key value text]}]
  (let [styles (get-styles)]
    [ui/menu-item {:key value :value value}
     [ui/list-item-icon {:style (:icon styles)}
      [flag key]]
     [ui/typography {:inline  true
                     :variant "inherit"}
      text]]))

(defn course-selector
  []
  (let [current-course @(re-frame/subscribe [::subs/current-course])
        styles (get-styles)]
    [ui/select {:value     current-course
                :on-change #(re-frame/dispatch [::ie/open-student-course-dashboard (-> % .-target .-value)])
                :style     (:main styles)}
     (for [course courses]
       (menu-item course))]))

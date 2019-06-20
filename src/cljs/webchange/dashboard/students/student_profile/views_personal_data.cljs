(ns webchange.dashboard.students.student-profile.views-personal-data
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.common.check-icon :refer [check-icon]]
    [webchange.dashboard.students.common.actions-menu :refer [actions-menu]]))

(defn translate
  [path]
  (get-in {:tablet "Tablet"
           :name   "Name"
           :age    "Age"
           :class  "Class"
           :course "Course"}
          path))

(def avatar-size 80)
(def avatar-style
  {:width            avatar-size
   :height           avatar-size
   :font-size        (str (/ avatar-size 40) "rem")
   :background-color "brown"})

(defn tablet-info
  [value]
  [:div {:style {:position "relative"
                 :top      -9}}
   [check-icon {:icon  ic/tablet
                :value value
                :style {:height       30
                        :top          9
                        :margin-right 8}}]
   (translate [:tablet])
   [check-icon {:value value}]])

(defn student-data-item
  [{:keys [text value]}]
  [ui/typography
   {:variant "body1"
    :style   {:padding "5px 0"}}
   [:strong (str text ": ")] value])

(defn data-column
  [& children]
  [ui/grid {:item true :xs 3}
   [ui/grid {:container true
             :style     {:height "100%"}}
    (for [child children]
      ^{:key child}
      [ui/grid {:item true :xs 12}
       child])]])

(defn personal-data
  [{:keys [id class-id first-name last-name age class course tablet?]}]
  [ui/grid {:container true}
   [ui/grid {:item true :xs 2}
    [ui/avatar {:style avatar-style}
     (str (get first-name 0) (get last-name 0))]]
   [data-column
    [student-data-item {:text (translate [:name]) :value (str first-name " " last-name)}]
    [student-data-item {:text (translate [:age]) :value age}]]
   [data-column
    [student-data-item {:text (translate [:class]) :value class}]
    [student-data-item {:text (translate [:course]) :value course}]]
   [data-column
    [tablet-info tablet?]]
   [ui/grid {:item true :xs 1}
    [actions-menu
     {:on-edit-click   #(re-frame/dispatch [::students-events/show-edit-student-form id])
      :on-remove-click #(re-frame/dispatch [::students-events/delete-student class-id id])}]]])

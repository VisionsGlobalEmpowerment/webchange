(ns webchange.dashboard.dashboard-layout.views-breadcrumbs
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.subs :as dashboard-subs]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.common.map-students :refer [map-student]]
    [webchange.routes :refer [redirect-to]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- translate
  [path]
  (get-in {:dashboard     "Dashboard"
           :classes-list  "Classes"
           :students-list "Students"}
          path))

(defn- breadcrumb-item
  [{:keys [disabled route text]}]
  [ui/button
   {:disabled disabled
    :on-click #(apply redirect-to route)
    :style    {:color     (get-in-theme [:palette :text :primary])
               :min-width "30px"}}
   (or text " ")])

(defn- breadcrumb-item-connector
  []
  [ic/chevron-right
   {:style {:top      "7px"
            :position "relative"}}])

(defn- root
  ([]
   (root false))
  ([is-transitional]
   [^{:key "root"}
   [breadcrumb-item {:text     (translate [:dashboard])
                     :route    [:dashboard]
                     :disabled (not is-transitional)}]]))

(defn- node
  [{:keys [key parents is-transitional text route]}]
  (conj parents
        ^{:key (str key "-connector")}
        [breadcrumb-item-connector]
        ^{:key key}
        [breadcrumb-item {:text     text
                          :route    route
                          :disabled (not is-transitional)}]))

(defn- classes-list
  ([]
   (classes-list false))
  ([is-transitional]
   (node {:key "classes-list"
          :parents (root true)
          :is-transitional is-transitional
          :text (translate [:classes-list])
          :route [:dashboard-classes]})))

(defn- class-profile
  ([class]
   (class-profile class false))
  ([class is-transitional]
   (node {:key "class-profile"
          :parents (classes-list true)
          :is-transitional is-transitional
          :text (:name class)
          :route [:dashboard-class-profile :class-id (:id class)]})))

(defn- students-list
  ([class]
   (students-list class false))
  ([class is-transitional]
   (node {:key "students-list"
          :parents (class-profile class true)
          :is-transitional is-transitional
          :text (translate [:students-list])
          :route [:dashboard-students :class-id (:id class)]})))

(defn- student-profile
  ([class student]
   (student-profile class student false))
  ([class student is-transitional]
   (node {:key "student-profile"
          :parents (students-list class true)
          :is-transitional is-transitional
          :text (:name student)
          :route [:dashboard-student-profile :class-id (:id class) :student-id (:id student)]})))

(defn breadcrumbs
  []
  (let [dashboard-main-content @(re-frame/subscribe [::dashboard-subs/current-main-content])
        class @(re-frame/subscribe [::classes-subs/current-class])
        student (map-student @(re-frame/subscribe [::students-subs/current-student]))]
    [:div
     (for [node (case dashboard-main-content
                  :classes-list (classes-list)
                  :class-profile (class-profile class)
                  :students-list (students-list class)
                  :student-profile (student-profile class student)
                  (root))] node)]))

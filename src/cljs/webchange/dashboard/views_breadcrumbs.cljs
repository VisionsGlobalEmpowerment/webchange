(ns webchange.dashboard.views-breadcrumbs
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.routes :refer [redirect-to]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn translate
  [path]
  (get-in {:dashboard "Dashboard"
           :classes   "Classes"
           :students  "Students"}
          path))

(defn breadcrumb-item
  [{:keys [disabled route text]}]
  [ui/button
   {:disabled disabled
    :on-click #(apply redirect-to route)
    :style    {:color     (get-in-theme [:palette :text :primary])
               :min-width "30px"}}
   text])

(defn breadcrumb-item-connector
  []
  [ic/chevron-right
   {:style {:top      "7px"
            :position "relative"}}])

(defn root
  ([]
   (root false))
  ([is-transitional]
   [^{:key "dashboard"}
   [breadcrumb-item {:text     (translate [:dashboard])
                     :route    [:dashboard]
                     :disabled (not is-transitional)}]]))

(defn classes
  ([]
   (classes false))
  ([is-transitional]
   (conj (root true)
         ^{:key "classes-connector"}
         [breadcrumb-item-connector]
         ^{:key "classes"}
         [breadcrumb-item {:text     (translate [:classes])
                           :route    [:dashboard-classes]
                           :disabled (not is-transitional)}])))

(defn students
  ([class-id]
   (students class-id false))
  ([class-id is-transitional]
   (conj (classes true)
         ^{:key (str "class-id-" class-id "-connector")}
         [breadcrumb-item-connector]
         ^{:key (str "class-id-" class-id)}
         [breadcrumb-item {:text     class-id
                           :disabled true}]
         ^{:key (str "class-" class-id "-students-connector")}
         [breadcrumb-item-connector]
         ^{:key (str "class-" class-id "-students")}
         [breadcrumb-item {:text     (translate [:students])
                           :disabled (not is-transitional)}])))

(defn breadcrumbs
  []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [:div
     (let [{:keys [class-id]} route-params
           nodes (case handler
                   :dashboard-classes (classes)
                   :dashboard-students (students class-id)
                   (root))]
       (for [node nodes] node))]))

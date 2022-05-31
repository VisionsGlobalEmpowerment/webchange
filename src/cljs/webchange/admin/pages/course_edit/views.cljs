(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- activities-list-item
  [{:keys [name preview] :as activity-data}]
  (print "activity-data" activity-data)
  (let [handle-remove-click #(print "Remove activity")]
    [l/list-item {:name       name
                  :img        preview
                  :class-name "activities-list-item"
                  :actions    [ui/icon-button {:icon     "remove"
                                               :title    "Remove"
                                               :variant  "light"
                                               :on-click handle-remove-click}]}]))

(defn- activities-list
  [{:keys [level-idx lesson-idx]}]
  (let [activities @(re-frame/subscribe [::state/lesson-activities level-idx lesson-idx])]
    [l/list {:class-name "activities-list"}
     (for [{:keys [idx] :as activity-data} activities]
       ^{:key idx}
       [activities-list-item activity-data])]))

(defn- lessons-list-item
  [{:keys [idx name activities-number level-idx]}]
  (r/with-let [expanded? (r/atom false)
               handle-remove-click #(print "Remove lesson" idx)]
    [:<>
     [l/list-item {:name       name
                   :class-name "lesson-list-item"
                   :actions    [ui/icon-button {:icon     "remove"
                                                :title    "Remove"
                                                :variant  "light"
                                                :on-click handle-remove-click}]}
      [l/content-right
       [:div.list-item-stats
        activities-number
        [ui/icon {:icon       "activity"
                  :class-name "activity"}]]]]
     [activities-list {:level-idx  level-idx
                       :lesson-idx idx}]]))

(defn- lessons-list
  [{:keys [level-idx]}]
  (let [lessons @(re-frame/subscribe [::state/level-lessons level-idx])]
    [l/list {:class-name "lessons-list"}
     (for [{:keys [idx] :as lesson-data} lessons]
       ^{:key idx}
       [lessons-list-item (assoc lesson-data :level-idx level-idx)])]))

(defn- levels-list-item
  [{:keys [idx name lessons-number]}]
  (r/with-let [expanded? (r/atom false)
               handle-remove-click #(print "Remove level" idx)]
    [:<>
     [l/list-item {:name       name
                   :class-name "level-list-item"
                   :actions    [ui/icon-button {:icon     "remove"
                                                :title    "Remove"
                                                :variant  "light"
                                                :on-click handle-remove-click}]}
      [l/content-right
       [:div.list-item-stats
        lessons-number
        [ui/icon {:icon        "lesson"
                  ::class-name "lesson"}]]]]
     [lessons-list {:level-idx idx}]]))

(defn- levels-list
  []
  (let [levels @(re-frame/subscribe [::state/course-levels])]
    [l/list {:class-name "levels-list"}
     (for [{:keys [idx] :as level-data} levels]
       ^{:key idx}
       [levels-list-item level-data])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--edit-course"}
     [page/header {:title "Edit Course"
                   :icon  "presentation"}]
     [page/main-content
      [levels-list]]]))

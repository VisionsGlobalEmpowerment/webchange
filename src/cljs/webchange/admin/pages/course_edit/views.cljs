(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.drag-and-drop :refer [draggable]]))

(defn- drop-allowed?
  [dragged-data target-data]
  (and (= (:type target-data)
          (:type dragged-data))
       (not= target-data dragged-data)))

(defn- handle-drop
  [props]
  (print ">>> Handle Drop" props))

(defn- reorder-control
  []
  [ui/icon {:icon       "reorder"
            :class-name "reorder-control"}])

(defn- activities-list-item
  [{:keys [id name preview level-idx lesson-idx]}]
  (let [handle-remove-click #(do (.stopPropagation %)
                                 (re-frame/dispatch [::state/remove-activity level-idx lesson-idx id]))]
    [l/list-item {:name       name
                  :img        preview
                  :class-name "activities-list-item"
                  :pre        [reorder-control]
                  :actions    [ui/icon-button {:icon     "remove"
                                               :title    "Remove"
                                               :variant  "light"
                                               :on-click handle-remove-click}]}]))

(defn- activities-list
  [{:keys [level-idx lesson-idx]}]
  (let [activities @(re-frame/subscribe [::state/lesson-activities level-idx lesson-idx])]
    [l/list {:class-name "sub-list"}
     (for [{:keys [idx] :as activity-data} activities]
       ^{:key idx}
       [activities-list-item (merge activity-data
                                    {:level-idx  level-idx
                                     :lesson-idx lesson-idx})])]))

(defn- lessons-list-item
  [{:keys [idx name activities-number level-idx]}]
  (r/with-let [expanded? (r/atom false)
               handle-item-click #(swap! expanded? not)
               handle-remove-click #(do (.stopPropagation %)
                                        (re-frame/dispatch [::state/remove-lesson level-idx idx]))]
    [:<>
     [draggable {:data          {:type   "lesson"
                                 :level  level-idx
                                 :lesson idx}
                 :drop-allowed? drop-allowed?
                 :on-drop       handle-drop}
      [l/list-item {:name       name
                    :class-name (ui/get-class-name {"list-item" true
                                                    "selected"  @expanded?})
                    :on-click   handle-item-click
                    :pre        [reorder-control]
                    :actions    [ui/icon-button {:icon     "remove"
                                                 :title    "Remove"
                                                 :variant  "light"
                                                 :on-click handle-remove-click}]}
       [l/content-right
        [:div.list-item-stats
         activities-number
         [ui/icon {:icon       "activity"
                   :class-name "activity"}]]]]]
     (when @expanded?
       [activities-list {:level-idx  level-idx
                         :lesson-idx idx}])]))

(defn- lessons-list
  [{:keys [level-idx]}]
  (let [lessons @(re-frame/subscribe [::state/level-lessons level-idx])]
    [l/list {:class-name "sub-list"}
     (for [{:keys [idx] :as lesson-data} lessons]
       ^{:key idx}
       [lessons-list-item (assoc lesson-data :level-idx level-idx)])]))

(defn- levels-list-item
  [{:keys [idx name lessons-number]}]
  (r/with-let [expanded? (r/atom false)
               handle-item-click #(swap! expanded? not)
               handle-remove-click #(do (.stopPropagation %)
                                        (re-frame/dispatch [::state/remove-level idx]))]
    [:<>
     [draggable {:data          {:type  "level"
                                 :level idx}
                 :drop-allowed? drop-allowed?
                 :on-drop       handle-drop}
      [l/list-item {:name       name
                    :class-name (ui/get-class-name {"list-item" true
                                                    "selected"  @expanded?})
                    :on-click   handle-item-click
                    :pre        [reorder-control]
                    :actions    [ui/icon-button {:icon     "remove"
                                                 :title    "Remove"
                                                 :variant  "light"
                                                 :on-click handle-remove-click}]}
       [l/content-right
        [:div.list-item-stats
         lessons-number
         [ui/icon {:icon        "lesson"
                   ::class-name "lesson"}]]]]]
     (when @expanded?
       [lessons-list {:level-idx idx}])]))

(defn- levels-list
  []
  (let [levels @(re-frame/subscribe [::state/course-levels])]
    [l/list {:class-name "levels-list"}
     (for [{:keys [idx] :as level-data} levels]
       ^{:key idx}
       [levels-list-item level-data])]))

(defn- actions-list-item
  [{:keys [icon text data]}]
  [draggable {:data data}
   [:li.actions-list-item
    [ui/icon {:icon icon}]
    text]])

(defn- actions-list
  []
  [:ul.actions-list
   [actions-list-item {:icon "add-item"
                       :text "Add Level"
                       :data {:type  "level"
                              :level "add"}}]
   [actions-list-item {:icon "add-item"
                       :text "Add Lesson"
                       :data {:type   "lesson"
                              :lesson "add"}}]
   [actions-list-item {:icon "activity"
                       :text "Add Activity"
                       :data {:type     "activity"
                              :activity "add"}}]])

(defn- side-bar
  []
  [page/side-bar {:title "Course Details"}
   [actions-list]])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--edit-course"}
     [page/header {:title "Edit Course"
                   :icon  "presentation"}]
     [page/main-content
      [levels-list]]
     [side-bar]]))

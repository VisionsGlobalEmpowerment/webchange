(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.course-info-form.view :refer [course-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.drag-and-drop :refer [draggable]]
    [webchange.utils.numbers :refer [try-parse-int]]))

(defn- drop-allowed?
  [dragged-data target-data]
  (and (= (:type target-data)
          (:type dragged-data))
       (not= target-data dragged-data)))

(defn- parse-fields
  [obj]
  (->> obj
       (map (fn [[field value]]
              [field (try-parse-int value)]))
       (into {})))

(defn- handle-drop
  [{:keys [dragged target side]}]
  (let [target (parse-fields target)
        dragged (parse-fields dragged)
        position (case (:vertical side)
                   :top :before
                   :bottom :after)]
    (case (:type dragged)
      "level" (if (= (:level dragged) "add")
                (re-frame/dispatch [::state/add-level {:target-level (:level target)
                                                       :position     position}])
                (re-frame/dispatch [::state/move-level {:source-level (:level dragged)
                                                        :target-level (:level target)
                                                        :position     position}]))
      "lesson" (if (= (:lesson dragged) "add")
                 (re-frame/dispatch [::state/add-lesson {:target-level  (:level target)
                                                         :target-lesson (:lesson target)
                                                         :position      position}])
                 (re-frame/dispatch [::state/move-lesson {:source-level  (:level dragged)
                                                          :source-lesson (:lesson dragged)
                                                          :target-level  (:level target)
                                                          :target-lesson (:lesson target)
                                                          :position      position}]))
      "activity" (if (and (contains? dragged :level)
                          (contains? dragged :lesson))
                   (re-frame/dispatch [::state/move-activity {:source-level    (:level dragged)
                                                              :source-lesson   (:lesson dragged)
                                                              :source-activity (:activity dragged)
                                                              :target-level    (:level target)
                                                              :target-lesson   (:lesson target)
                                                              :target-activity (:activity target)
                                                              :position        position}])
                   (re-frame/dispatch [::state/add-activity {:target-level    (:level target)
                                                             :target-lesson   (:lesson target)
                                                             :target-activity (:activity target)
                                                             :activity-id     (:activity dragged)
                                                             :position        position}]))
      nil)))

(defn- reorder-control
  []
  [ui/icon {:icon       "reorder"
            :class-name "reorder-control"}])

(defn- empty-list-placeholder
  [{:keys [data]}]
  [draggable {:data          data
              :drop-allowed? drop-allowed?
              :on-drop       handle-drop}
   [:div.empty-list-placeholder
    (str "Drop new " (:type data) " here")]])

(defn- activities-list-item
  [{:keys [idx name preview level-idx lesson-idx]}]
  (let [handle-remove-activity #(re-frame/dispatch [::state/remove-activity level-idx lesson-idx idx])
        handle-remove-click #(do (.stopPropagation %)
                                 (ui/with-confirmation {:message    "Remove Activity?"
                                                        :on-confirm handle-remove-activity}))]
    [draggable {:data          {:type     "activity"
                                :level    level-idx
                                :lesson   lesson-idx
                                :activity idx}
                :drop-allowed? drop-allowed?
                :on-drop       handle-drop}
     [l/list-item {:name       name
                   :img        preview
                   :class-name "activities-list-item"
                   :pre        [reorder-control]
                   :actions    [ui/icon-button {:icon     "remove"
                                                :title    "Remove"
                                                :variant  "light"
                                                :on-click handle-remove-click}]}]]))

(defn- activities-list
  [{:keys [level-idx lesson-idx]}]
  (let [activities @(re-frame/subscribe [::state/lesson-activities level-idx lesson-idx])]
    [l/list {:class-name "sub-list"}
     (for [{:keys [idx] :as activity-data} activities]
       ^{:key idx}
       [activities-list-item (merge activity-data
                                    {:level-idx  level-idx
                                     :lesson-idx lesson-idx})])
     (when (empty? activities)
       [empty-list-placeholder {:data {:type     "activity"
                                       :level    level-idx
                                       :lesson   lesson-idx
                                       :activity 0}}])]))

(defn- lessons-list-item
  [{:keys [idx name activities-number level-idx]}]
  (r/with-let [expanded? (r/atom false)
               handle-item-click #(swap! expanded? not)
               handle-remove-lesson #(re-frame/dispatch [::state/remove-lesson level-idx idx])
               handle-remove-click #(do (.stopPropagation %)
                                        (ui/with-confirmation {:message    "Remove Lesson?"
                                                               :on-confirm handle-remove-lesson}))]
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
       [lessons-list-item (assoc lesson-data :level-idx level-idx)])
     (when (empty? lessons)
       [empty-list-placeholder {:data {:type   "lesson"
                                       :level  level-idx
                                       :lesson 0}}])]))

(defn- levels-list-item
  [{:keys [idx name lessons-number]}]
  (r/with-let [expanded? (r/atom false)
               handle-item-click #(swap! expanded? not)
               handle-remove-level #(re-frame/dispatch [::state/remove-level idx])
               handle-remove-click #(do (.stopPropagation %)
                                        (ui/with-confirmation {:message    "Remove Level?"
                                                               :on-confirm handle-remove-level}))]
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

(defn- available-activities-list-item
  [{:keys [id name preview]}]
  [draggable {:data {:type     "activity"
                     :activity id}}
   [:div.available-activities-list-item
    [ui/icon {:icon       "add-item"
              :class-name "icon"}]
    [:div.name name]
    [ui/image {:src        preview
               :class-name "preview"}]]])

(defn- available-activities-list
  []
  (let [filter @(re-frame/subscribe [::state/available-activities-filter])
        handle-filter-change #(re-frame/dispatch [::state/set-available-activities-filter %])
        handle-back-click #(re-frame/dispatch [::state/open-course-info])
        available-activities @(re-frame/subscribe [::state/available-activities])]
    [page/side-bar {:title        "Add Activity"
                    :title-action [ui/icon-button {:icon       "arrow-left"
                                                   :variant    "light"
                                                   :class-name "back-button"
                                                   :on-click   handle-back-click}]}
     [:div.available-activities
      [ui/input {:value       filter
                 :on-change   handle-filter-change
                 :placeholder "search activities"
                 :icon        "search"
                 :class-name  "search"}]
      [:div.available-activities-list

       (for [{:keys [id] :as activity} available-activities]
         ^{:key id}
         [available-activities-list-item activity])]]]))

(defn- actions-list-item
  [{:keys [icon text data on-click]}]
  (if (some? data)
    [draggable {:data data}
     [:li.actions-list-item
      [ui/icon {:icon icon}]
      text]]
    [:li (cond-> {:class-name "actions-list-item button-item"}
                 (fn? on-click) (assoc :on-click on-click))
     [ui/icon {:icon icon}]
     text]))

(defn- actions-list
  []
  (let [handle-activities-click #(re-frame/dispatch [::state/open-available-activities])]
    [:ul.actions-list
     [actions-list-item {:icon "add-item"
                         :text "Add Level"
                         :data {:type  "level"
                                :level "add"}}]
     [actions-list-item {:icon "add-item"
                         :text "Add Lesson"
                         :data {:type   "lesson"
                                :lesson "add"}}]
     [actions-list-item {:icon     "activity"
                         :text     "Add Activity"
                         :on-click handle-activities-click}]]))

(defn- main-form
  [{:keys [course-slug]}]
  (r/with-let [form-editable? (r/atom false)
               handle-edit-click #(swap! form-editable? not)
               handle-form-saved #(do (reset! form-editable? false)
                                      (re-frame/dispatch [::state/set-course-info %]))]
    [page/side-bar {:title   "Course Details"
                    :actions [ui/icon-button {:icon     (if @form-editable? "close" "edit")
                                              :variant  "light"
                                              :on-click handle-edit-click}]}
     [course-info-form {:course-slug course-slug
                        :editable?   @form-editable?
                        :on-save     handle-form-saved}]
     (when-not @form-editable?
       [actions-list])]))

(defn- side-bar
  [props]
  (let [content @(re-frame/subscribe [::state/side-bar-content])]
    (case content
      :available-activities [available-activities-list]
      [main-form props])))

(defn- header
  []
  (let [{:keys [name levels lessons activities]} @(re-frame/subscribe [::state/course-statistic])]
    [page/header {:title      name
                  :icon       "presentation"
                  :class-name "page--edit-course--header"}
     [page/header-content-group {:icon "levels"}
      [:span (str levels " Levels")]]
     [:hr]
     [page/header-content-group {:icon "lesson"}
      [:span (str lessons " Lessons")]]
     [:hr]
     [page/header-content-group {:icon       "activity"
                                 :class-name "activities"}
      [:span (str activities " Activities")]]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [course-fetching? @(re-frame/subscribe [::state/course-fetching?])]
      [page/page {:class-name "page--edit-course"}
       [header]
       [page/main-content
        [levels-list]
        (when course-fetching?
          [ui/loading-overlay])]
       [side-bar props]])))

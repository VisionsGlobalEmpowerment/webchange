(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.pagination.views :refer [pagination]]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.course-info-form.view :refer [course-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
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
                   (re-frame/dispatch [::state/add-activity
                                       {:target-level    (:level target)
                                        :target-lesson   (:lesson target)
                                        :target-activity (:activity target)
                                        :activity-slug   (:activity dragged)
                                        :position        position}
                                       {:scene-id (:id dragged)}]))
      nil)))

(defn- empty-list-placeholder
  [{:keys [data]}]
  [draggable {:data          data
              :drop-allowed? drop-allowed?
              :on-drop       handle-drop}
   [:div.empty-list-placeholder
    (str "Drop new " (:type data) " here")]])

(defn- activities-list-item
  [{:keys [id idx name preview level-idx lesson-idx]}]
  (let [handle-play-click #(re-frame/dispatch [::state/preview-activity id])
        handle-remove-activity #(re-frame/dispatch [::state/remove-activity level-idx lesson-idx idx])
        handle-remove-click #(do (.stopPropagation %)
                                 (handle-remove-activity))
        locked? @(re-frame/subscribe [::state/locked?])]
    (if locked?
      [ui/list-item {:name       name
                     :pre        [ui/image {:src        preview
                                            :class-name "item-image"}]
                     :class-name "activities-list-item"}]
      [draggable {:data          {:type     "activity"
                                  :level    level-idx
                                  :lesson   lesson-idx
                                  :activity idx}
                  :drop-allowed? drop-allowed?
                  :on-drop       handle-drop}
       [ui/list-item {:name       name
                      :pre        [ui/image {:src        preview
                                             :class-name "item-image"}]
                      :class-name "activities-list-item"
                      :actions    [{:icon     "trash"
                                    :title    "Remove"
                                    :on-click handle-remove-click}
                                   {:icon     "play"
                                    :title    "Preview"
                                    :on-click handle-play-click}]}]])))

(defn- activities-list
  [{:keys [level-idx lesson-idx]}]
  (let [activities @(re-frame/subscribe [::state/lesson-activities level-idx lesson-idx])]
    [ui/list {:class-name "sub-list"}
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
  (r/with-let [expanded? (r/atom (= activities-number 0))]
    (let [locked? @(re-frame/subscribe [::state/locked?])
          handle-item-click #(when (> activities-number 0)
                               (swap! expanded? not))
          handle-remove-lesson #(re-frame/dispatch [::state/remove-lesson level-idx idx])
          handle-remove-click #(do (.stopPropagation %)
                                   (handle-remove-lesson))]
      (if locked?
        [:<>
         [ui/list-item {:name       name
                        :class-name (ui/get-class-name {"list-item" true
                                                        "selected"  @expanded?})
                        :on-click   handle-item-click
                        :pre        [:div.lesson-dnd
                                     [ui/icon {:icon "change-position"}]]
                        :controls   [:div.list-item-stats
                                     activities-number
                                     [ui/icon {:icon       "games"
                                               :class-name "activity"}]]}]
         (when @expanded?
           [activities-list {:level-idx  level-idx
                             :lesson-idx idx}])]
        [:<>
         [draggable {:data          {:type   "lesson"
                                     :level  level-idx
                                     :lesson idx}
                     :drop-allowed? drop-allowed?
                     :on-drop       handle-drop}
          [ui/list-item {:name       name
                         :class-name (ui/get-class-name {"list-item" true
                                                         "selected"  @expanded?})
                         :on-click   handle-item-click
                         :pre        [:div.lesson-dnd
                                      [ui/icon {:icon "change-position"}]]
                         :controls   [:div.list-item-stats
                                      activities-number
                                      [ui/icon {:icon       "games"
                                                :class-name "activity"}]]
                         :actions    [{:icon     "trash"
                                       :title    "Remove"
                                       :on-click handle-remove-click}]}]]
         (when @expanded?
           [activities-list {:level-idx  level-idx
                             :lesson-idx idx}])]))))

(defn- lessons-list
  [{:keys [level-idx]}]
  (let [lessons @(re-frame/subscribe [::state/level-lessons level-idx])]
    [ui/list {:class-name "sub-list"}
     (for [{:keys [id] :as lesson-data} lessons]
       ^{:key id}
       [lessons-list-item (assoc lesson-data :level-idx level-idx)])
     (when (empty? lessons)
       [empty-list-placeholder {:data {:type   "lesson"
                                       :level  level-idx
                                       :lesson 0}}])]))

(defn- levels-list-item
  [{:keys [idx name lessons-number]}]
  (r/with-let [expanded? (r/atom (= lessons-number 0))]
    (let [locked? @(re-frame/subscribe [::state/locked?])
          handle-item-click #(when (> lessons-number 0)
                               (swap! expanded? not))
          handle-remove-level #(re-frame/dispatch [::state/remove-level idx])
          handle-remove-click #(do (.stopPropagation %)
                                   (handle-remove-level))]
      (if locked?
        [:<>
         [ui/list-item {:name       name
                        :class-name (ui/get-class-name {"list-item" true
                                                        "selected"  @expanded?})
                        :on-click   handle-item-click
                        :controls   [:div.list-item-stats
                                     lessons-number
                                     [ui/icon {:icon        "games"
                                               ::class-name "lesson"}]]}]
         (when @expanded?
           [lessons-list {:level-idx idx}])]
        [:<>
         [draggable {:data          {:type  "level"
                                     :level idx}
                     :drop-allowed? drop-allowed?
                     :on-drop       handle-drop}
          [ui/list-item {:name       name
                         :class-name (ui/get-class-name {"list-item" true
                                                         "selected"  @expanded?})
                         :on-click   handle-item-click
                         :controls   [:div.list-item-stats
                                      lessons-number
                                      [ui/icon {:icon        "games"
                                                ::class-name "lesson"}]]
                         :actions    [{:icon     "trash"
                                       :title    "Remove"
                                       :on-click handle-remove-click}]}]]
         (when @expanded?
           [lessons-list {:level-idx idx}])]))))

(defn- levels-list
  []
  (let [levels @(re-frame/subscribe [::state/course-levels])]
    [ui/list {:class-name "levels-list"}
     (for [{:keys [id] :as level-data} levels]
       ^{:key id}
       [levels-list-item level-data])]))

(defn- available-activities-list-item
  [{:keys [id slug name preview]}]
  [draggable {:data {:type     "activity"
                     :activity slug
                     :id       id}}
   [:div.available-activities-list-item
    [:div.name name]
    [ui/image {:src        preview
               :class-name "preview"
               :lazy?      true}]]])

(defn- available-activities-list
  []
  (let [filter @(re-frame/subscribe [::state/available-activities-filter])
        handle-filter-change #(re-frame/dispatch [::state/set-available-activities-filter %])
        handle-back-click #(re-frame/dispatch [::state/open-course-info])
        available-activities @(re-frame/subscribe [::state/paged-activities])
        pagination-state @(re-frame/subscribe [::state/pagination-state])
        handle-page-button-click #(re-frame/dispatch [::state/set-current-page %])]
    [page/side-bar {:title         "Add Activity"
                    :icon          "arrow-left"
                    :icon-color    "blue-1"
                    :on-icon-click handle-back-click}
     [:div.available-activities
      [ui/input {:value       filter
                 :on-change   handle-filter-change
                 :placeholder "search"
                 :icon        "search"
                 :class-name  "search"}]
      [:div.available-activities-list
       (for [{:keys [id] :as activity} available-activities]
         ^{:key id}
         [available-activities-list-item activity])]
      (when (> (:total pagination-state) 1)
        [pagination (merge pagination-state
                           {:on-click handle-page-button-click})])]]))

(defn- actions-list-item
  [{:keys [icon text data on-click]}]
  (if (some? data)
    [draggable {:data data}
     [:li.actions-list-item
      [ui/icon {:icon icon}]
      text]]
    [:li (cond-> {:class-name "actions-list-item button-item"}
                 (fn? on-click) (assoc :on-click on-click))
     (when (some? icon)
       [ui/icon {:icon icon}])
     text]))

(defn- add-content-actions
  []
  (let [course-locked? @(re-frame/subscribe [::state/locked?])
        handle-activities-click #(re-frame/dispatch [::state/open-available-activities])]
    (when-not course-locked?
      [:<>
       [ui/info "Drag over a level, lesson or activity to the left in the spot where you'd like to add."]
       [actions-list-item {:icon "dnd"
                           :text "Add Level"
                           :data {:type  "level"
                                  :level "add"}}]
       [actions-list-item {:icon "dnd"
                           :text "Add Lesson"
                           :data {:type   "lesson"
                                  :lesson "add"}}]
       [actions-list-item {:icon     "games"
                           :text     "Add Activity"
                           :on-click handle-activities-click}]])))

(defn- duplicate-course
  []
  (let [course-locked? @(re-frame/subscribe [::state/locked?])
        handle-click #(re-frame/dispatch [::state/duplicate-course])]
    (when course-locked?
      [:<>
       [actions-list-item {:icon     "copy"
                           :text     "Duplicate Course"
                           :on-click handle-click}]
       [ui/info "This course is locked. Duplicate course to edit."]])))

(defn- actions-list
  []
  (let []
    [:div.actions
     [:ul.actions-list
      [add-content-actions]
      [duplicate-course]]]))

(defn- main-form
  [{:keys [course-slug]}]
  (let [form-editable? @(re-frame/subscribe [::state/course-form-editable?])
        locked? @(re-frame/subscribe [::state/locked?])
        can-lock? @(re-frame/subscribe [::state/can-lock?])

        handle-edit-click #(re-frame/dispatch [::state/set-course-form-editable true])
        handle-cancel-click #(re-frame/dispatch [::state/handle-course-form-canceled])
        handle-form-saved #(re-frame/dispatch [::state/handle-course-form-saved %])]
    [page/side-bar {:title    "Course Details"
                    :icon     "info"
                    :focused? form-editable?
                    :actions  [(when (or (not locked?) can-lock?)
                                 (if form-editable?
                                   {:icon     "close"
                                    :on-click handle-cancel-click}
                                   {:icon     "edit"
                                    :on-click handle-edit-click}))]}
     [course-info-form {:course-slug course-slug
                        :editable?   form-editable?
                        :can-lock    can-lock?
                        :on-save     handle-form-saved}]
     (when-not form-editable?
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
                  :icon       "courses"
                  :icon-color "blue-2"
                  :class-name "page--edit-course--header"
                  :stats      [{:icon       "levels"
                                :icon-color "blue-2"
                                :counter    levels
                                :label      "Levels"}
                               {:icon       "lesson"
                                :icon-color "blue-2"
                                :counter    lessons
                                :label      "Lessons"}
                               {:icon       "games"
                                :icon-color "blue-2"
                                :counter    activities
                                :label      "Activities"}]}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [course-fetching? @(re-frame/subscribe [::state/course-fetching?])]
      [page/page {:class-name "page--edit-course with-header"}
       [header]
       [page/content {:title "Course Table"
                      :icon  "edit"}
        [levels-list]
        (when course-fetching?
          [ui/loading-overlay])]
       [side-bar props]])))

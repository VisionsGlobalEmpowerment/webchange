(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.drag-and-drop :as dnd]))

(defonce dragged-item (atom {}))

(defn- handle-drag-start
  [event]
  (reset! dragged-item (dnd/get-data-set event))
  (set! (.. event -dataTransfer -effectAllowed) "move")
  (set! (.. event -dataTransfer -dropEffect) "move")
  (let [target (.-target event)]
    (.add (.-classList target) "dragged")))

(defn- handle-drag-over
  [event]
  (.preventDefault event)
  (.stopPropagation event))

(defn- drop-allowed?
  [dragged-data target-data]
  (and (= (:type target-data)
          (:type dragged-data))
       (not= target-data dragged-data)))

(defn- handle-drag-enter
  [event]
  (let [target-data (dnd/get-data-set event)]
    (when (drop-allowed? @dragged-item target-data)
      (.add (.. event -target -classList) "drag-over"))))

(defn- handle-drag-leave
  [event]
  (let [target (.-target event)]
    (.remove (.-classList target) "drag-over")))

(defn- handle-drag-end
  [event]
  (let [target (.-target event)
        all-items (.querySelectorAll js/document "[draggable]")]
    (.remove (.-classList target) "dragged")
    (.forEach all-items (fn [item]
                          (.remove (.-classList item) "drag-over")))))

(defn- handle-drop
  [event]
  (let [target-data (dnd/get-data-set event)]
    (when (drop-allowed? @dragged-item target-data)
      (js/console.log "handle-drop")
      (print "target" (dnd/get-data-set event))
      (print "dragged" @dragged-item))))

(defn- init-dnd
  [el]
  (.addEventListener el "dragstart" handle-drag-start)
  (.addEventListener el "dragover" handle-drag-over)
  (.addEventListener el "dragenter" handle-drag-enter)
  (.addEventListener el "dragleave" handle-drag-leave)
  (.addEventListener el "dragend" handle-drag-end)
  (.addEventListener el "drop" handle-drop))

(defn- reset-dnd
  [el]
  (.removeEventListener el "dragstart" handle-drag-start)
  (.removeEventListener el "dragover" handle-drag-over)
  (.removeEventListener el "dragenter" handle-drag-enter)
  (.removeEventListener el "dragleave" handle-drag-leave)
  (.removeEventListener el "dragend" handle-drag-end)
  (.removeEventListener el "drop" handle-drop))


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
                                        (re-frame/dispatch [::state/remove-lesson level-idx idx]))
               handle-ref #(when (some? %)
                             (init-dnd %))]
    [:<>
     [l/list-item {:name       name
                   :class-name (ui/get-class-name {"list-item" true
                                                   "selected"  @expanded?})
                   :on-click   handle-item-click
                   :pre        [reorder-control]
                   :html-attrs {:draggable   true
                                :data-type   "lesson"
                                :data-level  level-idx
                                :data-lesson idx}
                   :actions    [ui/icon-button {:icon     "remove"
                                                :title    "Remove"
                                                :variant  "light"
                                                :on-click handle-remove-click}]
                   :ref        handle-ref}
      [l/content-right
       [:div.list-item-stats
        activities-number
        [ui/icon {:icon       "activity"
                  :class-name "activity"}]]]]
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
                                        (re-frame/dispatch [::state/remove-level idx]))
               handle-ref #(when (some? %)
                             (init-dnd %))]
    [:<>
     [l/list-item {:name       name
                   :class-name (ui/get-class-name {"list-item" true
                                                   "selected"  @expanded?})
                   :on-click   handle-item-click
                   :pre        [reorder-control]
                   :html-attrs {:draggable  true
                                :data-type  "level"
                                :data-level idx}
                   :actions    [ui/icon-button {:icon     "remove"
                                                :title    "Remove"
                                                :variant  "light"
                                                :on-click handle-remove-click}]
                   :ref        handle-ref}
      [l/content-right
       [:div.list-item-stats
        lessons-number
        [ui/icon {:icon        "lesson"
                  ::class-name "lesson"}]]]]
     (when @expanded?
       [lessons-list {:level-idx idx}])]))

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

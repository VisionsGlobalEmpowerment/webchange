(ns webchange.editor-v2.course-table.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.keyboard-control :as keyboard]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.pagination :as pagination-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.views-context-menu :refer [context-menu]]
    [webchange.editor-v2.course-table.views-row :refer [activity-row]]
    [webchange.editor-v2.course-table.views-table-pagination :refer [pagination]]
    [webchange.editor-v2.course-table.utils.cell-data :refer [get-row-id click-event->cell-data]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [move-selection]]
    [webchange.editor-v2.course-table.utils-rows-number :refer [get-row-height get-rows-number get-actual-rows-number]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.routes :refer [redirect-to]]))

(def header-data [{:id :idx :title "#" :width "2%"}
                  {:id :level-idx :title "Level" :width "2%"}
                  {:id :lesson-idx :title "Lesson" :width "2%"}
                  {:id :concepts :title "Concepts" :width "10%"}
                  {:id :activity :title "Activities" :width "6%"}
                  {:id :abbr-global :title "Global Standard Abbreviation" :width "10%"}
                  {:id :skills :title "Standard/Competency" :width "10%"}
                  {:id :tags :title "Adaptation" :width "9%"}])

(defn- col-group
  [{:keys [columns]}]
  [:colgroup
   (for [{:keys [id width]} columns]
     ^{:key id}
     [:col {:style {:width width}}])])

(defn- header
  [{:keys [columns]}]
  [ui/table-head
   [ui/table-row
    (for [{:keys [id title]} columns]
      ^{:key id}
      [ui/table-cell {:class-name (clojure.core/name id)} title])]])

(defn- levels-count
  [data current-idx level-index]
  (->> data
       (filter (fn [{:keys [idx level-idx]}]
                 (and (>= idx current-idx)
                      (= level-idx level-index))))
       (count)))

(defn- lessons-count
  [data current-idx level-index lesson-index]
  (->> data
       (filter (fn [{:keys [idx level-idx lesson-idx]}]
                 (and (>= idx current-idx)
                      (= level-idx level-index)
                      (= lesson-idx lesson-index))))
       (count)))

(defn- body
  [{:keys [data columns rows-skip rows-count]}]
  (let [handle-cell-click (fn [event]
                            (let [data (click-event->cell-data event)]
                              (when (some? data)
                                (re-frame/dispatch [::selection-state/close-context-menu])
                                (re-frame/dispatch [::selection-state/set-selection data]))))
        handle-scroll (fn [event]
                        (.stopPropagation event)
                        (let [delta (if (> (.-deltaY event) 0) 1 -1)]
                          (re-frame/dispatch [::pagination-state/shift-skip-rows delta (count data)])))]
    (into [ui/table-body {:on-mouse-down handle-cell-click
                          :on-wheel      handle-scroll}]
          (loop [[activity & rest-activities] (->> data (drop rows-skip) (take rows-count))
                 rows []
                 current-level-idx nil
                 current-lesson-idx nil]
            (if (some? activity)
              (let [{:keys [idx level-idx lesson-idx]} activity
                    span-columns (cond-> {}
                                         (not= level-idx current-level-idx) (assoc :level-idx (levels-count data idx level-idx))
                                         (not= lesson-idx current-lesson-idx) (assoc :lesson-idx (lessons-count data idx level-idx lesson-idx))
                                         (not= lesson-idx current-lesson-idx) (assoc :concepts (lessons-count data idx level-idx lesson-idx)))
                    skip-columns (cond-> {}
                                         (= level-idx current-level-idx) (assoc :level-idx true)
                                         (and (= level-idx current-level-idx)
                                              (= lesson-idx current-lesson-idx)) (assoc :lesson-idx true)
                                         (and (= level-idx current-level-idx)
                                              (= lesson-idx current-lesson-idx)) (assoc :concepts true))]
                (recur rest-activities
                       (conj rows
                             ^{:key (get-row-id activity)}
                             [activity-row {:data         activity
                                            :columns      columns
                                            :span-columns span-columns
                                            :skip-columns skip-columns}])
                       (:level-idx activity)
                       (:lesson-idx activity)))
              rows)))))

(defn course-table
  []
  (let [container (atom nil)
        initialized? (r/atom false)
        handle-content-ref (fn [el]
                             (when (some? el)
                               (reset! container el)))
        handle-key-down (fn [event]
                          (keyboard/handle-event event
                                                 {:move-selection  #(move-selection % header-data)
                                                  :reset-selection #(re-frame/dispatch [::selection-state/reset-selection])}))]
    (r/create-class
      {:display-name "course-table"

       :component-did-mount
                     (fn [this]
                       (let [{:keys [course-id]} (r/props this)]
                         (re-frame/dispatch [::data-state/init course-id])
                         (let [row-height (get-row-height @container)
                               rows-number (get-rows-number @container row-height)]
                           (re-frame/dispatch [::pagination-state/set-page-rows rows-number])
                           (reset! initialized? true))))

       :component-did-update
                     (fn []
                       (let [actual-rows-number (get-actual-rows-number @container)]
                         (re-frame/dispatch [::pagination-state/set-actual-page-rows actual-rows-number])))

       :reagent-render
                     (fn [{:keys [course-id]}]
                       (let [data @(re-frame/subscribe [::data-state/table-data])
                             rows-skip @(re-frame/subscribe [::pagination-state/skip-rows])
                             rows-count @(re-frame/subscribe [::pagination-state/page-rows])]
                         [layout {:breadcrumbs [{:text     "Course"
                                                 :on-click #(redirect-to :course-editor-v2 :id course-id)}
                                                {:text "Table"}]
                                  :styles      {:content-container {:padding "0"}}
                                  :content-ref handle-content-ref}
                          [ui/paper {:style {:display        "flex"
                                             :flex-direction "column"
                                             :height         "100%"}}
                           [:div {:tab-index   0
                                  :on-key-down handle-key-down
                                  :style       {:flex-grow 1
                                                :overflow  "hidden"
                                                :outline   "none"}}
                            [ui/table {:class-name "course-table"
                                       :padding    "none"}
                             [col-group {:columns header-data}]
                             [header {:columns header-data}]
                             [body (merge {:columns header-data}
                                          (if @initialized?
                                            {:data       data
                                             :rows-skip  rows-skip
                                             :rows-count rows-count}
                                            {:data       [{}]
                                             :rows-skip  0
                                             :rows-count 1}))]]]
                           [pagination {:data data}]
                           [context-menu {:container container}]]]))})))

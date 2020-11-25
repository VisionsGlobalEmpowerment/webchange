(ns webchange.editor-v2.course-table.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.keyboard-control :as keyboard]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.views-activity :refer [activity-row]]
    [webchange.editor-v2.course-table.utils.cell-data :refer [cell->cell-data get-row-id]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [move-selection]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.routes :refer [redirect-to]]))

(def header-data [{:id :level :title "Level" :width 0}
                  {:id :lesson :title "Lesson" :width 0}
                  {:id :idx :title "#" :width 0}
                  ;{:id :concepts  :title "Concept"}
                  {:id :activity :title "Activities" :width 100}
                  ;{:id :abbr-global :title "Global Standard Abbreviation"}
                  ;{:title "TabSchool Reference"}
                  ;{:title "Standard/Competency"}
                  ])

(defn- col-group
  [{:keys [columns]}]
  [:colgroup
   (for [{:keys [id width]} columns]
     ^{:key id}
     [:col {:style {:width (str width "%")}}])])

(defn- header
  [{:keys [columns]}]
  [ui/table-head
   [ui/table-row
    (for [{:keys [id title width]} columns]
      ^{:key id}
      [ui/table-cell {:style {:width width}} title])]])

(defn- levels-count
  [data level-id]
  (->> data
       (filter (fn [{:keys [level]}]
                 (= level level-id)))
       (count)))

(defn- lessons-count
  [data level-id lesson-id]
  (->> data
       (filter (fn [{:keys [level lesson]}]
                 (and (= level level-id)
                      (= lesson lesson-id))))
       (count)))

(defn- body
  [{:keys [data columns]}]
  (r/with-let [_ (keyboard/enable {:enter           #(print "enter")
                                   :move-selection  #(move-selection data (:data @(re-frame/subscribe [::selection-state/selection])) % columns)
                                   :reset-selection #(print "reset-selection")})]
    (let [handle-cell-click (fn [event]
                              (print "handle-cell-click")
                              (let [data (-> event (.-target) (cell->cell-data))]
                                (re-frame/dispatch [::selection-state/set-selection :cell data])))]
      (into [ui/table-body {:on-click handle-cell-click}]
            (loop [[activity & rest-activities] data
                   rows []
                   current-level nil
                   current-lesson nil]
              (if (some? activity)
                (let [level-id (:level activity)
                      lesson-id (:lesson activity)
                      span-columns (cond-> {}
                                           (not= level-id current-level) (assoc :level (levels-count data level-id))
                                           (not= lesson-id current-lesson) (assoc :lesson (lessons-count data level-id lesson-id)))
                      skip-columns (cond-> {}
                                           (= level-id current-level) (assoc :level true)
                                           (= lesson-id current-lesson) (assoc :lesson true))]
                  (recur rest-activities
                         (conj rows
                               ^{:key (get-row-id activity)}
                               [activity-row {:data         activity
                                              :columns      columns
                                              :span-columns span-columns
                                              :skip-columns skip-columns}])
                         (:level activity)
                         (:lesson activity)))
                rows))))
    (finally
      (keyboard/disable))))

(defn course-table
  [{:keys [course-id]}]
  (r/with-let [_ (re-frame/dispatch [::data-state/init course-id])]
    (let [data @(re-frame/subscribe [::data-state/table-data])]
      [layout {:breadcrumbs [{:text     "Course"
                              :on-click #(redirect-to :course-editor-v2 :id course-id)}
                             {:text "Table"}]}
       [ui/paper
        [ui/table
         [col-group {:columns header-data}]
         [header {:columns header-data}]
         [body {:data    data
                :columns header-data}]]]])))

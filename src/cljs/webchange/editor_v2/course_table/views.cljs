(ns webchange.editor-v2.course-table.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.keyboard-control :as keyboard]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.edit :as edit-state]
    [webchange.editor-v2.course-table.state.pagination :as pagination-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.views-edit-form :refer [edit-form field-editable?]]
    [webchange.editor-v2.course-table.views-row :refer [activity-row]]
    [webchange.editor-v2.course-table.utils.cell-data :refer [cell->cell-data get-row-id]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [move-selection]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.routes :refer [redirect-to]]))

(def header-data [{:id :level :title "Level" :width 0}
                  {:id :lesson :title "Lesson" :width 0}
                  {:id :idx :title "#" :width 0}
                  {:id :concepts :title "Concepts" :width 10}
                  {:id :activity :title "Activities" :width 20}
                  {:id :abbr-global :title "Global Standard Abbreviation" :width 30}
                  {:id :skills :title "Standard/Competency" :width 30}
                  {:id :tags :title "Adaptation" :width 10}])

(defn- field->column
  [field-id columns]
  (some (fn [{:keys [id] :as column}]
          (and (= id field-id)
               column))
        columns))

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
    (for [{:keys [id title]} columns]
      ^{:key id}
      [ui/table-cell title])]])

(defn- levels-count
  [data current-idx level-id]
  (->> data
       (filter (fn [{:keys [idx level]}]
                 (and (>= idx current-idx)
                      (= level level-id))))
       (count)))

(defn- lessons-count
  [data current-idx level-id lesson-id]
  (->> data
       (filter (fn [{:keys [idx level lesson]}]
                 (and (>= idx current-idx)
                      (= level level-id)
                      (= lesson lesson-id))))
       (count)))

(defn- click-event->cell-data
  [event]
  (-> event
      (.-target)
      (.closest "td")
      (cell->cell-data)))

(defn- body
  [{:keys [data columns]}]
  (r/with-let [_ (keyboard/enable {:enter           #(print "enter")
                                   :move-selection  #(move-selection % columns)
                                   :reset-selection #(print "reset-selection")})]
    (let [rows-skip @(re-frame/subscribe [::pagination-state/skip-rows])
          rows-count @(re-frame/subscribe [::pagination-state/page-rows])

          handle-cell-click (fn [event]
                              (let [data (click-event->cell-data event)]
                                (re-frame/dispatch [::selection-state/set-selection :cell data])))
          handle-cell-double-click (fn [event]
                                     (let [{:keys [field] :as cell-data} (click-event->cell-data event)]
                                       (when (field-editable? cell-data)
                                         (re-frame/dispatch [::edit-state/open-menu {:cell-data cell-data
                                                                                     :title     (-> field (field->column columns) :title)}]))))
          handle-scroll (fn [event]
                          (let [delta (if (> (.-deltaY event) 0) 1 -1)]
                            (re-frame/dispatch [::pagination-state/shift-skip-rows delta (count data)])))]
      (into [ui/table-body {:on-click        handle-cell-click
                            :on-double-click handle-cell-double-click
                            :on-wheel        handle-scroll}]
            (loop [[activity & rest-activities] (->> data (drop rows-skip) (take rows-count))
                   rows []
                   current-level nil
                   current-lesson nil
                   counter 0]
              (if (some? activity)
                (let [{:keys [idx level lesson]} activity
                      span-columns (cond-> {}
                                           (not= level current-level) (assoc :level (levels-count data idx level))
                                           (not= lesson current-lesson) (assoc :lesson (lessons-count data idx level lesson)))
                      skip-columns (cond-> {}
                                           (= level current-level) (assoc :level true)
                                           (= lesson current-lesson) (assoc :lesson true))]
                  (recur rest-activities
                         (conj rows
                               ^{:key (get-row-id activity)}
                               [activity-row {:data         activity
                                              :columns      columns
                                              :span-columns span-columns
                                              :skip-columns skip-columns}])
                         (:level activity)
                         (:lesson activity)
                         (inc counter)))
                rows))))
    (finally
      (keyboard/disable))))

(defn- footer
  [{:keys [data]}]
  (let [rows-skip @(re-frame/subscribe [::pagination-state/skip-rows])
        rows-count @(re-frame/subscribe [::pagination-state/page-rows])
        from (inc rows-skip)
        to (+ rows-skip rows-count)
        total (count data)]
    [:div.footer {:style {:padding    "16px"
                          :text-align "right"
                          :border     "solid 1px #414141"}}
     [ui/typography
      (str "Rows: " from " - " to " of " total)]]))

(defn- get-element-height
  ([el]
   (get-element-height el {}))
  ([el {:keys [without-padding]
        :or   {without-padding false}}]
   (let [style (.getComputedStyle js/window el nil)
         ->int #(.parseInt js/Number %)
         get-prop #(.getPropertyValue style %)]
     (cond-> (-> "height" get-prop ->int)
             without-padding (-> (- (-> "padding-top" get-prop ->int))
                                 (- (-> "padding-bottom" get-prop ->int)))))))

(defn- get-rows-count
  [content-el]
  (let [header (.querySelector content-el "thead")
        footer (.querySelector content-el ".footer")
        content-row (.querySelector content-el "tbody > tr")]
    (when (and (some? header)
               (some? content-row))
      (let [content-height (get-element-height content-el {:without-padding true})
            header-height (get-element-height header)
            footer-height (get-element-height footer)
            content-row-height (get-element-height content-row)]
        (-> (- content-height header-height footer-height)
            (/ content-row-height)
            (Math/ceil))))))

(defn course-table
  []
  (let [container (atom nil)
        handle-content-ref (fn [el]
                             (when (some? el)
                               (reset! container el)))]
    (r/create-class
      {:display-name "course-table"

       :component-did-mount
                     (fn [this]
                       (let [{:keys [course-id]} (r/props this)]
                         (re-frame/dispatch [::data-state/init course-id])
                         (when (some? @container)
                           (re-frame/dispatch [::pagination-state/set-page-rows (get-rows-count @container)]))))

       :component-did-update
                     (fn []
                       (when (some? @container)
                         (re-frame/dispatch [::pagination-state/set-page-rows (get-rows-count @container)])))

       :reagent-render
                     (fn [{:keys [course-id]}]

                       (let [data @(re-frame/subscribe [::data-state/table-data])]
                         [layout {:breadcrumbs [{:text     "Course"
                                                 :on-click #(redirect-to :course-editor-v2 :id course-id)}
                                                {:text "Table"}]
                                  :content-ref handle-content-ref}
                          [ui/paper {:style {:display        "flex"
                                             :flex-direction "column"
                                             :height         "100%"}}
                           [:div {:style {:flex-grow 1
                                          :overflow  "hidden"}}
                            [ui/table {:class-name "course-table"}
                             [col-group {:columns header-data}]
                             [header {:columns header-data}]
                             [body {:data    data
                                    :columns header-data}]]]
                           [footer {:data data}]
                           [edit-form]]]))})))

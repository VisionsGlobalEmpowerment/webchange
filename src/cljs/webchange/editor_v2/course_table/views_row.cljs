(ns webchange.editor-v2.course-table.views-row
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]

    [webchange.editor-v2.course-table.fields.activities.views :refer [activities]]
    [webchange.editor-v2.course-table.fields.concepts.views :refer [concepts]]
    [webchange.editor-v2.course-table.fields.skills.views :refer [skills]]
    [webchange.editor-v2.course-table.fields.tags.views :refer [tags]]

    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [activity->cell-data cell-data->cell-attributes]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- index
  [{:keys [data]}]
  (:idx data))

(defn- lesson
  [{:keys [data]}]
  (:lesson-idx data))

(defn- level
  [{:keys [data]}]
  (:level-idx data))

(defn- default-component
  [{:keys [field]}]
  (let [color (get-in-theme [:palette :warning :default])]
    [:div {:style {:align-items "center"
                   :color       color
                   :display     "flex"}}
     [ic/warning {:style {:font-size    "16px"
                          :margin-right "8px"}}]
     [ui/typography {:style {:color color}} (str "<" field ">")]]))

(def components {:level-idx   [level]
                 :lesson-idx  [lesson]
                 :idx         [index]
                 :concepts    [concepts]
                 :activity    [activities]
                 :abbr-global [skills {:field :abbr}]
                 :skills      [skills {:field :name}]
                 :tags        [tags]})

(defn- get-component
  [id]
  (if (contains? components id)
    (get components id)
    default-component))

(defn- cell-selected?
  [selection-data cell-data field]
  (let [fields-to-check (cond
                          (= field :idx) [:level-idx :lesson-idx :activity-idx]
                          (some #{field} [:level-idx :lesson-idx :concepts]) [:level-idx :lesson-idx :field]
                          :else (keys selection-data))]
    (and (some? selection-data)
         (= (select-keys selection-data fields-to-check)
            (select-keys cell-data fields-to-check)))))

(defn- field-editable?
  [field]
  (some #{field} [:abbr-global :activity :concepts :skills :tags]))

(defn- field-cell
  [{:keys [data field span] :as props}]
  (let [selection @(re-frame/subscribe [::selection-state/selection])
        cell-data (activity->cell-data data field)
        spanned? (some? span)
        selected? (cell-selected? selection cell-data field)
        editable? (field-editable? (:field cell-data))
        [component component-props] (get-component field)]
    [ui/table-cell (cond-> (merge (:cell-props props)
                                  (cell-data->cell-attributes cell-data)
                                  {:class-name (clojure.core/name field)})
                           spanned? (assoc :row-span span)
                           selected? (update :class-name str " selected")
                           editable? (update :class-name str " editable"))
     [component (merge component-props
                       {:edit? selected?
                        :data  data})]]))

(defn- lesson-row-selected?
  [selection-data cell-data]
  (let [fields-to-check [:level-idx :lesson-idx]]
    (and (= (:field selection-data) :lesson-idx)
         (= (select-keys selection-data fields-to-check)
            (select-keys cell-data fields-to-check)))))

(defn activity-row
  [{:keys [data columns span-columns skip-columns]}]
  (let [filtered-columns (->> columns
                              (filter (fn [{:keys [id]}]
                                        (-> skip-columns
                                            (contains? id)
                                            (not)))))

        selection @(re-frame/subscribe [::selection-state/selection])
        cell-data (activity->cell-data data)
        lesson-selected? (lesson-row-selected? selection cell-data)]
    [ui/table-row {:class-name (if lesson-selected? "row-selected" "row-not-selected")}
     (for [{:keys [id]} filtered-columns]
       ^{:key id}
       [field-cell {:data  data
                    :span  (get span-columns id)
                    :field id}])]))

(ns webchange.editor-v2.course-table.views-row
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]

    [webchange.editor-v2.course-table.fields.activities.views :refer [activities]]
    [webchange.editor-v2.course-table.fields.concepts.views :refer [concepts]]
    [webchange.editor-v2.course-table.fields.skills.views :refer [skills]]
    [webchange.editor-v2.course-table.fields.tags.views :refer [tags]]

    [webchange.editor-v2.course-table.state.edit :refer [field-editable?] :as edit-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [activity->cell-data cell-data->cell-attributes]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- index
  [{:keys [data]}]
  (:idx data))

(defn- lesson
  [{:keys [data]}]
  (:lesson data))

(defn- level
  [{:keys [data]}]
  (:level data))

(defn- default-component
  [{:keys [field]}]
  (let [color (get-in-theme [:palette :warning :default])]
    [:div {:style {:align-items "center"
                   :color       color
                   :display     "flex"}}
     [ic/warning {:style {:font-size    "16px"
                          :margin-right "8px"}}]
     [ui/typography {:style {:color color}} (str "<" field ">")]]))

(def components {:level       level
                 :lesson      lesson
                 :idx         index
                 :concepts    concepts
                 :activity    activities
                 :abbr-global skills
                 :skills      skills
                 :tags        tags})

(defn- get-component
  [id]
  (if (contains? components id)
    (get components id)
    default-component))

(defn- cell-selected?
  [selection-type selection-data cell-data field]
  (let [fields-to-check (if (some #{field} [:level :lesson :concepts])
                          [:level :lesson :field]
                          (keys selection-data))]
    (and (= selection-type :cell)
         (= (select-keys selection-data fields-to-check)
            (select-keys cell-data fields-to-check)))))

(defn- field-cell
  [{:keys [data field span] :as props}]
  (let [selection @(re-frame/subscribe [::selection-state/selection])
        edit-mode? @(re-frame/subscribe [::edit-state/open?])
        cell-data (activity->cell-data data field)
        spanned? (some? span)
        selected? (cell-selected? (:type selection) (:data selection) cell-data field)
        editable? (field-editable? (:field cell-data))]
    [ui/table-cell (cond-> (merge (:cell-props props)
                                  (cell-data->cell-attributes cell-data))
                           spanned? (assoc :row-span span)
                           selected? (update :class-name str " selected")
                           editable? (update :class-name str " editable"))
     [(get-component field) {:edit? (and edit-mode?
                                         selected?)
                             :data  data}]]))

(defn activity-row
  [{:keys [data columns span-columns skip-columns]}]
  (let [filtered-columns (->> columns
                              (filter (fn [{:keys [id]}]
                                        (-> skip-columns
                                            (contains? id)
                                            (not)))))]
    [ui/table-row
     (for [{:keys [id]} filtered-columns]
       ^{:key id}
       [field-cell {:data  data
                    :span  (get span-columns id)
                    :field id}])]))

(ns webchange.editor-v2.course-table.views-activity
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [activity->cell-data cell-data->cell-attributes]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- cell-selected?
  [selection-type selection-data cell-data]
  (and (= selection-type :cell)
       (= selection-data cell-data)))

(defn- cell
  [{:keys [activity field] :as props}]
  (let [selection @(re-frame/subscribe [::selection-state/selection])
        cell-data (activity->cell-data activity field)
        selected? (cell-selected? (:type selection) (:data selection) cell-data)]
    (into [ui/table-cell (-> props
                             (merge (cell-data->cell-attributes cell-data))
                             (assoc :class-name (if selected? "selected" ""))
                             (assoc :padding "none")
                             (dissoc :activity)
                             (dissoc :field))]
          (-> (r/current-component) (r/children)))))

(defn- activity
  [{:keys [data field]}]
  [cell {:field    field
         :activity data}
   (:activity data)])

(defn- concepts
  [{:keys [data field]}]
  [cell {:field    field
         :activity data} (str (:concepts data))])

(defn- index
  [{:keys [data field]}]
  [cell {:field    field
         :activity data
         :align    "center"}
   (:idx data)])

(defn- lesson
  [{:keys [data span field]}]
  (let [props (cond-> {:activity data
                       :field    field
                       :align    "center"}
                      (some? span) (assoc :row-span span))]
    [cell props
     (:lesson data)]))

(defn- level
  [{:keys [data span field]}]
  (let [props (cond-> {:activity data
                       :field    field
                       :align    "center"}
                      (some? span) (assoc :row-span span))]
    [cell props
     (:level data)]))

(defn- skills-global
  [{:keys [data span field]}]
  (let [props (cond-> {:activity data
                       :field    field}
                      (some? span) (assoc :row-span span))]
    [cell props
     [:ul {:style {:padding 0}}
      (for [{:keys [abbr]} (:skills data)]
        ^{:key abbr}
        [:li abbr])]]))

(defn- default-component
  [{:keys [field]}]
  (let [color (get-in-theme [:palette :warning :default])]
    [ui/table-cell
     [:div {:style {:align-items "center"
                    :color       color
                    :display     "flex"}}
      [ic/warning {:style {:font-size    "16px"
                           :margin-right "8px"}}]
      [ui/typography {:style {:color color}} (str "<" field ">")]]]))

(def components {:level       level
                 :lesson      lesson
                 :idx         index
                 :concepts    concepts
                 :activity    activity
                 :abbr-global skills-global})

(defn- get-component
  [id]
  (if (contains? components id)
    (get components id)
    default-component))

(defn activity-row
  [{:keys [data columns span-columns skip-columns ref]}]
  (let [filtered-columns (->> columns
                              (filter (fn [{:keys [id]}]
                                        (-> skip-columns
                                            (contains? id)
                                            (not)))))]
    [ui/table-row {:ref ref}
     (for [{:keys [id]} filtered-columns]
       ^{:key id}
       [(get-component id) {:data  data
                            :span  (get span-columns id)
                            :field id}])]))

(ns webchange.editor-v2.course-table.views-row
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.editor-v2.course-table.views-row-common :refer [field-cell]]
    [webchange.editor-v2.course-table.views-row-skills :refer [skills-abbr skills-description]]
    [webchange.editor-v2.course-table.views-row-concepts :refer [concepts]]))

(defn- activity
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:class-name "activity"}})
   (:activity data)])

(defn- index
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:align "center"}})
   (:idx data)])

(defn- lesson
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:align "center"}})
   (:lesson data)])

(defn- level
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:align "center"}})
   (:level data)])

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
                 :abbr-global skills-abbr
                 :skills      skills-description})

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

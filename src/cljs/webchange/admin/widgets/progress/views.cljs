(ns webchange.admin.widgets.progress.views
  (:require
    [webchange.ui-framework.components.index :refer [avatar icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- item-row
  [{:keys [name code]}]
  [:div.item-row
   [avatar]
   [:div.info
    [:div.name name]
    [:dl
     [:dt "code"]
     [:dd code]]]])

(defn- items-list
  [{:keys [data]}]
  [:div.items-list
   [:div.items-header]
   (for [{:keys [id] :as item} data]
     ^{:key id}
     [item-row item])])

(defn- action-row
  [{:keys [id]}]
  (let [handle-click #(print "Edit" id)]
    [:div.action-row
     [icon-button {:icon       "edit"
                   :variant    "light"
                   :class-name "action-button"
                   :on-click   handle-click}]]))

(defn- actions-list
  [{:keys [data]}]
  [:div.actions-list
   [:div.actions-header]
   (for [{:keys [id] :as action} data]
     ^{:key id}
     [action-row action])])

(defn- progress-indicator
  [{:keys [filled?]}]
  [:div {:class-name (get-class-name {"progress-indicator" true
                                      "empty"              (not filled?)
                                      "filled"             filled?})}])

(defn- progress-item
  [{:keys [value max-value] :as props}]
  (print "progress-item" props)
  (let [indicators (->> (range max-value)
                        (map (fn [idx]
                               {:id      idx
                                :filled? (< idx value)})))]
    [:div.progress-item
     (for [{:keys [id] :as indicator} indicators]
       ^{:key id}
       [progress-indicator indicator])]))

(defn- progress-row
  [{:keys [data] :as props}]
  (print "progress-row" props)
  [:<>
   (for [{:keys [id] :as item} data]
     ^{:key id}
     [progress-item item])])

(defn- progress-table
  [{:keys [columns data]}]
  [:div.progress-table {:style {:grid-template-columns (str "repeat(" (count columns) ", auto)")}}
   (for [{:keys [id name]} columns]
     ^{:key id}
     [:div.header-item name])
   (for [{:keys [id] :as row} data]
     ^{:key id}
     [progress-row row])])

(defn progress
  [{:keys [data] :as props}]
  (let [{:keys [data rows columns]} data
        progress-data (->> rows
                           (map (fn [{row-id :id}]
                                  {:id   row-id
                                   :data (->> columns
                                              (map (fn [{column-id :id capacity :capacity}]
                                                     {:id        (str row-id "-" column-id)
                                                      :value     (get-in data [row-id column-id :value])
                                                      :max-value capacity})))})))]
    [:div.widget--progress
     [items-list {:data rows}]
     [progress-table {:columns columns
                      :data    progress-data}]
     [actions-list {:data rows}]]))

#_{:columns [{:id       :lesson-1
              :name     "Lesson 01"
              :capacity 18}
             {:id       :lesson-2
              :name     "Lesson 02"
              :capacity 5}
             {:id       :lesson-3
              :name     "Lesson 03"
              :capacity 24}]
   :rows    [{:id   :student-1
              :name "Student 1"
              :code "123"}
             {:id   :student-2
              :name "Student 2"
              :code "456"}
             {:id   :student-3
              :name "Student 3"
              :code "789"}]
   :data    {:student-1 {:lesson-1 {:value 8}
                         :lesson-2 {:value 5}
                         :lesson-3 {:value 3}}
             :student-2 {:lesson-1 {:value 8}
                         :lesson-2 {:value 3}
                         :lesson-3 {:value 0}}
             :student-3 {:lesson-1 {:value 0}
                         :lesson-2 {:value 0}
                         :lesson-3 {:value 3}}}}

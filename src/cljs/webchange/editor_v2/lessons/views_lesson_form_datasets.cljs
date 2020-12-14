(ns webchange.editor-v2.lessons.views-lesson-form-datasets
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:action-icon        {:font-size "1.4rem"}
   :add-item-container {:padding-right "20px"}
   :dataset-caption    {:border-bottom  (str "solid 1px " (get-in-theme [:palette :border :default]))
                        :font-size      "1.15rem"
                        :margin         "0 8px"
                        :padding        "8px 14px 3px"
                        :text-transform "capitalize"}})

(defn- add-concept-to-lesson
  [items concept]
  (conj (vec items) concept))

(defn- remove-concept-from-lesson
  [items concept-name]
  (->> items
       (filter #(not= (:name %) concept-name))))

(defn- get-item
  [items name]
  (let [item (->> items
                  (filter #(= (:name %) name))
                  first)]
    (select-keys item [:id :name])))

(defn- add-dataset-item-form
  [{:keys [data dataset-items lesson-key]}]
  (let [styles (get-styles)]
    [ui/grid {:container   true
              :spacing     24
              :align-items "center"}
     [ui/grid {:item true :xs 4}
      [ui/typography {:align   "right"
                      :variant "body1"}
       "Add"]]
     [ui/grid {:item  true :xs 8
               :style (:add-item-container styles)}
      [ui/form-control {:full-width true
                        :margin     "dense"}
       [ui/select {:value     ""
                   :variant   "outlined"
                   :on-change #(swap! data
                                      update-in
                                      [:lesson-sets lesson-key :items]
                                      (fn [list] (add-concept-to-lesson list (get-item dataset-items (-> % .-target .-value)))))}
        (for [item dataset-items]
          ^{:key (:name item)}
          [ui/menu-item {:value (:name item)} (:name item)])]]]]))

(defn- lesson-set-form
  [{:keys [data dataset-items lesson-key]}]
  (let [lesson-items (-> @data :lesson-sets lesson-key :items)
        not-used-dataset-items (->> dataset-items
                                    (filter
                                      (fn [dataset-item]
                                        (->> lesson-items
                                             (some (fn [lesson-item]
                                                     (= (:id dataset-item)
                                                        (:id lesson-item))))
                                             (not))))
                                    (sort-by :name))
        styles (get-styles)]
    [:div
     [ui/typography {:variant "h6"
                     :style   (:dataset-caption styles)}
      (name lesson-key) ":"]
     [ui/list
      (for [item lesson-items]
        ^{:key (:name item)}
        [ui/list-item
         [ui/list-item-text {:primary (:name item)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:aria-label "Delete"
                           :on-click   #(swap! data
                                               update-in
                                               [:lesson-sets lesson-key :items]
                                               (fn [list] (remove-concept-from-lesson list (:name item))))}
           [ic/delete {:style (:action-icon styles)}]]]])]
     [add-dataset-item-form {:data          data
                             :dataset-items not-used-dataset-items
                             :lesson-key    lesson-key}]]))

(defn lesson-datasets
  [{:keys [data dataset-items lesson-scheme]}]
  [ui/grid {:container true
            :spacing   24}
   (for [lesson-set (:lesson-sets lesson-scheme)]
     ^{:key (str lesson-set)}
     [ui/grid {:item true :xs 12}
      [lesson-set-form {:data          data
                        :dataset-items dataset-items
                        :lesson-key    (keyword lesson-set)}]])])

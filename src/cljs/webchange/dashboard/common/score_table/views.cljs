(ns webchange.dashboard.common.score-table.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.dashboard.common.score-table.views-legend :refer [score-table-legend]]
    [webchange.dashboard.common.score-table.views-theme :refer [score-colors]]))

(def border-width 3)
(defn get-score-style
  [levels score-percentage started? finished?]
  (let [low (get levels 0)
        high (get levels 1)]
    {:border-color     "#fff"
     :border-style     "solid"
     :border-width     border-width
     :background-color (cond (and started? (not finished?)) (:gray score-colors)
                             (>= score-percentage high) (:green score-colors)
                             (>= score-percentage low) (:yellow score-colors)
                             (> score-percentage 0) (:red score-colors)
                             :else "rgba(0,0,0,0)")}))

(defn align-values-list
  [list length]
  (into list (take (- length (count list))) (repeatedly (constantly nil))))

(defn score-header
  [{:keys [title items-title title-span]}]
  [ui/table-head
   [ui/table-row
    [ui/table-cell
     {:align "center"
      :style {:border    "none"
              :max-width 60}}
     [ui/typography
      {:variant "overline"}
      items-title]]
    [ui/table-cell
     {:align    "center"
      :col-span title-span}
     [ui/typography
      {:variant "overline"}
      title]]]])

(defn value-names
  [{:keys [names]}]
  [ui/table-row
   [ui/table-cell
    {:style {:border "none"}}]
   (for [name names]
     ^{:key (str "value-name-" name)}
     [ui/table-cell
      {:align "center"
       :style {:border "none"}}
      name])])

(defn values-legend
  [{:keys [legend legend-span]}]
  [ui/table-row
   [ui/table-cell
    {:style {:border "none"}}]
   [ui/table-cell
    {:col-span legend-span
     :style    {:border  "none"
                :padding 0}}
    [score-table-legend legend]]])

(defn value-item
  [{{:keys [label value percentage started finished]} :value levels :value-levels}]
  [ui/tooltip
   {:title                  (if value (str label " : " value) label)
    :placement              "top-end"
    :disable-hover-listener (not started)}
   [ui/table-cell
    {:style (get-score-style levels percentage started finished)}]])

(defn score-table
  [{:keys [legend levels title items-title]} data]
  (let [items data
        values-number (or (apply max (map #(->> % :values count) items)) 0)]
    [:div
     {:style {:padding border-width}}
     [ui/table
      {:padding "dense"}
      [score-header
       {:title       title
        :items-title items-title
        :title-span  values-number}]
      [ui/table-body
       (for [item items]
         ^{:key (:name item)}
         [ui/table-row
          [ui/table-cell
           {:align "right"
            :style {:border "none"
                    :max-width 50}}
           (:name item)]
          (let [values (align-values-list (:values item) values-number)]
            (map-indexed (fn [index value]
                           ^{:key (str (:name item) index)}
                           [value-item {:value value :value-levels levels}]
                           ) values))])
       [value-names
        {:names (range 1 (inc values-number))}]
       [values-legend
        {:legend      legend
         :legend-span values-number}]]]]))

(ns webchange.dashboard.students.student-profile.views-scores
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.dashboard.common.dashboard-page :refer [dashboard-page-block]]
    [webchange.dashboard.score-table.views :refer [score-table]]))

(defn translate
  [path]
  (get-in {:level "Level"}
          path))

(defn student-score-block
  [{:keys [data]}]
  (let [current-level (r/atom (->> (:levels data) keys first))]
    (fn [{:keys [title data legend]}]
      [dashboard-page-block
       {:title title}
       [ui/grid {:container true :spacing 16}
        [ui/grid {:item true :xs 2}
         [ui/typography {:variant "overline" :style {:padding    "17px 0"
                                                     :text-align "center"}}
          (translate [:level])]
         [ui/menu-list
          (for [level (keys (:levels data))]
            ^{:key level}
            [ui/menu-item
             {:selected (= level @current-level)
              :on-click #(reset! current-level level)
              :style    {:font-size       "0.8125rem"
                         :justify-content "center"
                         :padding         "5px 0px"}}
             level])]]
        [ui/grid {:item true :xs 10}
         [score-table
          {:title       (:title data)
           :items-title (:items-title data)
           :levels      (:marks data)
           :legend      legend}
          (get (:levels data) @current-level)]]]])))

(defn student-scores
  [data]
  [ui/grid
   {:container true
    :spacing   24}
   (for [scores data]
     ^{:key (:title scores)}
     [ui/grid
      {:item true :xs 6}
      [student-score-block scores]])])

(ns webchange.editor-v2.course-table.views-table-pagination
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.pagination :as pagination-state]))

(defn- pagination-button
  [{:keys [icon on-click]}]
  [ui/icon-button {:on-click on-click
                   :style    {:padding "8px"
                              :margin  "0 4px"}}
   [icon {:style {:font-size "16px"}}]])

(defn- current-page-info
  [{:keys [from to total]}]
  [ui/typography {:style {:display "inline-block"
                          :margin  "0 4px"}}
   (str "Rows: " from " - " to " of " total)])

(defn pagination
  [{:keys [data]}]
  (let [rows-skip @(re-frame/subscribe [::pagination-state/skip-rows])
        rows-count @(re-frame/subscribe [::pagination-state/page-rows])
        from (inc rows-skip)
        to (+ rows-skip rows-count)
        total (count data)

        handle-prev-page-click (fn [] (re-frame/dispatch [::pagination-state/shift-skip-rows (- rows-count) total]))
        handle-next-page-click (fn [] (re-frame/dispatch [::pagination-state/shift-skip-rows rows-count total]))

        handle-first-page-click (fn [] (re-frame/dispatch [::pagination-state/set-skip-rows 0]))
        handle-last-page-click (fn [] (re-frame/dispatch [::pagination-state/set-skip-rows (- total rows-count)]))]
    [:div.footer {:style {:padding    "8px"
                          :text-align "right"
                          :border     "solid 1px #414141"}}
     [pagination-button {:on-click handle-first-page-click :icon ic/first-page}]
     [pagination-button {:on-click handle-prev-page-click :icon ic/keyboard-arrow-left}]
     [current-page-info {:from from :to to :total total}]
     [pagination-button {:on-click handle-next-page-click :icon ic/keyboard-arrow-right}]
     [pagination-button {:on-click handle-last-page-click :icon ic/last-page}]]))

(ns webchange.student-dashboard.history.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.interpreter.events :as events]
    [webchange.student-dashboard.common.block-header.views :refer [block-header]]
    [webchange.student-dashboard.common.icons.views :as icons]
    [webchange.student-dashboard.events :as sde]
    [webchange.student-dashboard.history.views-filter :refer [history-filter]]
    [webchange.student-dashboard.history.views-history-list :refer [history-list]]
    [webchange.utils.deep-merge :refer [deep-merge]]))

(defn- get-styles
  []
  {:show-more {:margin-top "16px"
               :text-align "left"}})

(defn- filter-data
  [list filter-data]
  (filter (fn [item]
            (->> filter-data
                 (map (fn [[key value]] (= value (get item key))))
                 (every? identity)))
          list))

(defn history-block
  [{:keys [styles] :as props}]
  (let [styles (-> (get-styles)
                   (deep-merge (or styles {})))]
    [:div {:style (:container styles)}
     [block-header {:icon icons/history
                    :text "History"}]
     [history-list (-> props
                       (dissoc :style))]
     [:div {:style (:show-more styles)}
      [ui/button {:color    "primary"
                  :variant  "contained"
                  :on-click #(re-frame/dispatch [::sde/show-more])}
       "Show More"]]]))

(defn history-page
  [{:keys [data styles] :as props}]
  (let [styles (-> (get-styles)
                   (deep-merge (or styles {})))]
    (r/with-let [filter (r/atom {})]
      [:div {:style (:container styles)}
       [block-header {:icon   icons/history
                      :text   "History"
                      :action (r/as-element [ui/icon-button {:color    "primary"
                                                             :on-click #(re-frame/dispatch [::events/history-back])}
                                             [ic/navigate-before]])}]
       [history-filter {:data   data
                        :filter filter}]
       [history-list (-> props
                         (assoc :data (filter-data data @filter))
                         (dissoc :style))]])))

(ns webchange.student-dashboard.common.block-header.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:container      {:display     "flex"
                    :align-items "center"
                    :padding     "24px 6px"}
   :action-wrapper {:margin-right "16px"}
   :icon           {:margin-right "14px"}
   :text           {:display         "flex"
                    :flex-direction  "column"
                    :justify-content "center"}})

(defn block-header
  [{:keys [icon text action]}]
  (let [styles (get-styles)]
    [:div {:style (:container styles)}
     (when (some? action)
       [:div {:style (:action-wrapper styles)}
        action])
     [icon {:color (get-in-theme [:palette :primary :main])
            :style (:icon styles)}]
     [ui/typography {:variant "h2"
                     :style   (:text styles)}
      text]]))

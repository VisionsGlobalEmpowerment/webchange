(ns webchange.ui.components.date.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.date.utils :as utils]
    [webchange.ui.components.input.views :refer [input]]
    [webchange.ui.components.input-error.views :refer [input-error]]
    [webchange.ui.components.input-label.views :refer [input-label]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn date
  [{:keys [class-name disabled? error id label max min on-change required? value] :as props}]
  (r/with-let [el (r/atom nil)]
    (let [max (if (contains? props max) max (-> (utils/now) (utils/yyyy-mm-dd)))
          min (if (contains? props min) min "1960-01-01")
          handle-change (fn [value]
                          (when (fn? on-change)
                            (on-change value)))
          handle-picker-click #(when (some? @el)
                                 (.showPicker ^js @el))
          has-label? (some? label)]
      [:div {:class-name (get-class-name {"bbs--date-wrapper" true
                                          class-name          (some? class-name)})}
       [:div {:class-name "bbs--date-wrapper--header"}
        (when has-label?
          [input-label {:for       id
                        :required? required?}
           label])
        (when (some? error)
          [input-error error])]
       [input (cond-> {:type          "date"
                       :class-name    (get-class-name {"bbs--date" true})
                       :default-value value
                       :disabled?     disabled?
                       :on-change     handle-change
                       :ref-atom      el
                       :action        {:icon      "calendar"
                                       :disabled? disabled?
                                       :on-click  handle-picker-click}}
                      (some? id) (assoc :id id)
                      (some? max) (assoc :max max)
                      (some? min) (assoc :min min))]])))

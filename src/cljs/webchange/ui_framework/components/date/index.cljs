(ns webchange.ui-framework.components.date.index
  (:require
    [webchange.ui-framework.components.date.utils :as utils]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name disabled? error id max min on-change value] :as props}]
  (let [max (if (contains? props max) max (-> (utils/now) (utils/yyyy-mm-dd)))
        min (if (contains? props min) min "1960-01-01")
        handle-change (fn [event]
                        (when (fn? on-change)
                          (-> (.. event -target -value)
                              (on-change))))]
    [:div {:class-name (get-class-name {"wc-date-wrapper" true
                                        class-name        (some? class-name)})}
     [:input (cond-> {:type       "date"
                      :class-name (get-class-name {"wc-date"       true
                                                   "wc-date-error" (some? error)})
                      :value      value
                      :disabled   disabled?
                      :on-change  handle-change}
                     (some? id) (assoc :id id)
                     (some? max) (assoc :max max)
                     (some? min) (assoc :min min))]
     (when (some? error)
       [:label.wc-error error])]))

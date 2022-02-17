(ns webchange.ui-framework.components.date.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.date.utils :as utils]
    [webchange.ui-framework.components.input.index :as input]))

(def available-masks {"mm/dd/yyyy" {:blocks    ["m" "d" "y"]
                                    :delimiter "/"}})

(defn component
  [{:keys [mask on-change placeholder]
    :as   props
    :or   {mask      "mm/dd/yyyy"
           on-change #()}}]
  (r/with-let [real-value (atom "")
               display-value (r/atom "")
               focused? (r/atom false)
               mask-data (get available-masks mask)
               handle-change (fn [value]
                               (let [parsed-value (utils/parse-date value mask-data)]
                                 (reset! real-value (utils/to-iso-format parsed-value))
                                 (reset! display-value (utils/apply-mask parsed-value mask-data))
                                 (on-change @real-value)))]
    (if (some? mask-data)
      [:div {:class-name "wc-date-wrapper"}
       [input/component (merge props
                               {:value       @display-value
                                :placeholder (if @focused? mask placeholder)
                                :on-change   handle-change
                                :on-blur     #(reset! focused? false)
                                :on-focus    #(reset! focused? true)})]]
      [:div {:class-name "wc-date-wrapper"}
       (str "Not available mask: " mask)])))

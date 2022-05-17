(ns webchange.ui-framework.components.date-str.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.date-str.utils :as utils]
    [webchange.ui-framework.components.input.index :as input]))

(def available-masks {"mm/dd/yyyy" {:blocks    ["m" "d" "y"]
                                    :delimiter "/"}})

(defn component
  [{:keys [mask on-change placeholder]
    :as   props
    :or   {mask      "mm/dd/yyyy"
           on-change #()}}]
  (r/with-let [display-value (r/atom "")
               focused? (r/atom false)
               deleting? (atom false)
               {:keys [delimiter] :as mask-data} (get available-masks mask)
               handle-change (fn [value]
                               (if-not @deleting?
                                 (let [parsed-value (-> value
                                                        (utils/parse-date mask-data)
                                                        ;(utils/fix-date)
                                                        )]
                                   (reset! display-value (utils/apply-mask parsed-value mask-data))
                                   (let [result-value (utils/to-iso-format parsed-value)]
                                     (on-change result-value)))
                                 (reset! deleting? false)))
               handle-key-down (fn [{:keys [key]}]
                                 (let [last-char (->> (count @display-value)
                                                      (dec)
                                                      (subs @display-value))]
                                   (when (and (= key "Backspace")
                                              (= last-char delimiter))
                                     (let [value-without-last-two-chars (->> (count @display-value)
                                                                             (dec) (dec)
                                                                             (subs @display-value 0))]
                                       (reset! deleting? true)
                                       (reset! display-value value-without-last-two-chars)))))]
    (if (some? mask-data)
      [:div {:class-name "wc-date-str-wrapper"}
       [input/component (merge props
                               {:value       @display-value
                                :placeholder (if @focused? mask placeholder)
                                :on-change   handle-change
                                :on-blur     #(reset! focused? false)
                                :on-focus    #(reset! focused? true)
                                :on-key-down handle-key-down})]]
      [:div {:class-name "wc-date-str-wrapper"}
       (str "Not available mask: " mask)])))

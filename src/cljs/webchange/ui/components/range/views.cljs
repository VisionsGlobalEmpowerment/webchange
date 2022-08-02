(ns webchange.ui.components.range.views
  (:require
    [webchange.ui.utils.get-class-name :refer [get-class-name]]
    [webchange.utils.numbers :refer [try-parse-number]]))

(defn- event->value
  [event]
  (-> event
      (.. -target -value)
      (try-parse-number)))

(defn- value->percentage
  [value]
  (-> (* value 100)
      (Math/round)
      (str "%")))

(defn range
  [{:keys [class-name label max min on-change on-input step value]
    :or   {min  0
           max  1
           step 0.01}}]
  (let [handle-mouse-up (fn [event]
                          (when (fn? on-input)
                            (-> (event->value event)
                                (on-input))))
        handle-change (fn [event]
                        (when (fn? on-change)
                          (-> (event->value event)
                              (on-change))))]
    [:div {:class-name (get-class-name {"bbs--range" true
                                        class-name   (some? class-name)})}
     (when (some? label)
       [:label {:class-name "bbs--range--label"}
        label])
     [:div {:class-name "bbs--range--input-wrapper"}
      [:input {:type        "range"
               :on-change   handle-change
               :on-mouse-up handle-mouse-up
               :max         max
               :min         min
               :step        step
               :value       value}]]
     [:span {:class-name "bbs--range--value"}
      (value->percentage value)]]))

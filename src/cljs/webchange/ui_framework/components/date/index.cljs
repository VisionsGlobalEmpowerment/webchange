(ns webchange.ui-framework.components.date.index
  (:require
    [clojure.string :as str]
    [reagent.core :as r]
    [webchange.ui-framework.components.input.index :as input]))

(def masks {"mm/dd/yyy" ""})

(defn- parse-value
  [value]
  (-> value
      (str/replace #"\D" "")
      (subs 0 8)
      (str/replace #"(\d{4})(\d{2}(\d{2})?)?"
                   "$1-$2-$3")))

(defn- apply-mask
  [value]
  (str/replace value
               #"(\d{0,2})(\d{0,2})(\d{0,4})"
               "$1-$2-$3"))

(defn component
  [{:keys [mask on-change]
    :as   props
    :or   {mask      "mm/dd/yyy"
           on-change #()}}]
  (r/with-let [real-value (atom "")
               display-value (r/atom "")
               handle-change (fn [value]
                               (reset! real-value (parse-value value))
                               (reset! display-value (apply-mask @real-value))
                               (on-change @real-value))]
    [:div {:class-name "wc-date-wrapper"}
     [input/component (merge props
                             {:value     @display-value
                              :on-change handle-change})]]))

(ns webchange.ui-framework.components.select.index
  (:require
    [webchange.ui-framework.components.select.icon-down :as icon-down]
    [webchange.ui-framework.components.select.icon-up :as icon-up]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- fix-options
  [options]
  (->> options
       (map (fn [{:keys [enable?] :as option}]
              (if (some? enable?)
                (-> option
                    (assoc :disabled? (not enable?))
                    (dissoc :enable?))
                option)))))

;; variant [string]


(defn component
  [{:keys [options value variant class-name on-arrow-down-click on-arrow-up-click on-change show-buttons? width with-arrow?]
    :or   {show-buttons?       false
           with-arrow?         true
           options             []
           on-change           #()
           on-arrow-down-click #()
           on-arrow-up-click   #()}}]
  "Props:
   :variant - 'outlined' or none."
  (let [handle-change #(-> % (.. -target -value) (on-change))
        options (fix-options options)]
    [:div {:style      (if (some? width) {:width width} {})
           :class-name (get-class-name (cond-> (-> {"wc-select"  true
                                                    "with-arrow" with-arrow?}
                                                   (assoc class-name (some? class-name)))
                                               (some? variant) (assoc (str "variant-" variant) true)))}
     [:select {:value     value
               :on-change handle-change}
      (for [{:keys [text value disabled?]} options]
        ^{:key value}
        [:option {:value    value
                  :disabled disabled?}
         text])]
     (when show-buttons?
       [:div.controls
        [:button {:on-click on-arrow-up-click}
         [icon-up/data]]
        [:button {:on-click on-arrow-down-click}
         [icon-down/data]]])]))

(ns webchange.ui-framework.components.select.index
  (:require
    [webchange.ui-framework.components.select.icon-down :as icon-down]
    [webchange.ui-framework.components.select.icon-up :as icon-up]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [options value variant class-name on-arrow-down-click on-arrow-up-click on-change show-buttons? width with-arrow?]
    :or   {variant             ""
           show-buttons?       false
           with-arrow?         true
           options             []
           on-change           #()
           on-arrow-down-click #()
           on-arrow-up-click   #()}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:div {:style      (if (some? width) {:width width} {})
           :class-name (get-class-name (-> {"wc-select"  true
                                            "with-arrow" with-arrow?}
                                           (assoc variant true)
                                           (assoc class-name (some? class-name))))}
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

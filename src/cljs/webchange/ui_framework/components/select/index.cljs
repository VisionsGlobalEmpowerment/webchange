(ns webchange.ui-framework.components.select.index
  (:require
    [webchange.ui-framework.components.select.icon-down :as icon-down]
    [webchange.ui-framework.components.select.icon-up :as icon-up]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [options value variant class-name on-change show-buttons? width with-arrow?]
    :or   {variant       ""
           show-buttons? false
           with-arrow?   true
           options       []
           on-change     #()}
    :as   props}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:div {:style      (if (some? width) {:width width} {})
           :class-name (get-class-name (-> {"wc-select"  true
                                            "with-arrow" with-arrow?}
                                           (assoc variant true)
                                           (assoc class-name (some? class-name))))}
     [:select {:on-change handle-change}
      (for [{:keys [text value]} options]
        ^{:key value}
        [:option {:value    value
                  :selected (= value (:value props))}
         text])]
     (when show-buttons?
       [:div.controls
        [:button
         [icon-up/data]]
        [:button
         [icon-down/data]]])]))

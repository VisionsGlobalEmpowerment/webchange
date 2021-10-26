(ns webchange.ui-framework.components.range-input.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.tooltip.index :as tooltip]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def event->value #(->> (.. % -target -value)
                        (.parseFloat js/Number)))

(defn- has-value?
  [value]
  (some? value))

(defn component
  [{:keys [class-name value min max step on-change on-mouse-up]
    :or   {min         0
           max         1
           step        0.1
           on-mouse-up #()}}]
  (r/with-let [current-value (r/atom value)
               tooltip-open? (r/atom false)]
    (let [handle-change (fn [event]
                          (reset! current-value (event->value event))
                          (when (fn? on-change)
                            (on-change (event->value event))))
          handle-mouse-up (fn [event]
                            (when (fn? on-mouse-up)
                              (on-mouse-up (event->value event))))
          handle-mouse-enter #(reset! tooltip-open? true)
          handle-mouse-leave #(reset! tooltip-open? false)]
      [:div {:class-name (get-class-name (-> {"wc-range-input" true}
                                             (assoc class-name (some? class-name))))}
       [:input {:type           "range"
                :value          value
                :on-change      handle-change
                :on-mouse-enter handle-mouse-enter
                :on-mouse-leave handle-mouse-leave
                :on-mouse-up    handle-mouse-up
                :min            min
                :max            max
                :step           step}]
       [tooltip/component {:open? (and @tooltip-open?
                                       (has-value? @current-value))}
        @current-value]])))

(ns webchange.editor-v2.components.slider.views
  (:require
    [reagent.core :as r]))

(defn- el->value
  [el]
  (->> (.-value el)
       (.parseFloat js/Number)))

(defn slider
  [{:keys [value1 value2 min max on-change step]
    :or   {value1    0
           value2    0
           min       0
           max       10
           step      1
           on-change #()}}]
  (r/with-let [slider1 (atom nil)
               slider2 (atom nil)
               handle-change (fn []
                               (let [value1 (el->value @slider1)
                                     value2 (el->value @slider2)]
                                 (on-change (Math/min value1 value2)
                                            (Math/max value1 value2))))
               handle-ref (fn [store inst]
                            (when (some? inst)
                              (reset! store inst)
                              (when (and (some? @slider1)
                                         (some? @slider2)))))]
    [:section.range-slider
     [:input {:value     value1
              :min       min
              :max       max
              :step      step
              :type      "range"
              :on-change handle-change
              :ref       #(handle-ref slider1 %)}]
     [:input {:value     value2
              :min       min
              :max       max
              :step      step
              :type      "range"
              :on-change handle-change
              :ref       #(handle-ref slider2 %)}]]))

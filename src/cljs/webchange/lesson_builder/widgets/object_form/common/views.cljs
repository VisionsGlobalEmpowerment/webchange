(ns webchange.lesson-builder.widgets.object-form.common.views
  (:require
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn component-wrapper
  [{:keys [class-name label]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (ui/get-class-name {"component-wrapper" true
                                                    class-name          (some? class-name)})}
              (when (some? label)
                [ui/input-label label])])))

(defn flip-component
  [{:keys [data on-change]}]
  (let [handle-click (fn []
                       (let [scale (-> (get data :scale {:x 1 :y 1})
                                       (update :x #(* -1 %)))]
                         (on-change {:scale scale})))]
    [component-wrapper {:label "Flip"}
     [ui/button {:icon     "flip"
                 :color    "blue-1"
                 :on-click handle-click}]]))

(defn scale-component
  [{:keys [data on-change]}]
  (let [value (get-in data [:scale :x] 1)
        handle-change (fn [value]
                        (let [flip (-> (get-in data [:scale :x])
                                       (< 0))
                              scale {:x (if flip (- value) value)
                                     :y value}]
                          (on-change {:scale scale})))]
    [component-wrapper {:label      "Scale"
                        :class-name "scale-field"}
     [ui/input {:value     value
                :type      "float"
                :step      0.05
                :on-change handle-change}]]))

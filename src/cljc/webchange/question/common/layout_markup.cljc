(ns webchange.question.common.layout-markup
  (:require
    [webchange.question.common.params :as common-params]))

(defn get-layout-coordinates
  [{:keys [layout with-image?]}]
  (let [{:keys [width height]} common-params/template-size
        small-side (->> 1.618 (inc) (/ 1))
        big-side (- 1 small-side)]
    (if-not with-image?
      {:text       {:x      0
                    :y      0
                    :width  width
                    :height (* small-side height)}
       :options    {:x      0
                    :y      (* small-side height)
                    :width  width
                    :height (* big-side height)}
       :background {:x      0
                    :y      0
                    :width  width
                    :height height}}
      (case layout
        "horizontal" (let [left-side-width (* small-side width)]
                       {:image      {:x      0
                                     :y      0
                                     :width  left-side-width
                                     :height height}
                        :text       {:x      left-side-width
                                     :y      0
                                     :width  (- width left-side-width)
                                     :height (* small-side height)}
                        :options    {:x      left-side-width
                                     :y      (* small-side height)
                                     :width  (- width left-side-width)
                                     :height (* big-side height)}
                        :background {:x      left-side-width
                                     :y      0
                                     :width  (- width left-side-width)
                                     :height height}})
        "vertical" (let [top-side-height (* small-side height)]
                     {:image      {:x      0
                                   :y      0
                                   :width  (* small-side width)
                                   :height top-side-height}
                      :text       {:x      (* small-side width)
                                   :y      0
                                   :width  (* big-side width)
                                   :height top-side-height}
                      :options    {:x      0
                                   :y      top-side-height
                                   :width  width
                                   :height (- height top-side-height)}
                      :background {:x      0
                                   :y      top-side-height
                                   :width  width
                                   :height (- height top-side-height)}})))))

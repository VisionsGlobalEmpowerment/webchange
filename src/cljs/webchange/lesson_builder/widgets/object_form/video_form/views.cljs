(ns webchange.lesson-builder.widgets.object-form.video-form.views
  (:require
    [webchange.lesson-builder.widgets.object-form.common.views :refer [component-wrapper]]
    [webchange.ui.index :as ui]))

(defn- volume-component
  [{:keys [data on-change]}]
  (let [value (get data :volume 0.5)
        handle-change #(on-change {:volume %})]
    [component-wrapper {:label "Volume"}
     [ui/input {:value     value
                :on-change handle-change
                :type      "range"
                :step      0.05
                :min       0
                :max       1}]]))

(defn video-form
  [{:keys [class-name on-change] :as props}]
  (let [handle-change #(when (fn? on-change) (on-change %))
        component-props (merge props
                               {:on-change handle-change})]
    [:div {:class-name (ui/get-class-name {"video-form" true
                                           class-name   (some? class-name)})}
     [volume-component component-props]]))

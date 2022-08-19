(ns webchange.lesson-builder.widgets.object-form.text-tracing-pattern-form.views
  (:require
    [webchange.ui.index :as ui]))

(defn- dashed-component
  [{:keys [data on-change]}]
  (let [value (get data :dashed false)
        handle-change #(on-change {:dashed %})]
    [ui/switch {:checked?  value
                :on-change handle-change
                :label     "Dashed line?"}]))

(defn- show-lines-component
  [{:keys [data on-change]}]
  (let [value (get data :show-lines false)
        handle-change #(on-change {:show-lines %})]
    [ui/switch {:checked?  value
                :on-change handle-change
                :label     "Show lines?"}]))

(defn text-tracing-pattern-form
  [{:keys [class-name on-change] :as props}]
  (let [handle-change #(when (fn? on-change) (on-change %))
        component-props (merge props
                               {:on-change handle-change})]
    [:div {:class-name (ui/get-class-name {"text-tracing-form" true
                                           class-name          (some? class-name)})}
     [dashed-component component-props]
     [show-lines-component component-props]]))

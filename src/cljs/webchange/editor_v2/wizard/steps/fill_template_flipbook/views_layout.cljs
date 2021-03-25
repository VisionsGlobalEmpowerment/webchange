(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-layout
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def validation-map {:root [(fn [value] (when-not (some? value) "Required field"))]})

(defn- layout-option
  [{:keys [name selected? src value on-click]}]
  (let [image-props {:type   "image/svg+xml"
                     :width  128
                     :height 155}
        handle-click #(on-click value)]
    [:div {:class-name (get-class-name {"layout-option" true
                                        "selected"      selected?})
           :on-click   handle-click}
     [:div.image-wrapper
      [:img (merge image-props
                   {:src src})]]
     [:span.title name]]))

(defn layout
  [{:keys [data option validator]}]
  (r/with-let [data (connect-data data [(-> option :key keyword)] nil)
               {:keys [destroy error-message]} (v/init data validation-map validator)]
    (let [handle-option-click (fn [value]
                                (reset! data value))
          layout-options (->> (get option :options [])
                              (map (fn [{:keys [value] :as option}]
                                     (merge option
                                            {:selected? (= value @data)
                                             :on-click  handle-option-click})))
                              (doall))]
      [:div.layout-block
       [:div.options-wrapper
        (for [{:keys [value] :as option} layout-options]
          ^{:key value}
          [layout-option option])]
       [:div.error-message [error-message {:field-name :root}]]])
    (finally
      (destroy))))

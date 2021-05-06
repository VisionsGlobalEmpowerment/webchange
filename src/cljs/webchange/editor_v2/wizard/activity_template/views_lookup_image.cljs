(ns webchange.editor-v2.wizard.activity-template.views-lookup-image
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.components.index :refer [label]]))

(def lookup-validation-map {:root [(fn [value] (when-not (some? value) "Required field"))]})

(defn- image-option-block
  [{:keys [src value selected? on-click]}]
  [:div {:on-click   #(on-click value)
         :class-name (get-class-name {"lookup-image-item" true
                                      "active"            selected?})}
   [:img {:src src}]])

(defn- image-options-list
  [{:keys [options current-value on-click]}]
  [:div.lookup-image-list
   (for [{:keys [value] :as option} options]
     ^{:key value}
     [image-option-block (merge option
                                {:selected? (= value current-value)
                                 :on-click  on-click})])])

(defn lookup-image-option
  [{:keys [key option data validator]}]
  (r/with-let [current-value (r/atom nil)
               lookup-image-data (connect-data data [key] nil)
               {:keys [error-message]} (v/init lookup-image-data lookup-validation-map validator)
               handle-option-click (fn [value]
                                     (reset! lookup-image-data value)
                                     (reset! current-value value))]
    (let [{:keys [description]} option]
      [:div
       (when (some? description)
         [label {:class-name "field-label"} description])
       [image-options-list {:options       (:options option)
                            :current-value @lookup-image-data
                            :on-click      handle-option-click}]
       [error-message {:field-name :root}]])))

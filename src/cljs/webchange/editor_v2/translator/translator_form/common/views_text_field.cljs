(ns webchange.editor-v2.translator.translator-form.common.views-text-field
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]))

(defn text-field
  []
  (let [input (atom nil)]
    (r/create-class
      {:display-name "text-field"

       :component-did-update
                     (fn [this]
                       (let [{:keys [value]} (r/props this)
                             current-value (.-value @input)]
                         (when-not (= (trim-text value)
                                      (trim-text current-value))
                           (aset @input "value" value))))

       :reagent-render
                     (fn [props]
                       [ui/text-field (-> props
                                          (assoc :default-value (:value props))
                                          (assoc :input-props {:ref #(when % (reset! input %))})
                                          (dissoc :value))])})))

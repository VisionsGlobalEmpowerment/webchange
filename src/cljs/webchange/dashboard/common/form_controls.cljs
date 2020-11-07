(ns webchange.dashboard.common.form-controls
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.ui.theme :refer [w-colors]]
    [webchange.validation.validate :refer [validate]]))

(defn validation-message
  [{:keys [spec value dirty? custom-error]}]
  (let [error-message (cond
                        dirty? (validate spec value)
                        (some? custom-error) custom-error)]
    [ui/form-helper-text {:style {:color (:secondary w-colors)}} error-message]))

(defn text-field-validated
  []
  (let [dirty? (r/atom false)]
    (fn [{:keys [default-value on-change spec custom-error] :as props}]
      [ui/form-control {:margin "normal" :full-width true}
       [ui/text-field
        (merge (-> props
                   (dissoc :on-change)
                   (dissoc :spec)
                   (dissoc :custom-error)) {:on-change #(do (reset! dirty? true)
                                                            (on-change %))})]
       [validation-message {:spec         spec
                            :value        default-value
                            :custom-error custom-error
                            :dirty?       @dirty?}]])))

(defn select-validated
  []
  (let [dirty? (r/atom false)]
    (fn [{:keys [label value on-change spec custom-error] :as props} & children]
      [ui/form-control {:margin "normal" :full-width true}
       [ui/input-label {:required true} label]
       [ui/select
        (merge (-> props
                   (dissoc :on-change)
                   (dissoc :spec)
                   (dissoc :label)
                   (dissoc :custom-error)) {:on-change #(do (reset! dirty? true)
                                                            (on-change %))})
        children]
       [validation-message {:spec         spec
                            :value        value
                            :custom-error custom-error
                            :dirty?       @dirty?}]])))

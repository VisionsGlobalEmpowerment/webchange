(ns webchange.editor.components.data-sets.views-add-field-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor.components.data-sets.views-edit-field-modal :refer [edit-field-modal]]))

(defn add-field-form
  [data-atom]
  (r/with-let [modal-open? (r/atom false)]
              (let [handle-add-click (fn [] (reset! modal-open? true))
                    handle-modal-save (fn [_ new-data]
                                        (swap! data-atom update-in [:fields] conj new-data)
                                        (reset! modal-open? false))
                    handle-modal-cancel (fn [] (reset! modal-open? false))]
                [ui/grid {:container true
                          :spacing   16
                          :justify   "flex-end"}
                 [ui/grid {:item  true
                           :xs    4
                           :style {:text-align "right"}}
                  [ui/button
                   {:on-click handle-add-click
                    :color    "secondary"
                    :variant  "contained"}
                   "Add field"]
                  (when @modal-open?
                    [edit-field-modal {:open      @modal-open?
                                       :value     {}
                                       :on-save   handle-modal-save
                                       :on-cancel handle-modal-cancel}])]])))

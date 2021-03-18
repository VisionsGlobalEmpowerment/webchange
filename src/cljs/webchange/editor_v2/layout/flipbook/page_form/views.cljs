(ns webchange.editor-v2.layout.flipbook.page-form.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.events :as editor]))

(defn- open-action-dialog-button
  [{:keys [action-name]}]
  (let [window-params {:components {:description  {:hide? true}
                                    :node-options {:hide? true}
                                    :target       {:hide? true}}}
        handle-click (fn []
                       (re-frame/dispatch [::editor/show-translator-form-by-id action-name window-params]))]
    [ui/button {:on-click handle-click
                :style    {:margin-left "8px"}}
     "Edit Action"]))

(defn page-form
  [{:keys [page-data]}]
  (let [{:keys [text action]} page-data]
    (when (and (not (some? text))
               (some? action))
      [open-action-dialog-button {:action-name (keyword action)}])))

(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-control-row)

(defn control-row
  [{:keys [error-message label control]}]
  [:div.control-row
   (when (some? label)
     [:div.label
      label
      (when (some? error-message)
        error-message)])
   (when (some? control) [:div.control control])])

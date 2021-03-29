(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item-data
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]
    [webchange.ui-framework.components.index :refer [button chip text-input]]))

(defn audio-data
  [{:keys [target]}]
  [:div.audio-data
   (when (some? target)
     [chip {:label target}])])

(defn audio-data-form
  [{:keys [alias target on-save on-cancel]}]
  (r/with-let [current-data (r/atom {:alias  alias
                                     :target target})]
    [:div.audio-data-form
     [audio-target-selector {:value                    (or (:target @current-data) "")
                             :on-change                #(swap! current-data assoc :target %)
                             :custom-option-available? true}]
     [text-input {:value       (or (:alias @current-data) "")
                  :placeholder "File Name"
                  :on-change   #(swap! current-data assoc :alias %)}]
     [button {:shape    "rectangle"
              :variant  "outlined"
              :on-click on-cancel}
      "Cancel"]
     [button {:shape    "rectangle"
              :on-click #(on-save @current-data)}
      "Save"]]))

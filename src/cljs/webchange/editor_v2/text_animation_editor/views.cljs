(ns webchange.editor-v2.text-animation-editor.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.text-animation-editor.state :as state]
    [webchange.editor-v2.text-animation-editor.chunks-editor.form.views :refer [chunks-editor-form]]
    [webchange.editor-v2.text-animation-editor.views-chunks :refer [text-chunks]]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.ui-framework.components.index :refer [button dialog message]]
    [webchange.utils.text :refer [chunks->parts]]))

(defn- text-animation-form
  []
  (let [[text-object-name text-object-data] @(re-frame/subscribe [::state/text-object])
        selected-chunk @(re-frame/subscribe [::state/selected-chunk])
        selected-audio @(re-frame/subscribe [::state/selected-audio])
        parts (chunks->parts (:text text-object-data) (:chunks text-object-data))
        active-parts @(re-frame/subscribe [::state/active-parts])
        handle-chunks-change (fn [text-name text-data-patch]
                               (re-frame/dispatch [::state/set-current-text-data text-name text-data-patch]))]
    [:div.text-animation-editor
     [chunks-editor-form (merge (select-keys text-object-data [:text :chunks])
                                {:on-change             (fn [data] (handle-chunks-change (keyword text-object-name) data))
                                 :show-chunks?          false
                                 :origin-text-disabled? true})]
     [text-chunks {:parts              parts
                   :active-parts       active-parts
                   :selected-chunk-idx selected-chunk
                   :on-click           #(re-frame/dispatch [::state/select-chunk %])}]
     [audio-wave-form (merge selected-audio
                             {:height         96
                              :on-change      #(re-frame/dispatch [::state/select-audio %])
                              :show-controls? true})]]))

(defn text-chunks-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        cancel #(re-frame/dispatch [::state/cancel])
        apply #(re-frame/dispatch [::state/apply])
        form-available? @(re-frame/subscribe [::state/form-available?])
        selected-audio @(re-frame/subscribe [::state/selected-audio])
        selected-audio-bounds @(re-frame/subscribe [::state/bounds])]
    (when open?
      [dialog
       {:title    "Edit text animation chunks"
        :on-close cancel
        :actions  [[button {:on-click apply
                            :disabled (not form-available?)
                            :size     "big"}
                    "Apply"]
                   [button {:on-click cancel
                            :variant  "outlined"
                            :size     "big"}
                    "Cancel"]]}
       (if (or (nil? selected-audio)
               (nil? (:start selected-audio-bounds))
               (nil? (:end selected-audio-bounds)))
         [message {:type    "warn"
                   :message "Select audio region in translation dialog to configure text animation"}]
         [text-animation-form])])))

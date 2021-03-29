(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.state :as wave-form-state]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list-item-header :refer [header]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn audios-list-item
  [audio-data]
  (let [{:keys [url alias start duration selected? target]} audio-data
        wave-state-loading? @(re-frame/subscribe [::wave-form-state/audio-script-loading url])
        handle-change-data (fn [data] (re-frame/dispatch [::translator-form.scene/update-asset url data]))
        handle-select (fn [] (re-frame/dispatch [::dialog-form.actions/set-phrase-dialog-action-audio url]))
        handle-change-region (fn [region] (re-frame/dispatch [::dialog-form.actions/set-phrase-action-audio-region
                                                              url
                                                              (:start region)
                                                              (:duration region)]))
        handle-bring-to-top (fn [] (re-frame/dispatch [::translator-form.scene/update-asset-date url (.now js/Date)]))
        handle-clear-selection (fn [] (re-frame/dispatch [::dialog-form.actions/update-dialog-audio-action :phrase
                                                          {:audio url :start nil :end nil :duration nil}]))
        handle-delete (fn [] (re-frame/dispatch [::translator-form.scene/delete-asset url]))
        audio-data {:url   url
                    :start (or start 0)
                    :end   (+ start duration)}
        on-audio-data-change #(re-frame/dispatch [::translator-form.actions/update-phrase-region-data url])]
    [:div {:on-click   handle-select
           :class-name (get-class-name {"audios-list-item" true
                                        "selected"         selected?})}
     [audio-wave-form (merge audio-data
                             {:height               64
                              :on-change            handle-change-region
                              :on-audio-data-change on-audio-data-change
                              :file-name            alias
                              :show-controls?       true
                              :right-side-controls  [[header {:alias              alias
                                                              :target             target
                                                              :selected?          selected?
                                                              :on-change-data     handle-change-data
                                                              :on-bring-to-top    handle-bring-to-top
                                                              :on-clear-selection handle-clear-selection
                                                              :on-delete          handle-delete
                                                              :loading?           wave-state-loading?}]]})]]))
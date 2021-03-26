(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.audio-wave-form.state :as wave-form-state]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.components.confirm.views :refer [with-confirmation]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.dialog.dialog-form.views-audio-edit-info :refer [audio-info-form]]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-styles
  []
  {:block-wrapper          {:margin-bottom "8px"}
   :block-wrapper-selected {:border        "solid 1px #00c0ff"
                            :margin-bottom "8px"}
   :block-header           {:display         "flex"
                            :justify-content "space-between"
                            :padding         "0 0 8px 0"}

   :menu-button            {:padding "8px"}
   :menu-item-icon         {:font-size "18px"}

   :info-form              {:display         "flex"
                            :justify-content "space-between"
                            :width           "100%"}
   :form-button            {:margin-left "8px"
                            :padding     "8px"}
   :form-button-save       {:margin-left      "8px"
                            :padding          "6px"
                            :background-color (get-in-theme [:palette :secondary :main])}
   :alias-form             {:width "170px"}
   :target-form            {:margin-right "16px"}
   :target-form-label      {:top "-16px"}
   :target-form-input      {:margin 0}})

(defn- audio-info
  [{:keys [target]}]
  [:div
   (when-not (nil? target)
     [ui/chip {:label target
               :style {:margin  "0 10px 0 0"
                       :padding "0"}}])])

(defn- audio-menu
  [{:keys [on-edit on-delete on-bring-to-top on-clear-selection]}]
  (r/with-let [menu-anchor (r/atom nil)]
    (let [handle-edit #(do (reset! menu-anchor nil) (on-edit))
          handle-bring-to-top #(do (reset! menu-anchor nil) (on-bring-to-top))
          handle-delete #(do (reset! menu-anchor nil) (on-delete))
          handle-cancel #(reset! menu-anchor nil)
          handle-clear-selection #(do (reset! menu-anchor nil) (on-clear-selection))
          styles (get-styles)]
      [:div
       [ui/icon-button
        {:style    (:menu-button styles)
         :on-click #(reset! menu-anchor (.-currentTarget %))}
        [ic/more-horiz {:style (:menu-item-icon styles)}]]
       [ui/menu
        {:anchor-el @menu-anchor
         :open      (boolean @menu-anchor)
         :on-close  #(reset! menu-anchor nil)}
        [ui/menu-item {:on-click handle-bring-to-top}
         [ui/list-item-icon
          [ic/vertical-align-top {:style (:menu-item-icon styles)}]]
         "Bring To Top"]
        [ui/menu-item {:on-click handle-edit}
         [ui/list-item-icon
          [ic/edit {:style (:menu-item-icon styles)}]]
         "Name Recording"]
        [ui/menu-item {:on-click handle-clear-selection}
         [ui/list-item-icon
          [ic/edit {:style (:menu-item-icon styles)}]]
         "Clear selection"]
        [with-confirmation {:message    "Remove audio asset from scene?"
                            :on-confirm handle-delete
                            :on-cancel  handle-cancel}
         [ui/menu-item
          [ui/list-item-icon
           [ic/delete {:style (:menu-item-icon styles)}]]
          "Delete"]]]])))

(defn- header
  [{:keys [alias target on-change-data on-bring-to-top on-delete on-clear-selection loading?]}]
  (r/with-let [edit-state? (r/atom false)]
    (let [styles (get-styles)
          handle-edit #(reset! edit-state? true)
          handle-save #(do (reset! edit-state? false)
                           (on-change-data %))
          handle-cancel #(reset! edit-state? false)
          handle-bring-to-top #(on-bring-to-top)
          handle-delete on-delete
          handle-clear-selection on-clear-selection]
      [:div {:style (:block-header styles)}
       (if @edit-state?
         [audio-info-form {:alias     alias
                           :target    target
                           :on-save   handle-save
                           :on-cancel handle-cancel}]
         [audio-info {:alias  alias
                      :target target}])
       (if loading?
         [ui/circular-progress {:size 20 :color "secondary"}]
         (when-not @edit-state?
           [audio-menu {:on-edit            handle-edit
                        :on-bring-to-top    handle-bring-to-top
                        :on-clear-selection handle-clear-selection
                        :on-delete          handle-delete}]))])))

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
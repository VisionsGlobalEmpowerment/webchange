(ns webchange.editor-v2.dialog.dialog-form.audio-assets.views-audios-list
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.audio-wave-form.state :as wave-form-state]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.components.confirm.views :refer [with-confirmation]]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.ui.theme :refer [get-in-theme]]))

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
  [{:keys [alias target]}]
  [:div
   (when-not (nil? target)
     [ui/chip {:label target
               :style {:margin  "0 10px 0 0"
                       :padding "0"}}])
   [ui/typography {:variant "subtitle2"
                   :color   "default"
                   :style   {:display "inline-block"}}
    (or alias "Name is not defined")]])

(defn- audio-info-form
  [{:keys [alias target on-save on-cancel]}]
  (r/with-let [current-data (r/atom {:alias  alias
                                     :target target})]
    (let [styles (get-styles)]
      [:div {:style (:info-form styles)}
       [:div
        [ui/form-control {:style (:target-form styles)}
         [ui/input-label {:html-for "target"
                          :style    (:target-form-label styles)}
          "Character"]
         [audio-target-selector {:default-value            (or (:target @current-data) "")
                                 :styles                   {:control (:target-form-input styles)}
                                 :on-change                #(swap! current-data assoc :target %)
                                 :custom-option-available? true}]]
        [ui/form-control {:style (:alias-form styles)}
         [ui/text-field {:label     "File Name"
                         :value     (or (:alias @current-data) "")
                         :on-change #(swap! current-data assoc :alias (-> % .-target .-value))
                         :variant   "outlined"}]]]
       [:div
        [ui/tooltip {:title "Cancel" :placement "top"}
         [ui/icon-button
          {:style    (:form-button styles)
           :color    "primary"
           :on-click on-cancel}
          [ic/clear {:style (:menu-item-icon styles)}]]]
        [ui/tooltip {:title "Save" :placement "top"}
         [ui/icon-button
          {:style    (:form-button-save styles)
           :on-click #(on-save @current-data)}
          [ic/done {:style (:menu-item-icon styles)}]]]]])))

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
         "Edit Data"]
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
  [{:keys [alias target selected? on-change-data on-bring-to-top on-delete on-clear-selection loading?]}]
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
         (when (and selected?
                    (not @edit-state?))
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
        styles (get-styles)
        _ @(re-frame/subscribe [::wave-form-state/audio-script-data url])
        _ (re-frame/dispatch [::dialog-form.actions/update-phrase-region-data url])
        ]
    [ui/card {:on-click handle-select
              :style    (if selected?
                          (:block-wrapper-selected styles)
                          (:block-wrapper styles))}
     [ui/card-content
      [header {:alias              alias
               :target             target
               :selected?          selected?
               :on-change-data     handle-change-data
               :on-bring-to-top    handle-bring-to-top
               :on-clear-selection handle-clear-selection
               :on-delete          handle-delete
               :loading?           wave-state-loading?}]
      [audio-wave-form (merge audio-data
                              {:height         64
                               :on-change      handle-change-region
                               :on-script-data-update handle-select
                               :show-controls? selected?})]]]))

(defn audios-list
  [{:keys [audios]}]
  [:div
   (for [audio-data audios]
     ^{:key (:url audio-data)}
     [audios-list-item audio-data])])

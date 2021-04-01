(ns webchange.editor-v2.dialog-form.components.views-actions-bar
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.dialog.state.window :as translator.window]
    [webchange.editor-v2.history.views :as history]
    [webchange.editor-v2.text-animation-editor.state :as animation-editor]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]))

(defn- get-styles
  []
  {:layout-wrapper    {:display "flex"
                       :padding "8px 16px"
                       :width   "100%"}
   :layout-left-side  {:flex-grow 1}
   :layout-right-side {:flex-grow 0}
   :buttons-group     {:display "flex"}
   :button-wrapper    {:margin-left "16px"}
   :button            {:padding "6px 20px"}})

(defn- layout
  [{:keys [left-side right-side]}]
  (let [styles (get-styles)]
    [:div {:style (:layout-wrapper styles)}
     [:div {:style (:layout-left-side styles)}
      left-side]
     [:div {:style (:layout-right-side styles)}
      right-side]]))

(defn- edit-chunks-button
  []
  (let [handle-click #(re-frame/dispatch [::animation-editor/open])
        styles (get-styles)]
    [:div {:style (:button-wrapper styles)}
     [ui/button {:color    "primary"
                 :on-click handle-click
                 :style    (:button styles)}
      "Configure"]]))

(defn- save-button
  [{:keys [on-save]}]
  (let [styles (get-styles)]
    [:div {:style (:button-wrapper styles)}
     [ui/button {:color    "secondary"
                 :on-click on-save
                 :style    (:button styles)}
      "Save"]]))

(defn- save-close-button
  [{:keys [on-save on-close]}]
  (let [handle-click (fn []
                       (on-save)
                       (on-close))
        styles (get-styles)]
    [:div {:style (:button-wrapper styles)}
     [ui/button {:color    "secondary"
                 :variant  "contained"
                 :on-click handle-click
                 :style    (:button styles)}
      "Save & Close"]]))

(defn- close-button
  [{:keys [on-close with-confirm?]}]
  (r/with-let [confirm-open? (r/atom false)]
    (let [handle-click (fn []
                         (if with-confirm?
                           (reset! confirm-open? true)
                           (on-close)))
          styles (get-styles)]
      [:div {:style (:button-wrapper styles)}
       [ui/button {:on-click handle-click
                   :style    (:button styles)}
        "Close"]
       [confirm-dialog {:open?        confirm-open?
                        :on-confirm   on-close
                        :title        "Close window?"
                        :description  "You are going to close translation window without changes saving."
                        :confirm-text "Close"
                        :reject-text  "Cancel"}]])))

(defn actions-bar
  [{:keys [on-close]}]
  (let [has-changes? @(re-frame/subscribe [::translator-form.form/has-changes])
        handle-save #(re-frame/dispatch [::translator-form.form/save-changes])
        {:keys [single-phrase?]} @(re-frame/subscribe [::translator.window/modal-params])
        styles (get-styles)]
    [layout {:left-side  (r/as-element [history/controls])
             :right-side (r/as-element [:div {:style (:buttons-group styles)}
                                        (when single-phrase?
                                          [edit-chunks-button])
                                        [close-button {:with-confirm? has-changes?
                                                       :on-close      on-close}]
                                        [save-button {:on-save handle-save}]
                                        [save-close-button {:on-save  handle-save
                                                            :on-close on-close}]])}]))

(ns webchange.editor-v2.dialog.dialog-form.views-form-phrase
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :refer [get-inner-action get-empty-action]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]
    [webchange.editor-v2.translator.translator-form.common.views-text-field :refer [text-field]]))

(defn- volume-option
  []
  (r/with-let [current-value (or (r/atom (-> @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
                                             get-inner-action
                                             :volume))
                                 1)
               tooltip-open? (r/atom false)]
    [:div {:style {:display "flex"
                   :width   "100%"}}
     [ui/typography {:variant "body2"} "Voice Volume"]
     [ui/tooltip {:title     (or @current-value "")
                  :placement "top"
                  :open      @tooltip-open?}
      [:input {:value          @current-value
               :type           "range"
               :on-change      #(reset! current-value (.. % -target -value))
               :on-mouse-enter #(reset! tooltip-open? true)
               :on-mouse-leave #(reset! tooltip-open? false)
               :on-mouse-up    #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-volume @current-value])
               :min            0
               :max            1
               :step           0.01
               :style          {:cursor      "pointer"
                                :flex-grow   "1"
                                :margin-left "16px"}}]]]))

(defn node-options
  []
  (let [text-input-params {:placeholder "Enter options"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        {:keys [path]} @(re-frame/subscribe [::translator-form.actions/current-phrase-action-info])
        parallel? (= 3 (count path))
        main-parallel? (= 0 (last path))
        current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        offset-text (-> current-phrase-action get-empty-action :duration str trim-text)
        handle-offset-change (fn [event]
                               (let [new-value (.. event -target -value)]
                                 (re-frame/dispatch [::dialog-form.actions/set-phrase-action-offset
                                                     new-value])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [volume-option]]
     (if (and parallel? (not main-parallel?))
       [ui/grid {:item true :xs 6}
        [text-field (merge text-input-params
                           {:label           "Delay"
                            :value           (or offset-text "")
                            :on-change       handle-offset-change
                            :InputLabelProps {:shrink  true
                                              :focused true}})]])]))

(defn- label-with-warning
  [{:keys [label show-warning?]}]
  (if show-warning?
    [:div {:style {:display     "flex"
                   :align-items "center"
                   :position    "relative"
                   :top         "-5px"}}
     [warning-icon {:styles {:main {:margin-right "8px"}}}]
     label]
    label))

(defn phrase-block
  []
  (let [settings @(re-frame/subscribe [::translator-form/components-settings :phrase])
        text-input-params {:placeholder "Enter phrase text"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        origin-text (-> current-phrase-action get-inner-action :phrase-text)
        translated-text (-> current-phrase-action get-inner-action :phrase-text-translated)]
    (when-not (:hide? settings)
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       [ui/grid {:item true :xs 6}
        [text-field (merge text-input-params
                           {:label           (r/as-element [label-with-warning {:label         "Origin Text"
                                                                                :show-warning? (-> current-phrase-action validate/phrase-origin-text-defined? not)}])
                            :value           (or origin-text "")
                            :on-change       #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-phrase (.. % -target -value)])
                            :InputLabelProps {:shrink true}})]]
       [ui/grid {:item true :xs 6}
        [text-field (merge text-input-params
                           {:label           "Translated Text"
                            :value           (or translated-text "")
                            :on-change       #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-phrase-translated (.. % -target -value)])
                            :InputLabelProps {:shrink  true
                                              :focused true}})]]])))

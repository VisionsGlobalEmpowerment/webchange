(ns webchange.editor-v2.layout.flipbook.views-stage-text
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.layout.flipbook.state :as flipbook-state]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.state.state :as state]))

(defn- get-styles
  []
  {:save-text-button {:float         "right"
                      :margin-bottom "16px"}
   :edit-animation   {:position "absolute"}})

(defn- edit-text-action-button
  [{:keys [text-name phrase-action-path]}]
  (let [handle-click (fn []
                       (let [action-node {:path [text-name]}
                             phrase-node {:path phrase-action-path}]
                         (re-frame/dispatch [::translator-form.actions/set-current-dialog-action action-node])
                         (re-frame/dispatch [::translator-form.actions/set-current-phrase-action phrase-node])
                         (re-frame/dispatch [::dialog.window/open {:components {:description  {:hide? true}
                                                                                :node-options {:hide? true}
                                                                                :target       {:hide? true}
                                                                                :phrase       {:hide? true}
                                                                                :diagram      {:context-menu {:hide? true}}}}])))
        styles (get-styles)]
    [ui/button {:on-click handle-click
                :style    (:edit-animation styles)}
     "Edit Animation"]))

(defn- text-control
  [{:keys [name data]}]
  (r/with-let [styles (get-styles)
               changed? (r/atom false)
               initial-value (r/atom (get data :text ""))
               current-value (r/atom @initial-value)
               handle-change (fn [event]
                               (->> (.. event -target -value)
                                    (reset! current-value))
                               (->> (not= @current-value @initial-value)
                                    (reset! changed?)))
               handle-save (fn []
                             (reset! initial-value @current-value)
                             (reset! changed? false)
                             (re-frame/dispatch [::flipbook-state/set-loading-status :loading])
                             (re-frame/dispatch [::state/update-scene-object {:object-name       name
                                                                              :object-data-patch {:text @current-value}}
                                                 {:on-success [::flipbook-state/set-loading-status :done]}]))]
    (let [loading-status @(re-frame/subscribe [::flipbook-state/loading-status])
          control-disabled (= loading-status :loading)]
      [:div
       [ui/button {:on-click handle-save
                   :color    "secondary"
                   :disabled (or control-disabled (not @changed?))
                   :style    (:save-text-button styles)}
        (case loading-status
          :loading [ui/circular-progress {:size 20}]
          "Save")]
       [ui/text-field {:default-value @initial-value
                       :placeholder   "Enter page text here"
                       :on-change     handle-change
                       :multiline     true
                       :full-width    true
                       :variant       "outlined"
                       :disabled      control-disabled
                       :inputProps    {:style {:overflow "hidden"}}}]
       ])))

(defn- form-placeholder
  []
  [ui/typography {:style {:font-size  "24px"
                          :text-align "center"
                          :width      "100%"
                          :padding    "64px"
                          :color      "#757575"}}
   "Nothing to edit on this stage"])

(defn stage-text
  []
  (let [text-objects @(re-frame/subscribe [::flipbook-state/stage-text-data])]
    (if (empty? text-objects)
      [form-placeholder]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       (for [{:keys [action text phrase-action-path]} text-objects]
         ^{:key (:name text)}
         [ui/grid {:item true :xs 6}
          [edit-text-action-button {:text-name          (keyword action)
                                    :phrase-action-path phrase-action-path}]
          [text-control text]])])))

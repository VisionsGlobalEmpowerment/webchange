(ns webchange.editor-v2.layout.flipbook.views-stage-text
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.layout.flipbook.state :as flipbook-state]
    [webchange.editor-v2.translator.text.views-chunks-editor-form :refer [chunks-editor-form]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.state.state :as state]))

(defn- get-styles
  []
  {:edit-button {:float      "right"
                 :margin-top "16px"}})

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
                :style    (:edit-button styles)}
     "Edit Audio"]))

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
  (let [text-objects @(re-frame/subscribe [::flipbook-state/stage-text-data])
        handle-change (fn [text-name text-data-patch]
                        (re-frame/dispatch [::state/update-scene-object {:object-name       text-name
                                                                         :object-data-patch text-data-patch}]))]
    (if (empty? text-objects)
      [form-placeholder]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       (for [{:keys [action text phrase-action-path]} text-objects]
         ^{:key (:name text)}
         [ui/grid {:item true :xs 6}
          [chunks-editor-form (merge (select-keys (:data text) [:text :chunks])
                                     {:on-change (fn [data] (handle-change (:name text) data))})]
          [edit-text-action-button {:text-name          (keyword action)
                                    :phrase-action-path phrase-action-path}]])])))

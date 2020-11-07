(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.diagram.views :refer [diagram]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]))

(defn graph-component
  []
  (r/create-class
    {:display-name "graph-component"

     :component-did-update
                   (fn [this]
                     (let [{:keys [root]} (r/props this)]
                       (re-frame/dispatch [::translator-form.actions/init-current-phrase-action root])))

     :reagent-render
                   (fn [props]
                     [diagram (select-keys props [:graph :mode])])}))

(defn diagram-block
  []
  (let [{:keys [data root]} @(re-frame/subscribe [::translator-form.graph/graph-data])]
    [graph-component {:graph data
                      :root  root
                      :mode  :translation}]))

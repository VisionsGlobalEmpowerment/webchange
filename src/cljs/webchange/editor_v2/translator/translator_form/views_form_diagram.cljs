(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]))

(defn graph-component
  []
  (r/create-class
    {:display-name "graph-component"

     :should-component-update
                   (fn [_ [_ old-props] [_ new-props]]
                     (let [old-graph (:graph old-props)
                           new-graph (:graph new-props)
                           should-update? (not (= (count (keys old-graph))
                                                  (count (keys new-graph))))]
                       should-update?))

     :component-did-update
                   (fn [this]
                     (let [props (r/props this)]
                       (re-frame/dispatch [::translator-form.actions/init-current-phrase-action (:root props)])))

     :reagent-render
                   (fn [props]
                     [diagram-widget props])}))

(defn diagram-block
  []
  (let [{:keys [data root]} @(re-frame/subscribe [::translator-form.graph/graph-data])]
    [graph-component {:graph data
                      :root  root
                      :mode  :translation}]))

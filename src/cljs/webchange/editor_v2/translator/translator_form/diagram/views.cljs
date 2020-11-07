(ns webchange.editor-v2.translator.translator-form.diagram.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.diagram-utils.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram-utils.diagram-widget :refer [diagram-widget]]
    [webchange.editor-v2.translator.translator-form.diagram.items-factory.factory :refer [create-diagram-items]]
    [webchange.editor-v2.translator.translator-form.diagram.utils :refer [get-graph-changes]]))

(defn empty-graph-placeholder
  []
  [:div {:style {:display         "flex"
                 :height          "100%"
                 :align-items     "center"
                 :justify-content "center"}}
   [:div {:style {:color     "white"
                  :font-size "20px"}}
    "Empty"]])

(defn update-graph
  [engine nodes old-graph new-graph]
  (let [changes-list (->> (get-graph-changes old-graph new-graph)
                          (filter #(= (:type %) :update)))]
    (doseq [{:keys [type node data]} changes-list]
      (case type
        :update (.updateProps (get nodes node) (assoc data :name node))))
    (.setTimeout js/window (fn [] (when-not (nil? engine) (.repaintCanvas engine))) 100)))

(defn diagram
  []
  (let [diagram-engine (atom nil)
        diagram-nodes (atom nil)
        force-update? (r/atom false)]
    (r/create-class
      {:display-name "diagram"

       :should-component-update
                     (fn [_ [_ old-props] [_ new-props]]
                       (let [old-graph (:graph old-props)
                             new-graph (:graph new-props)
                             should-rebuild? (or @force-update?
                                                 (not (= (keys old-graph)
                                                         (keys new-graph))))]
                         (when-not should-rebuild?
                           (update-graph @diagram-engine @diagram-nodes old-graph new-graph))

                         should-rebuild?))

       :reagent-render
                     (fn [{:keys [graph mode]}]
                       (if (empty? graph)
                         [:div.diagram-container
                          [empty-graph-placeholder]]
                         (let [{:keys [nodes links]} (create-diagram-items graph)
                               {:keys [engine]} (init-diagram-model mode (vals nodes) links)]
                           (reset! diagram-engine engine)
                           (reset! diagram-nodes nodes)
                           [diagram-widget {:engine   engine
                                            :reorder? true
                                            :zoom?    true}])))})))

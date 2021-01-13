(ns webchange.editor-v2.question.question-form.diagram.items-factory.nodes-factory
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-model :refer [get-custom-model]]
    [webchange.editor-v2.scene-diagram.scene-parser.actions-tracks :refer [default-track]]
    [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer :refer [override-concept-actions]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]
    ))

(def coordinate-params {:x-offset 50
                        :y-offset 50
                        :x-step   300
                        :y-step   100})

(defn- index->coordinate
  [index offset step]
  (+ offset (* index step)))

(defn- index->coordinate-x
  [index]
  (index->coordinate index (:x-offset coordinate-params) (:x-step coordinate-params)))

(defn- index->coordinate-y
  [index]
  (index->coordinate index (:y-offset coordinate-params) (:y-step coordinate-params)))

(defn- create-node
  [{:keys [data position]}]
  (let [node (get-custom-model data)]
    (.setPosition node (:x position) (:y position))
    node))

(defn- clean-path [path]
  (vec (filter (fn [key] (not= key :data)) path)))

(defn- create-action-node
  [action-data position]
  (create-node {:data     {:data action-data
                           :path (get-in action-data [:action-path])}
                :position position}))

(defn- get-action-from-question
  [scene-data question-action action-type]
  (get-in scene-data (concat [:actions] [(keyword (get-in question-action [:data action-type]))])))

(defn- get-dialog
  [scene-data action-path]
 (:path (first  (filter
    (fn [action]
      (= (:editor-type action) "dialog"))
    (map-indexed (fn [idx action]
           (if (= "action" (:type action))
             (-> (get-in scene-data (concat [:actions] [(keyword (get-in action [:id]))]))
                 (assoc :path [(keyword (get-in action [:id]))])
                 )
             (assoc action :path (conj action-path idx))))
         (:data (get-in scene-data (concat [:actions] action-path))))))))

(defn prepare-nodes
  [scene-data path]
  (let [action (get-in scene-data (concat [:actions] path))
        success-action [(keyword (get-in action [:data :success]))]
        success-dialog (get-dialog scene-data success-action)
        fail-action [(keyword (get-in action [:data :fail]))]
        fail-dialog (get-dialog scene-data fail-action)
        nodes (-> [{:action-path   (concat path [:data :audio-data])
                    :type          :question
                    :action        action
                    :question-path path
                    :x             0
                    :y             0}]
                  (concat (map-indexed (fn [idx answer]
                                         {:action-path   (concat path [:data :answers idx :audio-data])
                                          :question-path path
                                          :type          :answer
                                          :action        action
                                          :answer        answer
                                          :x             idx
                                          :y             1
                                          :index         idx})
                                       (get-in action [:data :answers :data])))
                  (concat
                    [{:action-path (concat success-dialog [0])
                      :question-path path
                      :type          :dialog
                      :action        (get-in scene-data (concat [:actions] success-dialog))
                      :x 0
                      :y 2}
                     {:action-path (concat fail-dialog [0])
                      :question-path path
                      :type          :dialog
                      :action        (get-in scene-data (concat [:actions] fail-dialog))
                      :x 1
                      :y 2}]))]
    nodes))

(defn get-diagram-items
  [scene-data path]
  (let [prepare-nodes (prepare-nodes scene-data path)
        nodes (flatten (map (fn [node]
                              (create-action-node node {:x (index->coordinate-x (:x node))
                                                                   :y (index->coordinate-y (:y node))}))
                            prepare-nodes))]
    {:nodes nodes
     :links []}))

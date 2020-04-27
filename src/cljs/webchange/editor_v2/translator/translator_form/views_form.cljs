(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [ajax.core :refer [GET]]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-dialog-data
                                                                  get-graph
                                                                  get-current-action-data]]
    [webchange.editor-v2.translator.translator-form.audio-assets.views :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-description :refer [description-block]]
    [webchange.editor-v2.translator.translator-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.translator.translator-form.views-form-dialog :refer [dialog-block]]
    [webchange.editor-v2.translator.translator-form.views-form-phrase :refer [phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.subs :as subs]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:error-wrapper {:background-color (get-in-theme [:palette :background :default])}})

(defn- update-root-action-data
  [data-patch]
  (let [data-store @(re-frame/subscribe [::translator-form-subs/edited-actions-data])
        selected-phrase-node @(re-frame/subscribe [::editor-subs/current-action])
        {:keys [id name type data]} (get-current-action-data selected-phrase-node nil data-store)]
    (re-frame/dispatch [::translator-form-events/set-action-edited-data name id {:type type
                                                                                 :data (merge data data-patch)}])))

(defn- get-current-root-data
  [selected-phrase-node data-store]
  (let [name (first (:path selected-phrase-node))
        origin-data (:data selected-phrase-node)
        new-data (get-in data-store [[name nil] :data])]
    (merge origin-data new-data)))

(defn- set-current-concept
  [concept]
  (re-frame/dispatch [::translator-form-events/set-current-concept concept]))

(defn- set-selected-action
  [action]
  (re-frame/dispatch [::translator-form-events/set-current-selected-action action]))

(defn- reset-selected-action
  []
  (re-frame/dispatch [::translator-form-events/clean-current-selected-action]))

(defn- set-initial-state
  [{:keys [concept-required? concepts current-concept graph selected-action-node]}]
  (when (and concept-required? (nil? current-concept))
    (set-current-concept (first concepts)))
  (if concept-required?
    (when (and (nil? selected-action-node)
               (not (nil? current-concept)))
      (set-selected-action (->> graph get-root-nodes first (get graph))))
    (when (nil? selected-action-node)
      (set-selected-action (->> graph get-root-nodes first (get graph))))))

(defn translator-form
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])

        concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
        current-concept @(re-frame/subscribe [::translator-form-subs/current-concept])

        selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
        phrase-action-name (or (:origin-name @selected-phrase-node)
                               (keyword (:name @selected-phrase-node)))
        selected-action-node (re-frame/subscribe [::translator-form-subs/selected-action])
        selected-action-concept? (-> @selected-action-node (get-in [:data :concept-action]) (boolean))
        {:keys [graph has-concepts?]} (get-graph scene-data phrase-action-name {:current-concept current-concept})
        data-store @(re-frame/subscribe [::translator-form-subs/edited-actions-data])
        dialog-data (get-dialog-data @selected-phrase-node graph (fn [node-data]
                                                                   (get-current-action-data node-data current-concept data-store)))
        prepared-root-action-data (get-current-root-data @selected-phrase-node data-store)
        phrase-action-selected? (-> @selected-action-node (nil?) (not))
        concept-required? (or has-concepts? selected-action-concept?)]
    (set-initial-state {:concept-required?    concept-required?
                        :concepts             concepts
                        :current-concept      current-concept
                        :graph                graph
                        :selected-action-node @selected-action-node})
    [:div
     [description-block {:origin-text     (:phrase-description prepared-root-action-data)
                         :translated-text (:phrase-description-translated prepared-root-action-data)
                         :on-change       (fn [new-translated-description]
                                            (update-root-action-data {:phrase-description-translated new-translated-description}))}]
     (when concept-required?
       [concepts-block])
     [dialog-block {:dialog-data dialog-data}]
     [diagram-block {:graph graph}]
     [play-phrase-block {:graph           graph
                         :current-concept current-concept
                         :edited-data     data-store}]
     (if phrase-action-selected?
       [:div
        [phrase-block]
        [audios-block]]
       [ui/typography {:variant "subtitle1"}
        "Select action on diagram"])]))

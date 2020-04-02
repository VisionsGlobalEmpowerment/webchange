(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-audios
                                                                  get-dialog-data
                                                                  get-graph
                                                                  get-current-action-data]]
    [webchange.editor-v2.translator.translator-form.views-form-audios :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.translator.translator-form.views-form-dialog :refer [dialog-block]]
    [webchange.editor-v2.translator.translator-form.views-form-phrase :refer [phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.subs :as subs]))

(defn default-action-data
  [selected-action-concept? action-name concept-data scene-data]
  (let [action-data (if selected-action-concept?
                      (get-in concept-data [:data action-name])
                      (get-in scene-data [:actions action-name]))]
    action-data))

(defn get-update-path
  [current-path edited-field action-data]
  (let [single-action? (not (some #{(:type action-data)} ["parallel" "sequence-data"]))
        path-without-action-name (-> current-path rest vec)
        path-prefix (if single-action? [] [:data])]
    (vec (concat path-prefix
                 path-without-action-name
                 [edited-field]))))

(defn update-action-data!
  [original-action-name data-patch]
  (let [data-store @(re-frame/subscribe [::translator-subs/phrase-translation-data])
        current-concept @(re-frame/subscribe [::translator-subs/current-concept])
        selected-action-node @(re-frame/subscribe [::translator-subs/selected-action])
        {:keys [id type]} (get-current-action-data selected-action-node current-concept data-store)
        path (:path selected-action-node)

        scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        selected-action-concept? (-> selected-action-node (get-in [:data :concept-action]) (boolean))

        action-name (first path)
        current-data (get-in data-store [action-name :data] (default-action-data selected-action-concept? action-name current-concept scene-data))
        new-data (reduce
                   (fn [current-data [field value]]
                     (let [field-path (get-update-path path field current-data)]
                       (assoc-in current-data field-path value)))
                   current-data
                   data-patch)]
    (when (= original-action-name action-name)
      (re-frame/dispatch [::translator-events/set-phrase-translation-action action-name {:changed true
                                                                                         :type    type
                                                                                         :id      id
                                                                                         :data    new-data}]))))

(defn translator-form
  []
  (r/with-let []
              (let [current-concept @(re-frame/subscribe [::translator-subs/current-concept])
                    scene-id @(re-frame/subscribe [::subs/current-scene])
                    scene-data @(re-frame/subscribe [::subs/scene scene-id])
                    concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))

                    selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
                    phrase-action-name (or (:origin-name @selected-phrase-node)
                                           (keyword (:name @selected-phrase-node)))
                    selected-action-node (re-frame/subscribe [::translator-subs/selected-action])
                    selected-action-concept? (-> @selected-action-node (get-in [:data :concept-action]) (boolean))
                    {:keys [graph has-concepts?]} (get-graph scene-data phrase-action-name {:current-concept current-concept})
                    audios-list (get-audios scene-data graph)
                    data-store @(re-frame/subscribe [::translator-subs/phrase-translation-data])
                    dialog-data (get-dialog-data @selected-phrase-node graph (fn [node-data]
                                                                               (get-current-action-data node-data current-concept data-store)))
                    prepared-current-action-data (get-current-action-data @selected-action-node current-concept data-store)
                    phrase-action-selected? (-> @selected-action-node (nil?) (not))
                    concept-required? (or has-concepts? selected-action-concept?)]
                [:div
                 (when concept-required?
                   [concepts-block {:current-concept current-concept
                                    :concepts-list   concepts
                                    :on-change       #(re-frame/dispatch [::translator-events/set-current-concept %])}])

                 [dialog-block {:dialog-data dialog-data}]
                 [diagram-block {:graph graph}]
                 [play-phrase-block {:graph           graph
                                     :current-concept current-concept
                                     :edited-data     data-store}]
                 (if phrase-action-selected?
                   [:div
                    [phrase-block {:origin-text     (-> prepared-current-action-data :data :phrase-text)
                                   :translated-text (-> prepared-current-action-data :data :phrase-text-translated)
                                   :on-change       (fn [new-translated-text]
                                                      (update-action-data! (-> @selected-action-node :path first)
                                                                           {:phrase-text-translated new-translated-text}))}]
                    [audios-block {:scene-id  scene-id
                                   :audios    audios-list
                                   :action    prepared-current-action-data
                                   :on-change (fn [audio-key region-data]
                                                (update-action-data! (-> @selected-action-node :path first)
                                                                     (merge {:audio audio-key}
                                                                            (select-keys region-data [:start :duration]))))}]]
                   [ui/typography {:variant "subtitle1"}
                    "Select action on diagram"])
                 [ui/dialog
                  {:open       (and concept-required?
                                    (nil? current-concept))
                   :full-width true
                   :max-width  "xs"}
                  [ui/dialog-title
                   "Select Concept"]
                  [ui/dialog-content
                   [concepts-block {:current-concept current-concept
                                    :concepts-list   concepts
                                    :on-change       #(re-frame/dispatch [::translator-events/set-current-concept %])}]]
                  [ui/dialog-actions
                   [ui/button {:on-click #(do (re-frame/dispatch [::translator-events/clean-current-selected-action])
                                              (re-frame/dispatch [::translator-events/close-translator-modal]))}
                    "Cancel"]]]])
              (finally
                (re-frame/dispatch [::translator-events/clean-current-selected-action]))))

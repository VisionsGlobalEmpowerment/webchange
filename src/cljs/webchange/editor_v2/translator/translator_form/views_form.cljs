(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [ajax.core :refer [GET]]
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.translator-form.events :as translator-form.events]
    [webchange.editor-v2.translator.translator-form.subs :as form-subs]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-dialog-data
                                                                  get-graph
                                                                  get-current-action-data
                                                                  trim-text]]
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
  (let [data-store @(re-frame/subscribe [::form-subs/edited-actions-data])
        current-concept @(re-frame/subscribe [::translator-subs/current-concept])
        selected-action-node @(re-frame/subscribe [::translator-subs/selected-action])
        {:keys [id type]} (get-current-action-data selected-action-node current-concept data-store)
        path (:path selected-action-node)

        scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        selected-action-concept? (-> selected-action-node (get-in [:data :concept-action]) (boolean))

        action-name (first path)
        current-data (get-in data-store [[action-name id] :data] (default-action-data selected-action-concept? action-name current-concept scene-data))
        new-data (reduce
                   (fn [current-data [field value]]
                     (let [field-path (get-update-path path field current-data)]
                       (assoc-in current-data field-path value)))
                   current-data
                   data-patch)]
    (when (= original-action-name action-name)
      (re-frame/dispatch [::translator-form.events/set-action-edited-data action-name id {:type type
                                                                                          :data new-data}]))))

(defn- update-root-action-data
  [data-patch]
  (let [data-store @(re-frame/subscribe [::form-subs/edited-actions-data])
        selected-phrase-node @(re-frame/subscribe [::editor-subs/current-action])
        {:keys [id name type data]} (get-current-action-data selected-phrase-node nil data-store)]
    (re-frame/dispatch [::translator-form.events/set-action-edited-data name id {:type type
                                                                                 :data (merge data data-patch)}])))

(defn- get-current-root-data
  [selected-phrase-node data-store]
  (let [name (first (:path selected-phrase-node))
        origin-data (:data selected-phrase-node)
        new-data (get-in data-store [[name nil] :data])]
    (merge origin-data new-data)))

(defn- error-snackbar
  [{:keys [error on-close]}]
  (let [styles (get-styles)]
    [ui/snackbar {:open               (not (nil? error))
                  :anchor-origin      {:vertical   "bottom"
                                       :horizontal "right"}
                  :auto-hide-duration 6000
                  :on-close           on-close}
     [ui/snackbar-content {:message (r/as-element [ui/typography {:color "secondary"} error])
                           :style   (:error-wrapper styles)
                           :action  (r/as-element [ui/icon-button {:color    "secondary"
                                                                   :on-click on-close}
                                                   [ic/close]])}]]))

(defn- get-action-id
  [action-data current-concept]
  (let [concept-action? (-> action-data (get-in [:data :concept-action]) (boolean))]
    (if concept-action?
      (:id current-concept)
      nil)))

(defn- set-current-concept
  [concept]
  (re-frame/dispatch [::translator-events/set-current-concept concept]))

(defn- set-selected-action
  [action]
  (re-frame/dispatch [::translator-events/set-current-selected-action action]))

(defn- reset-selected-action
  []
  (re-frame/dispatch [::translator-events/clean-current-selected-action]))

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
  (r/with-let [error (r/atom nil)
               show-error #(reset! error %)
               hide-error #(reset! error nil)]
              (let [scene-id @(re-frame/subscribe [::subs/current-scene])
                    scene-data @(re-frame/subscribe [::subs/scene scene-id])

                    concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
                    current-concept @(re-frame/subscribe [::translator-subs/current-concept])

                    selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
                    phrase-action-name (or (:origin-name @selected-phrase-node)
                                           (keyword (:name @selected-phrase-node)))
                    selected-action-node (re-frame/subscribe [::translator-subs/selected-action])
                    selected-action-concept? (-> @selected-action-node (get-in [:data :concept-action]) (boolean))
                    {:keys [graph has-concepts?]} (get-graph scene-data phrase-action-name {:current-concept current-concept})
                    data-store @(re-frame/subscribe [::form-subs/edited-actions-data])
                    dialog-data (get-dialog-data @selected-phrase-node graph (fn [node-data]
                                                                               (get-current-action-data node-data current-concept data-store)))
                    prepared-root-action-data (get-current-root-data @selected-phrase-node data-store)
                    prepared-current-action-data @(re-frame/subscribe [::form-subs/current-action-data])
                    phrase-action-selected? (-> @selected-action-node (nil?) (not))
                    concept-required? (or has-concepts? selected-action-concept?)

                    handle-concept-changed #(do (reset-selected-action)
                                                (set-current-concept %))]
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
                   [concepts-block {:current-concept current-concept
                                    :concepts-list   concepts
                                    :on-change       handle-concept-changed}])
                 [dialog-block {:dialog-data dialog-data}]
                 [diagram-block {:graph graph}]
                 [play-phrase-block {:graph           graph
                                     :current-concept current-concept
                                     :edited-data     data-store}]
                 (if phrase-action-selected?
                   [:div
                    ^{:key (:name prepared-current-action-data)}
                    [phrase-block {:origin-text     (-> prepared-current-action-data :data :phrase-text trim-text)
                                   :translated-text (-> prepared-current-action-data :data :phrase-text-translated trim-text)
                                   :on-change       (fn [new-translated-text]
                                                      (update-action-data! (-> @selected-action-node :path first)
                                                                           {:phrase-text-translated new-translated-text}))}]
                    [audios-block]]
                   [ui/typography {:variant "subtitle1"}
                    "Select action on diagram"])
                 [error-snackbar {:error    @error
                                  :on-close hide-error}]])
              (finally
                (re-frame/dispatch [::translator-events/clean-current-selected-action]))))

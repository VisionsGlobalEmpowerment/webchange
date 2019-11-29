(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-audios
                                                                  get-graph
                                                                  get-used-concept-actions
                                                                  get-current-action-data]]
    [webchange.editor-v2.translator.translator-form.views-form-audios :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.translator.translator-form.views-form-phrase :refer [phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.subs :as subs]))

(defn update-action-data!
  [data-store id type name data]
  (swap! data-store assoc name {:changed true
                                :type    type
                                :id      id
                                :data    (merge (get-in @data-store [name :data])
                                                data)}))

(defn translator-form
  [data-store]
  (r/with-let [current-concept (r/atom nil)]
              (let [scene-id @(re-frame/subscribe [::subs/current-scene])
                    scene-data @(re-frame/subscribe [::subs/scene scene-id])
                    concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
                    concepts-scheme @(re-frame/subscribe [::editor-subs/course-concepts])

                    selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
                    phrase-action-name (keyword (:name @selected-phrase-node))
                    selected-action-node (re-frame/subscribe [::translator-subs/selected-action])
                    selected-action-concept? (-> @selected-action-node (get-in [:data :concept-action]) (boolean))

                    graph (get-graph scene-data phrase-action-name concepts-scheme)

                    used-concept-actions (get-used-concept-actions graph)
                    audios-list (get-audios scene-data concepts used-concept-actions)

                    prepared-current-action-data (get-current-action-data @selected-action-node @current-concept @data-store)]
                [:div
                 [ui/grid {:container true
                           :spacing   16
                           :justify   "space-between"}
                  [ui/grid {:item true
                            :xs   8}
                   [phrase-block {:node-data @selected-phrase-node}]]
                  [ui/grid {:item true :xs 4}
                   [concepts-block {:current-concept @current-concept
                                    :concepts-list   concepts
                                    :on-change       #(reset! current-concept %)}]]]
                 [diagram-block {:graph graph}]
                 [play-phrase-block {:graph graph
                                     :current-concept current-concept
                                     :edited-data data-store}]
                 [audios-block {:scene-id  scene-id
                                :audios    audios-list
                                :action    prepared-current-action-data
                                :on-change (fn [audio-key region-data]
                                             (update-action-data! data-store
                                                                  (:id prepared-current-action-data)
                                                                  (:type prepared-current-action-data)
                                                                  (:name prepared-current-action-data)
                                                                  (merge {:audio audio-key}
                                                                         (select-keys region-data [:start :duration]))))}]
                 [ui/dialog
                  {:open       (and selected-action-concept?
                                    (nil? @current-concept))
                   :full-width true
                   :max-width  "xs"}
                  [ui/dialog-title
                   "Select Concept"]
                  [ui/dialog-content
                   [concepts-block {:current-concept @current-concept
                                    :concepts-list   concepts
                                    :on-change       #(reset! current-concept %)}]]
                  [ui/dialog-actions
                   [ui/button {:on-click #(re-frame/dispatch [::translator-events/clean-current-selected-action])}
                    "Cancel"]]]
                 ])
              (finally
                (re-frame/dispatch [::translator-events/clean-current-selected-action]))))

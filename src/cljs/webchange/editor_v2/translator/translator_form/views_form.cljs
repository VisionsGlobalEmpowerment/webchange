(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.translator-form.views-form-audios :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.translator.translator-form.views-form-phrase :refer [phrase-block]]
    [webchange.subs :as subs]))

(defn get-current-action-data
  [action-name selected-node-data current-concept-data]
  (println "selected-node-data" selected-node-data)
  (let [concept-action? (get-in selected-node-data [:data :concept-action])]
    (if concept-action?
      (let [concept-action-name (-> selected-node-data :name keyword)]
        {:name concept-action-name
         :type :concept
         :data (get-in current-concept-data [:data concept-action-name])})
      {:name action-name
       :type :scene
       :data (:data selected-node-data)})))


(defn update-action-data!
  [data-store type name data]
  (println ">>> update-action-data!")
  (println "type" type)
  (println "name" name)
  (println "data" data)
  (swap! data-store assoc name {:type type
                                :data (merge (get-in @data-store [name :data])
                                             data)}))

(defn translator-form
  [data-store]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
        concepts-scheme @(re-frame/subscribe [::editor-subs/course-concepts])

        selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
        phrase-action-name (keyword (:name @selected-phrase-node))]
    (r/with-let [current-concept (r/atom nil)]
                (let [selected-action-node (re-frame/subscribe [::translator-subs/selected-action])
                      prepared-current-action-data (get-current-action-data (:name @selected-action-node) @selected-action-node @current-concept)]
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
                   [diagram-block {:scene-data      scene-data
                                   :action-name     phrase-action-name
                                   :concepts-scheme concepts-scheme}]
                   [audios-block {:scene-data  scene-data
                                  :action-data (:data prepared-current-action-data)
                                  :on-change   (fn [audio-key region-data]
                                                 (update-action-data! data-store
                                                                      (:type prepared-current-action-data)
                                                                      (:name prepared-current-action-data)
                                                                      {:audio    audio-key
                                                                       :start    (:start region-data)
                                                                       :duration (:duration region-data)})
                                                 )}]]))))

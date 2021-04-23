(ns webchange.editor-v2.translator.translator-form.views-form-play-phrase
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.events :as ce]
    [webchange.editor-v2.graph-builder.utils.graph-to-nodes-seq :refer [graph-to-nodes-seq]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets]]
    [webchange.resources.manager :as resources]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(def action-flow-id :translation-phrase-play)

(def common-button-params {:color "default"
                           :size  "small"
                           :style {:position "relative"
                                   :top      "-40px"
                                   :left     "100%"
                                   :margin   "-50px"}})

(defn adapt-action
  [{:keys [id start duration type volume] :as action}]
  (let [audio-action-data {:type     "audio"
                           :id       id
                           :start    start
                           :volume   volume
                           :duration duration}]
    (case type
      "audio" audio-action-data
      "animation-sequence" (merge audio-action-data {:key (:audio action)} {:id (:audio action)})
      "empty" action
      nil)))

(defn get-phrase-actions-sequence
  [graph]
  {:type       "sequence-data"
   :data       (->> graph
                          (graph-to-nodes-seq)
                          (map (fn [node]
                                 (let [[node-name node-data] (->> node seq first)]
                                   (merge node-data {:name node-name}))))
                          (map #(adapt-action (:data %)))
                          (filter #(not (nil? %)))
                          (vec))
                     })

(defn action->audios-list
  [action]
  (->> (:data action)
       (reduce
         (fn [result action-data]
           (conj result (:id action-data)))
         [])
       (filter #(not (nil? %)))
       (distinct)))

(defn play-phrase-block-button
  [action audios-list graph settings]
  (r/with-let [state (r/atom "pause")
               loading-progress (r/atom nil)]
              (let [action (-> action
                               (assoc :tags [action-flow-id])
                               (update :data conj {:type     "callback"
                                                   :callback #(reset! state "pause")}))]
                (when-not (:hide? settings)
                  (case @state
                    "pause" [fab (merge common-button-params
                                        {:disabled (nil? graph)
                                         :on-click (fn []
                                                       (reset! state "loading")
                                                       (resources/load-resources audios-list
                                                                                 {:on-progress #(reset! loading-progress %)
                                                                                  :on-complete #(do (reset! loading-progress nil)
                                                                                                    (reset! state "playing")
                                                                                                    (re-frame/dispatch [::ce/execute-action action]))}))})

                             [ic/play-arrow]]
                    "playing" [fab (merge common-button-params
                                          {:on-click (fn []
                                                       (reset! state "pause")
                                                       (re-frame/dispatch [::ce/execute-remove-flows {:flow-tag action-flow-id}]))})
                               [ic/pause]]
                    "loading" [fab (merge common-button-params
                                          {:disabled true})
                               [ui/circular-progress {:size    36
                                                      :variant (if (nil? @loading-progress)
                                                                 "indeterminate"
                                                                 "determinate")
                                                      :value   @loading-progress}]])))
              (finally
                (re-frame/dispatch [::ce/execute-remove-flows {:flow-tag action-flow-id}]))))


(defn play-phrase-block
  []
  (let [graph @(re-frame/subscribe [::translator-form.graph/graph])
        settings @(re-frame/subscribe [::translator-form/components-settings :play-phrase])
        action (get-phrase-actions-sequence graph)
        audios-list (action->audios-list action)]
    (play-phrase-block-button action audios-list graph settings)))
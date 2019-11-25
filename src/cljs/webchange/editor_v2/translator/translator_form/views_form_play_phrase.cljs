(ns webchange.editor-v2.translator.translator-form.views-form-play-phrase
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.events :as ce]
    [webchange.editor-v2.diagram.graph-builder.utils.graph-to-nodes-seq :refer [graph-to-nodes-seq]]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets
                                                                  get-current-action-data]]
    [webchange.interpreter.core :refer [load-assets]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(def action-flow-id :translation-phrase-play)

(defn adapt-action
  [{:keys [id start duration type] :as action}]
  (let [audio-action-data {:type     "audio"
                           :id       id
                           :start    start
                           :duration duration}]
    (case type
      "animation-sequence" (merge audio-action-data {:id (:audio action)})
      "empty" action
      nil)))

(defn get-phrase-actions-sequence
  [graph current-concept edited-data callback]
  {:type       "sequence-data"
   :unique-tag action-flow-id
   :data       (conj (->> graph
                          (graph-to-nodes-seq)
                          (map (fn [node]
                                 (let [[node-name node-data] (->> node seq first)]
                                   (get-current-action-data (merge node-data {:name node-name}) @current-concept @edited-data))))
                          (map #(adapt-action (:data %)))
                          (filter #(not (nil? %)))
                          (vec))
                     {:type     "callback"
                      :callback callback})})

(defn action->audios-list
  [action]
  (->> (:data action)
       (reduce
         (fn [result action-data]
           (conj result (:id action-data)))
         [])
       (filter #(not (nil? %)))
       (distinct)))

(defn play-phrase-block
  [{:keys [graph current-concept edited-data]}]
  (r/with-let [state (r/atom "pause")
               loading-progress (r/atom nil)]
              (let [common-button-params {:color "default"
                                          :size  "small"
                                          :style {:position "relative"
                                                  :top      "-40px"
                                                  :left     "100%"
                                                  :margin   "-50px"}}]
                (case @state
                  "pause" [fab (merge common-button-params
                                      {:disabled (nil? @current-concept)
                                       :on-click (fn []
                                                   (let [action (get-phrase-actions-sequence graph current-concept edited-data #(reset! state "pause"))
                                                         audios-list (action->audios-list action)]
                                                     (reset! state "loading")
                                                     (load-assets (audios->assets audios-list)
                                                                  #(reset! loading-progress %)
                                                                  #(do (reset! loading-progress nil)
                                                                       (reset! state "playing")
                                                                       (re-frame/dispatch [::ce/execute-action action])))))})

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
                                                    :value   @loading-progress}]]))
              (finally
                (re-frame/dispatch [::ce/execute-remove-flows {:flow-tag action-flow-id}]))))

(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.editor.form-elements.wavesurfer.wave-form :refer [audio-wave-form]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.diagram.graph-builder.filters.audios :refer [get-audios get-audio-data]]
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.subs :as subs]))

(defn trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn phrase-block
  [{:keys [text]}]
  [ui/text-field
   {:label           "Phrase Text"
    :placeholder     "Enter phrase text"
    :variant         "outlined"
    :full-width      true
    :value           text
    :margin          "normal"
    :multiline       true
    :disabled        true
    :InputLabelProps {:shrink true}}])

(defn audio-wave
  [{:keys [key start duration]} {:keys [selected? on-change]}]
  (let [audio-data {:key   key
                    :start start
                    :end   (+ start duration)}
        form-params {:height         128
                     :on-change      #(on-change key %)
                     :show-controls? selected?}
        card-style (if selected? {:border "solid 1px #00c0ff"} {})]
    [ui/card {:style card-style}
     [ui/card-content
      [ui/typography {:variant      "subtitle2"
                      :color        "default"
                      :gutterBottom :true}
       key]
      [audio-wave-form audio-data form-params]]]))

(defn audios-block
  [{:keys [audios selected-audio-key on-change]}]
  [:div
   [ui/typography {:variant "h6"
                   :style   {:margin "5px 0"}}
    "Audios"]
   (for [audio audios]
     ^{:key (:key audio)}
     [audio-wave audio {:selected? (= (:key audio) selected-audio-key)
                        :on-change on-change}])])

(defn update-action-data!
  [actions-data selected-action data]
  (let [action-path (:name selected-action)
        current-data (:data selected-action)]
    (swap! actions-data assoc action-path (merge current-data data))))

(defn save-actions-data!
  [actions-data scene-id]
  (doseq [[action-name action-data] @actions-data]
    (re-frame/dispatch [::events/edit-scene-action scene-id action-name action-data])))

(defn close-window!
  []
  (re-frame/dispatch [::translator-events/close-translator-modal]))

(defn translator-modal
  []
  (let [actions-data (atom {})
        scene-id (re-frame/subscribe [::subs/current-scene])
        translator-modal-state @(re-frame/subscribe [::translator-subs/translator-modal-state])
        selected-action @(re-frame/subscribe [::translator-subs/selected-action])
        handle-audio-changed (fn [audio-key region-data]
                               (update-action-data! actions-data
                                                    selected-action
                                                    {:audio    audio-key
                                                     :start    (:start region-data)
                                                     :duration (:duration region-data)}))
        handle-save #(do (save-actions-data! actions-data @scene-id)
                         (close-window!))
        handle-close #(close-window!)
        action-data @(re-frame/subscribe [::editor-subs/current-action])
        phrase-text (-> action-data
                        (get-in [:data :phrase-text])
                        (trim-text))
        action-name (keyword (:name action-data))
        graph (-> @(re-frame/subscribe [::subs/scene @scene-id])
                  (get-diagram-graph :translation action-name))
        audios (get-audios graph)
        selected-audio-key (-> selected-action :data get-audio-data :key)]
    [ui/dialog
     {:open       (boolean translator-modal-state)
      :on-close   handle-close
      :full-width true
      :max-width  "md"}
     [ui/dialog-title
      "Phrase Translation"]
     [ui/dialog-content {:class-name "translation-form"}
      [phrase-block {:text phrase-text}]
      [diagram-widget {:graph graph
                       :mode  :translation}]
      [audios-block {:audios             audios
                     :selected-audio-key selected-audio-key
                     :on-change          handle-audio-changed}]]
     [ui/dialog-actions
      [ui/button {:on-click handle-close}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-save}
       "Save"]]]))

(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor.form-elements.wavesurfer :refer [audio-wave-form]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.diagram.graph-builder.filters.audios :refer [get-audios]]
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.subs :as subs]))

(defn audio-wave
  [{:keys [key start duration]}]
  (let [audio-data {:key   key
                    :start start
                    :end   (+ start duration)}
        form-params {:height 128
                     :on-change #(println "on-change" %)}]
    [audio-wave-form audio-data form-params]))

(defn trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn translator-modal
  []
  (let [translator-modal-state @(re-frame/subscribe [::translator-subs/translator-modal-state])
        handle-close #(re-frame/dispatch [::translator-events/close-translator-modal])
        action-data @(re-frame/subscribe [::editor-subs/current-action])
        phrase-text (-> action-data
                        (get-in [:data :phrase-text])
                        (trim-text))]
    (let [action-name (keyword (:name action-data))
          scene-id (re-frame/subscribe [::subs/current-scene])
          graph (-> @(re-frame/subscribe [::subs/scene @scene-id])
                    (get-diagram-graph :translation action-name))
          audios (get-audios graph)]
      (.log js/console "phrase-text" phrase-text)
      [ui/dialog
       {:open       (boolean translator-modal-state)
        :on-close   handle-close
        :full-width true
        :max-width  "md"}
       [ui/dialog-title
        "Phrase Translation"]
       [ui/dialog-content
        {:class-name "translation-form"}
        [ui/text-field
         {:label           "Phrase Text"
          :placeholder     "Enter phrase text"
          :variant         "outlined"
          :full-width      true
          :value           phrase-text
          :margin          "normal"
          :multiline       true
          :disabled        true
          :InputLabelProps {:shrink true}}]
        [diagram-widget {:graph graph
                         :mode  :translation}]
        [ui/typography {:variant "h6"
                        :style   {:margin "5px 0"}}
         "Audios"]
        (for [audio audios]
          ^{:key audio}
          [ui/card
           [ui/card-content
            [audio-wave audio]]])
        ]
       [ui/dialog-actions
        [ui/button
         {:on-click handle-close}
         "Cancel"]]])))

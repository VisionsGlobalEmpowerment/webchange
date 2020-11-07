(ns webchange.editor-v2.dialog.dialog-form.views-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.dialog.views :refer [dialog-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-description :refer [description-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-phrase :refer [node-options phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-target :refer [target-block]]
    [webchange.editor-v2.translator.text.views-text-animation-editor :refer [text-chunks-modal]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:error-wrapper {:background-color (get-in-theme [:palette :background :default])}})

(defn translator-form
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        concept-required? @(re-frame/subscribe [::translator-form.graph/concept-required])
        action? (= "action" (get-in current-phrase-action [:data 1 :type]))
        ]
    [:div
     [description-block]
     (when concept-required?
       [concepts-block])
     [dialog-block]
     [diagram-block]
     [play-phrase-block]
     (if-not action?
      [target-block]
     )
     (if-not (nil? current-phrase-action)
       [:div
        [node-options]
        (if-not action?
          [:div
            [phrase-block]
            [audios-block]
           ]
        )
        ]
       [ui/typography {:variant "subtitle1"}
        "Select action on diagram"])
     [text-chunks-modal]]))

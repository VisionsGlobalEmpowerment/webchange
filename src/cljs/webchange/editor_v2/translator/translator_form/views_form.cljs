(ns webchange.editor-v2.translator.translator-form.views-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]
    [webchange.editor-v2.translator.translator-form.audio-assets.views :refer [audios-block]]
    [webchange.editor-v2.translator.translator-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-description :refer [description-block]]
    [webchange.editor-v2.translator.translator-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.translator.translator-form.views-form-dialog :refer [dialog-block]]
    [webchange.editor-v2.translator.translator-form.views-form-phrase :refer [phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:error-wrapper {:background-color (get-in-theme [:palette :background :default])}})

(defn translator-form
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        concept-required? @(re-frame/subscribe [::translator-form.graph/concept-required])]
    [:div
     [description-block]
     (when concept-required?
       [concepts-block])
     [dialog-block]
     [diagram-block]
     [play-phrase-block]
     (if-not (nil? current-phrase-action)
       [:div
        [phrase-block]
        [audios-block]]
       [ui/typography {:variant "subtitle1"}
        "Select action on diagram"])]))

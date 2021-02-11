(ns webchange.editor-v2.translator.text.views-chunks-editor-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.utils.text :refer  [text->chunks chunks->parts text-equals-parts?]]
    [webchange.editor-v2.translator.text.views-text-chunks :refer [text-chunks]]))

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn- event->value
  [event]
  (.. event -target -value))

(defn chunks-editor-form
  [{:keys [text chunks on-change show-chunks? origin-text-disabled?]
    :or   {show-chunks?          true
           origin-text-disabled? false}}]
  (let [handle-text-change (fn [event]
                             (let [current-text (event->value event)
                                   current-chunks (text->chunks current-text)]
                               (on-change {:text   current-text
                                           :chunks current-chunks})))
        handle-parts-change (fn [event]
                              (let [current-parts-str (event->value event)]
                                (when (text-equals-parts? text current-parts-str)
                                  (let [current-chunks (text->chunks text current-parts-str)]
                                    (on-change {:chunks current-chunks})))))]
    (let [parts (chunks->parts text chunks)]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       [ui/grid {:item true :xs 12}
        [ui/text-field (merge text-input-params
                              {:label     "Text"
                               :value     (or text "")
                               :disabled  origin-text-disabled?
                               :on-change handle-text-change})]]
       [ui/grid {:item true :xs 12}
        [ui/text-field (merge text-input-params
                              {:label       "Parts"
                               :value       (clojure.string/join " " parts)
                               :on-change   handle-parts-change
                               :helper-text "Use space to divide text into chunks"})]]
       (when show-chunks?
         [ui/grid {:item true :xs 12}
          [text-chunks {:parts parts}]])])))

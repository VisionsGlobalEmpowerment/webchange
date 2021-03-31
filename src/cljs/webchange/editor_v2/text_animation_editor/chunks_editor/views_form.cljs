(ns webchange.editor-v2.text-animation-editor.chunks-editor.views-form
  (:require
    [webchange.utils.text :refer [text->chunks chunks->parts text-equals-parts?]]
    [webchange.editor-v2.text-animation-editor.views-chunks :refer [text-chunks]]
    [webchange.ui-framework.components.index :refer [label text-area]]))

(defn chunks-editor-form
  [{:keys [text chunks on-change show-chunks? origin-text-disabled?]
    :or   {show-chunks?          true
           origin-text-disabled? false}}]
  (let [handle-text-change (fn [current-text]
                             (let [current-chunks (text->chunks current-text)]
                               (on-change {:text   current-text
                                           :chunks current-chunks})))
        handle-parts-change (fn [current-parts-str]
                              (when (text-equals-parts? text current-parts-str)
                                (let [current-chunks (text->chunks text current-parts-str)]
                                  (on-change {:chunks current-chunks}))))
        parts (chunks->parts text chunks)]
    [:div.chunks-editor-from
     [label {:class-name "chunks-editor-label"}
      "Text value:"]
     [text-area {:value     (or text "")
                 :disabled  origin-text-disabled?
                 :on-change handle-text-change
                 :variant   "outlined"}]
     [label {:class-name "chunks-editor-label"}
      "Use space to divide text into chunks:"]
     [text-area {:value     (clojure.string/join " " parts)
                 :on-change handle-parts-change
                 :variant   "outlined"}]
     (when show-chunks?
       [text-chunks {:parts parts}])]))

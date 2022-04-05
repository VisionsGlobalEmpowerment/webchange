(ns webchange.editor-v2.text-animation-editor.chunks-editor.form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.text-animation-editor.chunks-editor.form.state :as state]
    [webchange.utils.text :refer [text->chunks chunks->parts text-equals-parts?]]
    [webchange.editor-v2.text-animation-editor.views-chunks :refer [text-chunks]]
    [webchange.ui-framework.components.index :refer [label text-area]]))

(defn- text-form
  [{:keys [disabled?]}]
  (let [text @(re-frame/subscribe [::state/origin-text])
        handle-changed #(re-frame/dispatch [::state/handle-origin-text-changed %])]
    [:div
     [label {:class-name "chunks-editor-label"}
      "Text value:"]
     [text-area {:value     text
                 :disabled  disabled?
                 :on-change handle-changed
                 :variant   "outlined"}]]))

(defn- parts-form
  []
  (let [parts @(re-frame/subscribe [::state/parts])
        handle-parts-changed #(re-frame/dispatch [::state/handle-parts-changed %])]
    [:div
     [label {:class-name "chunks-editor-label"}
      "Use space to divide text into chunks:"]
     [text-area {:value     (clojure.string/join " " parts)
                 :on-change handle-parts-changed
                 :variant   "outlined"}]]))

(defn- chunks-form
  []
  (let [parts @(re-frame/subscribe [::state/parts])]
    [text-chunks {:parts parts}]))

(defn chunks-editor-form
  [{:keys [text chunks on-change]}]
  (re-frame/dispatch [::state/init {:text      text
                                    :chunks    chunks
                                    :on-change on-change}])
  (fn [{:keys [show-chunks? origin-text-disabled?]
        :or   {show-chunks?          true
               origin-text-disabled? false}}]
    [:div.chunks-editor-from
     [text-form {:disabled? origin-text-disabled?}]
     [parts-form]
     (when show-chunks?
       [chunks-form])]))

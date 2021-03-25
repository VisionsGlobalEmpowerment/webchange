(ns webchange.ui-framework.components.file.index
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.ui-framework.components.button.index :as button]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.text-input.index :as text-input]))

(defn- get-accept-extensions
  [type]
  (->> (case type
         "audio" ["mp3" "wav"]
         "image" ["gif" "jpg" "jpeg" "png"]
         [])
       (map #(str "." %))
       (join ",")))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn- upload-file
  [{:keys [callback file options type]
    :or   {callback #()
           options  {}}}]
  (re-frame/dispatch [::assets-events/upload-asset file {:options   options
                                                         :type      type
                                                         :on-finish callback}]))

(defn component
  [{:keys [button-text type on-change show-file-name? show-icon? with-upload?]
    :or   {button-text     "Choose File"
           on-change       #()
           show-icon?      true
           with-upload?    true
           show-file-name? true}}]
  (r/with-let [file-input (atom nil)
               uploading? (atom false)
               text-value (r/atom "Select file..")
               handle-change (fn [event]
                               (let [file (change-event->file event)]
                                 (when show-file-name? (reset! text-value (.-name file)))
                                 (if with-upload?
                                   (upload-file {:file     file
                                                 :type     type
                                                 :callback #(on-change (:url %))})
                                   (on-change file))))]
    [:div.wc-file
     [:input {:type      "file"
              :accept    (get-accept-extensions type)
              :on-change handle-change
              :ref       #(reset! file-input %)}]
     (when (and show-icon? (some? type))
       [icon/component {:icon type}])
     [text-input/component {:value      (if @uploading? "Uploading.." @text-value)
                            :class-name "file-name"
                            :disabled?  true}]
     [button/component {:on-click  #(.click @file-input)
                        :disabled? @uploading?}
      button-text]]))

(ns webchange.ui-framework.components.file.index
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.ui-framework.components.button.index :as button]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.text-input.index :as text-input]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-accept-extensions
  [type]
  (->> (case type
         "audio" ["mp3" "wav" "m4a"]
         "image" ["gif" "jpg" "jpeg" "png"]
         [])
       (map #(str "." %))
       (join ",")))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn- drop-event->file
  [event]
  (-> event
      (.. -dataTransfer -files)
      (.item 0)))

(defn- upload-file
  [{:keys [callback file options type]
    :or   {callback #()
           options  {}}}]
  (re-frame/dispatch [::assets-events/upload-asset file {:options   options
                                                         :type      type
                                                         :on-finish callback}]))

(defn- drag-and-drop
  [{:keys [on-drop]}]
  (r/with-let [active? (r/atom false)
               drop-area (r/atom nil)

               prevent-defaults #(do (.preventDefault %)
                                     (.stopPropagation %))
               handle-document-drag-enter #(do (prevent-defaults %)
                                               (reset! active? true))
               handle-area-drag-leave #(do (prevent-defaults %)
                                           (reset! active? false))
               handle-area-drop #(do (prevent-defaults %)
                                     (reset! active? false)
                                     (on-drop %))

               init-state (fn [ref]
                            (reset! drop-area ref)
                            (.addEventListener js/document "dragenter" handle-document-drag-enter)
                            (.addEventListener @drop-area "dragleave" handle-area-drag-leave)
                            (.addEventListener @drop-area "dragover" prevent-defaults)
                            (.addEventListener @drop-area "drop" handle-area-drop))]
    [:div {:class-name (get-class-name {"dnd-area" true
                                        "active "  @active?})}
     [:div {:class-name "dnd-message"}
      [:div "Drop files here"]
      [icon/component {:icon "drop-place"}]]
     [:div.drop-overlay {:ref #(when (some? %) (init-state %))}]]
    (finally
      (.removeEventListener js/document "dragenter" handle-document-drag-enter)
      (.removeEventListener @drop-area "dragleave" handle-area-drag-leave)
      (.removeEventListener @drop-area "dragover" prevent-defaults)
      (.removeEventListener @drop-area "drop" handle-area-drop))))

(defn component
  [{:keys [button-props
           button-text
           class-name
           disabled?
           drag-and-drop?
           on-change
           show-file-name?
           show-icon?
           show-input?
           type
           upload-options
           with-upload?]
    :or   {button-props    {}
           button-text     "Choose File"
           disabled?       false
           drag-and-drop?  false
           on-change       #()
           show-icon?      true
           show-input?     true
           upload-options  {}
           with-upload?    true
           show-file-name? true}}]
  (r/with-let [file-input (atom nil)
               uploading? (r/atom false)
               text-value (r/atom "Select file..")
               handle-change (fn [file]
                               (when show-file-name? (reset! text-value (.-name file)))
                               (if with-upload?
                                 (do (reset! uploading? true)
                                     (upload-file {:file     file
                                                   :type     type
                                                   :options  upload-options
                                                   :callback #(do
                                                                (reset! uploading? false)
                                                                (on-change (:url %)))}))
                                 (on-change file)))]
    [:div {:class-name (get-class-name (cond-> {"wc-file" true}
                                               (some? class-name) (assoc class-name true)))}
     [:input {:type      "file"
              :accept    (get-accept-extensions type)
              :on-change #(-> % change-event->file handle-change)
              :ref       #(reset! file-input %)}]
     (when (and show-icon? (some? type))
       [icon/component {:icon type}])

     (when show-input?
       [:div {:class-name "file-name-wrapper"
              :on-click   #(when-not disabled? (.click @file-input))}
        [text-input/component {:value      (if @uploading? "Uploading..." @text-value)
                               :class-name "file-name"
                               :disabled?  true}]])

     [button/component (merge {:on-click  #(.click @file-input)
                               :disabled? (or disabled? @uploading?)}
                              button-props)
      button-text]
     (when drag-and-drop?
       [drag-and-drop {:on-drop #(-> % drop-event->file handle-change)}])]))

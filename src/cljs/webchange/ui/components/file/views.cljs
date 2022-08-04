(ns webchange.ui.components.file.views
  (:require
    [clojure.string :refer [join]]
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

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

(defn file
  [{:keys [class-name
           class-name-button
           disabled?
           loading?
           on-change
           text
           type]
    :or   {disabled? false
           loading?  false
           text      "Choose File"}}]
  (r/with-let [file-input (atom nil)
               handle-change (fn [file]
                               (when (fn? on-change)
                                 (on-change file)))]
    [:div {:class-name (get-class-name (cond-> {"bbs--file" true}
                                               (some? class-name) (assoc class-name true)))}
     [:input {:type      "file"
              :accept    (get-accept-extensions type)
              :on-change #(-> % change-event->file handle-change)
              :ref       #(reset! file-input %)}]
     [button {:on-click   #(.click @file-input)
              :disabled?  disabled?
              :loading?   loading?
              :shape      "rounded"
              :class-name (get-class-name {class-name-button (some? class-name-button)})}
      text]]))

(ns webchange.lesson-builder.widgets.audio-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.audio-add.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui.widgets.index :as widgets]))

(defn- upload-file-button
  []
  (let [handle-change #(re-frame/dispatch [::state/add-audio-asset %])]
    [widgets/file {:type              "audio"
                   :text              "Upload Audio"
                   :language          "english"
                   :class-name-button "audio-add--upload-button"
                   :on-change         handle-change}]))

(defn- index
  []
  [:div.audio-add--index
   [ui/button {:icon      "record"
               :icon-side "left"
               :shape     "rounded"
               :color     "blue-1"}
    "Record Voice"]
   [upload-file-button]])

(defn audio-add
  []
  [:div.widget--audio-add
   [index]])

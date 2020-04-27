(ns webchange.editor-v2.translator.translator-form.audio-assets.audios-list.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.audio-assets.audios-list.audios-list-item.views :refer [audios-list-item]]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets]]
    [webchange.interpreter.core :refer [load-assets]]))

(defn- waves-list
  [{:keys [audios-data]}]
  [:div
   (for [audio-data audios-data]
     ^{:key (:url audio-data)}
     [audios-list-item audio-data])])

(defn- audios-loading-block
  [{:keys [audios-list loading-progress loaded]}]
  (when (= @loading-progress 0)
    (load-assets (audios->assets audios-list)
                 #(reset! loading-progress %)
                 #(reset! loaded true)))
  [ui/circular-progress {:color   "secondary"
                         :variant "determinate"
                         :value   @loading-progress
                         :style   {:margin-left "50%"
                                   :margin-top  18}}])

(defn audios-list
  [{:keys [audios]}]
  (r/with-let [assets-loaded (r/atom false)                 ;; ToDo: move out of list
               assets-loading-progress (r/atom 0)]
              [:div
               (if @assets-loaded
                 [waves-list {:audios-data      audios}]
                 [audios-loading-block {:audios-list      (map #(:url %) audios)
                                        :loading-progress assets-loading-progress
                                        :loaded           assets-loaded}])]))

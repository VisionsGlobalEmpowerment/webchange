(ns webchange.editor-v2.concepts.fields
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]))

(defmulti dataset-item-control #(:type %))

;
; String-typed
;

(defmethod dataset-item-control "string"
  [{:keys [on-change value]}]
  [ui/text-field {:full-width true :default-value value :on-change #(on-change (-> % .-target .-value))}])

(defn- select-file-form
  ([type uploading-atom on-change]
    (select-file-form type uploading-atom on-change nil))
  ([type uploading-atom on-change options]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        on-change (fn [js-file]
                    (reset! uploading-atom true)
                    (re-frame/dispatch [::concepts-events/upload-asset js-file {:type type :on-finish on-finish :options options}]))]
    [file-input/select-file-form {:on-change on-change}])))

;
; Image-typed
;

(defmethod dataset-item-control "image"
  [{:keys [on-change value]}]
  (r/with-let [uploading (r/atom false)]
    [ui/grid {:container true :justify "flex-start" :align-items "flex-end"}
     (if value
       [ui/avatar {:style {:width 60 :height 60} :src value}]
       [ui/avatar {:style {:width 60 :height 60}} [ic/image]])
     [ui/text-field {:style {:width "50%"} :value (str value) :on-change #(on-change (-> % .-target .-value))}]
     [select-file-form :image uploading on-change]
     (when @uploading
       [ui/circular-progress])]))

;
; Video-typed
;

(defmethod dataset-item-control "video"
  [{:keys [on-change value]}]
  (r/with-let [uploading (r/atom false)]
    [ui/grid {:container true :justify "flex-start" :align-items "flex-end"}
     [ui/avatar [ic/video-label]]
     [ui/text-field {:style {:width "50%"} :value (str value) :on-change #(on-change (-> % .-target .-value))}]
     [select-file-form :video uploading on-change]
     (when @uploading
       [ui/circular-progress])]))


;
; Unknown type
;

(defmethod dataset-item-control :default
  [_]
  [ui/chip {:label "Unsupported type" :color "secondary"}])
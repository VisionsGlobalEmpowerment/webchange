(ns webchange.editor-v2.translator.translator-form.views-form-audios
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.interpreter.core :refer [load-assets]]
    [webchange.editor.form-elements.wavesurfer.wave-form :refer [audio-wave-form]]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets]]
    [webchange.editor-v2.translator.translator-form.views-form-audio-upload :refer [upload-audio-form]]))

(defn get-action-audio-data
  [action-data audios]
  (when (some #{(:type action-data)} ["audio"
                                      "animation-sequence"])
    (let [action-key (or (get action-data :audio)
                         (get action-data :id))
          action-url (some (fn [{:keys [key url]}]
                             (and (= key action-key) url)) audios)]
      (merge (select-keys action-data [:start :duration])
             {:key (or action-url
                       action-key)}))))

(defn audio-wave
  [{:keys [key alias start duration selected?]} {:keys [on-click on-change]}]
  (let [audio-data {:key   key
                    :start (or start 0)
                    :end   (+ start duration)}
        form-params {:height         64
                     :on-change      on-change
                     :show-controls? selected?}
        border-style (if selected? {:border "solid 1px #00c0ff"} {})]
    [ui/card {:style    (merge border-style
                               {:margin-bottom 8})
              :on-click #(on-click key)}
     [ui/card-content
      [ui/typography {:variant "subtitle2"
                      :color   "default"}
       (or alias key)]
      [audio-wave-form audio-data form-params]]]))

(defn waves-list
  [{:keys [audios-data on-wave-click on-wave-region-change]}]
  [:div
   (for [audio-data audios-data]
     ^{:key (:key audio-data)}
     [audio-wave audio-data {:on-click  on-wave-click
                             :on-change on-wave-region-change}])])

(defn audio-key->audio-data
  [audios]
  (map
    (fn [{:keys [url alias]}]
      {:key       url
       :alias     alias
       :start     nil
       :duration  nil
       :selected? false})
    audios))

(defn update-audios-with-action
  [audios-data current-key action-audio-data]
  (map
    (fn [audio-data]
      (if (= (:key audio-data) current-key)
        (merge audio-data
               {:start     (:start action-audio-data)
                :duration  (:duration action-audio-data)
                :selected? true})
        (merge audio-data
               {:start     nil
                :duration  nil
                :selected? false})))
    audios-data))

(defn get-prepared-audios-data
  [audios-list current-key action-audio-data]
  (-> audios-list
      (audio-key->audio-data)
      (update-audios-with-action current-key action-audio-data)))

(defn audios-loading-block
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

(def current-key (r/atom nil))

(defn audios-list-block-render
  [{:keys [scene-id audios action on-change]}]
  (let [action-data (:data action)
        action-audio-data (get-action-audio-data action-data audios)
        audios-data (get-prepared-audios-data audios @current-key action-audio-data)]
    (r/with-let [assets-loaded (r/atom false)
                 assets-loading-progress (r/atom 0)]
                [:div
                 (if @assets-loaded
                   [waves-list {:audios-data           audios-data
                                :on-wave-click         (fn [key]
                                                         (reset! current-key key)
                                                         (on-change key))
                                :on-wave-region-change (fn [region]
                                                         (on-change @current-key region))}]
                   [audios-loading-block {:audios-list      (map #(:url %) audios)
                                          :loading-progress assets-loading-progress
                                          :loaded           assets-loaded}])
                 [upload-audio-form {:scene-id scene-id}]])))

(defn audios-list-block-did-mount
  [this]
  (let [{:keys [action audios]} (r/props this)
        action-data (:data action)
        action-audio-data (get-action-audio-data action-data audios)]
    (reset! current-key (:key action-audio-data))))

(defn audios-list-block-did-update
  [this [_ old-props]]
  (let [{:keys [action audios]} (r/props this)
        action-data (:data action)
        action-name (:name action)
        old-action-name (:name (:action old-props))
        action-audio-data (get-action-audio-data action-data audios)]
    (when-not (= action-name old-action-name)
      (reset! current-key (:key action-audio-data)))))

(def audios-list-block
  (with-meta audios-list-block-render
             {:component-did-mount  audios-list-block-did-mount
              :component-did-update audios-list-block-did-update}))

(defn audios-block
  [{:keys [action] :as props}]
  [:div
   [ui/typography {:variant "h6"
                   :style   {:margin "5px 0"}}
    "Audios"]
   (if-not (nil? (:data action))
     [audios-list-block props]
     [ui/typography {:variant "subtitle1"}
      "Select action on diagram"])])

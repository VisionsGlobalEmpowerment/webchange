(ns webchange.editor-v2.translator.translator-form.audio-assets.views
  (:require
    [clojure.string :refer [capitalize]]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.core :refer [load-assets]]
    [webchange.editor-v2.translator.translator-form.audio-assets.subs :as subs]
    [webchange.editor-v2.translator.translator-form.audio-assets.views-audio-wave :refer [audio-wave]]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets]]
    [webchange.editor-v2.translator.translator-form.views-form-audio-upload :refer [upload-audio-form]]))

(def current-key (r/atom nil))

(defn get-action-audio-data
  "Return a map of audio data from action.
   Can be used to render waveform
   keys:
  :key - audio url
  :start, :duration"
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

(defn waves-list
  [{:keys [audios-data on-change-region audios-filter targets]}]
  (let [filtered-audios-data (if-not (nil? audios-filter)
                               (filter (fn [{:keys [target]}]
                                         (= target (:target audios-filter)))
                                       (vec audios-data))
                               audios-data)]
    [:div
     (for [audio-data filtered-audios-data]
       ^{:key (:key audio-data)}
       [audio-wave (merge audio-data
                          {:on-change-region on-change-region
                           :targets          targets
                           :current-key      current-key})])]))

(defn audio-key->audio-data
  [audios]
  (map
    (fn [{:keys [url alias target]}]
      {:key       url
       :alias     alias
       :target    target
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

(defn audios-list-block-render
  [{:keys [scene-id action on-change-region audios-filter targets]}]
  (r/with-let [assets-loaded (r/atom false)
               assets-loading-progress (r/atom 0)]
              (let [action-data (:data action)
                    audios (map second @(re-frame/subscribe [::subs/assets-data]))
                    action-audio-data (get-action-audio-data action-data audios)
                    audios-data (get-prepared-audios-data audios @current-key action-audio-data)]
                [:div
                 (if @assets-loaded
                   [waves-list {:audios-data      audios-data
                                :audios-filter    audios-filter
                                :targets          targets
                                :action-name      (:name action)
                                :on-change-region on-change-region}]
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
  [props]
  (let [audios-targets @(re-frame/subscribe [::subs/available-targets])
        targets (conj audios-targets "any")]
    (r/with-let [current-target (r/atom "any")]
                [:div
                 [:div {:style {:margin-bottom "15px"}}
                  [ui/typography {:variant "h6"
                                  :style   {:display "inline-block"
                                            :margin  "5px 0"}}
                   "Audios"]
                  [ui/select {:value     @current-target
                              :on-change #(reset! current-target (->> % .-target .-value))
                              :style     {:margin "0 10px"
                                          :width  "150px"}}
                   (for [target targets]
                     ^{:key target}
                     [ui/menu-item {:value target}
                      (capitalize target)])]]
                 [audios-list-block (merge props
                                           {:targets       audios-targets
                                            :audios-filter (if-not (= @current-target "any")
                                                             {:target @current-target}
                                                             nil)})]])))

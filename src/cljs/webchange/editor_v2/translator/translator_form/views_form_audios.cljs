(ns webchange.editor-v2.translator.translator-form.views-form-audios
  (:require
    [clojure.string :refer [capitalize]]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.interpreter.core :refer [load-assets]]
    [webchange.editor.form-elements.wavesurfer.wave-form :refer [audio-wave-form]]
    [webchange.editor-v2.translator.translator-form.utils :refer [audios->assets]]
    [webchange.editor-v2.translator.translator-form.views-form-audio-upload :refer [upload-audio-form]]))

(def current-key (r/atom nil))

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
  [{:keys [key alias start duration selected? target]} {:keys [on-change]}]
  (r/with-let [on-change-region (fn [region]
                                  (on-change key region))
               on-select (fn []
                           (reset! current-key key)
                           (on-change key))]
    (let [audio-data {:key   key
                      :start (or start 0)
                      :end   (+ start duration)}
          form-params {:height         64
                       :on-change      on-change-region
                       :show-controls? selected?}
          border-style (if selected? {:border "solid 1px #00c0ff"} {})]
      [ui/card {:style    (merge border-style
                                 {:margin-bottom 8})
                :on-click on-select}
       [ui/card-content
        (when-not (nil? target)
          [ui/chip {:label target
                    :style {:margin  "0 10px 0 0"
                            :padding "0"}}])
        [ui/typography {:variant "subtitle2"
                        :color   "default"
                        :style   {:display "inline-block"}}
         (or alias key)]
        [audio-wave-form audio-data form-params]]])))

(defn waves-list
  [{:keys [audios-data on-change audios-filter]}]
  (let [filtered-audios-data (if-not (nil? audios-filter)
                               (filter (fn [{:keys [target]}]
                                         (= target (:target audios-filter)))
                                       (vec audios-data))
                               audios-data)]
    [:div
     (for [audio-data filtered-audios-data]
       ^{:key (:key audio-data)}
       [audio-wave audio-data {:on-change on-change}])]))

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
  [{:keys [scene-id audios action on-change audios-filter]}]
  (let [action-data (:data action)
        action-audio-data (get-action-audio-data action-data audios)
        audios-data (get-prepared-audios-data audios @current-key action-audio-data)]
    (r/with-let [assets-loaded (r/atom false)
                 assets-loading-progress (r/atom 0)]
                [:div
                 (if @assets-loaded
                   [waves-list {:audios-data           audios-data
                                :audios-filter         audios-filter
                                :on-change on-change}]
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
  [{:keys [action audios] :as props}]
  (let [show-audios? (-> (:data action) nil? not)
        targets (conj (->> audios
                           (map :target)
                           (filter #(-> % nil? not))
                           (distinct)) "any")]
    (r/with-let [current-target (r/atom "any")]
                [:div
                 [:div {:style {:margin-bottom "15px"}}
                  [ui/typography {:variant "h6"
                                  :style   {:display "inline-block"
                                            :margin  "5px 0"}}
                   "Audios"]
                  (when show-audios?
                    [ui/select {:value     @current-target
                                :on-change #(reset! current-target (->> % .-target .-value))
                                :style     {:margin "0 10px"
                                            :width  "150px"}}
                     (for [target targets]
                       ^{:key target}
                       [ui/menu-item {:value target}
                        (capitalize target)])])]
                 (if show-audios?
                   [audios-list-block (merge props
                                             {:audios-filter (if-not (= @current-target "any")
                                                               {:target @current-target}
                                                               nil)})]
                   [ui/typography {:variant "subtitle1"}
                    "Select action on diagram"])])))

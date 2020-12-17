(ns webchange.editor-v2.translator.text.views-text-animation-editor
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.text.core :refer [chunks->parts]]
    [webchange.editor-v2.translator.text.views-text-chunks :refer [text-chunks]]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.ui.components.message :refer [message]]))

(def modal-state-path [:editor-v2 :translator :text :chunks-modal-state])
(def selected-chunk-path [:editor-v2 :translator :text :chunks :selected])
(def audio-path [:editor-v2 :translator :text :chunks :audio])
(def bounds-path [:editor-v2 :translator :text :chunks :bounds])
(def data-path [:editor-v2 :translator :text :chunks :data])

(defn bound
  [{:keys [start end]} value]
  (cond
    (> start value) start
    (< end value) end
    :else value))

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::text-object
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])
     (re-frame/subscribe [::translator-form.scene/objects-data])])
  (fn [[{:keys [target]} objects]]
    [target (get objects (keyword target))]))

(re-frame/reg-sub
  ::selected-chunk
  (fn [db]
    (get-in db selected-chunk-path)))

(re-frame/reg-sub
  ::selected-audio
  (fn [db]
    (let [index (get-in db selected-chunk-path)
          audio (get-in db audio-path)
          data (get-in db data-path)
          bounds (get-in db bounds-path)
          {:keys [start end]} (->> data (filter #(= index (:chunk %))) first)]
      (when (some? audio)
        {:url   audio
         :start (bound bounds start)
         :end   (bound bounds end)}))))

(re-frame/reg-sub
  ::form-available?
  (fn []
    [(re-frame/subscribe [::selected-audio])])
  (fn [[selected-audio]]
    (some? selected-audio)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_ {:keys [audio start duration data target]}]]
    (let [chunks-count (-> (translator-form.scene/objects-data db)
                           (get (keyword target))
                           (get :chunks)
                           count)
          filtered-data (remove #(<= chunks-count (:chunk %)) data)]
      {:db       (-> db
                     (assoc-in modal-state-path true)
                     (assoc-in data-path filtered-data)
                     (assoc-in audio-path audio)
                     (assoc-in bounds-path {:start start :end (+ start duration)}))
       :dispatch [::select-chunk 0]})))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [db]} [_]]
    (let [data (get-in db data-path)]
      {:db       (assoc-in db modal-state-path false)
       :dispatch [::translator-form.actions/update-action :phrase {:data data}]})))

(re-frame/reg-event-fx
  ::select-chunk
  (fn [{:keys [db]} [_ index]]
    {:db (assoc-in db selected-chunk-path index)}))

(re-frame/reg-event-fx
  ::select-audio
  (fn [{:keys [db]} [_ {:keys [start end duration]}]]
    (let [index (get-in db selected-chunk-path)
          chunk {:at       start
                 :start    start
                 :end      end
                 :chunk    index
                 :duration duration}
          chunks (as-> (get-in db data-path) c
                       (remove #(= index (:chunk %)) c)
                       (conj c chunk)
                       (sort-by :chunk c))]
      {:db (assoc-in db data-path chunks)})))

(defn- get-styles
  []
  {:audio-container {:padding "16px"}})

(defn- text-chunks-form
  []
  (let [[text-object-name text-object-data] @(re-frame/subscribe [::text-object])
        available-text-objects @(re-frame/subscribe [::translator-form.scene/text-objects])
        selected-chunk @(re-frame/subscribe [::selected-chunk])
        selected-audio @(re-frame/subscribe [::selected-audio])
        parts (chunks->parts (:text text-object-data) (:chunks text-object-data))
        styles (get-styles)]
    (if (or (nil? selected-audio)
            (nil? (:start selected-audio))
            (nil? (:end selected-audio)))
      [message {:type    "warn"
                :message "Select audio region in translation dialog to configure text animation"}]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       [ui/grid {:item true :xs 12}
        [ui/paper {:style (:audio-container styles)}
         [audio-wave-form (merge selected-audio
                                 {:height         96
                                  :on-change      #(re-frame/dispatch [::select-audio %])
                                  :show-controls? true})]]]
       [ui/grid {:item true :xs 12}
        [ui/form-control {:full-width true
                          :style      (:control-container styles)}
         [ui/input-label "Target text"]
         [ui/select {:value     (or text-object-name "")
                     :variant   "outlined"
                     :on-change #(re-frame/dispatch [::translator-form.actions/set-text-animation-target (-> % .-target .-value)])}
          (for [[object-name {:keys [text]}] available-text-objects]
            ^{:key object-name}
            [ui/menu-item {:value object-name} text])]]]
       [ui/grid {:item true :xs 12}
        [text-chunks {:parts              parts
                      :selected-chunk-idx selected-chunk
                      :on-click           #(re-frame/dispatch [::select-chunk %])}]]])))

(defn text-chunks-modal
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        cancel #(re-frame/dispatch [::cancel])
        apply #(re-frame/dispatch [::apply])
        form-available? @(re-frame/subscribe [::form-available?])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   cancel
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title
        "Edit text animation chunks"]
       [ui/dialog-content {:class-name "translation-form"}
        [text-chunks-form]]
       [ui/dialog-actions
        [ui/button {:on-click cancel}
         "Cancel"]
        [ui/button {:color    "secondary"
                    :variant  "contained"
                    :disabled (not form-available?)
                    :on-click apply}
         "Apply"]]])))

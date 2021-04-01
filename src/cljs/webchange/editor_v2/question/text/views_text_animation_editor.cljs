(ns webchange.editor-v2.question.text.views-text-animation-editor
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.utils.text :refer [text->chunks chunks->parts]]
    [webchange.editor-v2.text-animation-editor.views-chunks :refer [text-chunks]]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]
    [webchange.ui.components.message :refer [message]]))

(def modal-state-path [:editor-v2 :question :text :chunks-modal-state])
(def selected-chunk-path [:editor-v2 :question :text :chunks :selected])
(def audio-path [:editor-v2 :question :text :chunks :audio])
(def bounds-path [:editor-v2 :question :text :chunks :bounds])
(def data-path [:editor-v2 :question :text :chunks :data])
(def text-object-path [:editor-v2 :question :text :object])

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
  (fn [db]
    (get-in db text-object-path)
    ))

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
  (fn [{:keys [db]} [_ {:keys [audio start duration data]} {:keys [chunks text]}]]
      {:db       (-> db
                     (assoc-in text-object-path {:chunks chunks :text text})
                     (assoc-in modal-state-path true)
                     (assoc-in data-path data)
                     (assoc-in audio-path audio)
                     (assoc-in bounds-path {:start start :end (+ start duration)}))
       :dispatch [::select-chunk 0]}))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [db]} [_]]
    (let [data (get-in db data-path)
          phrase-action-info (translator-form.actions/current-phrase-action-info db)]
      {:db       (assoc-in db modal-state-path false)
       :dispatch-n (list [::translator-form.scene/update-action
                          (au/node-path->action-path
                            (:path phrase-action-info))  {:data data}]
                         [::translator-form.scene/update-action
                          (au/node-path->action-path (drop-last (:path phrase-action-info)))
                          (get-in db text-object-path)])})))

(re-frame/reg-event-fx
  ::select-chunk
  (fn [{:keys [db]} [_ index]]
    {:db (assoc-in db selected-chunk-path index)}))

(re-frame/reg-event-fx
  ::set-parts
  (fn [{:keys [db]} [_ parts]]
    (let [original (get-in db (concat text-object-path [:text]))
          chunks (text->chunks original parts)]
    {:db (assoc-in db (concat text-object-path [:chunks]) chunks)})))

(re-frame/reg-event-fx
  ::set-text
  (fn [{:keys [db]} [_ text]]
    {:db (assoc-in db (concat text-object-path [:text]) text)}))

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
                       (remove #(nil? (:chunk %)) c)
                       (remove #(= index (:chunk %)) c)
                       (conj c chunk)
                       (sort-by :chunk c))]
      {:db (assoc-in db data-path chunks)})))

(defn- get-styles
  []
  {:audio-container {:padding "16px"}})

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn- text-chunks-form
  []
  (let [text-object-data @(re-frame/subscribe [::text-object])
        selected-chunk @(re-frame/subscribe [::selected-chunk])
        selected-audio @(re-frame/subscribe [::selected-audio])
        parts (chunks->parts (:text text-object-data) (:chunks text-object-data))
        origin-text (get text-object-data :text "")
        set-text #(re-frame/dispatch [::set-text (.. % -target -value)])
        set-parts #(re-frame/dispatch [::set-parts (.. % -target -value)])
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
        [text-chunks {:parts              parts
                      :selected-chunk-idx selected-chunk
                      :on-click           #(re-frame/dispatch [::select-chunk %])}]]

         [ui/grid {:container true
                   :spacing   16
                   :justify   "space-between"}
          [ui/grid {:item true :xs 12}
           [ui/text-field (merge text-input-params
                                 {:label     "Origin"
                                  :value     origin-text
                                  :on-change set-text})]]
          [ui/grid {:item true :xs 12}
           [ui/text-field (merge text-input-params
                                 {:label       "Parts"
                                  :value       (clojure.string/join " " parts)
                                  :on-change   set-parts
                                  :helper-text "Use space to divide text into chunks"})]]]

       ])))

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

(ns webchange.editor-v2.translator.text.chunks
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.text.core :refer [chunks->parts]]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]))

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
    (get objects (keyword target))))

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
      {:url   audio
       :start (bound bounds start)
       :end   (bound bounds end)})))

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

(defn text-chunks-form
  []
  (let [text-object @(re-frame/subscribe [::text-object])
        selected-chunk @(re-frame/subscribe [::selected-chunk])
        selected-audio @(re-frame/subscribe [::selected-audio])
        parts (chunks->parts (:text text-object) (:chunks text-object))]
    [:div
     [ui/grid {:container true
               :spacing   16
               :justify   "space-between"}
      [ui/grid {:item true :xs 12}
       [ui/card-content
        [audio-wave-form (merge selected-audio
                                {:height         96
                                 :on-change      #(re-frame/dispatch [::select-audio %])
                                 :show-controls? true})]]]
      [ui/grid {:item true :xs 8}
       (for [[index part] (map-indexed vector parts)]
         [ui/chip {:key      index
                   :label    part
                   :color    (if (= index selected-chunk) "primary" "secondary")
                   :on-click #(re-frame/dispatch [::select-chunk index])}])]]]))

(defn text-chunks-modal
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        cancel #(re-frame/dispatch [::cancel])
        apply #(re-frame/dispatch [::apply])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   cancel
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title
        "Configure text"]
       [ui/dialog-content {:class-name "translation-form"}
        [text-chunks-form]]
       [ui/dialog-actions
        [ui/button {:on-click cancel}
         "Cancel"]
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click apply}
          "Apply"]]]])))

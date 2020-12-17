(ns webchange.editor-v2.sandbox.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.sandbox.parse-actions :refer [find-all-actions]]
    [webchange.editor-v2.translator.text.core :refer [parts->chunks chunks->parts]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.subs :as subs]))

(def modal-state-path [:editor-v2 :sandbox :share-modal-state])
(def modal-share-link-state [:editor-v2 :sandbox :share-link-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::lesson-set-data
  (fn [db]
    (get-in db modal-share-link-state {})))

(re-frame/reg-sub
  ::lesson-sets
  (fn [db]
    (let [scene-data (subs/current-scene-data db)]
      (->> (find-all-actions scene-data {:type ["lesson-var-provider"]})
           (map second)
           (map :from)))))

(re-frame/reg-sub
  ::link
  (fn [db]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)
          lessons @(re-frame/subscribe [::lesson-set-data]) ;{:concepts {:item-ids [188 196 208], :dataset-id 4}}
          encoded-lessons (-> lessons clj->js js/JSON.stringify js/btoa js/encodeURIComponent)]
      (if (seq lessons)
        (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug "/" encoded-lessons)
        (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug)))))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in modal-state-path true)
             (assoc-in modal-share-link-state {}))}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-event-fx
  ::set-lesson-set-data
  (fn [{:keys [db]} [_ lesson-set-name dataset-id item-ids]]
    {:db (cond-> (assoc-in db (concat modal-share-link-state [lesson-set-name]) {})
                 (some? dataset-id) (assoc-in (concat modal-share-link-state [lesson-set-name :dataset-id]) dataset-id)
                 (some? item-ids) (assoc-in (concat modal-share-link-state [lesson-set-name :item-ids]) item-ids))}))

;;

(def text-input-params {:variant    "outlined"
                        :margin     "normal"
                        :multiline  true
                        :full-width true})

(defn- select-lesson-set-form
  [{:keys [name]}]
  (let [lesson-set-data (-> @(re-frame/subscribe [::lesson-set-data])
                            (get name))
        current-dataset-id (get lesson-set-data :dataset-id "")
        current-dataset-items (get lesson-set-data :item-ids [])

        datasets @(re-frame/subscribe [::editor-subs/course-datasets])
        dataset-items (->> @(re-frame/subscribe [::editor-subs/course-dataset-items])
                           (map second)
                           (filter (fn [{:keys [dataset-id]}] (= dataset-id current-dataset-id))))]
    [ui/grid {:container true
              :spacing   16}
     [ui/grid {:item  true :xs 4
               :style {:display     "flex"
                       :align-items "flex-end"}}
      [ui/typography {:variant "subtitle1"
                      :style   {:padding "10px"}} name]]
     [ui/grid {:item true :xs 3}
      [ui/form-control {:full-width true}
       [ui/input-label "Dataset"]
       [ui/select {:value     current-dataset-id
                   :variant   "outlined"
                   :on-change #(re-frame/dispatch [::set-lesson-set-data name (.. % -target -value)])}
        (for [{:keys [id name]} datasets]
          ^{:key id}
          [ui/menu-item {:value id} name])]]]
     [ui/grid {:item true :xs 5}
      [ui/form-control {:full-width true}
       [ui/input-label "Items"]
       [ui/select {:value     current-dataset-items
                   :variant   "outlined"
                   :on-change #(re-frame/dispatch [::set-lesson-set-data name current-dataset-id (js->clj (.. % -target -value))])
                   :multiple  true}
        (for [{:keys [id name]} dataset-items]
          ^{:key id}
          [ui/menu-item {:value id} name])]]]]))

(defn- select-lesson-sets-form
  []
  (let [lesson-sets (->> @(re-frame/subscribe [::lesson-sets])
                         (map keyword))]
    [ui/grid {:container true}
     (for [lesson-set-name lesson-sets]
       ^{:key lesson-set-name}
       [ui/grid {:item true :xs 12}
        [select-lesson-set-form {:name lesson-set-name}]])]))

(defn share-form
  []
  (let [link @(re-frame/subscribe [::link])]
    [:div
     [ui/grid {:container true
               :spacing   16
               :justify   "space-between"}
      [ui/grid {:item true :xs 12}
       [select-lesson-sets-form]]
      [ui/grid {:item true :xs 8}
       [ui/text-field (merge text-input-params
                             {:id    "share-link-text-input"
                              :label "Link"
                              :value link})]]
      [ui/grid {:item true :xs 4}
       [ui/button {:on-click #(let [text-field (js/document.getElementById "share-link-text-input")]
                                (.focus text-field)
                                (.select text-field)
                                (js/document.execCommand "copy"))
                   :style    {:margin-top "19px"}}
        [ic/content-copy {:style {:margin "6px"}}] "Copy link"]]]]))

(defn share-modal
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        close #(re-frame/dispatch [::close])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   close
        :full-width true}
       [ui/dialog-title
        "Share"]
       [ui/dialog-content {:class-name "share-form"}
        [share-form]]
       [ui/dialog-actions
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click close}
          "Close"]]]])))

(defn share-button
  []
  (let [handle-click (fn [] (re-frame/dispatch [::open]))]
    [ui/form-control {:full-width true
                      :margin     "normal"}
     [ui/button
      {:on-click handle-click}
      "Share"]
     [share-modal]]))

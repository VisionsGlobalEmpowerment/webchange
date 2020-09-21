(ns webchange.editor-v2.sandbox.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.text.core :refer [parts->chunks chunks->parts]]))

(def modal-state-path [:editor-v2 :sandbox :share-modal-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

;{:concepts {:item-ids [188 196 208], :dataset-id 4}}

(re-frame/reg-sub
  ::link
  (fn [db]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)
          encoded-lessons (-> {} clj->js js/JSON.stringify js/btoa js/encodeURIComponent)]
      (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug "/" encoded-lessons))))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path false)}))

(def text-input-params {:variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn share-form
  []
  (let [link @(re-frame/subscribe [::link])]
    [:div
     [ui/grid {:container true
               :spacing   16
               :justify   "space-between"}
      [ui/grid {:item true :xs 8}
       [ui/text-field (merge text-input-params
                             {:id "share-link-text-input"
                              :label "Link"
                              :value link})]]
      [ui/grid {:item true}
       [ui/button [ic/content-copy {:on-click #(let [text-field (js/document.getElementById "share-link-text-input")]
                                                 (.focus text-field)
                                                 (.select text-field)
                                                 (js/document.execCommand "copy"))}]]]]]))

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

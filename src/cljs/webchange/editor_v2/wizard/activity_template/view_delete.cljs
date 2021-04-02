(ns webchange.editor-v2.wizard.activity-template.view_delete
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    ;[webchange.editor-v2.wizard.activity-template.views-audio :as views-audio]
    [webchange.interpreter.renderer.state.editor :as editor]))
;
;(def modal-state-path [:editor-v2 :sandbox :background-music-modal-state])
;(def modal-share-link-state [:editor-v2 :sandbox :background-music-link-state])

;;; Subs
;
;(re-frame/reg-sub
;  ::modal-state
;  (fn [db]
;    (-> db
;        (get-in modal-state-path)
;        boolean)))
;
;;; Events
;
;(re-frame/reg-event-fx
;  ::open
;  (fn [{:keys [db]} [_]]
;    {:db (-> db
;             (assoc-in modal-state-path true)
;             (assoc-in modal-share-link-state {}))}))
;
;(re-frame/reg-event-fx
;  ::close
;  (fn [{:keys [db]} [_]]
;    {:db (assoc-in db modal-state-path false)}))
;
;(defn- background-music-form
;  [form-data]
;  [views-audio/audio-option {:key    :background-music
;                             :option {:type  "audio",
;                                      :label "Upload file", :options {}}
;                             :data   form-data}])
;
;(defn- background-music-modal
;  []
;  (let [form-data (r/atom {})
;        open? @(re-frame/subscribe [::modal-state])
;        close #(re-frame/dispatch [::close])
;        save #(re-frame/dispatch [::state/save :background-music @form-data [::close]])]
;    (when open?
;      [ui/dialog
;       {:open       true
;        :on-close   close
;        :full-width true}
;       [ui/dialog-title "Background music"]
;       [ui/dialog-content {:class-name "share-form"}
;        [background-music-form form-data]]
;       [ui/dialog-actions
;        [ui/button {:on-click close}
;         "Cancel"]
;        [:div {:style {:position "relative"}}
;         [ui/button {:color    "secondary"
;                     :variant  "contained"
;                     :on-click save}
;          "Save"]]]
;       ])))


(defn delete-object-option
  [{:keys [key option data validator]}]
  (let [current-scene-objects @(re-frame/subscribe [::editor/selected-object])
        ]
    [:div
     (if (not current-scene-objects)
       [:span "You should select object to delete, on scene preview screen"]
       (do
         (swap! data assoc :object current-scene-objects)
         [:span "Are you sure you want to delete ball?"]))]))

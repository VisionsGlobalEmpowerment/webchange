(ns webchange.editor-v2.layout.components.common-actions.background-music
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.views-audio :as views-audio]
    [webchange.subs :as subs]
    [webchange.editor-v2.dialog.dialog-form.views-volume :as views-volume]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block-button]]
    [webchange.ui-framework.components.index :refer [icon range-input]]
    [webchange.editor-v2.layout.components.common_actions.state :as state]))

(def modal-state-path [:editor-v2 :sandbox :background-music-modal-state])

(def modal-remove-state-path [:editor-v2 :sandbox :background-music-remove-modal-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::modal-remove-state
  (fn [db]
    (-> db
        (get-in modal-remove-state-path)
        boolean)))


;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in modal-state-path true))}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-event-fx
  ::open-remove
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in modal-remove-state-path true))}))

(re-frame/reg-event-fx
  ::close-remove
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-remove-state-path false)}))


(defn- background-music-form
  [form-data]
  [:div
   [:div [views-audio/audio-option {:key    :background-music
                                    :option {:type  "audio",
                                             :label "Upload file", :options {}}
                                    :data   form-data}]]
   (let [current-value (r/atom (get-in @form-data [:background-music :volume] 1))
         src (get-in @form-data [:background-music :src])
         action {:type "sequence-data"
                 :data [{:type   "audio"
                         :id     src
                         :volume (get-in @form-data [:background-music :volume])
                         }]
                 }
         ]
     [:div
      [:div (play-phrase-block-button action [src] src {:hide? false})]
      [:div [views-volume/volume-view {:value     current-value
                                       :on-change #(swap! form-data assoc-in [:background-music :volume] %)}]]
      [:span "To apply new volume and file to preview button, please stop and start playback"]]

     )
   ])

(defn- background-music-modal
  []
  (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
        action-name (get-in scene-data [:triggers :music :action])
        volume (get-in scene-data [:actions (keyword action-name) :volume])
        src (get-in scene-data [:actions (keyword action-name) :id])
        open? @(re-frame/subscribe [::modal-state])
        close #(re-frame/dispatch [::close])]
    (r/with-let [form-data (r/atom {:background-music
                                    {:volume volume
                                     :src    src}})
                 ]
                (let [save #(re-frame/dispatch [::state/save :background-music @form-data [::close]])]
                  (println volume src @form-data)
                  (when open?
                    [ui/dialog
                     {:open       true
                      :on-close   close
                      :full-width true}
                     [ui/dialog-title "Background music"]
                     [ui/dialog-content {:class-name "share-form"}
                      [background-music-form form-data]]
                     [ui/dialog-actions
                      [ui/button {:on-click close}
                       "Cancel"]
                      [:div {:style {:position "relative"}}
                       [ui/button {:color    "secondary"
                                   :variant  "contained"
                                   :on-click save}
                        "Save"]]]
                     ])))))

(defn- background-remove-music-modal
  []
  (let [form-data (r/atom {})
        open? @(re-frame/subscribe [::modal-remove-state])
        close #(re-frame/dispatch [::close-remove])
        save #(do
                (re-frame/dispatch [::state/save :background-music-remove @form-data [::close]])
                (re-frame/dispatch [::close-remove]))]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   close
        :full-width true}
       [ui/dialog-title "Are you sure you want remove background music?"]
       [ui/dialog-actions
        [ui/button {:on-click close}
         "Cancel"]
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click save}
          "Save"]]]
       ])))

(defn background-music
  []
  (let [handle-click (fn [] (re-frame/dispatch [::open]))]
    [ui/form-control {:full-width true
                      :margin     "normal"}
     [ui/button
      {:on-click handle-click}
      "Background music"]
     [background-music-modal]]))


(defn remove-background-music
  []
  (let [handle-click-remove (fn [] (re-frame/dispatch [::open-remove]))]
    [ui/form-control {:full-width true
                      :margin     "normal"}
     [ui/button
      {:on-click handle-click-remove}
      "Remove background music"]
     [background-remove-music-modal]
     ]))
(ns webchange.editor-v2.activity-form.generic.components.background-music.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.generic.components.background-music.state :as state]
    [webchange.subs :as subs]
    [webchange.ui-framework.components.index :refer [audio button card dialog file label range-input]]))

(def default-volume 0.5)
(def modal-state-path [:editor-v2 :sandbox :background-music-modal-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
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

(defn- music-option
  [{:keys [name url on-click]}]
  (let [handle-click #(on-click {:name name :url url})]
    [card {:class-name "music-option"
           :on-click   handle-click}
     [audio {:url url}]
     [:div.name (or name url)]]))

(defn- music-options-list
  [{:keys [on-change]}]
  (let [music-options @(re-frame/subscribe [::state/music-options])]
    [:div.music-options-list
     [label "Available options:"]
     (for [{:keys [url] :as option} music-options]
       ^{:key url}
       [music-option (merge option
                            {:on-click on-change})])]))

(defn- upload-music
  [{:keys [on-change]}]
  (let [handle-change (fn [url] (on-change {:url  url
                                            :name "Uploaded audio"}))]
    [:div
     [label "Upload music:"]
     [card {:class-name "upload-form-wrapper"}
      [file {:type            "audio"
             :on-change       handle-change
             :show-file-name? false
             :show-input?     false
             :class-name      "upload-form"
             :button-text     "Choose File"}]]]))

(defn- current-audio
  [{:keys [src volume on-volume-change]}]
  (into [:div.current-value]
        (cond-> [[label "Current music:"]]
                (nil? src) (concat [[:div.undefined-src "Music is not selected"]])
                (some? src) (concat [[audio {:url    src
                                             :volume volume}]
                                     [range-input {:value     volume
                                                   :min       0
                                                   :max       1
                                                   :step      0.01
                                                   :on-change on-volume-change}]]))))

(defn- background-music-form
  [form-data]
  (let [audio-src (get-in @form-data [:background-music :src])
        audio-volume (get-in @form-data [:background-music :volume])
        handle-change-src (fn [{:keys [url]}]
                            (swap! form-data assoc-in [:background-music :src] url))
        handle-change-volume (fn [value]
                               (swap! form-data assoc-in [:background-music :volume] value))]
    [:div.background-music-form
     [current-audio {:src              audio-src
                     :volume           audio-volume
                     :on-volume-change handle-change-volume}]
     [upload-music {:on-change handle-change-src}]
     [music-options-list {:on-change handle-change-src}]]))

(defn open-set-music-window
  []
  (re-frame/dispatch [::open]))

(defn set-music-window
  []
  (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
        action-name (get-in scene-data [:triggers :music :action])
        volume (get-in scene-data [:actions (keyword action-name) :volume] default-volume)
        src (get-in scene-data [:actions (keyword action-name) :id])
        open? @(re-frame/subscribe [::modal-state])
        handle-close #(re-frame/dispatch [::close])]
    (r/with-let [form-data (r/atom {:background-music
                                    {:volume volume
                                     :src    src}})]
      (let [handle-save #(re-frame/dispatch [::state/save :background-music @form-data [::close]])]
        [dialog {:open?    open?
                 :on-close handle-close
                 :title    "Background music !!"
                 :actions  [[button {:on-click handle-save
                                     :size     "big"}
                             "Save"]
                            [button {:on-click handle-close
                                     :variant  "outlined"
                                     :color    "default"
                                     :size     "big"}
                             "Cancel"]]}
         [background-music-form form-data]]))))

(defn remove-background-music
  []
  (re-frame/dispatch [::state/save :background-music-remove {}]))

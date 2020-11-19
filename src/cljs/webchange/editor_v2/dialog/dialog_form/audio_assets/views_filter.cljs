(ns webchange.editor-v2.dialog.dialog-form.audio-assets.views-filter
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :as db]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]))

(defn path-to-db
  [path]
  (->> path
       (concat [:audios-filter])
       (db/path-to-db)))

(re-frame/reg-sub
  ::current-audios-filter
  (fn [db]
    (get-in db (path-to-db [:current-filter]))))

(re-frame/reg-event-fx
  ::set-current-audios-filter
  (fn [{:keys [db]} [_ filter-value]]
    {:db (assoc-in db (path-to-db [:current-filter]) filter-value)}))

(defn- get-styles
  []
  {:wrapper {:style {:margin-bottom "15px"}}
   :title   {:display "inline-block"
             :margin  "5px 0"}})

(defn audios-filter
  []
  (let [empty-target-option {:text "Any" :value ""}
        handle-target-changed (fn [target] (re-frame/dispatch [::set-current-audios-filter {:target target}]))
        styles (get-styles)]
    [:div (:wrapper styles)
     [ui/typography {:variant "h6"
                     :style   (:title styles)}
      "Filter audios:"]
     [audio-target-selector {:default-value (:value empty-target-option)
                             :extra-options [empty-target-option]
                             :on-change     handle-target-changed}]]))
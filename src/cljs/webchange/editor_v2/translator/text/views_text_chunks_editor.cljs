(ns webchange.editor-v2.translator.text.views-text-chunks-editor
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.text.views-chunks-editor-form :refer [chunks-editor-form]]))

(def modal-state-path [:editor-v2 :translator :text :configuration-modal-state])
(def confirm-modal-state-path [:editor-v2 :translator :text :confirm-modal-state])
(def current-text-info-path [:editor-v2 :translator :text :current-text-info])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::confirm-modal-state
  (fn [db]
    (-> db
        (get-in confirm-modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::current-dialog-text-info
  (fn [db]
    (get-in db current-text-info-path)))

(re-frame/reg-sub
  ::current-dialog-text
  (fn []
    [(re-frame/subscribe [::current-dialog-text-info])
     (re-frame/subscribe [::translator-form.scene/objects-data])])
  (fn [[{:keys [path]} objects]]
    (get-in objects path)))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::translator-form/init-state]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path false)
     :dispatch [::translator-form/reset-state]}))

(re-frame/reg-event-fx
  ::save
  (fn [_ _]
    {:dispatch-n (list [::translator-form/save-changes] [::close])}))

(re-frame/reg-event-fx
  ::set-current-dialog-text
  (fn [{:keys [db]} [_ text-object-info]]
    {:db (assoc-in db current-text-info-path text-object-info)}))

(re-frame/reg-event-fx
  ::update-text-data
  (fn [{:keys [db]} [_ text-data-patch]]
    (let [{path :path} (get-in db current-text-info-path)]
      {:dispatch [::translator-form.scene/update-object path text-data-patch]})))

(defn- configuration-form-container
  []
  (let [text-data @(re-frame/subscribe [::current-dialog-text])]
    [chunks-editor-form (merge (select-keys text-data [:text :chunks])
                               {:on-change (fn [data-patch]
                                             (re-frame/dispatch [::update-text-data data-patch]))})]))

(defn configuration-modal
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        save #(re-frame/dispatch [::save])
        close #(re-frame/dispatch [::close])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   close
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title
        "Edit text chunks"]
       [ui/dialog-content {:class-name "translation-form"}
        [configuration-form-container]]
       [ui/dialog-actions
        [ui/button {:on-click close}
         "Cancel"]
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click save}
          "Save"]]]])))

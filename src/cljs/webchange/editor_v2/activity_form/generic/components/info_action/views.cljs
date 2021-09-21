(ns webchange.editor-v2.activity-form.generic.components.info-action.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
   [webchange.ui-framework.components.index :refer [dialog]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-dashboard.views-course-info :refer [course-info]]))

(def modal-state-path [:editor-v2 :info-action-modal :state])

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

(defn open-info-window
  []
  (re-frame/dispatch [::open]))

(defn info-window
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        close #(re-frame/dispatch [::close])]
    (when open?
      ;; [ui/dialog
      ;;  {:open       true
      ;;   :on-close   close
      ;;   :full-width true}
      ;;  [ui/dialog-title "Info"]
      ;;  [ui/dialog-content {:class-name "share-form"}
      ;;   [course-info {:title "Choose Your Topic"}]]
      ;;  [ui/dialog-actions
      ;;   [ui/button {:on-click close}
      ;;    "Cancel"]]]
      
      [dialog {:open?    open?
               :on-close close
               :title    "Info"}
       [course-info {:title "Choose Your Topic"}]]
      )))
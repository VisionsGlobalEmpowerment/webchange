(ns webchange.editor-v2.activity-form.generic.components.add-image.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.views-image :as views-image]
    [webchange.editor-v2.wizard.activity-template.views-string :as views-string]
    [webchange.ui-framework.components.index :refer [icon range-input]]
    [webchange.editor-v2.activity-form.generic.components.add-image.state :as state]))

(def modal-state-path [:editor-v2 :add-image-modal :state])

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

(defn open-add-image-window
  []
  (re-frame/dispatch [::open]))

(defn add-image-window
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        close #(re-frame/dispatch [::close])]
    (r/with-let [form-data (r/atom {})]
      (let [save #(re-frame/dispatch [::state/save :add-image @form-data [::close]])]
        (when open?
          [ui/dialog
           {:open       true
            :on-close   close
            :full-width true}
           [ui/dialog-title "Add image"]
           [ui/dialog-content {:class-name "share-form"}
            [views-string/string-option {:key    :name
                                         :option {:description "Your image name"
                                                  :placeholder "Image name"}
                                         :data   form-data}]
            [views-image/image-option {:key    :image
                                       :option {:type  "image",
                                                :label "Upload file", :options {}}
                                       :data   form-data}]]
           [ui/dialog-actions
            [ui/button {:on-click close}
             "Cancel"]
            [:div {:style {:position "relative"}}
             [ui/button {:color    "secondary"
                         :variant  "contained"
                         :on-click save}
              "Save"]]]])))))

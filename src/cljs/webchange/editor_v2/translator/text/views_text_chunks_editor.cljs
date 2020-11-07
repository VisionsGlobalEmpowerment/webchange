(ns webchange.editor-v2.translator.text.views-text-chunks-editor
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.translator.text.views-text-chunks :refer [text-chunks]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.text.core :refer [parts->chunks chunks->parts]]))

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
  ::set-text
  (fn [{:keys [db]} [_ text]]
    (let [{path :path} (get-in db current-text-info-path)]
      (let [parts (clojure.string/split text #" ")
            chunks (parts->chunks text parts)]
        {:dispatch [::translator-form.scene/update-object path {:text text :chunks chunks}]}))))

(re-frame/reg-event-fx
  ::set-parts
  (fn [{:keys [db]} [_ str-value]]
    (let [{path :path} (get-in db current-text-info-path)
          original (-> db (translator-form.scene/objects-data) (get-in path) :text)
          original-stripped (clojure.string/replace original #" " "")
          value-stripped (clojure.string/replace str-value #" " "")]
      (when (= original-stripped value-stripped)
        (let [parts (clojure.string/split str-value #" ")
              chunks (parts->chunks original parts)]
          {:dispatch [::translator-form.scene/update-object path {:chunks chunks}]})))))

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn configuration-form
  []
  (let [current-dialog-text @(re-frame/subscribe [::current-dialog-text])
        origin-text (get current-dialog-text :text "")
        parts (chunks->parts origin-text (:chunks current-dialog-text))
        set-text #(re-frame/dispatch [::set-text (.. % -target -value)])
        set-parts #(re-frame/dispatch [::set-parts (.. % -target -value)])]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 12}
      [ui/text-field (merge text-input-params
                            {:label     "Origin"
                             :value     origin-text
                             :on-change set-text})]]
     [ui/grid {:item true :xs 12}
      [text-chunks {:parts parts}]]
     [ui/grid {:item true :xs 12}
      [ui/text-field (merge text-input-params
                            {:label       "Parts"
                             :value       (clojure.string/join " " parts)
                             :on-change   set-parts
                             :helper-text "Use space to divide text into chunks"})]]]))

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
        [configuration-form]]
       [ui/dialog-actions
        [ui/button {:on-click close}
         "Cancel"]
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click save}
          "Save"]]]])))

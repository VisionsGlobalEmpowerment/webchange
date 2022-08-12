(ns webchange.lesson-builder.tools.question-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.blocks.menu.state :as menu]
    [webchange.lesson-builder.blocks.stage.second-stage.state :as second-stage]
    [webchange.question.preview :refer [get-scene-data]]
    [webchange.utils.scene-data :refer [get-scene-background]]))

(def path-to-db :lesson-builder/question-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; form data

(def form-data-key :form-data)

(defn- get-form-data
  [db]
  (get db form-data-key))

(defn- set-form-data
  [db value]
  (assoc db form-data-key value))

(defn update-form-data
  [db key data-patch]
  (if (map? data-patch)
    (update-in db [form-data-key key] merge data-patch)
    (assoc-in db [form-data-key key] data-patch)))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

;; question-type

(def question-type-key :question-type)

(re-frame/reg-sub
  ::question-type
  :<- [::form-data]
  #(get % question-type-key))

(re-frame/reg-sub
  ::question-type-options
  (fn []
    [{:text  "Multiple choice image"
      :value "multiple-choice-image"}
     {:text  "Multiple choice text"
      :value "multiple-choice-text"}
     {:text  "Thumbs up & thumbs down"
      :value "thumbs-up-n-down"}]))

(re-frame/reg-event-fx
  ::set-question-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (update-form-data db question-type-key value)}))

;; current object

(def current-object-key :current-object)

(defn- get-current-object
  [db]
  (get db current-object-key))

(defn- set-current-object
  [db value]
  (assoc db current-object-key value))

(re-frame/reg-sub
  ::current-object
  :<- [path-to-db]
  get-current-object)

;; events

(re-frame/reg-event-fx
  ::init-state
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ {:keys [question-id]}]]
    (editor-state/register-select-object-handler "second" [::show-object-form])
    (let [question-params (get-in activity-data [:objects (keyword question-id) :metadata :params])]
      {:db       (set-form-data db question-params)
       :dispatch [::second-stage/init ::activity-data]})))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [db]} [_]]
    {:db         (dissoc db path-to-db)
     :dispatch-n [[::second-stage/reset]]}))

(re-frame/reg-event-fx
  ::show-object-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ option-object-name]]
    {:db       (set-current-object db option-object-name)
     :dispatch [::menu/open-component :question-option]}))

;; scene data

(re-frame/reg-sub
  ::activity-data
  :<- [::state/activity-data]
  :<- [::form-data]
  (fn [[activity-data form-data]]
    (let [[_ current-background] (get-scene-background activity-data)]
      (get-scene-data form-data {:background current-background}))))

;; 

;{:ok-image        {:src        /images/questions/ok.png
;                   :image-size contain}
; :task-text       {:text Question placeholder
;                   :font-size 64}
; :option-4-text   {:text Option 4
;                   :font-size 48}
; :option-4-image  {:src        /images/questions/option4.png
;                   :image-size contain}
; :option-3-image  {:src        /images/questions/option3.png
;                   :image-size contain}
; :ok-text         {:text      Ok
;                   :font-size 48}
; :option-2-text   {:text Option 2
;                   :font-size 48}
; :thumbs-up-image {:src        /images/questions/thumbs_up.png
;                   :image-size contain}
; :answers-number  one
; :layout          vertical
; :option-1-text   {:text Option 1
;                   :font-size 48}
; :option-1-image  {:src        /images/questions/option1.png
;                   :image-size contain}
; :option-2-image  {:src        /images/questions/option2.png
;                   :image-size contain}
; :thumbs-up-text  {:text Thumbs Up
;                   :font-size 48}
; :task-type       text
; :question-type   multiple-choice-image
; :alias           Question 1 - ball
; :option-1-value option-1
; :option-3-text {:text Option 3
;                 :font-size 48}
; :thumbs-down-image {:src        /images/questions/thumbs_down.png
;                     :image-size contain}
; :thumbs-down-value thumbs-down
; :task-image {:src        /images/questions/question.png
;              :image-size contain}
; :options-number 3
; :mark-options [thumbs-up thumbs-down]
; :option-2-value option-2
; :option-4-value option-4
; :correct-answers [2]
; :version 2
; :thumbs-down-text {:text Thumbs Down
;                    :font-size 48}
; :ok-value ok
; :thumbs-up-value thumbs-up
; :option-3-value option-3}

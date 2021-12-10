(ns webchange.editor-v2.activity-form.common.object-form.image-form.state
  (:require
   [re-frame.core :as re-frame]
   [webchange.state.state :as core-state]
   [webchange.interpreter.renderer.state.scene :as state-renderer]
   [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:image-form])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [image-data (merge {:scale {:x 1 :y 1}}
                            (select-keys objects-data [:image-size :src :scale :visible]))
          image-tags (get-in objects-data [:editable? :image-tags])
          edit-tags (get-in objects-data [:editable? :edit-tags])
          form-params (get-in objects-data [:editable? :edit-form])
          upload-options (select-keys objects-data [:max-width :max-height :min-width :min-height])]
      {:dispatch-n [[::state/init id {:data        image-data
                                      :names       objects-names
                                      :form-params form-params}]
                    [::set-image-tags id image-tags]
                    [::set-edit-tags id edit-tags]
                    [::set-upload-options id upload-options]]})))

(re-frame/reg-sub
  ::show-select-image-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :select-image]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-upload-image-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :upload-image]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-apply-to-all-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :apply-to-all]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-visible-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :visible]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-image-fit-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :image-fit]))
  (fn [show-control?]
    show-control?))

;; Upload options

(def upload-options-path :upload-options)

(re-frame/reg-event-fx
  ::set-upload-options
  (fn [{:keys [db]} [_ id options]]
    {:db (assoc-in db (path-to-db id [upload-options-path]) options)}))

(re-frame/reg-sub
  ::upload-options
  (fn [db [_ id]]
    (get-in db (path-to-db id [upload-options-path]))))

(re-frame/reg-event-fx
  ::upload-start
  (fn [{:keys [_]} [_ id]]
    {:dispatch [::state/set-loading-status id :loading]}))

(re-frame/reg-event-fx
  ::upload-finish
  (fn [{:keys [_]} [_ id]]
    {:dispatch [::state/set-loading-status id :loaded]}))

;; Image Src

(re-frame/reg-sub
  ::image-src
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :src)))

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [db]} [_ id src]]
    (let [form-destroyed? (state/form-destroyed? db id)]
      (when-not form-destroyed?
        {:dispatch [::state/update-current-data id {:src src}]}))))

;; Image Tag

(def image-tags-path :image-tag)

(re-frame/reg-event-fx
  ::set-image-tags
  (fn [{:keys [db]} [_ id tags]]
    {:db (assoc-in db (path-to-db id [image-tags-path]) tags)}))

(re-frame/reg-sub
  ::image-tags
  (fn [db [_ id]]
    (get-in db (path-to-db id [image-tags-path]))))

;; Apply to all
(def edit-tag-path :edit-tag)

(re-frame/reg-event-fx
  ::set-edit-tags
  (fn [{:keys [db]} [_ id tags]]
    {:db (assoc-in db (path-to-db id [edit-tag-path]) tags)}))

(re-frame/reg-sub
  ::edit-tags
  (fn [db [_ id]]
    (get-in db (path-to-db id [edit-tag-path]))))

(defn- get-object-keys-by-tag
  "Return object names for each object in scene with given tag"
  [db tag]
  (let [objects (core-state/get-objects-data db)]
    (->> objects
         (filter (fn [[object-name object]]
                   (some #{tag} (get-in object [:editable? :edit-tags]))))
         (map first))))

(re-frame/reg-event-fx
  ::apply-to-all
  (fn [{:keys [db]} [_ id tags]]
    (let [current-data (state/get-current-data db id)
          object-keys (get-object-keys-by-tag db (first tags))
          patches-list (map (fn [object-key]
                              {:object-name       object-key
                               :object-data-patch current-data})
                            object-keys)
          render-events (map (fn [object-key]
                               [::state-renderer/set-scene-object-state object-key current-data])
                             object-keys)]

      {:dispatch-n (conj render-events 
                         [::core-state/update-scene-objects {:patches-list patches-list}])})))

;; Hide image

(re-frame/reg-sub
  ::current-visible
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :visible true)))

(re-frame/reg-event-fx
  ::set-visible
  (fn [{:keys [db]} [_ id visible?]]
    {:dispatch [::state/update-current-data id {:visible visible?}]}))

;; Image fit

(re-frame/reg-sub
  ::current-image-fit
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :image-size)))

(re-frame/reg-event-fx
  ::set-image-fit
  (fn [{:keys [_]} [_ id value]]
    {:dispatch [::state/update-current-data id {:image-size value}]}))

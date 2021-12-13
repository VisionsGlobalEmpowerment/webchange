(ns webchange.templates.library.categorize.templates.round-1
  (:require
    [webchange.templates.library.categorize.templates.common :refer [get-draggable-item]]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.utils.list :refer [distinct-by-key]]))

(def params-object-names [:say-item :target :correct-drop :box])
(def var-object-names [])
(def var-action-names [])
(def all-vars-in-actions ["clear-target-vars"])

(defn- init-tracks-metadata
  [template {:keys [tracks]}]
  (assoc-in template [:metadata :tracks] [{:title (get-in tracks [:generic :title] "Round 1")
                                           :nodes []}
                                          {:title (get-in tracks [:items :title] "Round 1 - items")
                                           :nodes []}]))

(defn- add-background
  [template {:keys [background]}]
  (let [{:keys [src]} background
        asset {:url src :size 10 :type "image"}
        object-name "background"
        object-data {:type "background" :src src}]
    (-> template
        (update :assets conj asset)
        (update :objects assoc (keyword object-name) object-data)
        (update :scene-objects conj [object-name]))))

(defn- get-box-name
  [name]
  (str "box-" name))

(defn- add-boxes
  [template {:keys [boxes]}]
  (->> boxes
       (reduce (fn [template {:keys [name position src]}]
                 (let [asset {:url src :size 10 :type "image"}
                       object-name (get-box-name name)
                       object-data (merge position
                                          {:type       "image"
                                           :src        src
                                           :transition object-name})]
                   (-> template
                       (update :assets conj asset)
                       (update :objects assoc (keyword object-name) object-data)
                       (update :scene-objects conj [object-name])
                       (update-in [:actions :unhighlight-all :data] conj {:target     object-name
                                                                          :type       "set-attribute"
                                                                          :attr-name  "highlight"
                                                                          :attr-value false}))))
               template)))

(defn- add-items
  [template props]
  (let [add-assets (fn [template {:keys [items]}]
                     (let [sources (->> items (map :src) (distinct))
                           assets-data (->> sources
                                            (map (fn [src]
                                                   {:url src :size 10 :type "image"})))]
                       (update template :assets concat assets-data)))
        add-objects (fn [template {:keys [items]}]
                      (->> items
                           (map-indexed vector)
                           (reduce (fn [template [idx {:keys [target position src pick-dialog correct-dialog]}]]
                                     (let [object-name (str "item-" idx)
                                           object-data (get-draggable-item (cond-> {:position position
                                                                                    :src      src
                                                                                    :target   object-name
                                                                                    :test     [(str "#^" (get-box-name "") ".*")]
                                                                                    :say-item (:name pick-dialog)
                                                                                    :box      (get-box-name target)}
                                                                                   (some? correct-dialog) (assoc :say-correct (:name correct-dialog))))]
                                       (-> template
                                           (update :objects assoc (keyword object-name) object-data)
                                           (update :scene-objects conj [object-name]))))
                                   template)))]
    (-> template
        (add-assets props)
        (add-objects props))))

(defn- add-items-dialogs
  [template {:keys [items]}]
  (->> items
       (distinct-by-key :target)
       (reduce (fn [template {:keys [pick-dialog correct-dialog]}]
                 (let [pick-dialog-name (-> pick-dialog :name keyword)
                       pick-dialog-data (-> (:phrase pick-dialog)
                                            (dialog/default)
                                            (assoc :unique-tag "item"))
                       pick-dialog-track-item-data {:type      "dialog"
                                                    :action-id pick-dialog-name}

                       correct-dialog-name (-> correct-dialog :name keyword)
                       correct-dialog-data (-> (:phrase correct-dialog)
                                               (dialog/default)
                                               (assoc :unique-tag "item"))
                       correct-dialog-track-item-data {:type      "dialog"
                                                       :action-id correct-dialog-name}]
                   (cond-> template
                           (some? pick-dialog) (-> (update :actions assoc pick-dialog-name pick-dialog-data)
                                                   (update-in [:metadata :tracks 1 :nodes] conj pick-dialog-track-item-data))
                           (some? correct-dialog) (-> (update :actions assoc correct-dialog-name correct-dialog-data)
                                                      (update-in [:metadata :tracks 1 :nodes] conj correct-dialog-track-item-data)))))
               template)))

(defn- add-generic-dialogs
  [template {:keys [generic-dialogs]}]
  (reduce (fn [template [_ {:keys [name prompt phrase phrase-description]}]]
            (let [dialog-name (keyword name)
                  dialog-data (dialog/default phrase {:phrase-description phrase-description})

                  track-item-prompt {:type "prompt" :text prompt}
                  track-item-data {:type "dialog" :action-id dialog-name}]
              (-> template
                  (update :actions assoc dialog-name dialog-data)
                  (update-in [:metadata :tracks 0 :nodes] conj track-item-prompt)
                  (update-in [:metadata :tracks 0 :nodes] conj track-item-data))))
          template
          generic-dialogs))

(defn- items-have-correct-dialog?
  [{:keys [items]}]
  (some (fn [item]
          (contains? item :correct-dialog))
        items))

(defn get-template
  "Create Round 1.
   Properties description:
   - boxes - list of boxes descriptions:
       - name - unique box name, used to define item's target
       - position - where box has to be placed, {:x :y}
       - src - box image src
   - items - items of boxes descriptions:
       - target - target box name
       - position - where item has to be placed, {:x :y}
       - src - item image src
       - pick-dialog - dialog, played when the item is being dragged:
           - name - dialog name used in :actions field of scene-data
           - phrase - dialog phrase description
       - correct-dialog - dialog, played when the item is placed in right box (target). Optional
           - name - dialog name used in :actions field of scene-data
           - phrase - dialog phrase text
   - background - background data:
       - src - background image src
   - generic-dialogs - other dialogs:
       - intro - round introduction
           - name - dialog name used in :actions field of scene-data
           - prompt - text for prompt item in dialogs form. Optional
           - phrase - dialog phrase text. Optional
           - phrase-description - dialog phrase description text. Optional"
  [{:keys [generic-dialogs items boxes] :as props}]
  (-> {:assets        []
       :objects       {}
       :scene-objects []
       :actions       {:handle-drag-start    {:type        "stop-transition"
                                              :from-params [{:action-property "id" :param-property "target"}]}

                       :handle-drag-move     {:type        "action"
                                              :from-params [{:action-property "id"
                                                             :param-property  "say-item"}]}

                       :handle-drag-end      {:type "sequence-data"
                                              :data [{:type        "test-var-scalar"
                                                      :from-params [{:action-property "var-name"
                                                                     :param-property  "box"}]
                                                      :value       true
                                                      :success     "correct-option"
                                                      :fail        "wrong-option"}
                                                     {:type "action"
                                                      :id   "clear-target-vars"}]}

                       :handle-collide-enter {:type "sequence-data"
                                              :data [{:type        "set-attribute"
                                                      :attr-name   "highlight"
                                                      :attr-value  true
                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                     {:type        "set-variable"
                                                      :from-params [{:action-property "var-name"
                                                                     :param-property  "target"}]
                                                      :var-value   true}]}

                       :handle-collide-leave {:type "sequence-data"
                                              :data [{:type        "set-attribute"
                                                      :attr-name   "highlight"
                                                      :attr-value  false
                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                     {:type        "set-variable"
                                                      :from-params [{:action-property "var-name"
                                                                     :param-property  "target"}]
                                                      :var-value   false}]}

                       :clear-target-vars    {:type "parallel"
                                              :data (->> boxes
                                                         (map (fn [{:keys [name]}]
                                                                (get-box-name name)))
                                                         (map (fn [box-object-name]
                                                                {:type      "set-variable"
                                                                 :var-name  box-object-name
                                                                 :var-value false})))}

                       :correct-option       {:type "sequence-data"
                                              :data (->> [{:type "action" :id "unhighlight-all"}
                                                          {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                          {:type "action" :id "object-in-right-box"}
                                                          (when (items-have-correct-dialog? props)
                                                            {:type        "action"
                                                             :from-params [{:param-property  "correct-drop"
                                                                            :action-property "id"}]})
                                                          {:type "action" :id (-> generic-dialogs :correct-answer :name)}
                                                          (cond-> {:type       "test-var-inequality"
                                                                   :var-name   "sorted-objects"
                                                                   :value      (count items)
                                                                   :inequality ">="
                                                                   :success    "finish-scene"}
                                                                  (contains? generic-dialogs :continue-sorting) (assoc :fail (-> generic-dialogs :continue-sorting :name)))]
                                                         (remove nil?))}

                       :object-in-right-box  {:type        "set-attribute"
                                              :attr-name   "visible"
                                              :attr-value  false
                                              :from-params [{:action-property "target"
                                                             :param-property  "target"}]}

                       :wrong-option         {:type "parallel"
                                              :data [{:type "action" :id (-> generic-dialogs :wrong-answer :name)}
                                                     {:type "action" :id "unhighlight-all"}
                                                     {:type "action" :id "object-revert"}]}

                       :object-revert        {:type        "transition"
                                              :from-params [{:action-property "transition-id"
                                                             :param-property  "target"}
                                                            {:action-property "to"
                                                             :param-property  "init-position"}]}

                       :unhighlight-all      {:type "parallel"
                                              :data []}     ; Data filled by add-boxes

                       :init-activity        {:type "sequence-data"
                                              :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                     {:type "action" :id (-> generic-dialogs :intro :name)}]}

                       :finish-scene         {:type "sequence-data"
                                              :data [{:type "action" :id (-> generic-dialogs :finish-dialog :name)}
                                                     {:type "action" :id "finish-activity"}]}

                       :stop-activity        {:type "stop-activity"}
                       :finish-activity      {:type "finish-activity"}}

       :triggers      {:back  {:on "back" :action "stop-activity"}
                       :start {:on "start" :action "init-activity"}}
       :metadata      {:autostart true}}
      (init-tracks-metadata props)
      (add-background props)
      (add-boxes props)
      (add-items props)
      (add-items-dialogs props)
      (add-generic-dialogs props)))

(ns webchange.templates.library.categorize.templates.round-1
  (:require
    [webchange.templates.library.categorize.templates.common :refer [get-draggable-item]]
    [webchange.templates.utils.dialog :as dialog]))

(def params-object-names [:say-item :target :correct-drop :box])
(def var-object-names [])
(def var-action-names [])

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
  [idx]
  (str "box-" idx))

(defn- add-boxes
  [template {:keys [boxes]}]
  (->> boxes
       (map-indexed vector)
       (reduce (fn [template [idx {:keys [position src]}]]
                 (let [asset {:url src :size 10 :type "image"}
                       object-name (get-box-name idx)
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
  [template {:keys [items]}]
  (->> items
       (map-indexed vector)
       (reduce (fn [template [idx {:keys [position src pick-dialog correct-dialog]}]]
                 (let [asset {:url src :size 10 :type "image"}
                       object-name (str "item-" idx)
                       object-data (get-draggable-item {:position    position
                                                        :src         src
                                                        :target      object-name
                                                        :test        [(str "#^" (get-box-name "") ".*")]
                                                        :say-item    (:name pick-dialog)
                                                        :say-correct (:name correct-dialog)
                                                        :box         (get-box-name idx)})]
                   (-> template
                       (update :assets conj asset)
                       (update :objects assoc (keyword object-name) object-data)
                       (update :scene-objects conj [object-name]))))
               template)))

(defn- add-items-dialogs
  [template {:keys [items]}]
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
              (-> template
                  (update :actions assoc pick-dialog-name pick-dialog-data)
                  (update :actions assoc correct-dialog-name correct-dialog-data)
                  (update-in [:metadata :tracks 1 :nodes] conj pick-dialog-track-item-data)
                  (update-in [:metadata :tracks 1 :nodes] conj correct-dialog-track-item-data))))
          template
          items))

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

(defn get-template
  [{:keys [generic-dialogs items] :as props}]
  (-> {:assets        []
       :objects       {}
       :scene-objects []
       :actions       {:handle-drag-start    {:type        "stop-transition"
                                              :from-params [{:action-property "id" :param-property "target"}]}

                       :handle-drag-move     {:type        "action"
                                              :from-params [{:action-property "id"
                                                             :param-property  "say-item"}]}

                       :handle-drag-end      {:type        "test-var-scalar"
                                              :from-params [{:action-property "var-name"
                                                             :param-property  "box"}]
                                              :value       true
                                              :success     "correct-option"
                                              :fail        "wrong-option"}

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

                       :correct-option       {:type "sequence-data"
                                              :data [{:type "action" :id "unhighlight-all"}
                                                     {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                     {:type "action" :id "object-in-right-box"}
                                                     {:type        "action"
                                                      :from-params [{:param-property  "correct-drop"
                                                                     :action-property "id"}]}
                                                     {:type "action" :id (-> generic-dialogs :correct-answer :name)}
                                                     {:type       "test-var-inequality"
                                                      :var-name   "sorted-objects"
                                                      :value      (count items)
                                                      :inequality ">="
                                                      :success    "finish-scene"}]}

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
       :metadata      {:autostart true
                       :tracks    [{:title "Round 1"
                                    :nodes []}
                                   {:title "Round 1 - items"
                                    :nodes []}]}}
      (add-background props)
      (add-boxes props)
      (add-items props)
      (add-items-dialogs props)
      (add-generic-dialogs props)))

(ns webchange.templates.library.categorize.templates.round-2
  (:require
    [webchange.templates.library.categorize.templates.common :refer [add-background get-draggable-item]]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.utils.deep-merge :refer [deep-merge]]
    [webchange.utils.list :refer [distinct-by-key]]))

(def params-object-names [:say-item :target :correct-drop :box])
(def var-object-names [])
(def var-action-names [])
(def all-vars-in-actions ["clear-target-vars"])

(defn- init-tracks-metadata
  [template {:keys [tracks]}]
  (assoc-in template [:metadata :tracks] [{:title (get-in tracks [:generic :title] "Round 2")
                                           :nodes []}
                                          {:title (get-in tracks [:items :title] "Round 2 - items")
                                           :nodes []}
                                          {:title (get-in tracks [:items :title] "Round 2 - boxes")
                                           :nodes []}]))

(defn- add-track-dialog
  [template track-type track-item-data]
  (let [track-number (get {:generic 0
                           :item    1
                           :box     2}
                          track-type 0)]
    (update-in template [:metadata :tracks track-number :nodes] conj track-item-data)))

(defn- add-librarian
  [template {:keys [generic-dialogs]}]
  (let [object-name "librarian"
        object-data {:type      "animation"
                     :x         250 :y 1000
                     :width     351 :height 717
                     :name      "senoravaca" :skin "lion"
                     :anim      "idle" :speed 0.3 :start true
                     :editable? {:select true :show-in-tree? true}
                     :actions   {:click {:on "click" :type "action"
                                         :id (-> generic-dialogs :tap-instructions :name)}}}]
    (-> template
        (update :objects assoc (keyword object-name) object-data)
        (update :scene-objects conj [object-name]))))

(defn- get-box-name
  [name]
  (str "box-" name))

(defn- get-box-dialog-name
  [box-name]
  (str "box-" box-name "-dialog"))

(defn- add-boxes
  [template {:keys [boxes]}]
  (->> boxes
       (reduce (fn [template {:keys [name position src]}]
                 (let [asset {:url src :size 10 :type "image"}
                       object-name (get-box-name name)
                       object-data (merge position
                                          {:type       "image"
                                           :src        src
                                           :transition object-name
                                           :actions    {:click {:on "click" :type "action"
                                                                :id (get-box-dialog-name name)}}})]
                   (-> template
                       (update :assets conj asset)
                       (update :objects assoc (keyword object-name) object-data)
                       (update :scene-objects conj [object-name])
                       (update-in [:actions :unhighlight-all :data] conj {:target     object-name
                                                                          :type       "set-attribute"
                                                                          :attr-name  "highlight"
                                                                          :attr-value false}))))
               template)))

(defn- add-boxes-dialogs
  [template {:keys [boxes]}]
  (->> boxes
       (reduce (fn [template {:keys [name]}]
                 (let [box-dialog-name (get-box-dialog-name name)
                       box-dialog-data (-> name
                                           (dialog/default {:phrase-description (str "Box " name " clicked")})
                                           (assoc :unique-tag "box"))
                       box-dialog-track-item {:type      "dialog"
                                              :action-id box-dialog-name}]
                   (-> template
                       (update :actions assoc (keyword box-dialog-name) box-dialog-data)
                       (add-track-dialog :box box-dialog-track-item))))
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
                                                   (add-track-dialog :item pick-dialog-track-item-data))
                           (some? correct-dialog) (-> (update :actions assoc correct-dialog-name correct-dialog-data)
                                                      (add-track-dialog :item correct-dialog-track-item-data)))))
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
                  (add-track-dialog :generic track-item-prompt)
                  (add-track-dialog :generic track-item-data))))
          template
          generic-dialogs))

(defn- items-have-correct-dialog?
  [{:keys [items]}]
  (some (fn [item]
          (contains? item :correct-dialog))
        items))

(def default-generic-dialogs
  {:intro            {:name               "intro"
                      :prompt             "Start dialog"
                      :phrase             "intro"
                      :phrase-description "Introduce task"}
   :tap-instructions {:name   "tap-instructions"
                      :phrase "Tap instructions"}
   :correct-answer   {:name               "correct-answer"
                      :phrase             "correct-answer"
                      :phrase-description "correct answer"
                      :prompt             "Correct answer"}
   :wrong-answer     {:name               "wrong-answer"
                      :phrase             "wrong-answer"
                      :phrase-description "wrong answer"
                      :prompt             "Wrong answer"}
   :finish-dialog    {:name               "finish-dialog"
                      :phrase             "finish-answer"
                      :phrase-description "finish answer"
                      :prompt             "Finish dialog"}})

(defn get-template
  "Create Round 2.
   Properties description:
   - boxes - list of boxes descriptions:
       - name - unique box name, used to define item's target
       - position - where box has to be placed, {:x :y}
       - src - box image src
   - items - list of items descriptions:
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
       - src - background image src (for single background)
       or
       - background - {:src} - background layer data (for layered background)
       - decoration - the same as for 'background' field
       - surface - the same as for 'background' field
   - generic-dialogs - other dialogs:
       - intro - round introduction
           - name - dialog name used in :actions field of scene-data
           - prompt - text for prompt item in dialogs form. Optional
           - phrase - dialog phrase text. Optional
           - phrase-description - dialog phrase description text. Optional
       - tap-instructions - click librarian dialog
       - correct-answer - common dialog for all correct cases. The same fields as in the 'intro'
       - wrong-answer - common dialog for all incorrect cases. The same fields as in the 'intro'
       - continue-sorting - dialog after 'correct-answer' played when collected items number is less then needed. Optional
       - finish-dialog - dialog in the end of the round. The same fields as in the 'intro'
   - tracks - dialogs tracks options. Optional:
       - items - sortable items track options. Optional:
           - title - title of track. Optional
       - generic - generic dialogs track options. Optional:
           - title - title of track. Optional
   - librarian-position - where librarian should be placed:
                              'before' - behind boxes/tables and items. Default
                              'between' - between boxes/tables and items
                              'after' - in front of boxes/tables and items."
  [params]
  (let [{:keys [generic-dialogs items boxes librarian-position] :as props} (deep-merge {:generic-dialogs    default-generic-dialogs
                                                                                        :librarian-position "before"}
                                                                                       params)]
    (cond-> {:assets        []
             :objects       {}
             :scene-objects []
             :actions       {:handle-drag-start    {:type        "stop-transition"
                                                    :from-params [{:action-property "id" :param-property "target"}]}

                             :handle-drag-move     {:type        "action"
                                                    :from-params [{:action-property "id"
                                                                   :param-property  "say-item"}]}

                             :handle-drag-end      {:type "sequence-data"
                                                    :data [{:type "test-expression"
                                                            :expression ["eq" "#collided-object-name" "#box"]
                                                            :success     "correct-option"
                                                            :fail        "wrong-option"}
                                                           {:type "action"
                                                            :id   "clear-target-vars"}]}

                             :handle-collide-enter {:type "sequence-data"
                                                    :data [{:type        "set-variable"
                                                            :from-params [{:action-property "var-name"
                                                                           :param-property  "target"}]
                                                            :var-value   true}
                                                           {:type        "set-attribute"
                                                            :attr-name   "highlight"
                                                            :attr-value  true
                                                            :from-params [{:action-property "target" :param-property "target"}]}]}

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
                                                    :data [{:type "test-expression"
                                                            :expression "#collided-object-name"
                                                            :success     (-> generic-dialogs :wrong-answer :name)}
                                                           {:type "action" :id "unhighlight-all"}
                                                           {:type "action" :id "object-revert"}]}

                             :object-revert        {:type        "transition"
                                                    :from-params [{:action-property "transition-id"
                                                                   :param-property  "target"}
                                                                  {:action-property "to"
                                                                   :param-property  "init-position"}]}

                             :unhighlight-all      {:type "parallel"
                                                    :data []} ; Data filled by add-boxes

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
            :always (init-tracks-metadata props)
            :always (add-background props)
            (= librarian-position "before") (add-librarian props)
            :always (add-boxes props)
            :always (add-boxes-dialogs props)
            (= librarian-position "between") (add-librarian props)
            :always (add-items props)
            :always (add-items-dialogs props)
            (= librarian-position "after") (add-librarian props)
            :always (add-generic-dialogs props))))

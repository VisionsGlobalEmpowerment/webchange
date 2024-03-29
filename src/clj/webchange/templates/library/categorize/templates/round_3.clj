(ns webchange.templates.library.categorize.templates.round-3
  (:require
    [webchange.templates.library.categorize.templates.common :refer [add-background get-draggable-item]]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.utils.deep-merge :refer [deep-merge]]
    [webchange.utils.list :refer [distinct-by-key]]))

(def params-object-names [:say-item :target :correct-drop :box])
(def var-object-names ["instruction" "check-collide"])
(def var-action-names ["next-task" "correct-answer"])
(def all-vars-in-actions ["clear-target-vars"])

(defn- init-tracks-metadata
  [template {:keys [tracks]}]
  (assoc-in template [:metadata :tracks] [{:title (get-in tracks [:generic :title] "Round 3")
                                           :nodes []}
                                          {:title (get-in tracks [:items :title] "Round 3 - tasks")
                                           :nodes []}
                                          {:title (get-in tracks [:items :title] "Round 3 - items")
                                           :nodes []}]))

(defn- add-track-dialog
  [template track-type track-item-data]
  (let [track-number (get {:generic 0
                           :task    1
                           :item    2}
                          track-type 0)]
    (update-in template [:metadata :tracks track-number :nodes] conj track-item-data)))

(defn- add-librarian
  [template {:keys [_]}]
  (let [object-name "librarian"
        object-data {:type      "animation"
                     :x         250 :y 1000
                     :width     351 :height 717
                     :name      "senoravaca" :skin "lion"
                     :anim      "idle" :speed 0.3 :start true
                     :editable? {:select true :show-in-tree? true}
                     :actions   {:click {:on "click" :type "action"
                                         :id "tap-instructions"}}}]
    (-> template
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
                                           :width      253
                                           :height     253
                                           :transition object-name
                                           :states     {:highlighted     {:highlight true}
                                                        :not-highlighted {:highlight false}}})]
                   (-> template
                       (update :assets conj asset)
                       (update :objects assoc (keyword object-name) object-data)
                       (update :scene-objects conj [object-name]))))
               template)))

(defn- get-item-name
  [name]
  (str "item-" name))

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
                           (reduce (fn [template {:keys [name target position src pick-dialog correct-dialog]}]
                                     (let [object-name (get-item-name name)
                                           object-data (get-draggable-item (cond-> {:position position
                                                                                    :src      src
                                                                                    :target   object-name
                                                                                    :test     [(str "#^" (get-box-name "") ".*")]
                                                                                    :say-item (:name pick-dialog)
                                                                                    :box      (get-box-name target)
                                                                                    :states   {:init-position   position
                                                                                               :highlighted     {:highlight true}
                                                                                               :not-highlighted {:highlight false}}}
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

(defn- get-instruction-name
  [instruction-idx]
  (str "instruction-" instruction-idx))

(defn add-instructions
  [template {:keys [tasks]}]
  (->> tasks
       (map-indexed vector)
       (map (fn [[idx value]] [(inc idx) value]))
       (reduce (fn [template [idx {:keys [item box instruction]}]]
                 (let [instruction-description (if (some? instruction)
                                                 instruction
                                                 (str "Put the " item " picture on the " box "picture."))

                       instruction-name (get-instruction-name idx)
                       instruction-data (dialog/default instruction-name
                                                        {:phrase-description instruction-description})

                       track-prompt-data {:type "prompt"
                                          :text instruction-description}
                       track-dialog-data {:type      "dialog"
                                          :action-id (keyword instruction-name)}]
                   (-> template
                       (update :actions assoc (keyword instruction-name) instruction-data)
                       (add-track-dialog :task track-prompt-data)
                       (add-track-dialog :task track-dialog-data))))
               template)))

(defn- get-task-name
  [task-idx]
  (str "task-" task-idx))

(defn- add-tasks
  [template {:keys [tasks]}]
  (->> tasks
       (map-indexed vector)
       (map (fn [[idx value]] [(inc idx) value]))
       (reduce (fn [template [idx {:keys [item box]}]]
                 (let [instruction-name (get-instruction-name idx)

                       item-name (get-item-name item)
                       box-name (get-box-name box)

                       next-task (if (-> (count tasks) (= idx)) "finish" (-> idx inc get-task-name))

                       task-name (get-task-name idx)
                       task-data {:type "sequence-data",
                                  :data [{:type "set-variable" :var-name "check-collide" :var-value [item-name box-name]}
                                         {:type "set-variable" :var-name "current-item" :var-value (str item-name "-r3")}
                                         {:type "set-variable" :var-name "current-box" :var-value (str box-name "-r3")}
                                         {:type "set-variable" :var-name "next-task" :var-value next-task}
                                         {:type "set-variable" :var-name "instruction" :var-value instruction-name}
                                         {:type "action" :id instruction-name}]}]
                   (-> template
                       (update :actions assoc (keyword task-name) task-data))))
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

(def default-generic-dialogs
  {:intro          {:name               "intro"
                    :prompt             "Start dialog"
                    :phrase             "intro"
                    :phrase-description "Introduce task"}
   :correct-answer {:name               "correct-answer-dialog"
                    :phrase             "correct-answer"
                    :phrase-description "correct answer"
                    :prompt             "Correct answer"}
   :wrong-answer   {:name               "wrong-answer-dialog"
                    :phrase             "wrong-answer"
                    :phrase-description "wrong answer"
                    :prompt             "Wrong answer"}
   :finish-dialog  {:name               "finish-dialog"
                    :phrase             "finish-answer"
                    :phrase-description "finish answer"
                    :prompt             "Finish dialog"}})

(defn get-template
  "Create Round 3.
   Properties description:
   - boxes - list of boxes descriptions:
       - name - unique box name, used to define task box
       - position - where box has to be placed, {:x :y}
       - src - box image src
   - items - list of items descriptions:
       - name - unique item name, used to define task item
       - position - where item has to be placed, {:x :y}. Also can contain additional props e.g. rotation, scale.. 
       - src - item image src
       - pick-dialog - dialog, played when the item is being dragged:
           - name - dialog name used in :actions field of scene-data
           - phrase - dialog phrase description
       - correct-dialog - dialog, played when the item is placed in right box (target). Optional
           - name - dialog name used in :actions field of scene-data
           - phrase - dialog phrase text
   - tasks 
       - item - what item has to be placed
       - box - where item has to be placed
       - instruction - instruction phrase description. Optional
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
       - correct-answer - common dialog for all correct cases. The same fields as in the 'intro'
       - wrong-answer - common dialog for all incorrect cases. The same fields as in the 'intro'
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
  (let [{:keys [generic-dialogs librarian-position] :as props} (deep-merge {:generic-dialogs    default-generic-dialogs
                                                                            :librarian-position "before"}
                                                                           params)]
    (cond-> {:assets        []
             :objects       {}
             :scene-objects []
             :actions       {:handle-drag-start    {:type        "set-variable"
                                                    :from-params [{:action-property "var-name"
                                                                   :param-property  "target"}]
                                                    :var-value   true}

                             :handle-drag-move     {:type        "action"
                                                    :from-params [{:action-property "id"
                                                                   :param-property  "say-item"}]}

                             :handle-drag-end      {:type "sequence-data"
                                                    :data [{:type "test-expression"
                                                            :expression ["and"
                                                                         ["eq" "#collided-object-name" "@current-box"]
                                                                         ["eq" "#target" "@current-item"]]
                                                            :success     "correct-answer"
                                                            :fail        "wrong-answer"}]}

                             :handle-collide-enter {:type "sequence-data"
                                                    :data [{:type        "set-variable"
                                                            :from-params [{:action-property "var-name"
                                                                           :param-property  "target"}]
                                                            :var-value   true}
                                                           {:type        "state"
                                                            :id          "highlighted"
                                                            :from-params [{:action-property "target" :param-property "target"}]}]}

                             :handle-collide-leave {:type "sequence-data"
                                                    :data [{:type        "state"
                                                            :id          "not-highlighted"
                                                            :from-params [{:action-property "target" :param-property "target"}]}
                                                           {:type        "set-variable"
                                                            :from-params [{:action-property "var-name"
                                                                           :param-property  "target"}]
                                                            :var-value   false}]}

                             :correct-answer       {:type "sequence-data"
                                                    :data [{:type        "state"
                                                            :id          "init-position"
                                                            :from-params [{:action-property "target" :param-property "target"}]}
                                                           {:type "action"
                                                            :id   (-> generic-dialogs :correct-answer :name)}
                                                           {:type     "action"
                                                            :from-var [{:var-name "next-task" :action-property "id"}]}]}

                             :wrong-answer         {:type "sequence-data"
                                                    :data [{:type "test-expression"
                                                            :expression "#collided-object-name"
                                                            :success     (-> generic-dialogs :wrong-answer :name)}
                                                           {:type "action" :id "object-revert"}]}

                             :object-revert        {:type        "state"
                                                    :id          "init-position"
                                                    :from-params [{:action-property "target" :param-property "target"}]}

                             :stop-activity        {:type "stop-activity"}
                             :finish               {:type "sequence-data"
                                                    :data [{:type "action" :id (-> generic-dialogs :finish-dialog :name)}
                                                           {:type "action" :id "finish-activity"}]}

                             :start-activity       {:type "sequence-data"
                                                    :data [{:type "action" :id (-> generic-dialogs :intro :name)}
                                                           {:type "action" :id (get-task-name 1)}]}

                             :finish-activity      {:type "finish-activity"}

                             :tap-instructions     {:type     "action"
                                                    :from-var [{:var-name "instruction" :action-property "id"}]}}

             :triggers      {:start {:on "start" :action "start-activity"}}
             :metadata      {:autostart true}}
            :always (init-tracks-metadata props)
            :always (add-background props)
            (= librarian-position "before") (add-librarian props)
            :always (add-boxes props)
            (= librarian-position "between") (add-librarian props)
            :always (add-items props)
            :always (add-items-dialogs props)
            (= librarian-position "after") (add-librarian props)
            :always (add-tasks props)
            :always (add-instructions props)
            :always (add-generic-dialogs props))))

(comment
  (let [props {:boxes [{:name "box1"} {:name "box2"}]}]
    (concat ["or"]
            (->> props :boxes (map :name) (map get-box-name) (map #(str "@" %))))))

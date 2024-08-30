(ns webchange.templates.library.slide-riddle-no-concept
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]))

(def template-options
  [{:type "note"
    :text "A riddle will be given and there will be a correct answer and two incorrect answers. You can have as many rounds of riddles as you’d like.Here, you will specify the image options that will be shown related to the riddle. Within the builder you will enter the dialog for the riddle."}
   {:type "slide-riddle-rounds"
    :key "rounds"}])

(def m {:id          38
        :name        "Slide riddle"
        :tags        ["listening comprehension" "rhyming"]
        :description "Slide riddle (no concept)"
        :actions     {:add-round {:title   "Add round",
                                  :options {:image-correct {:label   "Correct image"
                                                            :description "Correct image"
                                                            :type    "image"
                                                            :options {:max-width  100
                                                                      :max-height 100
                                                                      :min-height 50
                                                                      :min-width  50}}
                                            :image-wrong-1 {:label   "Wrong image 1"
                                                            :description "Wrong image 1"
                                                            :type    "image"
                                                            :options {:max-width  100
                                                                      :max-height 100
                                                                      :min-height 50
                                                                      :min-width  50}}
                                            :image-wrong-2 {:label   "Wrong image 2"
                                                            :description "Wrong image 2"
                                                            :type    "image"
                                                            :options {:max-width  100
                                                                      :max-height 100
                                                                      :min-height 50
                                                                      :min-width  50}}}}
                      :remove-round {:title   "Remove round",
                                     :options {:which {:label   "Select Round"
                                                       :type    "lookup"
                                                       :options []}}}
                      :edit-round {:title    "Edit round"
                                   :default-props "edit-round"
                                   :options  {:which {:label   "Select Round"
                                                      :type    "lookup"
                                                      :options []}
                                              :image-correct {:label   "Correct image"
                                                              :description "Correct image"
                                                              :type    "image"
                                                              :optional? true
                                                              :options {:max-width  100
                                                                        :max-height 100
                                                                        :min-height 50
                                                                        :min-width  50}}
                                              :image-wrong-1 {:label   "Wrong image 1"
                                                              :description "Wrong image 1"
                                                              :type    "image"
                                                              :optional? true
                                                              :options {:max-width  100
                                                                        :max-height 100
                                                                        :min-height 50
                                                                        :min-width  50}}
                                              :image-wrong-2 {:label   "Wrong image 2"
                                                              :description "Wrong image 2"
                                                              :type    "image"
                                                              :optional? true
                                                              :options {:max-width  100
                                                                        :max-height 100
                                                                        :min-height 50
                                                                        :min-width  50}}}}
                      :template-options {:title "Template Options"
                                         :options template-options}}})

(def t {:assets
        [{:url "/raw/img/park/slide/background2.jpg", :type "image"}
         {:url "/raw/img/park/slide/slide.png", :type "image"}
         {:url "/raw/img/park/slide/side.png", :type "image"}
         {:url "/raw/img/park/slide/line_01.png", :size 1, :type "image"}
         {:url "/raw/img/park/slide/line_02.png", :size 1, :type "image"}
         {:url "/raw/img/park/slide/line_03.png", :size 1, :type "image"}],
        :objects
        {:background {:type "background", :src "/raw/img/park/slide/background2.jpg"},
         :box1
         {:type       "animation",
          :x          810,
          :y          216,
          :width      671,
          :height     633,
          :scale      {:x 0.25, :y 0.25},
          :scene-name "box1",
          :anim       "idle2",
          :loop       true,
          :name       "boxes",
          :skin       "qwestion",
          :speed      0.3,
          :start      true,
          :draggable  true,
          :actions    {:click      {:on     "click",
                                    :type   "action",
                                    :id     "box-clicked",
                                    :params {:target "box1"}},
                       :drag-end   {:on     "drag-end",
                                    :type   "action",
                                    :id     "stop-drag",
                                    :params {:box           "box1"
                                             :init-position {:x 810, :y 216 :duration 1}}},
                       :drag-start {:id "start-drag", :on "drag-start", :type "action",
                                    :params {:box "box1"}}},
          :states     {:init-position {:x 810, :y 216}}},
         :box2
         {:type       "animation",
          :x          500,
          :y          287,
          :width      671,
          :height     633,
          :scale      {:x 0.25, :y 0.25},
          :scene-name "box2",
          :transition "box2",
          :anim       "idle2",
          :loop       true,
          :name       "boxes",
          :skin       "qwestion",
          :speed      0.3,
          :start      true,
          :draggable  true,
          :actions    {:click      {:on     "click",
                                    :type   "action",
                                    :id     "box-clicked",
                                    :params {:target "box2"}},
                       :drag-end   {:on     "drag-end",
                                    :type   "action",
                                    :id     "stop-drag",
                                    :params {:box           "box2"
                                             :init-position {:x 500, :y 287 :duration 1}}},
                       :drag-start {:id "start-drag", :on "drag-start", :type "action",
                                    :params {:box "box2"}}},
          :states     {:init-position {:x 500, :y 287}}},
         :box3
         {:type       "animation",
          :x          655,
          :y          212,
          :width      671,
          :height     633,
          :scale      {:x 0.25, :y 0.25},
          :scene-name "box3",
          :transition "box3",
          :anim       "idle2",
          :loop       true,
          :name       "boxes",
          :skin       "qwestion",
          :speed      0.3,
          :start      true,
          :draggable  true,
          :actions    {:click      {:on     "click",
                                    :type   "action",
                                    :id     "box-clicked",
                                    :params {:target "box3"}},
                       :drag-end   {:on     "drag-end",
                                    :type   "action",
                                    :id     "stop-drag",
                                    :params {:box           "box3"
                                             :init-position {:x 655, :y 212 :duration 1}}},
                       :drag-start {:id "start-drag", :on "drag-start", :type "action",
                                    :params {:box "box3"}}},
          :states     {:init-position {:x 655, :y 212}}},
         :spot
         {:type    "image",
          :x       826,
          :y       524,
          :filters [{:name "brightness" :value 0}
                    {:name "glow" :outer-strength 0 :color 0xffd700}]
          :src     "/raw/img/park/slide/line_01.png",},
         :mari
         {:type       "animation",
          :x          1600,
          :y          580,
          :width      473,
          :height     511,
          :transition "mari",
          :anim       "idle",
          :name       "mari",
          :scale-x    0.5,
          :scale-y    0.5,
          :speed      0.35,
          :start      true
          :editable?  {:select true :drag true :show-in-tree? true}
          :actions    {:click {:id "guide-click" :on "click" :type "action"}}},
         :slide      {:type "image", :x 200, :y 190, :width 997, :height 758, :src "/raw/img/park/slide/slide.png"},
         :slide-side {:type "image", :x 591, :y 450, :width 234, :height 497, :src "/raw/img/park/slide/side.png"}},
        :scene-objects
        [["background"] ["slide" "slide-side" "spot" "mari" "box1" "box3" "box2"]],
        :actions
        {:start-drag           {:type "sequence-data",
                                :data [{:type        "copy-variable" :var-name "active-box"
                                        :from-params [{:param-property "box", :action-property "from"}]}
                                       {:id       "check-collide-2",
                                        :type     "set-interval",
                                        :action   "check-collide",
                                        :interval 100}]},
         :stop-drag            {:type "sequence-data",
                                :data [{:id "check-collide-2", :type "remove-interval"}
                                       {:type     "test-var-scalar", :value true,
                                        :success  "spot-selected", :fail "revert-position"
                                        :var-name "spot-selected"}
                                       {:type "set-attribute", :target "spot", :attr-value false :attr-name "highlight"}]}
         :highlight            {:type "sequence-data",
                                :data
                                [{:type "set-variable", :var-name "spot-selected", :var-value true}
                                 {:type "set-attribute" :target "spot" :attr-name "highlight" :attr-value true}]}
         :unhighlight          {:type "sequence-data",
                                :data
                                [{:type "set-variable", :var-name "spot-selected", :var-value false}
                                 {:type "set-attribute" :target "spot" :attr-name "highlight" :attr-value false}]},
         :check-collide        {:type "sequence-data",
                                :data
                                [{:fail        "unhighlight",
                                  :type        "test-transitions-collide",
                                  :success     "highlight",
                                  :from-params  [{:param-property "box", :action-property "transition-1"}]
                                  :transition-2 "spot"}]},
         :revert-position
         {:type        "transition"
          :from-params [{:param-property "box", :action-property "transition-id"}
                        {:param-property "init-position", :action-property "to"}]}
         :pick-wrong           {:type "sequence-data",
                                :data [{:type     "action"
                                        :from-var [{:var-name        "dragged-item"
                                                    :var-property    "dragged-dialog"
                                                    :action-property "id"}]}
                                       {:type "action" :id "revert-position"}]},
         :pick-correct         {:type "sequence-data",
                                :data
                                [{:type "parallel"
                                  :data [{:type "sequence-data",
                                          :data [{:type "empty", :duration 2000}
                                                 {:id "slide-current-target", :type "action"}]}
                                         {:type "sequence-data",
                                          :data [{:type     "action"
                                                  :from-var [{:var-name        "dragged-item"
                                                              :var-property    "dragged-dialog"
                                                              :action-property "id"}]}
                                                 {:type "empty", :duration 1000}
                                                 {:type "action" :id "script"}]}]}]},
         :spot-selected        {:type "sequence-data",
                                :data
                                [{:type "action" :id "unhighlight"}
                                 {:type "action" :id "timeout-timer"}
                                 {:type        "copy-variable"
                                  :var-name    "dragged-item"
                                  :from-params [{:action-property "from" :param-property "box"}]}
                                 {:fail     "pick-wrong",
                                  :value1   true
                                  :type     "test-value",
                                  :success  "pick-correct",
                                  :from-var [{:var-name        "dragged-item"
                                              :var-property    "correct"
                                              :action-property "value2"}]}]}

         :box-clicked          {:type "sequence-data",
                                :data [{:type "action" :id "timeout-timer"}
                                       {:type        "copy-variable"
                                        :var-name    "clicked-item"
                                        :from-params [{:action-property "from" :param-property "target"}]}
                                       {:type "action"
                                        :from-var [{:var-name        "clicked-item",
                                                    :var-property    "click-dialog",
                                                    :action-property "id"}]}]}

         :reset-boxes
         {:type "sequence-data",
          :data
          [{:type "set-attribute" :target "box1" :attr-name "visible" :attr-value false}
           {:type "set-attribute" :target "box2" :attr-name "visible" :attr-value false}
           {:type "set-attribute" :target "box3" :attr-name "visible" :attr-value false}
           {:type "empty", :duration 100}
           {:id "init-position", :type "state", :target "box1"}
           {:id "init-position", :type "state", :target "box2"}
           {:id "init-position", :type "state", :target "box3"}
           {:skin "qwestion", :type "set-skin", :target "box1"}
           {:skin "qwestion", :type "set-skin", :target "box2"}
           {:skin "qwestion", :type "set-skin", :target "box3"}
           {:type "set-attribute" :target "box1" :attr-name "visible" :attr-value true}
           {:type "set-attribute" :target "box2" :attr-name "visible" :attr-value true}
           {:type "set-attribute" :target "box3" :attr-name "visible" :attr-value true}]},
         :slide-current-target
         {:type "sequence-data",
          :data [{:to          {:ease [0.1 0.1], :bezier [{:x 930, :y 560} {:x 795, :y 775} {:x 975, :y 920}], :duration 1.5},
                  :type        "transition",
                  :from-params [{:param-property "box", :action-property "transition-id"}]}
                 {:type "empty", :duration 1000}]},
         :assign-boxes         {:type "sequence-data",
                                :data
                                [{:type       "set-slot",
                                  :target     "box1",
                                  :from-var   [{:var-name "box1", :var-property "image-src", :action-property "image"}],
                                  :slot-name  "box1",
                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                 {:type       "set-slot",
                                  :target     "box2",
                                  :from-var   [{:var-name "box2", :var-property "image-src", :action-property "image"}],
                                  :slot-name  "box1",
                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                 {:type       "set-slot",
                                  :target     "box3",
                                  :from-var   [{:var-name "box3", :var-property "image-src", :action-property "image"}],
                                  :slot-name  "box1",
                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}]}
         :remove-timeout-timer {:type "remove-interval"
                                :id   "timeout-timer"}
         :timeout-timer        {:type     "set-interval"
                                :id       "timeout-timer"
                                :interval 35000
                                :action   "timeout"}
         :timeout              {:type       "action"
                                :unique-tag "instructions"
                                :from-var   [{:var-name        "timeout-instructions-action"
                                              :action-property "id"}]},
         :script               {:type   "workflow"
                                :data   [{:type "start-activity"}
                                         {:type "parallel"
                                          :data [{:type "set-variable" :var-name "tap-instructions-action" :var-value "empty"}
                                                 {:type "set-variable" :var-name "timeout-instructions-action" :var-value "empty"}]}
                                         {:type "action" :id "intro-dialog"}]
                                :on-end "finish-activity"}
         :empty                {:type "empty" :duration 100}
         :guide-click          {:type       "action"
                                :unique-tag "instructions"
                                :from-var   [{:var-name        "tap-instructions-action"
                                              :action-property "id"}]}

         :highlight-spot       {:type               "transition"
                                :transition-id      "spot"
                                :return-immediately true
                                :from               {:brightness 0 :glow 0}
                                :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}
         :finish-activity
         {:type "sequence-data"
          :data [{:id "timeout-timer", :type "remove-interval"}
                 {:type "action" :id "finish-dialog"}
                 {:type "finish-activity"}]},
         :start-activity       {:type "start-activity"},
         :stop-activity        {:type "stop-activity"}
         :intro-dialog         (dialog/default "Intro")
         :finish-dialog
         (dialog/default "Finish dialog")}
        :triggers
        {:stop  {:on "back", :action "stop-activity"},
         :start {:on "start", :action "script"}},
        :metadata
        {:autostart true
         :tracks    [{:id    "main"
                      :title "Main Track"
                      :nodes [{:type      "dialog"
                               :action-id "intro-dialog"}
                              {:type      "dialog"
                               :action-id "finish-dialog"}]}]
         :available-actions [{:action "highlight-spot"
                              :name   "Highlight spot"}]}})

(defn- rename-rounds [activity-data]
  (let [tracks (get-in activity-data [:metadata :tracks])
        index (atom 0)
        new-tracks (mapv (fn [track]
                           (if (.startsWith (:title track) "Round")
                             (assoc track :title (str "Round " (swap! index inc)))
                             track))
                     tracks)]
    (assoc-in activity-data [:metadata :tracks] new-tracks)))

(defn recreate-drop-downs [activity-data]
  (let [num-rounds (get-in activity-data [:metadata :num-rounds])
        options (mapv (fn [i]
                        {:name (str "Round " (inc i)) :value i})
                  (range num-rounds))]
    (-> activity-data
      (assoc-in [:metadata :actions :edit-round :options :which :options] options)
      (assoc-in [:metadata :actions :remove-round :options :which :options] options))))

(defn- create-round
  [activity-data args]
  (let [next-round (or (:which args)
                       (-> activity-data
                           (get-in [:metadata :next-round-id])
                           (or 0)
                           (inc)))
        round-action-name (str "round-" next-round)
        riddle-name (str "round-" next-round "-riddle")
        tap-name (str "round-" next-round "-tap")
        timeout-name (str "round-" next-round "-timeout")
        item1-click-name (str "round-" next-round "-item1-clicked")
        item2-click-name (str "round-" next-round "-item2-clicked")
        item3-click-name (str "round-" next-round "-item3-clicked")
        item1-drag-name (str "round-" next-round "-item1-dragged")
        item2-drag-name (str "round-" next-round "-item2-dragged")
        item3-drag-name (str "round-" next-round "-item3-dragged")
        main-track (-> activity-data
                       (get-in [:metadata :tracks])
                       (first)
                       (update :nodes concat [{:type "track" :track-id round-action-name}]))
        round-track {:id    round-action-name
                     :title (str "Round " next-round)
                     :nodes [{:type      "dialog"
                              :action-id riddle-name}
                             {:type      "dialog"
                              :action-id item1-click-name}
                             {:type      "dialog"
                              :action-id item2-click-name}
                             {:type      "dialog"
                              :action-id item3-click-name}
                             {:type      "dialog"
                              :action-id item1-drag-name}
                             {:type      "dialog"
                              :action-id item2-drag-name}
                             {:type      "dialog"
                              :action-id item3-drag-name}
                             {:type      "dialog"
                              :action-id tap-name}
                             {:type      "dialog"
                              :action-id timeout-name}]}
        tracks (as-> activity-data x
                     (get-in x [:metadata :tracks])
                     (drop 1 x)
                     (concat [main-track] x [round-track]))]
    (-> activity-data
        (update-in [:metadata :num-rounds] inc)
        recreate-drop-downs
        (assoc-in [:actions (keyword item1-click-name)] (dialog/default "Item correct click"))
        (assoc-in [:actions (keyword item2-click-name)] (dialog/default "Item 1 incorrect click"))
        (assoc-in [:actions (keyword item3-click-name)] (dialog/default "Item 2 incorrect click"))
        (assoc-in [:actions (keyword item1-drag-name)] (dialog/default "Item correct picked"))
        (assoc-in [:actions (keyword item2-drag-name)] (dialog/default "Item 1 incorrect picked"))
        (assoc-in [:actions (keyword item3-drag-name)] (dialog/default "Item 2 incorrect picked"))
        (assoc-in [:actions (keyword riddle-name)] (dialog/default "Riddle"))
        (assoc-in [:actions (keyword tap-name)] (dialog/default "Tap instructions"))
        (assoc-in [:actions (keyword timeout-name)] (dialog/default "Timeout instructions"))
        (assoc-in [:actions (keyword round-action-name)] {:type "sequence-data"
                                                          :data [{:type "set-variable" :var-name "item-1" :var-value {:dragged-dialog item1-drag-name
                                                                                                                      :click-dialog   item1-click-name
                                                                                                                      :image-src      (get-in args [:image-correct :src])
                                                                                                                      :correct        true}}
                                                                 {:type "set-variable" :var-name "item-2" :var-value {:dragged-dialog item2-drag-name
                                                                                                                      :click-dialog   item2-click-name
                                                                                                                      :image-src      (get-in args [:image-wrong-1 :src])
                                                                                                                      :correct        false}}
                                                                 {:type "set-variable" :var-name "item-3" :var-value {:dragged-dialog item3-drag-name
                                                                                                                      :click-dialog   item3-click-name
                                                                                                                      :image-src      (get-in args [:image-wrong-2 :src])
                                                                                                                      :correct        false}}
                                                                 {:type "set-variable" :var-name "tap-instructions-action" :var-value tap-name}
                                                                 {:type "set-variable" :var-name "timeout-instructions-action" :var-value timeout-name}
                                                                 {:from      ["item-1" "item-2" "item-3"],
                                                                  :type      "vars-var-provider",
                                                                  :shuffled  true,
                                                                  :variables ["box1" "box2" "box3"]}
                                                                 {:type "action" :id "reset-boxes"}
                                                                 {:type "action" :id "assign-boxes"}
                                                                 {:type "action" :id riddle-name}
                                                                 {:type "action" :id "timeout-timer"}]})
        (update-in [:actions :script :data] concat [{:type "action" :id round-action-name :workflow-user-input true}])
        (assoc-in [:metadata :tracks] tracks)
        (assoc-in [:metadata :next-round-id] next-round)
        (update :assets concat [{:url (get-in args [:image-correct :src]), :size 1, :type "image"}
                                {:url (get-in args [:image-wrong-1 :src]), :size 1, :type "image"}
                                {:url (get-in args [:image-wrong-2 :src]), :size 1, :type "image"}])
        rename-rounds)))

(defn- find-round-name [index tracks]
  (let [index (if (string? index) (Integer/parseInt index) index)]
    (:id (nth (filter #(.startsWith (:title %) "Round") tracks) index))))

(defn- remove-round
  [activity-data round-id]
  (let [tracks (get-in activity-data [:metadata :tracks])
        round-name (find-round-name round-id tracks)
        main-track (-> tracks
                       first
                       (update :nodes (fn [nodes]
                                        (vec (filter #(not= (:track-id %) round-name) nodes)))))
        new-tracks (->> tracks
                        (filter #(not= (:id %) round-name))
                        (drop 1)
                        (concat [main-track])
                        (vec))
        actions (:actions activity-data)
        new-actions (->  actions
                         (select-keys (filter #(not (.startsWith (name %) round-name)) (keys actions)))
                         (update-in [:script :data]
                                    (fn [data]
                                      (filter #(not (= (:id %) round-name)) data))))]
    (-> activity-data
        (update-in [:metadata :num-rounds] dec)
        recreate-drop-downs
        (assoc-in [:metadata :tracks] new-tracks)
        (assoc-in [:actions] new-actions)
        rename-rounds)))

(defn- edit-round
  [activity-data args]
  (let [tracks (get-in activity-data [:metadata :tracks])
        round-name (find-round-name (:which args) tracks)
        correct (get-in args [:image-correct :src])
        wrong-1 (get-in args [:image-wrong-1 :src])
        wrong-2 (get-in args [:image-wrong-2 :src])
        change-image (fn [ad src id]
                       (-> ad
                         (assoc-in [:actions (keyword round-name) :data id :var-value :image-src] src)
                         (update :assets concat [{:url src, :size 1, :type "image"}])))]
    (-> activity-data
      (change-image correct 0)
      (change-image wrong-1 1)
      (change-image wrong-2 2))))

(defn create-activity
  [args]
  (-> (common/init-metadata m t args)
      (assoc-in [:metadata :num-rounds] 0)
      (assoc-in [:metadata :saved-props :template-options :rounds] [])))

(defn- get-round-data [activity-data round-id]
  (let [tracks (get-in activity-data [:metadata :tracks])
        round-name (find-round-name round-id tracks)
        get-image #(get-in activity-data [:actions (keyword round-name) :data % :var-value :image-src])]
    {:which round-id
     :image-correct {:src (get-image 0)}
     :image-wrong-1 {:src (get-image 1)}
     :image-wrong-2 {:src (get-image 2)}}))

(defn- create-paired-changes [activity-data]
  (into {}
        (map (fn [n]
               [n (get-round-data activity-data n)])
             (range (get-in activity-data [:metadata :num-rounds])))))

(defn- create-saved-props
  [activity-data]
  (if (> (get-in activity-data [:metadata :num-rounds]) 0)
    (-> activity-data
        (assoc-in [:metadata :saved-props :edit-round]
                  (get-round-data activity-data 0))
        (assoc-in [:metadata :actions :edit-round :options :which :paired-changes]
                  (create-paired-changes activity-data)))
    activity-data))

(defn- delete-rounds
  [activity-data rounds]
  (reduce remove-round activity-data rounds))

(defn- edit-rounds
  [activity-data rounds]
  (reduce edit-round activity-data rounds))

(defn- add-rounds
  [activity-data rounds]
  (reduce create-round activity-data rounds))

(defn- template-options
  [activity-data {:keys [rounds delete-last-round]}]
  (let [prev-rounds-number (-> activity-data
                               (get-in [:metadata :saved-props :template-options :rounds])
                               (count))
        new-rounds-number (count rounds)
        rounds-to-delete (concat (if delete-last-round
                                   [prev-rounds-number]
                                   [])
                                 (range new-rounds-number prev-rounds-number))
        rounds-to-edit (->> rounds
                            (take prev-rounds-number)
                            (drop-last (if delete-last-round 1 0)))
        rounds-to-add (->> rounds
                           (drop (count rounds-to-edit)))]
    (-> activity-data
        (delete-rounds rounds-to-delete)
        (edit-rounds rounds-to-edit)
        (add-rounds rounds-to-add)
        (assoc-in [:metadata :saved-props :template-options] {:rounds rounds}))))

(defn- add-round-to-saved-props
  [activity-data round]
  (let [current-round (-> (get-in activity-data [:metadata :next-round-id])
                          (dec))]
    (update-in activity-data [:metadata :saved-props :template-options :rounds] concat
               [(assoc round :which current-round)])))

(defn- remove-round-from-saved-props
  [activity-data round-id]
  (update-in activity-data [:metadata :saved-props :template-options :rounds]
             (fn [rounds]
               (remove #(= round-id (:which %)) rounds))))

(defn- edit-round-in-saved-props
  [activity-data {:keys [which] :as round}]
  (update-in activity-data [:metadata :saved-props :template-options :rounds]
             (fn [rounds]
               (map #(if (= which (:which %))
                       round
                       %)))))

(defn update-activity
  [old-data args]
  (create-saved-props
   (case (:action-name args)
     "add-round" (-> old-data
                     (create-round args)
                     (add-round-to-saved-props args))
     "remove-round" (-> old-data
                        (remove-round (:which args))
                        (remove-round-from-saved-props (:which args)))
     "edit-round" (-> old-data
                      (edit-round args)
                      (edit-round-in-saved-props args))
     "template-options" (template-options old-data args))))

(core/register-template m create-activity update-activity)

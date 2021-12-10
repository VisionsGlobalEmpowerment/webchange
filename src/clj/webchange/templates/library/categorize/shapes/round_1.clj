(ns webchange.templates.library.categorize.shapes.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.random-position :refer [get-items-positions]]))

(def stage-width 1920)
(def item-width 220)
(def item-height 170)
(def target-y 777)
(def target-width 280)
(def target-height 250)
(def target-padding 10)

(defn reduce->
  [val f coll]
  (reduce f val coll))

(defn- add-layer
  [template]
  (update template :scene-objects conj []))

(defn- add-to-last-layer
  [template object-name]
  (let [scene-objects (get template :scene-objects [[]])
        last-layer (last scene-objects)
        rest-layers (-> scene-objects drop-last vec)]
    (-> template
        (assoc-in [:scene-objects] (->> object-name
                                        (conj last-layer)
                                        (conj rest-layers))))))

(def dialog-name-suffix "item")

(defn- get-item-dialog-name
  [{:keys [shape]}]
  (str shape "-" dialog-name-suffix))

(defn- get-target-name
  [{:keys [shape]}]
  (str shape "-box"))

(defn- get-target-variable-name
  [{:keys [shape]}]
  (str shape "-box-selected"))

(defn- add-target
  [template {:keys [position shape]}]
  (let [target-name (get-target-name {:shape shape})
        target-src (str "/raw/img/categorize-shapes/" shape "-box.png")
        target-data (-> {:type "image"
                         :src  target-src}
                        (merge position))]
    (-> template
        (update-in [:assets] conj {:url target-src :size 10 :type "image"})
        (assoc-in [:objects (keyword target-name)] target-data)
        (add-to-last-layer target-name))))

(defonce item-indexes (atom {}))

(defn- get-shape-index!
  [shape]
  (let [new-idx (-> @item-indexes (get shape 0) (inc))]
    (swap! item-indexes assoc shape new-idx)
    new-idx))

(defn- add-item
  [template {:keys [position shape]}]
  (let [idx (get-shape-index! shape)
        item-name (-> (str shape "-" idx))
        item-src (str "/raw/img/categorize-shapes/" shape ".png")
        item-data (-> {:type      "image"
                       :src       item-src
                       :draggable true
                       :actions   {:drag-start {:type   "action"
                                                :on     "drag-start"
                                                :id     "start-drag"
                                                :params {:say-item         (get-item-dialog-name {:shape shape})
                                                         :target           item-name
                                                         :placement-target (get-target-name {:shape shape})}}
                                   :drag-end   {:id     "dragged"
                                                :on     "drag-end"
                                                :type   "action"
                                                :params {:box            (get-target-name {:shape shape})
                                                         :target         item-name
                                                         :init-position  (merge {:duration 1}
                                                                                position)
                                                         :check-variable (get-target-variable-name {:shape shape})}}}}
                      (merge position))]
    (-> template
        (update-in [:assets] conj {:url item-src :size 10 :type "image"})
        (assoc-in [:objects (keyword item-name)] item-data)
        (add-to-last-layer item-name))))

(defn- add-item-dialog
  [template {:keys [shape]}]
  (let [dialog-name (-> (get-item-dialog-name {:shape shape})
                        (keyword))
        [common-track items-track] (get-in template [:metadata :tracks])]
    (-> template
        (update-in [:actions] merge {dialog-name (-> (dialog/default shape)
                                                     (assoc :unique-tag dialog-name-suffix))})
        (assoc-in [:metadata :tracks] [common-track
                                       (update items-track :nodes conj {:type      "dialog"
                                                                        :action-id dialog-name})]))))

(defn- get-target-position
  [target-idx total-count]
  (let [total-width (-> (+ target-width (* 2 target-padding)) (* total-count))
        group-x (-> (- stage-width total-width) (/ 2))
        item-full-width (+ target-width (* 2 target-padding))
        item-x (-> (* target-idx item-full-width) (+ target-padding))]
    {:x (+ group-x item-x)
     :y target-y}))

; Available shapes: "circle" "oval" "rectangle" "square" "star" "triangle"

(def targets (let [shapes ["circle" "oval"]]
               (->> shapes
                    (map-indexed (fn [idx shape]
                                   {:shape    shape
                                    :position (get-target-position idx (count shapes))})))))

(def items (let [shapes (concat (->> "circle" repeat (take 6))
                                (->> "oval" repeat (take 6)))
                 padding 50
                 target-region {:x      padding
                                :y      padding
                                :width  (- stage-width (* 2 padding) item-width)
                                :height (- target-y (* 2 padding) item-height)}
                 positions (get-items-positions (map (fn [shape]
                                                       (case shape
                                                         "circle" {:width 200 :height 200}
                                                         "oval" {:width 240 :height 170}
                                                         {:width 200 :height 200}))
                                                     shapes)
                                                target-region)]
             (map (fn [shape position]
                    {:shape    shape
                     :position position})
                  shapes
                  positions)))

(def template-round-1 (-> {:assets        [{:url "/raw/img/categorize-shapes/background-white.png", :size 10, :type "image"}
                                           {:url "/raw/img/categorize-shapes/question.png", :size 10, :type "image"}],
                           :objects       {:background {:type "background", :src "/raw/img/categorize-shapes/background-white.png"}},
                           :scene-objects [["background"]],
                           :actions       {:object-in-right-box {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                                 :from-params [{:action-property "target" :param-property "target"}]},
                                           :object-revert       {:type        "transition",
                                                                 :from-params [{:action-property "transition-id" :param-property "target"}
                                                                               {:action-property "to" :param-property "init-position"}]}
                                           :wrong-option        {:type "sequence-data",
                                                                 :data [{:id "unhighlight-all" :type "action"}
                                                                        {:id "object-revert", :type "action"}
                                                                        {:id "wrong-answer", :type "action"}],}
                                           :correct-option      {:type "sequence-data",
                                                                 :data [{:id "unhighlight-all" :type "action"}
                                                                        {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                                        {:id "object-in-right-box", :type "action"}
                                                                        {:id "correct-answer", :type "action"}
                                                                        {:type       "test-var-inequality"
                                                                         :var-name   "sorted-objects",
                                                                         :value      (count items),
                                                                         :inequality ">=",
                                                                         :success    "finish-scene"}]}
                                           :dragged             {:type "sequence-data"
                                                                 :data [{:type        "copy-variable",
                                                                         :var-name    "current-selection-state"
                                                                         :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                        {:type "set-variable", :var-name "say", :var-value false}
                                                                        {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                        {:type     "test-var-scalar",
                                                                         :success  "correct-option",
                                                                         :fail     "object-revert",
                                                                         :value    true,
                                                                         :var-name "current-selection-state"}]}
                                           :highlight           {:type "sequence-data"
                                                                 :data [{:type        "set-variable",
                                                                         :var-value   true
                                                                         :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                        {:type        "set-attribute" :attr-name "highlight" :attr-value true
                                                                         :from-params [{:action-property "target" :param-property "transition"}]}]}
                                           :unhighlight         {:type "sequence-data"
                                                                 :data [{:type        "set-variable",
                                                                         :var-value   false
                                                                         :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                        {:type        "set-attribute" :attr-name "highlight" :attr-value false
                                                                         :from-params [{:action-property "target" :param-property "transition"}]}]}
                                           :unhighlight-all     {:type "parallel"
                                                                 :data (->> targets
                                                                            (map (fn [{:keys [shape]}]
                                                                                   {:type       "set-attribute"
                                                                                    :attr-name  "highlight"
                                                                                    :attr-value false
                                                                                    :target     (get-target-name {:shape shape})}))
                                                                            (vec))}
                                           :next-check-collide  {:type "sequence-data"
                                                                 :data [{:type     "set-timeout"
                                                                         :action   "check-collide"
                                                                         :interval 10}]}
                                           :check-collide       {:type "sequence-data"
                                                                 :data [{:type          "test-transitions-and-pointer-collide",
                                                                         :success       "highlight",
                                                                         :fail          "unhighlight",
                                                                         :transitions   (->> targets
                                                                                             (map get-target-name)
                                                                                             (vec))
                                                                         :action-params (->> targets
                                                                                             (map (fn [{:keys [shape]}]
                                                                                                    {:check-variable (get-target-variable-name {:shape shape})}))
                                                                                             (vec))}
                                                                        {:type     "test-var-scalar",
                                                                         :success  "next-check-collide",
                                                                         :value    true,
                                                                         :var-name "next-check-collide"}]}
                                           :say-item            {:type "sequence-data"
                                                                 :data [{:type "action" :from-params [{:action-property "id"
                                                                                                       :param-property  "say-item"}]}
                                                                        {:type     "test-var-scalar",
                                                                         :success  "next-say",
                                                                         :value    true,
                                                                         :var-name "say"}]}
                                           :next-say            {:type "sequence-data"
                                                                 :data [{:type     "set-timeout"
                                                                         :action   "say-item"
                                                                         :interval 100}]}
                                           :start-drag          {:type "sequence-data"
                                                                 :data [{:type        "stop-transition"
                                                                         :from-params [{:action-property "id" :param-property "target"}]}
                                                                        {:type "action", :id "reset-selected-vars"}
                                                                        {:type "set-variable", :var-name "say", :var-value true}
                                                                        {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                        {:id "next-say" :type "action"}
                                                                        {:id "next-check-collide" :type "action"}]}
                                           :init-activity       {:type "sequence-data"
                                                                 :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                        {:type "action" :id "intro"}]}
                                           :reset-selected-vars {:type "sequence-data"
                                                                 :data (->> targets
                                                                            (map (fn [{:keys [shape]}]
                                                                                   {:type      "set-variable"
                                                                                    :var-name  (get-target-variable-name {:shape shape})
                                                                                    :var-value false}))
                                                                            (vec))}
                                           :intro               {:type               "sequence-data",
                                                                 :editor-type        "dialog",
                                                                 :data               [{:type "sequence-data"
                                                                                       :data [{:type "empty" :duration 0}
                                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                 :phrase             "intro",
                                                                 :phrase-description "Introduce task"}
                                           :correct-answer      {:type               "sequence-data",
                                                                 :editor-type        "dialog",
                                                                 :data               [{:type "sequence-data"
                                                                                       :data [{:type "empty" :duration 0}
                                                                                              {:type        "animation-sequence",
                                                                                               :phrase-text "New action", :audio nil}]}],
                                                                 :phrase             "correct-answer",
                                                                 :phrase-description "correct answer"}
                                           :wrong-answer        {:type               "sequence-data",
                                                                 :editor-type        "dialog",
                                                                 :data               [{:type "sequence-data"
                                                                                       :data [{:type "empty" :duration 0}
                                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                 :phrase             "wrong-answer",
                                                                 :phrase-description "wrong answer"}
                                           :finish-scene        {:type "sequence-data",
                                                                 :data [{:type "action" :id "finish-dialog"}
                                                                        {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                        {:type "action", :id "finish-activity"}],}
                                           :finish-dialog       {:type               "sequence-data",
                                                                 :editor-type        "dialog",
                                                                 :data               [{:type "sequence-data"
                                                                                       :data [{:type "empty" :duration 0}
                                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                 :phrase             "finish-dialog",
                                                                 :phrase-description "finish dialog"}
                                           :stop-activity       {:type "stop-activity"},
                                           :finish-activity     {:type "finish-activity"}}
                           :triggers      {:back  {:on "back" :action "stop-activity"}
                                           :start {:on "start", :action "init-activity"}}
                           :metadata      {:autostart true
                                           :tracks    [{:title "Round 1"
                                                        :nodes [{:type "dialog" :action-id :intro}
                                                                {:type "prompt" :text "Correct answer"}
                                                                {:type "dialog" :action-id :correct-answer}
                                                                {:type "prompt" :text "Wrong answer"}
                                                                {:type "dialog" :action-id :wrong-answer}
                                                                {:type "prompt" :text "Finish dialog"}
                                                                {:type "dialog" :action-id :finish-dialog}]}
                                                       {:title "Round 1 - items"
                                                        :nodes []}]}}
                          (add-layer)
                          (reduce-> add-target targets)
                          (add-layer)
                          (reduce-> add-item items)
                          (reduce-> add-item-dialog targets)))

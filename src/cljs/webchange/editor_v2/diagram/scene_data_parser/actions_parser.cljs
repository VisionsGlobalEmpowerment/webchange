(ns webchange.editor-v2.diagram.scene-data-parser.actions-parser)

(def not-nil? (complement nil?))

(defn last-index?
  [list index]
  (and
    (= index (dec (count list)))
    (not (= -1 index))))

(defn last?
  [list item]
  (let [index (.indexOf list item)]
    (last-index? list index)))

(defn next-to-index
  [list index]
  (if-not (or (= -1 index)
              (last-index? list index))
    (nth list (inc index))
    nil))

(defn next-to
  [list item]
  (let [index (.indexOf list item)]
    (next-to-index list index)))

(defn add-next-property
  [next data]
  (assoc data :next (cond
                      (sequential? next) (vec next)
                      (not-nil? next) [next]
                      :else [])))

(defn add-parent-property
  [parent data]
  (if-not (nil? parent)
    (assoc data :parent parent)
    data))

(defmulti parse-action
          (fn [_ action-data _ _]
            (:type action-data)))

(defmethod parse-action "test-var-scalar"
  [action-name action-data parent-action next-action]
  (let [success (->> action-data :success keyword)
        fail (->> action-data :fail keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-next-property [success fail])
         (add-parent-property parent-action)
         (assoc {} action-name))))

(defmethod parse-action "sequence"
  [action-name action-data parent-action next-action]
  (let [first-item (->> action-data :data first keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-next-property first-item)
         (add-parent-property parent-action)
         (assoc {} action-name))))

(defmethod parse-action "sequence-data"
  [action-name action-data parent-action next-action]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result child-action]
        (let [[child-action-name child-action-data] child-action
              [next-child-action-name _] (next-to child-actions child-action)]
          (merge
            result
            (parse-action child-action-name
                          child-action-data
                          action-name
                          (or next-child-action-name next-action)))))
      (->> {:type (:type action-data)
            :data action-data}
           (add-next-property (->> child-actions first first))
           (add-parent-property parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-action "parallel"
  [action-name action-data parent-action next-action]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result [child-action-name child-action-data]]
        (merge result (parse-action child-action-name
                                    child-action-data
                                    action-name
                                    next-action)))
      (->> {:type (:type action-data)
            :data action-data}
           (add-next-property (->> child-actions (map first)))
           (add-parent-property parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-action :default
  [action-name action-data parent-action next-action]
  (->> {:type (:type action-data)
        :data action-data}
       (add-next-property next-action)
       (add-parent-property parent-action)
       (assoc {} action-name)))

;; ---

(defmulti parse-actions-chain
          (fn [_ _ action-data _ _]
            (:type action-data)))

(defmethod parse-actions-chain "sequence"
  [actions-data node-name node-data parent-action next-action]
  (let [parsed-action (parse-action node-name node-data parent-action next-action)
        sequence-data (->> node-data :data (map keyword))]
    (reduce
      (fn [result [index next-node-name]]
        (let [next-node-data (get actions-data next-node-name)]
          (merge result (parse-actions-chain actions-data
                                             next-node-name
                                             next-node-data
                                             node-name
                                             (or (next-to-index sequence-data index) next-action))))
        )
      parsed-action
      (map-indexed (fn [index item] [index item]) sequence-data))))

(defmethod parse-actions-chain "parallel"
  [actions-data node-name node-data parent-action next-action]
  (let [parsed-action (parse-action node-name node-data parent-action next-action)
        next-actions (->> parsed-action keys (filter #(not (= % node-name))))]
    (reduce
      (fn [result next-action-name]
        (merge result (parse-actions-chain actions-data
                                           next-action-name
                                           (->> next-action-name (get parsed-action) :data)
                                           node-name
                                           next-action)))
      parsed-action
      next-actions)
    ))

(defmethod parse-actions-chain "test-var-scalar"
  [actions-data node-name node-data parent-action next-action]
  (let [next-nodes [(->> node-data :success keyword)
                    (->> node-data :fail keyword)]]
    (reduce
      (fn [result next-node-name]
        (merge result (parse-actions-chain actions-data
                                           next-node-name
                                           (get actions-data next-node-name))))
      (parse-action node-name node-data parent-action next-action)
      next-nodes)))

(defmethod parse-actions-chain :default
  [_ node-name node-data parent-action next-action]
  (parse-action node-name node-data parent-action next-action))

(defn parse-actions
  [scene-data]
  (let [actions-data (:actions scene-data)
        start-node-name :click-on-box1
        start-node-data (get actions-data start-node-name)]
    (parse-actions-chain actions-data start-node-name start-node-data nil nil)))




(def scene-data {:objects {:box1 {:type       "transparent"
                                  :x          505 :y 375
                                  :width      772 :height 1032
                                  :scale      {:x 0.3 :y 0.3}
                                  :origin     {:type "center-center"}
                                  :scene-name "box1"
                                  :transition "box1"
                                  :states     {:default {:type "transparent"}
                                               :visible {:type  "animation" :name "boxes" :anim "come" :skin "qwestion"
                                                         :speed 0.3 :loop false :start true}}
                                  :actions    {:click {:type "action" :id "click-on-box1" :on "click"}}}}

                 :actions {
                           :senora-vaca-audio-touch-second-box {:type     "animation-sequence",
                                                                :target   "senoravaca",
                                                                :track    1,
                                                                :offset   52.453,
                                                                :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                                :data     [{:start 52.6, :end 53.467, :duration 0.867, :anim "talk"}
                                                                           {:start 54.36, :end 56.307, :duration 1.947, :anim "talk"}
                                                                           {:start 56.987, :end 59.173, :duration 2.186, :anim "talk"}],
                                                                :start    52.453,
                                                                :duration 6.987}

                           :set-current-box2                   {:type "parallel"
                                                                :data [{:type "set-variable" :var-name "current-box" :var-value "box2"}
                                                                       {:type "set-variable" :var-name "current-position-x" :var-value 850}]}


                           :click-on-box1                      {:type     "test-var-scalar"
                                                                :var-name "current-box"
                                                                :value    "box1"
                                                                :success  "first-word"
                                                                :fail     "pick-wrong"}

                           :first-word                         {:type       "sequence"
                                                                :data       ["show-first-box-word"
                                                                             "introduce-word"
                                                                             "bye-current-box"
                                                                             "set-current-box2"
                                                                             "senora-vaca-audio-touch-second-box"]
                                                                :unique-tag "box"}

                           :show-first-box-word                {:type "parallel"
                                                                :data [{:type "animation" :target "box1" :id "wood" :loop false}
                                                                       {:type     "set-skin" :target "box1"
                                                                        :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                                       {:type "copy-variable" :var-name "current-word" :from "item-1"}
                                                                       {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}

                           :bye-current-box                    {:type "sequence-data"
                                                                :data [{:type "parallel"
                                                                        :data [{:type     "animation" :id "jump"
                                                                                :from-var [{:var-name "current-box" :action-property "target"}]}
                                                                               {:type     "transition" :to {:y -100 :duration 2}
                                                                                :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                                           {:var-name "current-position-x" :action-property "to.x"}]}]}
                                                                       {:type     "state" :id "default"
                                                                        :from-var [{:var-name "current-box" :action-property "target"}]}]}

                           :vaca-this-is-var                   {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-this-is-action"}]}

                           :vaca-can-you-say                   {:type     "animation-sequence",
                                                                :target   "senoravaca",
                                                                :track    1,
                                                                :offset   20.28,
                                                                :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                                :data     [{:start 20.363, :end 20.98, :duration 0.617, :anim "talk"}],
                                                                :start    20.28,
                                                                :duration 0.813}

                           :vaca-question-var                  {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-question-action"}]}

                           :vaca-word-var                      {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-word-action"}]}

                           :introduce-word                     {:type "sequence"
                                                                :data ["empty-big"
                                                                       "vaca-this-is-var"
                                                                       "empty-small"
                                                                       "vaca-can-you-say"
                                                                       "vaca-question-var"
                                                                       "empty-small"
                                                                       "vaca-word-var"
                                                                       "empty-big"]}

                           :pick-wrong                         {:type "sequence"
                                                                :data ["audio-wrong"]}

                           :audio-wrong                        {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}

                           :empty-small                        {:type "empty" :duration 500}
                           :empty-big                          {:type "empty" :duration 1000}}})
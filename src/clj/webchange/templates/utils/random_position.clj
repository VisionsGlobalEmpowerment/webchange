(ns webchange.templates.utils.random-position
  (:require
    [clojure.tools.logging :as log]))

(defn- get-item-position
  [target-region]
  (let [round #(-> % Math/floor int)
        get-rand (fn [range]
                   (-> range rand round))]
    {:x (-> (:width target-region) (get-rand) (+ (:x target-region)))
     :y (-> (:height target-region) (get-rand) (+ (:y target-region)))}))

(defn- items-intersect?
  [position-1 bounds-1 position-2 bounds-2]
  (let [x-min (:x position-1)
        x-max (+ (:x position-1) (:width bounds-1))
        y-min (:y position-1)
        y-max (+ (:y position-1) (:height bounds-1))
        in-bounds? (fn [{:keys [x y]}]
                     (and (>= x x-min) (<= x x-max) (>= y y-min) (<= y y-max)))

        x1 (:x position-2)
        x2 (+ (:x position-2)
              (:width bounds-2))

        y1 (:y position-2)
        y2 (+ (:y position-2)
              (:height bounds-2))

        a {:x x1 :y y1}
        b {:x x2 :y y1}
        c {:x x2 :y y2}
        d {:x x1 :y y2}]

    (or (in-bounds? a)
        (in-bounds? b)
        (in-bounds? c)
        (in-bounds? d))))

(defn- check-new-position
  [position used-positions item-bounds]
  (or (empty? used-positions)
      (->> used-positions
           (map (fn [used-position]
                  (items-intersect? position item-bounds used-position used-position)))
           (some true?)
           (not))))

(defn- get-new-position
  [used-positions item-bounds target-region]
  (let [status (atom {:done?         false
                      :value         nil
                      :attempts-left 500})]
    (while (and (-> @status :done? not)
                (-> @status :attempts-left (> 0)))
      (let [attempts-left (:attempts-left @status)
            position (merge (get-item-position target-region)
                            item-bounds)]
        (reset! status {:done?         (check-new-position position used-positions item-bounds)
                        :value         position
                        :attempts-left (dec attempts-left)})))
    (:value @status)))

(defn get-items-positions
  [items target-region]
  "items - list of {:width :height}
   target-region - {:width :height}"
  (reduce (fn [used-positions item]
            (conj used-positions (get-new-position used-positions item target-region)))
          []
          items))

(ns webchange.templates.library.recording-studio.generate-buttons
  (:require
    [webchange.templates.library.recording-studio.layout :refer [layout-params]]))

(defn add-start-play-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:start-play-button layout-params)

        bg-1-size width
        bg-2-size (- width (* 2 border-width))
        icon-size 68

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))
        icon-name (-> name (str "-icon"))]
    (merge objects-data
           {(keyword name)              {:type     "group"
                                         :children [background-1-name background-2-name icon-name]
                                         :x        x
                                         :y        y
                                         :visible  false
                                         :actions  {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (/ bg-1-size 2)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             border-width
                                         :y             border-width
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius (/ bg-2-size 2)
                                         :fill          0x10BC2B}
            (keyword icon-name)         {:type   "svg-path"
                                         :x      (-> (- bg-1-size icon-size) (/ 2) (+ 15))
                                         :y      (/ (- bg-1-size icon-size) 2)
                                         :width  icon-size
                                         :height icon-size
                                         :fill   "#FFFFFF"
                                         :data   "M50.95 27.57L6.05798 0.63479C3.39189 -0.964869 0 0.955585 0 4.06476V57.9352C0 61.0444 3.39189 62.9649 6.05798 61.3652L50.95 34.43C53.5394 32.8764 53.5394 29.1236 50.95 27.57Z"}})))

(defn add-stop-play-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:stop-play-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              {:type     "group"
                                         :children [background-1-name background-2-name]
                                         :x        x
                                         :y        y
                                         :visible  false
                                         :actions  {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (* border-width 1.5)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             (/ (- bg-1-size bg-2-size) 2)
                                         :y             (/ (- bg-1-size bg-2-size) 2)
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius border-width
                                         :fill          0x10BC2B}})))

(defn add-start-record-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:start-record-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              {:type     "group"
                                         :children [background-1-name background-2-name]
                                         :x        x
                                         :y        y
                                         :visible  false
                                         :actions  {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (/ bg-1-size 2)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             border-width
                                         :y             border-width
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius (/ bg-2-size 2)
                                         :fill          0xED1C24}})))

(defn add-stop-record-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:stop-record-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              {:type     "group"
                                         :children [background-1-name background-2-name]
                                         :x        x
                                         :y        y
                                         :visible  false
                                         :actions  {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (* border-width 1.5)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             (/ (- bg-1-size bg-2-size) 2)
                                         :y             (/ (- bg-1-size bg-2-size) 2)
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius border-width
                                         :fill          0xED1C24}})))

(defn add-approve-button
  [objects-data name {:keys [on-click]}]
  (merge objects-data
         {(keyword name)           (merge {:type     "group"
                                           :visible  false
                                           :filters  [{:name "brightness" :value 0}
                                                      {:name "glow" :outer-strength 0 :color 0xffd700}]
                                           :children ["approve-background"
                                                      "approve-playback-button"]}
                                          (select-keys (:approve-button layout-params)
                                                       [:x :y :width :height]))
          :approve-background      {:type          "rectangle"
                                    :x             0
                                    :y             0
                                    :transition    "approve-background"
                                    :width         96
                                    :height        96
                                    :border-radius 48
                                    :fill          0xFF5C00}
          :approve-playback-button {:type    "svg-path"
                                    :x       20
                                    :y       25
                                    :width   128
                                    :height  128
                                    :fill    "#FFFFFF",
                                    :actions {:click {:id on-click :on "click" :type "action" :unique-tag "intro"}}
                                    :data    "M 9.29193 13.1343L0 22.3134L22.6633 45L59 9.47761L49.1793 0L22.6633 26.194L9.29193 13.1343"}}))

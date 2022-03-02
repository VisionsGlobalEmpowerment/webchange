(ns webchange.templates.library.flipbook.activity-template)

(def template {:assets        [{:url "/raw/img/flipbook/next-page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/prev-page.png" :size 1 :type "image"}

                               {:url "/raw/img/flipbook/pages_1.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/pages_2.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/shadow_page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/corner_left.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/corner_right.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/logo_2.png" :size 1 :type "image"}]
               :objects       {:book              {:type         "flipbook"
                                                   :transition   "book"
                                                   :x            0
                                                   :y            0
                                                   :width        "---"
                                                   :height       "---"
                                                   :pages        []
                                                   :prev-control "prev-page"
                                                   :next-control "next-page"}
                               :page-numbers      {:type     "group"
                                                   :x        0
                                                   :y        "---"
                                                   :visible  false
                                                   :children ["left-page-number" "right-page-number"]}
                               :left-page-number  {:type           "text"
                                                   :vertical-align "top"
                                                   :font-size      24
                                                   :font-family    "Roboto"
                                                   :fill           0x323232
                                                   :x              "---"
                                                   :y              0
                                                   :text           "00"}
                               :right-page-number {:type           "text"
                                                   :vertical-align "top"
                                                   :font-size      24
                                                   :font-family    "Roboto"
                                                   :fill           0x323232
                                                   :x              "---"
                                                   :y              0
                                                   :align          "right"
                                                   :text           "01"}
                               :prev-page         {:type             "image"
                                                   :x                32
                                                   :y                979
                                                   :opacity          0.7
                                                   :src              "/raw/img/flipbook/corner_left.png"
                                                   :actions          {:click {:id "prev-page-click" :on "click" :type "action"}}
                                                   :interpreter-mode "!editor"}
                               :next-page         {:type             "image"
                                                   :x                1787
                                                   :y                979
                                                   :opacity          0.7
                                                   :src              "/raw/img/flipbook/corner_right.png"
                                                   :actions          {:click {:id "next-page-click" :on "click" :type "action"}}
                                                   :interpreter-mode "!editor"}
                               :spine             {:type "group" :x "---" :y 0 :children []}}
               :scene-objects [["spine" "book"] ["prev-page" "next-page" "page-numbers"]]
               :actions       {:render-scene    {:type   "flipbook-init"
                                                 :target "book"}
                               :start-scene     {:type "sequence-data"
                                                 :data [{:type   "flipbook-read-cover"
                                                         :target "book"}]}
                               :prev-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-backward"
                                                         :target "book"}]}
                               :next-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-forward"
                                                         :target "book"}]}}
               :triggers      {:render {:on     "render"
                                        :action "render-scene"}
                               :start  {:on     "start"
                                        :action "start-scene"}}
               :metadata      {:autostart      true
                               :stage-size     :contain
                               :stages         []
                               :flipbook-name  "book"
                               :flipbook-pages {:total        0
                                                :current-side "right"}}
               :audio         {}})

(defn- apply-page-size
  [activity-data {:keys [width height]}]
  (let [page-number-margin {:h 64
                            :v 32}]
    (-> activity-data
        (assoc-in [:objects :book :width] (* width 2))
        (assoc-in [:objects :book :height] height)
        (assoc-in [:objects :spine :x] width)
        (assoc-in [:objects :page-numbers :y] (:v page-number-margin))
        (assoc-in [:objects :left-page-number :x] (:h page-number-margin))
        (assoc-in [:objects :right-page-number :x] (- (* width 2) (:h page-number-margin))))))

(defn- add-book-spine
  [activity-data spine-name {:keys [height]}]
  (let [half-height (-> height (/ 2) (Math/ceil))
        right-part-width 30
        left-part-width 20]
    (-> activity-data
        (update-in [:objects spine-name :children] concat ["spine-pt-1" "spine-pt-2"
                                                           "spine-pt-3" "spine-pt-3-2"
                                                           "spine-pt-4" "spine-pt-4-2"])
        (update :objects merge
                {:spine-pt-1   {:type "polygon"
                                :x    -30
                                :y    0
                                :path [[0 20] [30 0] [30 height] [0 (- height 20)]]
                                :fill 0x349B65}
                 :spine-pt-2   {:type "polygon"
                                :x    (-> (- right-part-width) (- left-part-width))
                                :y    0
                                :fill 0x1C7D4A
                                :path [[0 50] [left-part-width 20] [left-part-width (- height 20)] [0 (- height 50)]]}
                 :spine-pt-3   {:type "polygon"
                                :x    -30
                                :y    (- half-height 300)
                                :fill 0xBEC155
                                :path [[0 20] [30 0] [30 20] [0 40]]}
                 :spine-pt-3-2 {:type "polygon"
                                :x    (-> (- right-part-width) (- left-part-width))
                                :y    (- half-height 280)
                                :fill 0xAFB224
                                :path [[0 30] [left-part-width 0] [left-part-width 20] [0 50]]}
                 :spine-pt-4   {:type "polygon"
                                :x    -30
                                :y    (+ half-height 300)
                                :fill 0xBEC155
                                :path [[0 20] [30 40] [30 20] [0 0]]}
                 :spine-pt-4-2 {:type "polygon"
                                :x    (-> (- right-part-width) (- left-part-width))
                                :y    (+ half-height 270)
                                :fill 0xAFB224
                                :path [[0 20] [left-part-width 50] [left-part-width 30] [0 0]]}}))))

(defn get-template
  [page-params]
  (-> template
      (apply-page-size page-params)
      (add-book-spine :spine page-params)))

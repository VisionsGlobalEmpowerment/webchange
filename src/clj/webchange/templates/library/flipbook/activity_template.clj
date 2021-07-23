(ns webchange.templates.library.flipbook.activity-template)

(def template {:assets        [{:url "/raw/img/flipbook/next-page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/prev-page.png" :size 1 :type "image"}

                               {:url "/raw/img/flipbook/pages_1.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/pages_2.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/shadow_page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/corner_left.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/corner_right.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/logo_2.png" :size 1 :type "image"}]
               :objects       {:background        {:type       "rectangle"
                                                   :x          0
                                                   :y          0
                                                   :width      "---"
                                                   :height     "---"
                                                   :fill       0xf5f5f5}
                               :book              {:type         "flipbook"
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
                                                   :interpreter-mode "!editor"}}
               :scene-objects [["background"] ["book"] ["prev-page" "next-page" "page-numbers"]]
               :actions       {:start-scene     {:type "sequence-data"
                                                 :data [{:type   "flipbook-init"
                                                         :target "book"}]}
                               :prev-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-backward"
                                                         :target "book"}]}
                               :next-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-forward"
                                                         :target "book"}]}}
               :triggers      {:start {:on     "start"
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
        (assoc-in [:objects :background :width] (* width 2))
        (assoc-in [:objects :background :height] height)
        (assoc-in [:objects :book :width] (* width 2))
        (assoc-in [:objects :book :height] height)
        (assoc-in [:objects :page-numbers :y] (:v page-number-margin))
        (assoc-in [:objects :left-page-number :x] (:h page-number-margin))
        (assoc-in [:objects :right-page-number :x] (- (* width 2) (:h page-number-margin))))))

(defn get-template
  [page-params]
  (-> template
      (apply-page-size page-params)))

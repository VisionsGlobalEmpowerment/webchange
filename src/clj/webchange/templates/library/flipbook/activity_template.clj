(ns webchange.templates.library.flipbook.activity-template)

(def template {:assets        [{:url "/raw/img/flipbook/next-page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/prev-page.png" :size 1 :type "image"}]
               :objects       {:book      {:type         "flipbook"
                                           :transition   "book"
                                           :x            0
                                           :y            0
                                           :width        "---"
                                           :height       "---"
                                           :pages        []
                                           :prev-control "prev-page"
                                           :next-control "next-page"}
                               :prev-page {:type    "image"
                                           :x       "---"
                                           :y       "---"
                                           :width   95
                                           :height  95
                                           :src     "/raw/img/flipbook/prev-page.png"
                                           :actions {:click {:id "prev-page-click" :on "click" :type "action"}}}
                               :next-page {:type    "image"
                                           :x       "---"
                                           :y       "---"
                                           :width   95
                                           :height  95
                                           :src     "/raw/img/flipbook/next-page.png"
                                           :actions {:click {:id "next-page-click" :on "click" :type "action"}}}}
               :scene-objects [["book"] ["prev-page" "next-page"]]
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
                               :flipbook-pages {:total        0
                                                :current-side "right"}}
               :audio         {}})

(defn- apply-page-size
  [activity-data {:keys [width height padding]}]
  (let [flip-button-size {:width  95
                          :height 95}]
    (-> activity-data
        (assoc-in [:objects :book :width] (* width 2))
        (assoc-in [:objects :book :height] height)
        (assoc-in [:objects :prev-page :x] padding)
        (assoc-in [:objects :prev-page :y] (-> (- height (:height flip-button-size)) (- padding)))
        (assoc-in [:objects :next-page :x] (-> (* width 2) (- padding (:width flip-button-size))))
        (assoc-in [:objects :next-page :y] (-> (- height (:height flip-button-size)) (- padding))))))

(defn get-template
  [page-params]
  (-> template
      (apply-page-size page-params)))

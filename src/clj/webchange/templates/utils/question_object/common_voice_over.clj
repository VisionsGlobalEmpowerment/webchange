(ns webchange.templates.utils.question-object.common-voice-over)

(def default-size 80)

(defn- create-background
  [{:keys [object-name size]}]
  {(keyword object-name) {:type          "rectangle"
                          :x             0
                          :y             0
                          :width         size
                          :height        size
                          :border-radius (/ size 2)
                          :fill          0xFFFFFF}})

(defn create
  [{:keys [object-name x y size on-click on-click-params]
    :or   {x    0
           y    0
           size default-size}}]
  (let [background-name (str object-name "-background")
        actions (cond-> {}
                        (some? on-click) (assoc :click (cond-> {:type "action"
                                                                :on   "click"
                                                                :id   on-click}
                                                               (some? on-click-params) (assoc :params on-click-params))))]
    (merge {(keyword object-name) {:type     "group"
                                   :x        x
                                   :y        y
                                   :children [background-name]
                                   :actions  actions}}
           (create-background {:object-name background-name
                               :size        size}))))

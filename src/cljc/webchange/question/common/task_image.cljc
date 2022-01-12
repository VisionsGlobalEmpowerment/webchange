(ns webchange.question.common.task-image)

(defn create
  [form-data layout {:keys [param-name container-name image-name]}]
  (let [params (->> (keyword param-name)
                    (get form-data))

        {:keys [x y width height]} (:image layout)
        image-padding 12

        background-name (str container-name "-background")]
    {:objects {(keyword container-name)  {:type     "group"
                                          :x        x
                                          :y        y
                                          :children [background-name image-name]}
               (keyword image-name)      (merge {:type       "image"
                                                 :x          (/ width 2)
                                                 :y          (/ height 2)
                                                 :width      (->> (* image-padding 2) (- width))
                                                 :height     (->> (* image-padding 2) (- height))
                                                 :image-size "contain"
                                                 :origin     {:type "center-center"}
                                                 :editable?  {:select true}
                                                 :metadata   {:question-form-param param-name}}
                                                params)
               (keyword background-name) {:type          "rectangle"
                                          :x             0
                                          :y             0
                                          :width         width
                                          :height        height
                                          :fill          0xFFFFFF
                                          :border-radius 24
                                          :border-width  4
                                          :border-color  0xFFFFFF}}
     :assets  [{:url  (:src params)
                :size 1
                :type "image"}]}))

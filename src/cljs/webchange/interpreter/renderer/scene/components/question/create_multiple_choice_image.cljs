(ns webchange.interpreter.renderer.scene.components.question.create-multiple-choice-image
  (:require
    [webchange.interpreter.renderer.scene.components.question.create-common-option-image :as option-image]
    [webchange.interpreter.renderer.scene.components.question.create-common-voice-over :as voice-over]
    [webchange.interpreter.renderer.scene.components.question.create-utils :refer [add-name-suffix]]))

;{:children              ()
; :parent                #object[e [object Object]]
; :task                  {:type text-image
;                         :text Who do you think the main character
;                         or    most important character is going to be in this book?
;                               :img /images/questions/question.png}
; :layout                horizontal
; :type                  question
; :question-type         multiple-choice-image
; :object-name           :question-1
; :options               {:label audio-text
;                         :data  [{:img  /images/questions/option1.png
;                                  :text Cow}
;                                 {:img  /images/questions/option2.png
;                                  :text Deer}
;                                 {:img  /images/questions/option3.png
;                                  :text Fox}
;                                 {:img  /images/questions/option4.png
;                                  :text Skunk}]}
; :correct-answers-count one}


(defn- get-substrate
  [parent-name {:keys [width height]} {:keys []}]
  {:type        "rectangle"
   :object-name (add-name-suffix parent-name "substrate")
   :x           0
   :y           0
   :width       width
   :height      height
   :fill        0xFFFFFF})

(defn- get-background
  [parent-name {:keys [width height primary-color sides-ratio-h]} {:keys []}]
  (let [left-margin (* sides-ratio-h width)]
    {:type        "rectangle"
     :object-name (add-name-suffix parent-name "background")
     :x           left-margin
     :y           0
     :width       (- width left-margin)
     :height      height
     :fill        primary-color}))

(defn- get-task-image
  [parent-name {:keys [width height sides-ratio-h padding]} {:keys [task]}]
  (let [{:keys [image]} task

        container-width (* sides-ratio-h width)
        container-height height

        image-x (/ container-width 2)
        image-y (/ height 2)
        image-width (- container-width (* padding 2))
        image-height (- container-height (* padding 2))]
    {:type        "image"
     :object-name (add-name-suffix parent-name "task-image")
     :src         (:src image)
     :x           image-x
     :y           image-y
     :max-width   image-width
     :max-height  image-height
     :origin      {:type "center-center"}
     :editable?   {:select true}}))

(defn- get-task
  [{:keys [object-name x y width text]}]
  {:type        "group"
   :object-name object-name
   :x           x
   :y           y
   :children    [(voice-over/create {:object-name (add-name-suffix object-name "voice-over")
                                     :x           0
                                     :y           0})
                 {:type           "text"
                  :object-name    (add-name-suffix object-name "text")
                  :text           text
                  :x              voice-over/default-size
                  :y              0
                  :width          (- width voice-over/default-size)
                  :word-wrap      true
                  :font-size      60
                  :vertical-align "top"}]})

(defn- get-options
  [{:keys [object-name question-name]} parent-name {:keys [width height sides-ratio-h padding]} {:keys [options]}]
  (let [options-x (* sides-ratio-h width)
        options-y 500
        options-width (- width options-x)
        option-padding 10

        options-count (-> (:data options) (count))
        children (map-indexed (fn [idx option]
                                (print "idx" idx)
                                (let [option-width (/ (- options-width option-padding) options-count)
                                      option-height 500]
                                  (merge option
                                         {:idx           idx
                                          :x             (+ (/ option-padding 2) (* idx option-width) option-padding)
                                          :y             option-padding
                                          :width         (- option-width (* 2 option-padding))
                                          :height        (- option-height (* 2 option-padding))
                                          :question-name question-name})))
                              (:data options))]
    {:type        "group"
     :object-name object-name
     :x           options-x
     :y           options-y
     :children    (map (fn [{:keys [idx] :as option}]
                         (option-image/create (merge option
                                                     {:object-name (add-name-suffix object-name (str "option-" idx))})))
                       children)}))

(defn create
  [{:keys [object-name task] :as props}]
  (let [group-name (add-name-suffix object-name "group")
        {:keys [sides-ratio-h width] :as common-params} {:x             0
                                                         :y             0
                                                         :width         1920
                                                         :height        1080
                                                         :primary-color 0xFFA301
                                                         :sides-ratio-h 0.4
                                                         :padding       10}]
    [{:type        "group"
      :object-name group-name
      :x           (:x common-params)
      :y           (:y common-params)
      :children    [(get-substrate group-name common-params props)
                    (get-background group-name common-params props)
                    (get-task-image group-name common-params props)
                    (get-task {:object-name (add-name-suffix group-name "task")
                               :x           (* sides-ratio-h width)
                               :y           100
                               :width       (- width (* sides-ratio-h width))
                               :text        (:text task)})
                    (get-options {:object-name   (add-name-suffix group-name "options")
                                  :question-name object-name} group-name common-params props)]}]))

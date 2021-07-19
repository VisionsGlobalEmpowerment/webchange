(ns webchange.interpreter.renderer.scene.components.question.create-common-option-image
  (:require
    [webchange.interpreter.renderer.scene.components.question.create-common-voice-over :as voice-over]
    [webchange.interpreter.renderer.scene.components.question.create-utils :refer [add-name-suffix]]))

(defn- create-image
  [{:keys [object-name x y width height src]
    :or   {x 0
           y 0}}]
  {:type        "group"
   :object-name object-name
   :x           x
   :y           y
   :children    [{:type          "rectangle"
                  :object-name   (add-name-suffix object-name "background")
                  :x             0
                  :y             0
                  :width         width
                  :height        height
                  :border-radius 20
                  :border-width  2
                  :border-color  0x000000
                  :fill          0xFFFFFF}
                 {:type        "image"
                  :object-name (add-name-suffix object-name "image")
                  :src         src
                  :x           (/ width 2)
                  :y           (/ height 2)
                  :max-width   width
                  :max-height  height
                  :origin      {:type "center-center"}}]})

(defn- create-text
  [{:keys [object-name x y width text]
    :or   {x 0
           y 0}}]
  {:type        "text"
   :object-name object-name
   :text        text
   :x           x
   :y           y
   :width       width
   :font-size   60
   :vertical-align "top"})

(defn create
  [{:keys [object-name x y width height img text]
    :or   {x 0
           y 0}}]
  (let [gap 20
        text-height 80
        image-width width
        image-height (- height text-height)]
    {:type        "group"
     :object-name object-name
     :x           x
     :y           y
     :children    [(create-image {:object-name (add-name-suffix object-name "image")
                                  :src         img
                                  :width       image-width
                                  :height      image-height})
                   (create-text {:object-name (add-name-suffix object-name "text")
                                 :text        text
                                 :x           (+ voice-over/default-size gap)
                                 :y           (+ image-height gap)})
                   (voice-over/create {:object-name (add-name-suffix object-name "voice-over")
                                       :x           0
                                       :y           (+ image-height gap)})]}))

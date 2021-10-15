(ns webchange.interpreter.renderer.scene.components.animation.utils.movements
  (:require
    ["gsap/umd/TweenMax" :refer [TweenMax]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.animation.utils.state :as state-utils]
    [webchange.interpreter.renderer.scene.components.utils :as common-utils]
    [webchange.resources.manager :as resources]
    [webchange.utils.scene-action-data :refer [animation-tracks]]))

;; px per second AnimationStateListener
(def walk-speed 236)
(def slot-name "placeholder")
(def attachment-name "placeholder")

(defn- log
  [name value]
  (js/console.log name value)
  (aset js/window name value))

(defn- get-distance
  [p1 p2]
  (Math/sqrt (Math/pow (- (:x p1) (:x p2)) 2)
             (Math/pow (- (:y p1) (:y p2)) 2)))

(defn- get-duration
  [p1 p2 speed]
  (-> (get-distance p1 p2)
      (/ speed)))

(defn- wrapper->spine-object
  [wrapper]
  (let [group (:object wrapper)]
    (nth (.-children group) 0)))


(defn- get-texture-src
  [texture]
  (let [textures (.-textureCacheIds texture)]
    (when (some? textures)
      (first textures))))

(defn- get-image-src
  [target]
  (let [wrapper (:object target)
        wrapped-item (nth (.-children wrapper) 0)
        texture (.-texture wrapped-item)]
    (get-texture-src texture)))

(defn- set-slot-scale
  [slot scale]
  (aset (.-bone slot) "scaleX" scale)
  (aset (.-bone slot) "scaleY" scale))

(defn- get-attachment-data
  [animation attachment-name]
  (let [skeleton (.-skeleton animation)
        attachment (.getAttachmentByName skeleton slot-name attachment-name)]
    {:width  (.-width attachment)
     :height (.-height attachment)}))

(defn- equate-sizes
  [animation slot texture attachment-name]
  (let [attachment-size (get-attachment-data animation attachment-name)
        target-size {:width  (.-width texture)
                     :height (.-height texture)}
        scale (/ (:width target-size) (:width attachment-size))]
    (set-slot-scale slot scale)))

(defn- give-img-in-hands
  [animation image-src]
  (let [resource (resources/get-resource image-src)
        texture (.-texture resource)
        skeleton (.-skeleton animation)
        slot (.findSlot skeleton slot-name)]
    (equate-sizes animation slot texture attachment-name)
    (try
      (.hackTextureAttachment animation slot-name attachment-name texture (.-orig texture))
      ;; Ignore pixi-spine error of trying to update empty current attachment
      (catch js/Object e))))

;; Core

(defn- run-seq
  [& steps]
  (let [[current-step & rest-steps] steps
        [handler & params] current-step
        callback #(apply run-seq rest-steps)]
    (apply handler (concat [] params [callback]))))

(defn- interpolate
  [{:keys [from to duration on-progress on-end ease]
    :or   {ease "Linear.easeNone"}}]
  (let [container (clj->js from)
        handle-progress (fn []
                          (->> (keys from)
                               (select-keys (js->clj container :keywordize-keys true))
                               (on-progress)))
        handle-end (fn [] (on-end))
        params (cond-> {:ease ease}
                       (fn? on-progress) (assoc :onUpdate handle-progress)
                       (fn? on-end) (assoc :onComplete handle-end))]
    (TweenMax.to container
                 duration
                 (-> (merge to params)
                     (clj->js)))))

(defn- set-character-direction
  [character-wrapper from to]
  (let [{:keys [get-scale set-scale]} character-wrapper
        direction (if (> (- (:x to) (:x from)) 0) 1 -1)
        current-scale (get-scale)]
    (set-scale (assoc current-scale :x (-> (:x current-scale)
                                           (Math/abs)
                                           (* direction))))))

(defn- get-character-scale
  [character]
  (let [{character-get-scale :get-scale} character]
    (-> (character-get-scale)
        (get :x)
        (Math/abs))))

(defn- get-character-speed
  [character]
  (->> (get-character-scale character)
       (* walk-speed)))

(defn- get-pass-item-distance
  [character char-position target-position]
  (let [default-distance 300
        scale (get-character-scale character)
        direction (if (> (:x target-position)
                         (:x char-position))
                    - +)]
    (-> (* default-distance scale)
        (direction))))

;; Methods

(defn- go-to
  ([character target on-end]
   (go-to character target {} on-end))
  ([character target {:keys [distance]} on-end]
   (let [{character-get-position :get-position} character
         {target-get-position :get-position} target

         from (character-get-position)

         to (target-get-position)
         target-width (-> target wrapper->spine-object .getBounds .-width)
         target-position (cond-> (update to :x (if (> (:x to) (:x from)) - +) target-width)
                                 (= distance "give-item") (update :x + (get-pass-item-distance character from to)))

         speed (get-character-speed character)
         duration (get-duration from target-position speed)
         mix-time 0.3

         {remove-animation :remove-animation
          set-animation    :set-animation
          set-position     :set-position} character]
     (set-character-direction character from target-position)
     (set-animation (:main animation-tracks) "walk" true)
     (interpolate {:from        from
                   :to          target-position
                   :duration    duration
                   :on-progress (fn [position]
                                  (set-position position))
                   :on-end      (fn []
                                  (remove-animation (:main animation-tracks) mix-time)
                                  (on-end))}))))

(defn- pick-up-item
  [character image callback]
  (let [image-src (get-image-src image)
        animation (wrapper->spine-object character)
        state (:state character)
        {play-animation-once  :play-animation-once
         subscribe-once       :subscribe-once
         reset-idle-animation :reset-idle-animation} character
        {target-set-visibility :set-visibility} image]
    (when (some? image-src) (give-img-in-hands animation image-src))
    (subscribe-once "take" (fn [] (target-set-visibility false)))
    (play-animation-once (:main animation-tracks) "pick-up_item"
                         (fn []
                           (state-utils/take-image state image)
                           (reset-idle-animation)
                           (callback)))))

(defn- put-item
  [character target callback]
  (let [{character-get-position :get-position
         play-animation-once    :play-animation-once
         reset-idle-animation   :reset-idle-animation
         subscribe-once         :subscribe-once
         state                  :state} character]
    (subscribe-once "give" (fn []
                             (let [image (state-utils/give-image state)
                                   {target-get-position :get-position} target
                                   {image-set-position   :set-position
                                    image-set-visibility :set-visibility} image
                                   position (-> (target-get-position)
                                                (update :x (if (> (:x (target-get-position)) (:x (character-get-position)))
                                                             + -) 40)
                                                (update :y - 45))]
                               (image-set-position position)
                               (js/setTimeout #(image-set-visibility true) 10)
                               (reset-idle-animation))))

    (play-animation-once (:main animation-tracks) "put_item" callback)))

(defn- give-item
  [character target on-end]
  (let [{character-group      :object
         character-state      :state
         character-reset-idle :reset-idle-animation} character
        {target-group      :object
         target-state      :state
         target-reset-idle :reset-idle-animation} target

        character-animation (wrapper->spine-object character)
        attachment (.getAttachmentByName (.-skeleton character-animation) slot-name attachment-name)
        image-src (get-texture-src (.. attachment -region -texture))

        target-z-index (common-utils/get-z-index target-group)]

    (common-utils/set-z-index character-group (inc target-z-index))
    (common-utils/sort-children (.-parent character-group))

    (let [{character-play-once :play-animation-once} character
          {target-play-once :play-animation-once
           subscribe-once   :subscribe-once} target]
      (when (some? image-src)
        (give-img-in-hands (wrapper->spine-object target) image-src))

      (set-character-direction target (utils/get-position target-group) (utils/get-position character-group))
      (subscribe-once "take" (fn []
                               (state-utils/pass-image character-state target-state)
                               (character-reset-idle)
                               (target-reset-idle)))

      (character-play-once (:main animation-tracks) "give_item")
      (target-play-once (:main animation-tracks) "take_item" on-end))))

;; API

(defn- walk
  [character target callback]
  (run-seq [go-to character target]
           [callback]))

(defn- pick-up
  [character target callback]
  (run-seq [go-to character target]
           [pick-up-item character target]
           [callback]))

(defn- put
  [character target callback]
  (run-seq [go-to character target]
           [put-item character target]
           [callback]))

(defn- give
  [character target callback]
  (run-seq [go-to character target {:distance "give-item"}]
           [give-item character target]
           [callback]))

(defn move
  [action character target callback]
  (case action
    "go-to" (walk character target callback)
    "pick-up" (pick-up character target callback)
    "put" (put character target callback)
    "give" (give character target callback)))

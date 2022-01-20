(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Container Graphics]]
    [webchange.interpreter.renderer.scene.components.group.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.state.state :as state]))

(def default-props {:x              {}
                    :y              {}
                    :ref            {}
                    :on-click       {}
                    :filters        {}
                    :type           {:default "group"}
                    :metadata       {}
                    :mask           {}
                    :pivot          {}
                    :flipbook-page? {}})

(defn- create-container
  [{:keys [x y z-index]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})
    (utils/set-z-index z-index)))

(defn- create-mask
  [{:keys [x y width height]
    :or   {x      0
           y      0
           width  0
           height 0}}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRect x y width height)
    (.endFill 0x000000)))

(defn- apply-group-pivot
  [group pivot]
  (let [width (.-width group)
        height (.-height group)

        current-pivot (utils/get-pivot group)
        new-pivot {:x (* width (:x pivot))
                   :y (* height (:y pivot))}

        new-position (-> (utils/get-position group)
                         (update :x + (- (:x new-pivot) (:x current-pivot)))
                         (update :y + (- (:y new-pivot) (:y current-pivot))))]

    (utils/set-pivot group new-pivot)
    (utils/set-position group new-position)))

(def component-type "group")

(defn create
  "Create `group` component.
    Props params:
    :x - component x-position.
    :y - component y-position.
    :on-click - on click event handler.
    :ref - callback function that must be called with component wrapper.
    :children - vector og object names to group"
  [{:keys [parent type ref on-click filters metadata flipbook-page? mode mask pivot] :as props}]
  (let [group (create-container (cond-> props
                                        (:question? metadata) (assoc :z-index 100)))
        wrapped-group (wrap type (:object-name props) group {:question? (:question? metadata)})]

    (when (some? pivot)
      (utils/set-handler group "childAdded" #(apply-group-pivot group pivot)))

    (when mask
      (let [mask-obj (create-mask mask)]
        (.addChild group mask-obj)
        (aset group "mask" mask-obj)))

    (.addChild parent group)
    (apply-filters group filters)

    (when (and flipbook-page?
               (= mode ::modes/editor))
      (utils/set-handler group "click" #(re-frame/dispatch [::state/set-current-object (-> props :object-name clojure.core/name)])))
    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    wrapped-group))

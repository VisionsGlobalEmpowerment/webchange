(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Container]]
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
                    :flipbook-page? {}})

(defn- create-container
  [{:keys [x y z-index]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})
    (utils/set-z-index z-index)))

(def component-type "group")

(defn create
  "Create `group` component.
    Props params:
    :x - component x-position.
    :y - component y-position.
    :on-click - on click event handler.
    :ref - callback function that must be called with component wrapper.
    :children - vector og object names to group"
  [{:keys [parent type ref on-click filters metadata flipbook-page? mode] :as props}]
  (let [group (create-container (cond-> props
                                        (:question? metadata) (assoc :z-index 100)))
        wrapped-group (wrap type (:object-name props) group {:question? (:question? metadata)})]

    (.addChild parent group)
    (apply-filters group filters)

    (when (and flipbook-page?
               (= mode ::modes/editor))
      (utils/set-handler group "click" #(re-frame/dispatch [::state/set-current-object (-> props :object-name clojure.core/name)])))
    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    wrapped-group))

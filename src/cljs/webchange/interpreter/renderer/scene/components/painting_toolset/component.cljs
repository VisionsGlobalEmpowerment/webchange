(ns webchange.interpreter.renderer.scene.components.painting-toolset.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics string2hex]]
    [webchange.interpreter.renderer.scene.components.painting-toolset.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]))

(def default-props {:x            {:default 0}
                    :y            {:default 0}
                    :width        {}
                    :height       {}
                    :name         {}
                    :on-change    {}
                    :ref          {}
                    :default-tool {:default "felt-tip"}
                    :filters      {:default [{:name "brightness" :value 0}]}
                    :scale        {:default {:x 1 :y 1}}})

(def tools-definitions {:brush    {:defaults {:type "image",
                                              :x    -280,
                                              :y    450,
                                              :src  "/raw/img/library/painting-tablet/brush.png"}
                                   :active   {:x -180}
                                   :inactive {:x -280}}
                        :eraser   {:defaults {:type "image",
                                              :x    -118,
                                              :y    794,
                                              :src  "/raw/img/library/painting-tablet/eraser.png"}
                                   :active   {:x -18}
                                   :inactive {:x -118}}
                        :felt-tip {:defaults {:type "image",
                                              :x    -316,
                                              :y    273,
                                              :src  "/raw/img/library/painting-tablet/felt-tip.png"}
                                   :active   {:x -216}
                                   :inactive {:x -316}}
                        :pencil   {:defaults {:type "image",
                                              :x    -283,
                                              :y    622,
                                              :src  "/raw/img/library/painting-tablet/pencil.png"}
                                   :active   {:x -183}
                                   :inactive {:x -283}}})

(defn- create-container
  [{:keys [x y scale filters]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)
    (apply-filters filters)))

(defn- activate
  [state active-tool]
  (doall
    (for [[tool-name {:keys [set-position]}] (:tools @state)]
      (if (= active-tool tool-name)
        (set-position (get-in tools-definitions [tool-name :active]))
        (set-position (get-in tools-definitions [tool-name :inactive]))))))

(defn- create-tool!
  [group type state {:keys [on-change object-name]}]
  (let [defaults (get-in tools-definitions [type :defaults])
        on-click (fn []
                   (activate state type)
                   (when on-change
                     (on-change {:tool (name type)})))
        tool (image/create (assoc defaults
                             :object-name (str object-name "-tool-" (name type))
                             :parent group
                             :on-click on-click))]
    (swap! state assoc-in [:tools type] tool)))

(def component-type "painting-toolset")

(defn create
  "Create `painting-toolset` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :scale - image scale. Default: {:x 1 :y 1}.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-change - on change event handler."
  [{:keys [parent type object-name ref default-tool] :as props}]
  (let [group (create-container props)
        state (atom {:tools {}})
        wrapped-group (wrap type object-name group)]

    (create-tool! group :brush state props)
    (create-tool! group :felt-tip state props)
    (create-tool! group :pencil state props)
    (create-tool! group :eraser state props)

    (.addChild parent group)

    (when-not (nil? ref) (ref wrapped-group))

    (activate state (keyword default-tool))

    wrapped-group))

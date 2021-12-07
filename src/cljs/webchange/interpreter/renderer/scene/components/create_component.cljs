(ns webchange.interpreter.renderer.scene.components.create-component
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.scene.components.collisions :refer [has-collision-handler? enable-collisions! register-object]]
    [webchange.interpreter.renderer.scene.components.index :refer [components]]
    [webchange.interpreter.renderer.scene.components.props-utils :refer [get-props get-object-props]]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.logger.index :as logger]))

(def default-object-props {:draggable             {}
                           :on-drag-end           {}
                           :on-drag-end-options   {}
                           :on-drag-start         {}
                           :on-drag-start-options {}
                           :on-drag-move          {}
                           :on-drag-move-options  {}
                           :on-collide            {}
                           :collide-test          {}
                           :transition-name       {}
                           :visible               {:default true}
                           :rotation              {:default 0}
                           :opacity               {}
                           :custom-data           {}})

(defn- init-display-object!
  [{object :object :as wrapper} props props-to-exclude]
  (let [default-props (apply dissoc default-object-props props-to-exclude)
        {:keys [draggable visible rotation opacity] :as props} (get-object-props props default-props)]
    (register-object object props)
    (when (some? draggable)
      (when draggable
        (enable-drag! object (select-keys props [:on-drag-start :on-drag-start-options
                                                 :on-drag-move :on-drag-move-options
                                                 :on-drag-end :on-drag-end-options]))))
    (when (has-collision-handler? props)
      (enable-collisions! object props))
    (when (some? visible)
      (w/set-visibility wrapper visible))
    (when (some? rotation)
      (w/set-rotation wrapper rotation))
    (when (some? opacity)
      (w/set-opacity wrapper opacity))))

(defn- get-component
  [type]
  (let [{:keys [constructor default-props] :as component} (get components type)]
    (when (nil? constructor)
      (-> (str "Object with type <" type "> can not be drawn because it is not defined") js/Error. throw))
    (when (nil? default-props)
      (-> (str "Default props for <" type "> are not defined") js/Error. throw))
    component))

(defn- container-component?
  [component-type]
  (some #{component-type} ["flipbook" "group"]))

(defn- set-component-name!
  [component-wrapper component-props]
  (set! (.-name (:object component-wrapper))
        (clojure.core/name (:object-name component-props))))

(defn create-component
  [{:keys [type] :as props}]
  (logger/group-folded "create component" type (:object-name props))
  (logger/trace "props" props)
  (try
    (let [{:keys [constructor default-props]} (get-component type)
          prepared-props (get-props type props default-props {:exclude-check (keys default-object-props)})
          component-wrapper (constructor prepared-props)
          children (if (container-component? type)
                     (let [group-instance (:container component-wrapper)]
                       (->> (case type
                              "flipbook" (->> (:pages props) (map first))
                              (:children props))
                            (map (fn [child]
                                   (create-component (-> child
                                                         (assoc :parent group-instance)
                                                         (assoc :mode (:mode props))))))
                            (doall)))
                     [])]
      (logger/trace "prepared-props" prepared-props)

      (set-component-name! component-wrapper prepared-props)
      (init-display-object! component-wrapper props (keys default-props))

      (when (nil? component-wrapper)
        (-> (str "Constructor for <" type "> did not return component wrapper") js/Error. throw))

      (re-frame/dispatch [::state/register-object component-wrapper])
      (logger/group-end "create component" type (:object-name props))

      (when (= (:object-name props) :scene)
        (utils/sort-children (:container component-wrapper)))

      {:props    prepared-props
       :wrapper  component-wrapper
       :children children})
    (catch js/Object e
      (logger/error e))))

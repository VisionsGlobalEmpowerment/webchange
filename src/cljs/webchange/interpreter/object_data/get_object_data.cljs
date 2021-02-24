(ns webchange.interpreter.object-data.get-object-data
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]
    [webchange.subs :as subs]
    [webchange.interpreter.object-data.group-params :refer [with-group-params]]
    [webchange.interpreter.object-data.navigation-param :refer [with-navigation-params]]
    [webchange.interpreter.object-data.object-filters :refer [with-filter-params]]
    [webchange.interpreter.renderer.scene.components.animation.animation-params :refer [animations-params]]))

(defn prepare-anim-object-params
  "Overwrite animation properties. Set default skin if no skin provided."
  [object]
  (if-let [anim-data (get animations-params (-> object :name keyword))]
    (merge object anim-data (select-keys object [:skin]))
    object))

(defn filter-extra-props
  [props extra-props-names]
  (reduce (fn [props prop-to-remove]
            (dissoc props prop-to-remove))
          props
          extra-props-names))

(defn prepare-object-data
  [name scene-id get-data]
  (let [object (->> (get-data name)
                    (with-navigation-params scene-id name))
        type (-> object :type keyword)
        object-data (case type
                      :background (-> (merge object
                                             {:object-name (keyword name)}
                                             (with-filter-params object))
                                      (filter-extra-props [:brightness :filter]))
                      :layered-background (-> (merge object
                                                     {:object-name (keyword name)}
                                                     (with-filter-params object))
                                              (filter-extra-props [:brightness :filter]))
                      :button (-> (merge object
                                         {:object-name (keyword name)}
                                         (with-group-params object))
                                  (filter-extra-props []))
                      :image (-> object
                                 (assoc :object-name (keyword name))
                                 (with-group-params)
                                 (with-filter-params)
                                 (filter-extra-props [:actions :brightness :filter :highlight :eager]))
                      :questions (-> object
                                     (assoc :object-name (keyword name))
                                     (with-group-params)
                                     (with-filter-params)
                                     (filter-extra-props []))
                      :transparent (-> object
                                       (with-group-params)
                                       (assoc :object-name (keyword name))
                                       (filter-extra-props [:actions :origin]))
                      :group (let [group-params (with-group-params object)
                                   children-params (->> (:children object)
                                                        (map (fn [name] (prepare-object-data name scene-id get-data)))
                                                        (remove nil?))]
                               (-> (merge object
                                          group-params
                                          {:object-name (keyword name)
                                           :children    children-params})
                                   (filter-extra-props [:width :height])))
                      :flipbook (let [group-params (with-group-params object)
                                      pages-params (->> (:pages object)
                                                        (map (fn [{:keys [object action]}]
                                                               [(prepare-object-data object scene-id get-data) action]))
                                                        (map (fn [[object-params action]]
                                                               [(-> object-params
                                                                    (assoc :visible false)
                                                                    (dissoc :x)
                                                                    (dissoc :y))
                                                                action]))
                                                        (remove nil?))]
                                  (-> (merge object
                                             group-params
                                             {:object-name (keyword name)
                                              :pages       pages-params})
                                      (filter-extra-props [])))
                      :animation (let [anim-object (prepare-anim-object-params object)
                                       animation-name (or (:scene-name anim-object) (:name anim-object))]
                                   (-> anim-object
                                       (with-group-params)
                                       (assoc :object-name (keyword name))
                                       (assoc :on-mount #(re-frame/dispatch [::ier/register-animation animation-name %]))
                                       (filter-extra-props [:actions :width :height :origin :meshes])))
                      :text (-> object
                                (with-group-params)
                                (merge {:object-name (keyword name)})
                                (filter-extra-props []))
                      :rectangle (-> object
                                     (with-group-params)
                                     (merge {:object-name (keyword name)})
                                     (filter-extra-props []))
                      :carousel (-> object
                                    (merge {:object-name (keyword name)})
                                    (filter-extra-props []))
                      :painting-area (-> object
                                         (with-group-params)
                                         (assoc :object-name (keyword name))
                                         (filter-extra-props []))
                      :colors-palette (-> object
                                          (with-group-params)
                                          (assoc :object-name (keyword name))
                                          (filter-extra-props [:var :var-name]))
                      :video (-> (merge object
                                        {:object-name (keyword name)}
                                        (with-group-params object))
                                 (filter-extra-props []))
                      :animated-svg-path (-> object
                                             (with-group-params)
                                             (assoc :object-name (keyword name))
                                             (filter-extra-props []))
                      :svg-path (-> object
                                    (with-group-params)
                                    (assoc :object-name (keyword name))
                                    (filter-extra-props []))
                      :matrix (let [children-params (let [{:keys [el width height dx dy max skip]} object
                                                          skip-coordinates (partition 2 skip)]
                                                      (->> (for [y (range (/ height dy)) x (range (/ width dx))] [x y])
                                                           (filter (fn [coordinate] (not (some #(= coordinate %) skip-coordinates))))
                                                           (take max)
                                                           (map-indexed (fn [index [x y]]
                                                                          (merge (prepare-object-data el scene-id get-data)
                                                                                 {:x           (* x dx)
                                                                                  :y           (* y dy)
                                                                                  :object-name (-> el (str "-" index) (keyword))
                                                                                  :group-name  (keyword el)})))))]
                                (-> object
                                    (with-group-params)
                                    (merge {:type        "group"
                                            :object-name (keyword name)
                                            :children    children-params})
                                    (filter-extra-props [:el :dx :width :dy :max :height])))
                      :propagate (-> object
                                     (with-group-params)
                                     (merge {:type        "group"
                                             :object-name (keyword name)})
                                     (filter-extra-props [:el-height :el :width :el-width :height]))
                      :traffic-light (-> object
                                         (assoc :object-name (keyword name))
                                         (with-group-params)
                                         (with-filter-params)
                                         (filter-extra-props [:actions :brightness :filter :highlight :width :height :eager]))
                      :timer (-> object
                                 (assoc :object-name (keyword name))
                                 (with-group-params)
                                 (with-filter-params)
                                 (filter-extra-props []))
                      :counter (-> object
                                   (assoc :object-name (keyword name))
                                   (with-group-params)
                                   (with-filter-params)
                                   (filter-extra-props []))
                      (-> (str "Object with type " type " can not be drawn because it is not defined (" name ")") (js/Error.) (throw)))]
    (-> object-data
        (filter-extra-props [:actions :states :scene-name :transition :filter-transition]))))

(defn get-object-data
  ([scene-id name]
   (prepare-object-data name scene-id (fn [name] @(re-frame/subscribe [::subs/scene-object-with-var scene-id name]))))
  ([scene-id name objects-data]
   (prepare-object-data name scene-id (fn [name] (get objects-data (keyword name))))))

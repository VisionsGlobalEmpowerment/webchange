;[webchange.common.scene-components.components :as components]
;[webchange.interpreter.utils.find-exit :refer [find-exit-position find-path]]
;[webchange.common.kimage :refer [kimage]]
;[webchange.common.painting-area :refer [painting-area]]
;[webchange.common.svg-path :refer [svg-path]]
;[webchange.common.copybook :refer [copybook]]
;[webchange.common.colors-palette :refer [colors-palette]]
;[webchange.common.animated-svg-path :refer [animated-svg-path]]
;[webchange.common.matrix :refer [matrix-objects-list]]
;[webchange.common.anim :refer [anim]]
;[webchange.common.text :refer [chunked-text]]
;[webchange.common.carousel :refer [carousel]]
;[webchange.interpreter.components.video :refer [video]]
;[webchange.interpreter.core :refer [get-data-as-url]]
;
;[webchange.interpreter.utils.position :refer [compute-x compute-y compute-scale get-viewbox top-left top-right bottom-center]]
;[webchange.common.core :refer [prepare-anim-rect-params
;                                   prepare-colors-palette-params
;                                   prepare-group-params
;                                   prepare-painting-area-params
;                                   prepare-animated-svg-path-params
;                                   with-origin-offset
;                                   with-filter-transition]]


(ns webchange.interpreter.object-data.get-object-data
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]
    [webchange.subs :as subs]
    [webchange.common.anim :refer [prepare-anim-object-params]]
    [webchange.interpreter.object-data.group-params :refer [with-group-params]]

    [webchange.interpreter.object-data.navigation-param :refer [with-navigation-params]]
    [webchange.interpreter.object-data.object-filters :refer [with-filter-params]]))

;; ----

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
                                      (filter-extra-props [:brightness :filter :transition]))
                      :layered-background (-> (merge object
                                                     {:object-name (keyword name)}
                                                     (with-filter-params object))
                                              (filter-extra-props [:brightness :filter :transition]))
                      :button (-> (merge object
                                         {:object-name (keyword name)}
                                         (with-group-params object))
                                  (filter-extra-props []))
                      :image (-> (merge object
                                        {:object-name (keyword name)}
                                        (with-group-params object)
                                        (with-filter-params object))
                                 (filter-extra-props [:actions :brightness :filter :highlight :states :transition :width :height :eager :origin]))
                      :transparent (-> object
                                       (with-group-params)
                                       (assoc :object-name (keyword name))
                                       (filter-extra-props [:actions :transition :scene-name]))
                      :group (let [group-params (with-group-params object)
                                   children-params (->> (:children object)
                                                        (map (fn [name] (prepare-object-data name scene-id get-data)))
                                                        (remove nil?))]
                               (-> (merge object
                                          group-params
                                          {:object-name (keyword name)
                                           :children    children-params})
                                   (filter-extra-props [:transition :width :height])))
                      ;:placeholder [placeholder scene-id name o]
                      :animation (let [anim-object (prepare-anim-object-params object)
                                       animation-name (or (:scene-name anim-object) (:name anim-object))]
                                   (-> anim-object
                                       (with-group-params)
                                       (assoc :object-name (keyword name))
                                       (assoc :on-mount #(re-frame/dispatch [::ier/register-animation animation-name %]))
                                       (filter-extra-props [:actions :scene-name :transition :width :height :origin :meshes])))
                      :text (-> object
                                (with-group-params)
                                (merge {:object-name (keyword name)})
                                (filter-extra-props []))
                      :carousel (-> object
                                    (merge {:object-name (keyword name)})
                                    (filter-extra-props [:transition]))
                      :painting-area (-> object
                                         (with-group-params)
                                         (assoc :object-name (keyword name))
                                         (filter-extra-props []))
                      ;:copybook [copybook o]
                      :colors-palette (-> object
                                          (with-group-params)
                                          (assoc :object-name (keyword name))
                                          (filter-extra-props []))
                      :video (-> (merge object
                                        {:object-name (keyword name)}
                                        (with-group-params object))
                                 (filter-extra-props []))
                      ;:animated-svg-path [animated-svg-path (prepare-animated-svg-path-params o)]
                      ;:svg-path [svg-path o]
                      ;:matrix [matrix-object scene-id name o draw-object]
                      :propagate (-> object
                                     (with-group-params)
                                     (merge {:type        "group"
                                             :object-name (keyword name)})
                                     (filter-extra-props [:el-height :el :width :el-width :height]))
                      (do (.warn js/console "[PARAMS PREPARING]" (str "Object with type " type " can not be parsed because it is not defined"))
                          nil)
                      ;(throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined")))
                      )]
    (-> object-data
        (filter-extra-props [:actions :states]))))

(defn get-object-data
  ([scene-id name]
   (prepare-object-data name scene-id (fn [name] @(re-frame/subscribe [::subs/scene-object-with-var scene-id name]))))
  ([scene-id name objects-data]
   (prepare-object-data name scene-id (fn [name] (get objects-data (keyword name))))))

;(declare group)
;(declare matrix-object)
;(declare placeholder)
;(declare button)
;(declare image)
;(declare animation)
;(declare text)
;(declare carousel-object)
;(declare get-painting-area)
;(declare get-colors-palette)
;(declare background)
;(declare layered-background)
;(declare empty-component)

;(defn empty-component [_] nil)

;(defn prepare-anim-rect-params
;  [params]
;  (-> {:width   (:width params)
;       :height  (:height params)
;       :opacity 0
;       :scale-y -1}
;      (with-parent-origin params)
;      (with-origin-offset)
;      ))

;(defn prepare-painting-area-params
;  [object]
;  (-> object
;      (merge {:key (:var-name object)
;              :on-change #(re-frame/dispatch [::vars.events/execute-set-progress {:var-name  (:var-name object)
;                                                                                  :var-value %}])})))

;(defn prepare-colors-palette-params
;  [object]
;  (-> object
;      prepare-actions))

;(defn prepare-animated-svg-path-params
;  [object]
;  (-> object
;      (merge {:data (:path object)})
;      (dissoc :path)))

;(defn placeholder
;  [scene-id name {item :var :as object}]
;  [image scene-id name (cond-> object
;                               :always (assoc :type "image")
;                               (contains? object :image-src) (assoc :src (get item (-> object :image-src keyword)))
;                               (contains? object :image-width) (assoc :width (get item (-> object :image-width keyword)))
;                               (contains? object :image-height) (assoc :height (get item (-> object :image-height keyword))))])
;
;(defn text
;  [scene-id name object]
;  [:> Group (prepare-group-params object)
;   (if (:chunks object)
;     (let [on-mount #(re-frame/dispatch [::ie/register-text %])]
;       [chunked-text scene-id name (assoc object :on-mount on-mount)])
;     [:> Text (dissoc object :x :y)])])
;
;(defn get-painting-area
;  [_ _ params]
;  [painting-area (prepare-painting-area-params params)])
;
;(defn get-colors-palette
;  [_ _ params]
;  [colors-palette (prepare-colors-palette-params params)])

;(defn group
;  [scene-id name object]
;  [:> Group (prepare-group-params object)
;   (for [child (:children object)]
;     ^{:key (str scene-id child)} [draw-object scene-id child])])

;(defn matrix-object
;  [scene-id name object d]
;  [:> Group (prepare-group-params object)
;   (matrix-objects-list object scene-id d)])

;(defn image
;  [scene-id name object]
;  [renderer/image (merge {:src (:src object)}
;                         (prepare-group-params object)
;                         (filter-params object))])

;(defn button
;  [scene-id name object]
;  [components/button-interpreter (-> object
;                                     (merge {:name name :scene-id scene-id})
;                                     (prepare-group-params))])

;(defn background
;  [scene-id name object]
;  [renderer/image (merge {:src       (:src object)
;                          :listening false}
;                         (filter-params object))])

;(defn layered-background
;  [scene-id name object]
;  (let [layers (map (fn [key] {:key key
;                               :src (get-data-as-url (get-in object [key :src]))})
;                    [:background :surface :decoration])]
;    [:> Group {}
;     (for [{:keys [key src]} layers]
;       (when src
;         ^{:key key}
;         [kimage (merge {:src       src
;                         :listening false}
;                        (filter-params (first (:data object))))]))]))

;(defn animation
;  [_ _ object]
;  (let [anim-object (prepare-anim-object-params object)
;        animation-name (or (:scene-name anim-object) (:name anim-object))
;        animation-params (-> anim-object
;                             (merge (prepare-group-params anim-object))
;                             (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %])))
;        filtered-animation-params (reduce (fn [params param-to-remove]
;                                            (dissoc params param-to-remove))
;                                          animation-params
;                                          [:actions :listening :scene-name :states :transition :type :width :height :origin])]
;    [renderer/animation filtered-animation-params]))

;(defn carousel-object [scene-id name object]
;  [:> Group (prepare-group-params object)
;   [carousel object]])

;(defn navigation-helper []
;  (let [position @(re-frame/subscribe [::subs/navigation])]
;    (when position
;      [:> Group (select-keys position [:x :y])
;       [kimage {:src (get-data-as-url "/raw/img/ui/hand.png")}]])))

;
;(defn group
;  [scene-id name object]
;  [:> Group (prepare-group-params object)
;   (for [child (:children object)]
;     ^{:key (str scene-id child)} [draw-object scene-id child])])

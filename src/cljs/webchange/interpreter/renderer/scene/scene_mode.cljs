(ns webchange.interpreter.renderer.scene.scene-mode
  (:require
    [webchange.interpreter.renderer.scene.modes.enable :refer [enable-mode-props enable-mode-helpers!]]
    [webchange.logger.index :as logger]))

(declare init-mode-props)
(declare init-mode-object-props)

(defn- update-children
  [{:keys [type] :as object} mode]
  (case type
    "flipbook" (update object :pages (fn [children]
                                       (map (fn [[object action]]
                                              [(init-mode-object-props object mode) action])
                                            children)))
    (update object :children #(init-mode-props % mode))))

(defn- init-mode-object-props
  [object mode]
  (-> (enable-mode-props mode object)
      (update-children mode)))

(defn- init-mode-props
  [objects mode]
  (map #(init-mode-object-props % mode) objects))

(defn- check-mode
  [{:keys [interpreter-mode]} current-mode]
  (let [current-mode (clojure.core/name current-mode)
        object-modes (clojure.string/split interpreter-mode ",")]
    (if (some? interpreter-mode)
      (some (fn [object-mode]
              (let [[negative? mode] (if (clojure.string/starts-with? object-mode "!")
                                       [true (subs object-mode 1)]
                                       [false object-mode])]
                (if negative?
                  (not= mode current-mode)
                  (= mode current-mode))))
            object-modes)
      true)))

(defn- filter-children
  [{:keys [type] :as object} mode]
  (case type
    "group" (assoc object :children (reduce (fn [result child]
                                              (if (check-mode child mode)
                                                (conj result (filter-children child mode))
                                                result))
                                            []
                                            (:children object)))
    "flipbook" (assoc object :pages (reduce (fn [result [page action]]
                                              (if (check-mode page mode)
                                                (conj result [(filter-children page mode) action])
                                                result))
                                            []
                                            (:pages object)))
    object))

(defn- filter-mode-objects
  [objects mode]
  (reduce (fn [result object]
            (if (check-mode object mode)
              (conj result (filter-children object mode))
              result))
          []
          objects))

(defn apply-mode
  [objects mode]
  (-> objects
      (filter-mode-objects mode)
      (init-mode-props mode)))

(defn init-mode-object-helpers!
  [{:keys [props wrapper children]} mode]
  (enable-mode-helpers! mode (:object wrapper) props)
  (doseq [child children]
    (init-mode-object-helpers! child mode)))

(defn init-mode-helpers!
  [scene-object mode]
  (logger/group-folded "init mode helpers" (clojure.core/name mode))
  (init-mode-object-helpers! scene-object mode)
  (logger/group-end "init mode helpers" (clojure.core/name mode))
  scene-object)

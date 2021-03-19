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

(defn init-mode-props
  [objects mode]
  (map #(init-mode-object-props % mode) objects))

(defn init-mode-object-helpers!
  [{:keys [props wrapper children]} mode]
  (enable-mode-helpers! mode (:object wrapper) props)
  (doseq [child children]
    (init-mode-object-helpers! child mode)))

(defn init-mode-helpers!
  [scene-object mode]
  (logger/group-folded "init mode helpers" (clojure.core/name mode))
  (init-mode-object-helpers! scene-object mode)
  (logger/group-end "init mode helpers" (clojure.core/name mode)))

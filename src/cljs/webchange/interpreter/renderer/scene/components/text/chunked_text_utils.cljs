(ns webchange.interpreter.renderer.scene.components.text.chunked-text-utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [chunk-transition-name]]
    [webchange.interpreter.renderer.scene.components.text.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.state.scene :as scene]))


(defn- register-chunk
  [{:keys [chunk text-object]} type object-name]
  (let [chunk-name (chunk-transition-name (name object-name) (:index chunk))
        wrapper (wrap type (keyword chunk-name) text-object nil)]
    (re-frame/dispatch [::ier/register-transition chunk-name (atom wrapper)])
    (when (some? chunk-name)
      (re-frame/dispatch [::scene/register-object wrapper]))))

(defn register-chunks
  [chunks object-name type]
  (doall (map #(register-chunk % type object-name) chunks)))

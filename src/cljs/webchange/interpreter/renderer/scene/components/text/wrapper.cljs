(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as events]
    [webchange.interpreter.renderer.scene.components.text.chunked-text :refer [create-chunked-text]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [chunk-transition-name]]
    [webchange.interpreter.renderer.scene.components.text.utils :as utils]
    [webchange.interpreter.renderer.scene.components.utils :refer [emit]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.logger.index :as logger]))

(declare wrap)

(defn- register-chunk
  [{:keys [chunk text-object]} type object-name state]
  (let [chunk-name (chunk-transition-name (name object-name) (:index chunk))
        wrapper (wrap type (keyword chunk-name) text-object nil nil)]
    (when (some? chunk-name)
      (swap! state update :chunks conj chunk-name)
      (re-frame/dispatch [::events/register-transition chunk-name (atom wrapper)])
      (re-frame/dispatch [::scene/register-object wrapper]))))

(defn register-chunks
  [chunks object-name type state]
  (doall (map #(register-chunk % type object-name state) chunks)))

(defn- reset-text-chunks
  [state value]
  (let [{:keys [chunks container]} @state]
    (doseq [chunk-name chunks]
      (re-frame/dispatch [::scene/unregister-object (keyword chunk-name)])
      (re-frame/dispatch [::events/unregister-transition chunk-name]))
    (doseq [child (.removeChildren container)]
      (.destroy child))

    (swap! state assoc :chunks [])
    (swap! state assoc-in [:props :text] value)

    (let [props (:props @state)
          chunks (create-chunked-text container props)]
      (register-chunks chunks (:object-name props) (:type props) state))))

(defn wrap
  [type name text-object chunks state]
  (create-wrapper {:name                    name
                   :type                    type
                   :object                  text-object
                   :chunks                  chunks
                   :set-text                (fn [value]
                                              (if (:chunked? @state)
                                                (reset-text-chunks state value)
                                                (aset text-object "text" value))

                                              (emit text-object "textChanged"))
                   :set-highlight           (fn [highlight]
                                              (let [highlight-filter-set (f/has-filter-by-name text-object "glow")]
                                                (if (and (not highlight) highlight-filter-set) (f/set-filter text-object "" {}))
                                                (if (and highlight (not highlight-filter-set))
                                                  (f/set-filter text-object "glow" {}))))

                   :set-permanent-pulsation (fn [permanent-pulsation]
                                              (let [pulsation-filter-set (f/has-filter-by-name text-object "pulsation")]
                                                (if (and (not permanent-pulsation) pulsation-filter-set) (f/set-filter text-object "" {}))
                                                (if (and permanent-pulsation (not pulsation-filter-set))
                                                  (f/set-filter text-object "pulsation" (assoc permanent-pulsation :no-interval true)))))
                   :set-fill                (fn [value]
                                              (utils/set-fill text-object value))
                   :get-fill                (fn []
                                              (utils/get-fill text-object))
                   :set-font-size           (fn [font-size]
                                              (utils/set-font-size text-object font-size)
                                              (emit text-object "fontSizeChanged"))
                   :set-align               (fn [align]
                                              (swap! state assoc-in [:props :align] align)
                                              (emit text-object "textAlignChanged" state))
                   :set-font-family         (fn [font-family]
                                              (if (some? font-family)
                                                (do (utils/set-font-family text-object font-family)
                                                    (emit text-object "fontFamilyChanged"))
                                                (logger/warn "[:set-font-family] Font family is not defined")))}))

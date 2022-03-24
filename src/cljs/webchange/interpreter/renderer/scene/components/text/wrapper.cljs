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
        wrapper (wrap type (keyword chunk-name) text-object nil (atom {:is-chunk true}))]
    (when (some? chunk-name)
      (swap! state update :chunks conj chunk-name)
      (re-frame/dispatch [::events/register-transition chunk-name (atom wrapper)])
      (re-frame/dispatch [::scene/register-object wrapper]))))

(defn register-chunks
  [chunks object-name type state]
  (doall (map #(register-chunk % type object-name state) chunks)))

(defn- create-chunks!
  [state]
  (let [{:keys [container props]} @state
        chunks (create-chunked-text container props)]
    (register-chunks chunks (:object-name props) (:type props) state)))

(defn- reset-chunks!
  [state]
  (let [{:keys [chunks container]} @state]
    (doseq [chunk-name chunks]
      (re-frame/dispatch [::scene/unregister-object (keyword chunk-name)])
      (re-frame/dispatch [::events/unregister-transition chunk-name]))
    (doseq [child (.removeChildren container)]
      (.destroy child))
    (swap! state assoc :chunks [])))

(defn- update-chunks!
  [state chunks]
  (reset-chunks! state)
  (swap! state assoc-in [:props :chunks] chunks)
  (create-chunks! state))

(defn- update-text!
  [state text]
  (reset-chunks! state)
  (swap! state assoc-in [:props :text] text)
  (create-chunks! state))

(defn wrap
  [type name text-object chunks state]
  (create-wrapper {:name                    name
                   :type                    type
                   :object                  text-object
                   :chunks                  chunks
                   :set-text                (fn [value]
                                              (swap! state assoc-in [:props :text] value)

                                              (if (:chunked? @state)
                                                (update-text! state value)
                                                (utils/check-text-placeholder text-object (:props @state)))

                                              (emit text-object "textChanged"))
                   :update-chunks           (fn [{:keys [callback params]}]
                                              (print "new chunks" (get params :chunks []))
                                              (->> (map merge
                                                        (get-in @state [:props :chunks])
                                                        (get params :chunks []))
                                                   (update-chunks! state))
                                              (when (fn? callback)
                                                (callback)))
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
                                              (swap! state assoc-in [:props :fill] value)
                                              (if (:is-chunk @state)
                                                (utils/set-fill text-object value)
                                                (utils/check-text-placeholder text-object (:props @state))))
                   :get-fill                (fn []
                                              (utils/get-fill text-object))
                   :set-font-size           (fn [font-size]
                                              (utils/set-font-size text-object font-size)
                                              (emit text-object "fontSizeChanged"))
                   :set-align               (fn [align]
                                              (swap! state assoc-in [:props :align] align)
                                              (utils/set-align text-object align)
                                              (emit text-object "textAlignChanged" state))
                   :set-font-family         (fn [font-family]
                                              (if (some? font-family)
                                                (do (utils/set-font-family text-object font-family)
                                                    (emit text-object "fontFamilyChanged"))
                                                (logger/warn "[:set-font-family] Font family is not defined")))}))

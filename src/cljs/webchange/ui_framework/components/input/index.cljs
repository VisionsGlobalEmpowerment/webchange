(ns webchange.ui-framework.components.input.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- subscribe-document
  [handler]
  (.addEventListener js/document "keydown" handler false))

(defn- unsubscribe-document
  [handler]
  (.removeEventListener js/document "keydown" handler false))

(defn- subscribe-document?
  [{:keys [on-esc-press]}]
  (fn? on-esc-press))

(defn component
  [{:keys [class-name
           default-value
           disabled?
           id
           value
           on-click
           on-change
           on-enter-press
           on-esc-press
           placeholder
           select-on-focus?
           type]
    :as   props
    :or   {disabled?        false
           on-change        #()
           placeholder      ""
           select-on-focus? false
           type             "str"}}]
  (r/with-let [handle-document-key-down (fn [event]
                                          (case (.-keyCode event)
                                            27 (when (fn? on-esc-press) (on-esc-press))
                                            "default"))
               _ (when (subscribe-document? props)
                   (subscribe-document handle-document-key-down))]
    (let [handle-change #(-> % (.. -target -value) (on-change))
          handle-click (fn [event]
                         (when select-on-focus? (.select (.-target event)))
                         (when (fn? on-click) (on-click event)))
          handle-key-press (fn [event]
                             (case (.-key event)
                               "Enter" (when (fn? on-enter-press) (on-enter-press (.. event -target -value)))
                               "default"))]
      [:input (cond-> {:class-name  (get-class-name (-> {"wc-input" true}
                                                        (assoc class-name (some? class-name))))
                       :disabled    disabled?
                       :placeholder placeholder
                       :on-change   handle-change
                       :on-click    handle-click}
                      (= type "int") (assoc :type "number")
                      (some? id) (assoc :id id)
                      (some? value) (assoc :value value)
                      (some? default-value) (assoc :default-value default-value)
                      (fn? on-enter-press) (assoc :on-key-press handle-key-press))])
    (finally
      (unsubscribe-document handle-document-key-down))))

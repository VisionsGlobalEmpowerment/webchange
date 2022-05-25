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
  [{:keys [action
           class-name
           default-value
           disabled?
           error
           id
           value
           label
           on-blur
           on-click
           on-change
           on-enter-press
           on-esc-press
           on-focus
           on-key-down
           placeholder
           ref-atom
           required?
           select-on-focus?
           step
           type]
    :as   props
    :or   {disabled?        false
           on-change        #()
           placeholder      ""
           required?        false
           select-on-focus? false
           step             1
           type             "str"}}]
  (r/with-let [handle-document-key-down (fn [event]
                                          (case (.-keyCode event)
                                            27 (when (fn? on-esc-press) (on-esc-press))
                                            "default"))
               _ (when (subscribe-document? props)
                   (subscribe-document handle-document-key-down))]
    (let [handle-change (fn [event]
                          (when (fn? on-change)
                            (on-change (cond->> (.. event -target -value)
                                                (= type "int") (.parseInt js/Number)
                                                (= type "float") (.parseFloat js/Number)))))
          handle-click (fn [event]
                         (when select-on-focus? (.select (.-target event)))
                         (when (fn? on-click) (on-click event)))
          handle-key-down (fn [event]
                            (on-key-down {:key (.-key event)}))
          handle-key-press (fn [event]
                             (case (.-key event)
                               "Enter" (when (fn? on-enter-press) (on-enter-press (.. event -target -value)))
                               "default"))
          handle-blur #(on-blur (.. % -target -value))
          handle-focus on-focus
          handle-ref-atom (fn [ref]
                            (when (some? ref)
                              (reset! ref-atom ref)))

          has-label? (some? label)
          id (or id (when has-label? (random-uuid)))]
      [:div {:class-name (get-class-name (-> {"wc-input-wrapper" true}
                                             (assoc class-name (some? class-name))))}
       (when has-label?
         [:label {:for        id
                  :class-name "wc-input-label"}
          label])
       [:input (cond-> {:class-name  (get-class-name {"wc-input"       true
                                                      "wc-input-error" (some? error)})
                        :disabled    disabled?
                        :placeholder (cond-> placeholder
                                             required? (str " *"))
                        :on-input    handle-change
                        :on-change   handle-change
                        :on-click    handle-click}
                       (or (= type "int")
                           (= type "float")) (-> (assoc :type "number")
                                                 (assoc :step step))
                       (= type "password") (assoc :type "password")
                       (some? id) (assoc :id id)
                       (some? value) (assoc :value value)
                       (some? default-value) (assoc :default-value default-value)
                       (some? ref-atom) (assoc :ref handle-ref-atom)
                       (fn? on-enter-press) (assoc :on-key-press handle-key-press)
                       (fn? on-key-down) (assoc :on-key-down handle-key-down)
                       (fn? on-blur) (assoc :on-blur handle-blur)
                       (fn? on-focus) (assoc :on-focus handle-focus))]
       (when (some? error)
         [:label.wc-error error])
       (when (some? action)
         [:div.action-wrapper action])])
    (finally
      (unsubscribe-document handle-document-key-down))))

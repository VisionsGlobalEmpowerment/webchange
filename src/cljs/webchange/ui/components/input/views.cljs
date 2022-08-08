(ns webchange.ui.components.input.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.icon.views :refer [general-icon]]
    [webchange.ui.components.input-error.views :refer [input-error]]
    [webchange.ui.components.input-label.views :refer [input-label]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- subscribe-document
  [handler]
  (.addEventListener js/document "keydown" handler false))

(defn- unsubscribe-document
  [handler]
  (.removeEventListener js/document "keydown" handler false))

(defn- subscribe-document?
  [{:keys [on-esc-press]}]
  (fn? on-esc-press))

(defn input
  [{:keys [action
           class-name
           default-value
           disabled?
           error
           icon
           id
           value
           label
           name
           on-blur
           on-click
           on-change
           on-enter-press
           on-esc-press
           on-focus
           on-key-down
           placeholder
           ref
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
                                                (= type "int") (.parseInt js/Number)))))
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
          handle-ref (fn [el]
                       (when (some? el)
                         (when (fn? ref)
                           (ref el))
                         (when (some? ref-atom)
                           (reset! ref-atom el))))

          has-label? (some? label)
          id (or id (when has-label? (random-uuid)))]
      [:div {:class-name       (get-class-name (-> {"bbs--input-wrapper" true}
                                                   (assoc class-name (some? class-name))))
             :data-wrapper-for id}
       (when (or has-label? (some? error))
         [:div {:class-name "bbs--input-wrapper--header"}
          (when has-label?
            [input-label {:for       id
                          :required? required?}
             label])
          (when (some? error)
            [input-error error])])
       [:input (cond-> {:class-name  (get-class-name {"bbs--input" true})
                        :disabled    disabled?
                        :placeholder (cond-> placeholder
                                             (and required?
                                                  (-> placeholder empty? not)) (str " *"))
                        :on-input    handle-change
                        :on-change   handle-change
                        :on-click    handle-click}
                       (or (= type "int")
                           (= type "float")) (-> (assoc :type "number")
                                                 (assoc :step step))
                       (= type "range") (-> (assoc :step (:step props))
                                            (assoc :min (:min props))
                                            (assoc :max (:max props)))
                       (some? type) (assoc :type type)
                       (some? id) (assoc :id id)
                       (some? name) (assoc :name name)
                       (some? value) (assoc :value value)
                       (some? default-value) (assoc :default-value default-value)
                       (or (some? ref)
                           (some? ref-atom)) (assoc :ref handle-ref)
                       (fn? on-enter-press) (assoc :on-key-press handle-key-press)
                       (fn? on-key-down) (assoc :on-key-down handle-key-down)
                       (fn? on-blur) (assoc :on-blur handle-blur)
                       (fn? on-focus) (assoc :on-focus handle-focus))]

       (when (some? icon)
         [:div.icon-wrapper [general-icon {:icon icon}]])
       (when (some? action)
         (let [with-text? (some? (:text action))
               button-props (merge {:shape "rounded"
                                    :color (if with-text? "yellow-1" "grey-3")}
                                   action)]
           [:div.action-wrapper
            (if with-text?
              [button button-props (:text action)]
              [button button-props])]))])
    (finally
      (unsubscribe-document handle-document-key-down))))

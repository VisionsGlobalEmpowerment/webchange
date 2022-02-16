(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-common
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn target-control
  [{:keys [value options on-change show-value-only?]}]
  (let [value-str (cond
                    (empty? value) "select target"
                    show-value-only? value
                    :default (or (some (fn [option]
                                         (and (= value (:value option))
                                              (:text option)))
                                       options)
                                 value))]
    [menu {:class-name "targets-menu"
           :el         (r/as-element [:span.target-value value-str])
           :items      (->> options
                            (map (fn [{:keys [text text-prefix value]}]
                                   {:text        text
                                    :text-prefix text-prefix
                                    :on-click    #(on-change value)})))}]))

(defn- empty-text-value?
  [value]
  (or (= value nil) (= value "")))

(defn- select-content-editable-text!
  [el]
  (let [range (.createRange js/document)
        selection (.getSelection js/window)
        text-node (-> (.-childNodes el) (.item 0))]
    (.setStart range text-node 0)
    (.setEnd range text-node (.-length text-node))
    (.removeAllRanges selection)
    (.addRange selection range)))

(defn text-control
  [{:keys [on-change on-enter-press on-ctrl-enter-press placeholder]}]
  (r/with-let [ref (atom nil)
               current-value (atom nil)
               empty-value? (r/atom false)
               handle-key-down (fn [e]
                                 (when (= (.-key e) "Enter")
                                   (.preventDefault e)
                                   (if (.-ctrlKey e)
                                     (on-ctrl-enter-press)
                                     (on-enter-press))))
               check-empty-value! (fn []
                                    (if (empty-text-value? @current-value)
                                      (do (reset! empty-value? true)
                                          (->> (or placeholder "Enter text")
                                               (set! (.-innerText @ref))))
                                      (reset! empty-value? false)))
               handle-focus (fn []
                              (when (empty-text-value? @current-value)
                                (select-content-editable-text! @ref)))

               handle-blur check-empty-value!]
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :component-did-mount
                     (fn [this]
                       (let [{:keys [editable? value]} (r/props this)]
                         (reset! current-value value)
                         (check-empty-value!)
                         (when editable?
                           (.addEventListener @ref "keydown" handle-key-down false))))
       :component-will-unmount
                     (fn []
                       (.removeEventListener @ref "keydown" handle-key-down))
       :reagent-render
                     (fn [{:keys [value editable? placeholder]}]
                       (let [show-placeholder? (nil? value)
                             handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (reset! current-value new-value)
                                               (on-change new-value)
                                               (check-empty-value!)))]
                         [:span (cond-> {:class-name (get-class-name {"text"          true
                                                                      "text-disabled" (not editable?)
                                                                      "placeholder"   @empty-value?})
                                         :ref        #(when (some? %) (reset! ref %))
                                         :on-focus   handle-focus
                                         :on-blur    handle-blur}
                                        editable? (merge {:on-input                          handle-change
                                                          :content-editable                  true
                                                          :suppress-content-editable-warning true}))
                          (if show-placeholder?
                            placeholder
                            value)]))})))

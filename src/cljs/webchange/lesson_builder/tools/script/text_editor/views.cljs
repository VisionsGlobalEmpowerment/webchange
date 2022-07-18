(ns webchange.lesson-builder.tools.script.text-editor.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn- empty-text-value?
  [value]
  (or (= value nil) (= value "")))

(defn- select-content-editable-text!
  [el]
  (let [selection (.getSelection js/window)]
    (.selectAllChildren selection el)
    (.collapseToEnd selection)))

(defn- text-control
  [{:keys [class-name on-change on-enter-press on-ctrl-enter-press placeholder on-parent-focus]}]
  (let [ref (atom nil)
        current-value (atom nil)
        empty-value? (r/atom false)
        handle-key-down (fn [e]
                          (when (= (.-key e) "Enter")
                            (.preventDefault e)
                            (if (.-ctrlKey e)
                              (on-ctrl-enter-press)
                              (on-enter-press))))
        try-placeholder! (fn []
                           (if (empty-text-value? @current-value)
                             (do (reset! empty-value? true)
                                 (->> (or placeholder "Enter text")
                                      (set! (.-innerText @ref))))
                             (reset! empty-value? false)))
        try-empty! (fn []
                     (when @empty-value?
                       (set! (.-innerText @ref) "")
                       (reset! empty-value? false)))
        check-and-select! (fn []
                            (when @ref
                              (try-empty!)
                              (select-content-editable-text! @ref)))
        handle-blur (fn [] (try-placeholder!))]
    (when on-parent-focus
      (swap! on-parent-focus conj check-and-select!))
    (r/create-class
      {:display-name "text-control"

       :should-component-update
       (constantly false)

       :component-did-mount
       (fn [this]
         (let [{:keys [editable? value]} (r/props this)]
           (reset! current-value value)
           (try-placeholder!)
           (when editable?
             (.addEventListener @ref "keydown" handle-key-down false))))

       :component-will-unmount
       (fn []
         (.removeEventListener @ref "keydown" handle-key-down))

       :reagent-render
       (fn [{:keys [value editable? placeholder]
             :or   {editable? true}}]
         (let [show-placeholder? (nil? value)
               handle-change (fn [event]
                               (let [new-value (.. event -target -innerText)]
                                 (reset! current-value new-value)
                                 (on-change new-value)))]
           [:span (cond-> {:class-name (ui/get-class-name {"text"          true
                                                           "text-disabled" (not editable?)
                                                           "placeholder"   @empty-value?
                                                           class-name      (some? class-name)})
                           :ref        #(when (some? %) (reset! ref %))
                           :on-click   #(do (try-empty!)
                                            (.stopPropagation %))
                           :on-blur    handle-blur}
                          editable? (merge {:on-input                          handle-change
                                            :content-editable                  true
                                            :suppress-content-editable-warning true}))
            (if show-placeholder?
              placeholder
              value)]))})))

(defn text-editor
  [{:keys [actions class-name on-change value]}]
  [:div {:class-name (ui/get-class-name {"component--text-editor" true
                                         class-name               (some? class-name)})}
   [ui/icon {:icon       "character"
             :class-name "text-editor--icon"}]
   [text-control {:value       value
                  :on-change   on-change
                  :placeholder "Enter phrase text"
                  :class-name  "text-editor--value"}]
   (when-not (empty? actions)
     [:div.text-editor--actions
      (for [[idx action] (map-indexed vector actions)]
        ^{:key idx}
        [ui/button (merge {:class-name "text-editor--action"}
                          action)])])])

(ns webchange.ui-framework.components.select-image.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-bounding-rect get-class-name]]))

(defn- current-value
  [{:keys [on-click ref show-image? value value-img variant]}]
  [:div {:class-name (get-class-name {"current-value"          true
                                      (str "variant-" variant) (some? variant)})
         :on-click   on-click
         :ref        #(when (some? %) (reset! ref %))}
   (cond
     (and (some? value-img) show-image?) [:img {:src value-img}]
     (some? value) [:div.no-image value]
     :else [:div.no-value])
   [:div.arrow]])

(defn- options-list-empty-item
  [{:keys [on-click]}]
  (let [handle-click #(on-click nil)]
    [:div {:class-name "options-list-item empty"
           :on-click   handle-click}
     "None"]))

(defn- options-list-item
  [{:keys [on-click text thumbnail value]}]
  (let [handle-click #(on-click value)
        title (or text value)]
    (if (some? thumbnail)
      [:div.options-list-item
       [:img {:src      thumbnail
              :on-click handle-click
              :title    title}]]
      [:div {:class-name "options-list-item no-image"
             :on-click   handle-click}
       title])))

(defn- options-list
  [{:keys [allow-empty? on-click options dimensions ref]}]
  [:div {:class-name "options-list"
         :style      {:top   (:top dimensions)
                      :left  (:left dimensions)
                      :width (:width dimensions)}
         :ref        #(when (some? %) (reset! ref %))}
   [:div.items-wrapper
    (when allow-empty?
      [options-list-empty-item {:on-click on-click}])
    (for [{:keys [value] :as option} options]
      ^{:key value}
      [options-list-item (merge option
                                {:on-click on-click})])]])

(defn- get-menu-position
  [menu-button]
  (let [{:keys [x y width height]} (get-bounding-rect menu-button)]
    {:top   (+ y height)
     :left  x
     :width width}))

(defn- img-src?
  [current-value options]
  (some (fn [{:keys [value thumbnail]}]
          (and (= current-value value)
               thumbnail))
        options))

(defn- current-value-in-options?
  [options current-value]
  (some (fn [{:keys [value]}]
          (= value current-value))
        options))

(defn- add-to-options
  [options value]
  (concat [{:value     value
            :thumbnail value}]
          options))

(defn component
  [{:keys [allow-empty? class-name disabled? on-change options show-image? title value variant]
    :or   {allow-empty? false
           disabled?    false
           on-change    #()
           options      []
           show-image?  true}}]
  (r/with-let [show-options-list? (r/atom false)
               options-list-ref (r/atom nil)
               options-list-dimensions (r/atom {:top  0
                                                :left 0})

               current-value-ref (atom nil)
               close-menu-ref (atom nil)

               handle-document-click #(when-not (.contains @options-list-ref (.-target %)) (@close-menu-ref))
               set-document-click-handler #(js/document.addEventListener "click" handle-document-click)
               reset-document-click-handler #(js/document.removeEventListener "click" handle-document-click)

               close-menu #(do (reset! show-options-list? false)
                               (reset-document-click-handler))

               handle-list-item-click (fn [value]
                                        (close-menu)
                                        (on-change value))

               _ (reset! close-menu-ref close-menu)]
    (let [show-menu #(when-not disabled?
                       (reset! show-options-list? true)
                       (reset! options-list-dimensions (get-menu-position @current-value-ref))
                       (set-document-click-handler))
          options (if (and (some? value)
                           (->> value (current-value-in-options? options) (not)))
                    (add-to-options options value)
                    options)]
      [:div (cond-> {:class-name (get-class-name (cond-> {"wc-select-image" true
                                                          "disabled"        disabled?}
                                                         (some? class-name) (assoc class-name true)))}
                    (some? title) (assoc :title title))
       [current-value {:value       value
                       :value-img   (img-src? value options)
                       :on-click    show-menu
                       :ref         current-value-ref
                       :show-image? show-image?
                       :variant     variant}]
       (when @show-options-list?
         [options-list {:options      options
                        :allow-empty? allow-empty?
                        :on-click     handle-list-item-click
                        :dimensions   @options-list-dimensions
                        :ref          options-list-ref}])])))

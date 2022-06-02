(ns webchange.ui-framework.components.image.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name get-uid]]))

(defonce handlers (atom {}))

(defn- init-image
  [id]
  (let [handler (get @handlers id)]
    (when (fn? handler)
      (handler)
      (swap! handlers dissoc id))))

(defn- register-image
  [id handler]
  (swap! handlers assoc id handler))

(defonce observer (js/IntersectionObserver.
                    (fn [entries]
                      (.forEach entries
                                (fn [entry]
                                  (let [intersection-ratio (.. entry -intersectionRatio)
                                        target-id (.. entry -target -id)
                                        visible? (> intersection-ratio 0)]
                                    (when visible?
                                      (init-image target-id))))))))

(defn- load-image
  [src on-load oe-error]
  (let [img (js/Image.)]
    (set! (.-onload img) on-load)
    (set! (.-onerror img) oe-error)
    (set! (.-src img) src)))

(defn component
  [{:keys [id]}]
  (let [id (atom (or id (get-uid)))
        el (atom nil)
        status (r/atom :loading)
        handle-load (fn []
                      (reset! status :loaded))
        handle-error (fn []
                       (reset! status :error))
        handle-ref (fn [ref]
                     (when (some? ref)
                       (reset! el ref)
                       (.observe observer @el)))]
    (r/create-class
      {:display-name "wc-image"

       :component-did-mount
       (fn [this]
         (let [{:keys [src]} (r/props this)]
           (register-image @id #(load-image src handle-load handle-error))))

       :component-will-unmount
       (fn []
         (.unobserve observer @el))

       :reagent-render
       (fn [{:keys [class-name src]}]
         [:div {:id         @id
                :class-name (get-class-name {"wc-image" true
                                             class-name (some? class-name)})
                :title      src
                :ref        handle-ref}
          (case @status
            :loading [:div.loading
                      [circular-progress/component]]
            :loaded [:img {:src src}]
            :error [:div.not-loaded
                    [icon/component {:icon "image-broken"}]])])})))

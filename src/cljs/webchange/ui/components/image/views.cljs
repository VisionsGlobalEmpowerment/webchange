(ns webchange.ui.components.image.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.progress.views :refer [circular-progress]]
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]
    [webchange.utils.observer :as observer]
    [webchange.utils.uid :refer [get-uid]]))

(defn- load-image
  [src on-load oe-error]
  (let [img (js/Image.)]
    (set! (.-onload img) on-load)
    (set! (.-onerror img) oe-error)
    (set! (.-src img) src)))

(defn image
  [{:keys [id state]}]
  (let [id (or id (get-uid))
        el (atom nil)
        status (r/atom (if (nil? state)
                         :loading
                         (keyword state)))
        handle-load (fn []
                      (reset! status :loaded))
        handle-error (fn []
                       (reset! status :error))
        handle-ref (fn [ref]
                     (when (some? ref)
                       (reset! el ref)))]
    (r/create-class
      {:display-name "bbs--image"

       :component-did-mount
       (fn [this]
         (let [{:keys [src lazy? state]} (r/props this)
               load #(load-image src handle-load handle-error)]
           (when (nil? state)
             (if lazy?
               (observer/observe @el id load)
               (load)))))

       :component-will-unmount
       (fn []
         (observer/un-observe id))

       :reagent-render
       (fn [{:keys [class-name on-click src title]}]
         (->> (r/current-component)
              (r/children)
              (into [:div (cond-> {:id         id
                                   :class-name (get-class-name {"bbs--image" true
                                                                class-name   (some? class-name)})
                                   :title      src
                                   :ref        handle-ref
                                   :on-click   on-click}
                                  (some? title) (assoc :title title))
                     (case @status
                       :loading [:div.loading
                                 [circular-progress]]
                       :loaded [:img {:src src}]
                       :error [:div.not-loaded
                               [system-icon {:icon "image-broken"}]])])))})))

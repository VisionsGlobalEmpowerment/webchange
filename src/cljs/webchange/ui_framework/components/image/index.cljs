(ns webchange.ui-framework.components.image.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.observer :as observer]
    [webchange.utils.uid :refer [get-uid]]))

(defn- load-image
  [src on-load oe-error]
  (let [img (js/Image.)]
    (set! (.-onload img) on-load)
    (set! (.-onerror img) oe-error)
    (set! (.-src img) src)))

(defn component
  [{:keys [id]}]
  (let [id (or id (get-uid))
        el (atom nil)
        status (r/atom :loading)
        handle-load (fn []
                      (reset! status :loaded))
        handle-error (fn []
                       (reset! status :error))
        handle-ref (fn [ref]
                     (when (some? ref)
                       (reset! el ref)))]
    (r/create-class
      {:display-name "wc-image"

       :component-did-mount
       (fn [this]
         (let [{:keys [src lazy?]} (r/props this)
               load #(load-image src handle-load handle-error)]
           (if lazy?
             (observer/observe @el id load)
             (load))))

       :component-will-unmount
       (fn []
         (observer/un-observe id))

       :reagent-render
       (fn [{:keys [class-name src]}]
         (->> (r/current-component)
              (r/children)
              (into [:div {:id         id
                           :class-name (get-class-name {"wc-image" true
                                                        class-name (some? class-name)})
                           :title      src
                           :ref        handle-ref}
                     (case @status
                       :loading [:div.loading
                                 [circular-progress/component]]
                       :loaded [:img {:src src}]
                       :error [:div.not-loaded
                               [icon/component {:icon "image-broken"}]])])))})))

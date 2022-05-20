(ns webchange.ui-framework.components.image.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  []
  (let [status (r/atom :loading)
        handle-load (fn []
                      (reset! status :loaded))
        handle-error (fn []
                       (reset! status :error))]
    (r/create-class
      {:display-name "wc-image"

       :component-did-mount
       (fn [this]
         (let [{:keys [src]} (r/props this)
               img (js/Image.)]
           (set! (.-onload img) handle-load)
           (set! (.-onerror img) handle-error)
           (set! (.-src img) src)))

       :reagent-render
       (fn [{:keys [class-name src]}]
         [:div {:class-name (get-class-name {"wc-image" true
                                             class-name (some? class-name)})
                :title      src}
          (case @status
            :loading [:div.loading
                      [circular-progress/component]]
            :loaded [:img {:src src}]
            :error [:div.not-loaded
                    [icon/component {:icon "image-broken"}]])])})))

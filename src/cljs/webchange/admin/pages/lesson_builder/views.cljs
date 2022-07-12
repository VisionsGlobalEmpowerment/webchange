(ns webchange.admin.pages.lesson-builder.views
  (:require
    ["react" :as react]
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.utils.lazy-component :refer [lazy-component]]))

(defn page
  [{:keys [activity-id]}]
  (let [lesson-builder (-> webchange.lesson-builder.views/index
                           (shadow.lazy/loadable)
                           (lazy-component))]
    [:div.page--lesson-builder
     (if (some? activity-id)
       [:> react/Suspense {:fallback (r/as-element [ui/loading-overlay])}
        [:> lesson-builder {:activity-id activity-id}]])]))

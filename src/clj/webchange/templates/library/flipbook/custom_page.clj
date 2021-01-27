(ns webchange.templates.library.flipbook.custom-page
  (:require
    [webchange.templates.library.flipbook.custom-page--image-only :as image-only]
    [webchange.templates.library.flipbook.custom-page--text-at-top :as text-at-top]
    [webchange.templates.library.flipbook.custom-page--text-big-at-bottom :as text-big-at-bottom]
    [webchange.templates.library.flipbook.custom-page--text-only :as text-only]
    [webchange.templates.library.flipbook.custom-page--text-small-at-bottom :as text-small-at-bottom]))

(defn create
  [page-params {:keys [page-type] :as content-params}]
  (case page-type
    :image-only (image-only/create page-params content-params)
    :text-at-top (text-at-top/create page-params content-params)
    :text-big-at-bottom (text-big-at-bottom/create page-params content-params)
    :text-only (text-only/create page-params content-params)
    :text-small-at-bottom (text-small-at-bottom/create page-params content-params)))

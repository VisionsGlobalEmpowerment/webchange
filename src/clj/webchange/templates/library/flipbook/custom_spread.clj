(ns webchange.templates.library.flipbook.custom-spread
  (:require
    [webchange.templates.library.flipbook.custom-spread--text-small-transparent--left :as text-small-transparent--left]
    [webchange.templates.library.flipbook.custom-spread--text-small-transparent--right :as text-small-transparent--right]))

(defn- get-spread-constructors
  [layout]
  (case layout
    :text-small-transparent [text-small-transparent--left/create text-small-transparent--right/create]))

(defn create
  [page-params {:keys [page-type] :as content-params}]
  (let [[left-page-constructor right-page-constructor] (get-spread-constructors page-type)
        left-page-data (left-page-constructor page-params content-params)
        right-page-data (right-page-constructor page-params content-params)]
    [left-page-data right-page-data]))

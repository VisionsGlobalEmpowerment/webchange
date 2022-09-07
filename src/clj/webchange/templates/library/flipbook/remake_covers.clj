(ns webchange.templates.library.flipbook.remake-covers
  (:require
    [webchange.templates.library.flipbook.add-page :refer [add-page] :as page]
    [webchange.templates.library.flipbook.remove-page :refer [remove-page]]
    [webchange.templates.library.flipbook.utils :refer [get-book-object-name]]
    [webchange.utils.list :refer [find-item-position]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]))

(def pages-to-remake [{:object        "page-cover"
                       :constructor   front-cover/create
                       :params-getter page/activity->front-cover-props}
                      {:object        "generic-front-page"
                       :constructor   generic-front/create
                       :params-getter page/activity->generic-front-props}
                      {:object        "credits-page"
                       :constructor   credits/create
                       :params-getter page/activity->credits-props}
                      {:object        "page-cover-back"
                       :constructor   back-cover/create
                       :params-getter page/activity->back-cover-props}])

(defn remake-covers
  [activity-data props page-params]
  (let [book-object-name (get-book-object-name activity-data)
        pages (get-in activity-data [:objects (keyword book-object-name) :pages])]

    (reduce (fn [activity-data {:keys [object constructor params-getter]}]
              (let [page-position (find-item-position pages #(= (:object %) object))]
                (-> activity-data
                    (remove-page {:page-number page-position :force-remove? true} page-params)
                    (add-page constructor page-params (merge (params-getter props)
                                                             {:position page-position})))))
            activity-data
            pages-to-remake)))

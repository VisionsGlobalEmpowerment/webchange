(ns webchange.page-title.views
  (:require
    [reagent.core :as r]
    [webchange.utils.flipbook :as flipbook-utils]))

(def default-title "TabSchool")

(defn- get-flipbook-title
  [{:keys [scene-data]}]
  (let [title (get-in scene-data [:metadata :history :created :cover-title])]
    (when (and (string? title)
               (not-empty title))
      (str "Book: " title))))

(defn- get-page-title
  [{:keys [scene-data title] :as props}]
  (or title
      (cond
        (flipbook-utils/flipbook-activity? scene-data) (get-flipbook-title props))
      default-title))

(defn page-title
  [{:keys []}]
  (r/create-class
    {:display-name   "page-title"
     :component-did-mount
                     (fn [this]
                       (->> (r/props this)
                            (get-page-title)
                            (set! (.-title js/document))))

     :reagent-render (fn [] [:div])}))

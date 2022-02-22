(ns webchange.page-title.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.utils.flipbook :as flipbook-utils]))

(def default-title "TabSchool")

(defn- get-flipbook-title
  [{:keys [scene-data]}]
  (let [title (get-in scene-data [:metadata :history :created :cover-title])]
    (when (and (string? title)
               (not-empty title))
      (str "Book: " title))))

(defn- get-page-title
  [{:keys [scene-data scene-info title] :as props}]
  (let [scene-name (get scene-info :name)]
    (or title
        (cond
          (flipbook-utils/flipbook-activity? scene-data) (get-flipbook-title props)
          (string? scene-name) scene-name)
        default-title)))

(defn page-title
  [{:keys []}]
  (r/create-class
    {:display-name   "page-title"
     :reagent-render (fn [props]
                       (let [scene-info @(re-frame/subscribe [::subs/current-scene-info])]
                         (->> (merge {:scene-info scene-info} props)
                              (get-page-title)
                              (set! (.-title js/document)))
                         [:div {:style {:display "none"}}]))}))

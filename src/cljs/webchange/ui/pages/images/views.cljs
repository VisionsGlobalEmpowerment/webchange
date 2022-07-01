(ns webchange.ui.pages.images.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}
             {:state "loading"}
             {:state "error"}])

(def variants [{:src "/images/app/launcher_icon_192x192.png"
                :class-name "image-example"}])

(defn- component
  [props]
  [ui/image (assoc props :title (js/JSON.stringify (clj->js props)))])

(defn- variant-states
  [{:keys [component variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn page
  []
  [:div#page--images
   [layout {:title "Images"}
    [panel {:class-name "images-panel"}
     (for [[idx variant] (map-indexed vector variants)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx
                        :component   component}])]]])

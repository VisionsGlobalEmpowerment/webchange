(ns webchange.templates.library.flipbook.add-text
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.library.flipbook.display-names :refer [update-display-names]]
    [webchange.utils.flipbook :as f]
    [webchange.utils.scene-action-data :refer [create-text-animation-action]]
    [webchange.utils.scene-data :as s]
    [webchange.utils.text :refer [text->chunks]]))

(defn- get-text-name
  [activity-data page-object-name]
  (let [{:keys [children]} (->> (keyword page-object-name)
                                (s/get-scene-object activity-data))]
    (str page-object-name "-text-" (count children))))

(defn get-text-data
  [text {:keys [width padding]}]
  (let [content {:text      text
                 :chunks    (text->chunks text)
                 :word-wrap true}
        dimensions {:x     padding
                    :y     padding
                    :width (->> (* 2 padding) (- width))}
        align {:align          "left"
               :vertical-align "top"}
        font {:fill        0
              :font-family "Lexend Deca"
              :font-size   38}]
    (merge {:type      "text"
            :editable? {:select true
                        :drag   true
                        :restrict-x true}}
           content dimensions align font)))

(defn add-text
  [activity-data page-idx {:keys [page-params]}]
  (let [{:keys [action object]} (f/get-page-data activity-data page-idx)
        text "Text"
        text-name (get-text-name activity-data object)
        text-data (get-text-data text page-params)
        text-action (create-text-animation-action {:inner-action {:target      text-name
                                                                  :phrase-text text}})]
    (-> activity-data
        (update :objects assoc (keyword text-name) text-data)
        (update-in [:objects (keyword object) :children] concat [text-name])
        (update-in [:actions (keyword action) :data] concat [text-action])
        (update-display-names))))

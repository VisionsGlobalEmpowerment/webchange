(ns webchange.editor-v2.scene.views-data
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.interpreter.core :refer [load-course]]
    [webchange.editor-v2.scene.data.background.views-background :refer [change-background]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.utils :refer [keyword->caption]]))

(def diagram-modes [:full-scene "Full Scene View"
                    :phrases "Translation"])

(defn phrase-action-data?
  [action-data]
  (contains? action-data :phrase))

(defn scene-data->phrases-list
  [scene-data]
  (->> (:actions scene-data)
       (filter (fn [[_ action-data]]
                 (phrase-action-data? action-data)))
       (reduce (fn [result [action-name action-data]]
                 (assoc result action-name {:data   action-data
                                            :phrase (keyword->caption (:phrase action-data))}))
               {})))

(defn scene-data->objects-list
  [scene-data]
  (->> (:objects scene-data)
       (filter (fn [[_ object-data]] (= "text" (:type object-data))))
       (filter (fn [[_ object-data]] (not-empty (:chunks object-data))))))

(defn get-scenes-options
  [scenes-list]
  (let [prepared-scenes ["home"
                         "sandbox"
                         "see-saw"
                         "swings"
                         "volleyball"
                         "library"
                         "book"
                         "hide-n-seek"
                         "cycling"
                         "cinema"
                         "cinema-video"
                         "letter-intro"
                         "park-poem"
                         "running"
                         "slide"
                         "writing-lesson"
                         "writing-practice"
                         "magic-hat"
                         "cycling-letters"
                         "volleyball-letters"
                         "pinata"]]
    (->> scenes-list
         (map (fn [scene-id]
                {:value    scene-id
                 :text     (s/replace scene-id #"-" " ")
                 :disabled (->> prepared-scenes
                                (some #{scene-id})
                                (not))}))
         (sort-by (fn [{:keys [value]}]
                    (let [index (.indexOf prepared-scenes value)]
                      (if (= index -1) 999 index)))))))

(defn data
  []
  (let [course-id (re-frame/subscribe [::subs/current-course])
        scene-id (re-frame/subscribe [::subs/current-scene])
        scenes (re-frame/subscribe [::subs/course-scenes])
        diagram-mode (re-frame/subscribe [::editor-subs/diagram-mode])
        scene-data (re-frame/subscribe [::subs/scene @scene-id])]
    (let [objects (scene-data->objects-list @scene-data)
          scenes-options (get-scenes-options @scenes)]
      [:div.data-selector
       [ui/form-control {:full-width true
                         :margin     "normal"}
        [ui/input-label "Scene"]
        [ui/select {:value     (or @scene-id "")
                    :on-change #(redirect-to :course-editor-v2-scene :id @course-id :scene-id (.. % -target -value))}
         (for [{:keys [value text disabled]} scenes-options]
           ^{:key (str value)}
           [ui/menu-item {:value    value
                          :disabled disabled
                          :style    {:text-transform "capitalize"}}
            text])
         ]]
       [ui/form-control {:full-width true
                         :margin     "normal"}
        [ui/input-label "Diagram Mode"]
        [ui/select {:value     (or @diagram-mode "")
                    :on-change #(let [mode (-> %
                                               (.. -target -value)
                                               (keyword))]
                                  (re-frame/dispatch [::editor-events/set-diagram-mode mode]))}
         (for [[mode-value mode-text] (partition 2 diagram-modes)]
           ^{:key (str mode-value)}
           [ui/menu-item {:value mode-value}
            mode-text])
         ]]
       [change-background]
       (when (not-empty objects)
         [ui/form-control {:full-width true
                           :margin     "normal"}
          [ui/input-label "Select Object"]
          [ui/select {:value     ""
                      :on-change #(let [object-id (-> % (.. -target -value) (keyword))]
                                    (re-frame/dispatch [::editor-events/show-configure-object-form {:path [object-id]}]))}
           (for [[object-id {:keys [type]}] objects]
             ^{:key (name object-id)}
             [ui/menu-item {:value object-id} (str (name object-id) " (" type ")")])]])])))

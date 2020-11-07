(ns webchange.editor-v2.lessons.views-lesson-form-scenes
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.lessons.views-lesson-form-scene-form :refer [edit-scene-modal]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:action-icon             {:font-size "1.4rem"}
   :activity-row            {:display         "flex"
                             :justify-content "space-between"}
   :activity-row-header     {:display         "flex"
                             :justify-content "space-between"
                             :padding-bottom  "6px"
                             :padding-top     "6px"
                             :border-bottom   (str "solid 1px " (get-in-theme [:palette :border :default]))}
   :activity-name           {:flex       "1 1 auto"
                             :text-align "left"}
   :activity-time           {:width      "165px"
                             :padding    "0 16px"
                             :flex       "0 0 auto"
                             :text-align "center"}
   :activity-scored         {:width      "150px"
                             :padding    "0 16px"
                             :flex       "0 0 auto"
                             :text-align "center"}
   :activity-actions        {:width      "90px"
                             :flex       "0 0 auto"
                             :text-align "right"}})

(defn- list-header
  [{:keys [on-add]}]
  (let [items {:activity-name           "Activity"
               :activity-time           "Time Expected (sec)"
               :activity-scored         "Score Expected"}
        styles (get-styles)]
    [ui/list-item {:style (:activity-row-header styles)}
     (for [[key title] items]
       ^{:key key}
       [ui/typography {:variant "body1"
                       :style   (get styles key)}
        title])
     [:div {:style (:activity-actions styles)}
      [ui/tooltip {:title      "Add Scene"
                   :aria-label "Add Scene"
                   :placement  "top"}
       [ui/icon-button {:color    "secondary"
                        :on-click #(do (.stopPropagation %)
                                       (on-add))}
        [ic/add {:style (:action-icon styles)}]]]]]))

(defn- scene-list-item
  [{:keys [idx scene on-edit on-delete]}]
  (let [styles (get-styles)]
    [ui/list-item {:button   true
                   :on-click #(on-edit idx)
                   :style    (:activity-row styles)}
     [ui/typography {:variant "body1"
                     :style   (:activity-name styles)}
      (:activity scene)]
     [ui/typography {:variant "body1"
                     :style   (:activity-time styles)}
      (:time-expected scene)]
     [ui/typography {:variant "body1"
                     :style   (:activity-scored styles)}
      (if (-> scene :scored boolean)
        (when (:expected-score-percentage scene)
          (str (:expected-score-percentage scene) "%"))
        "not scored")]
     [:div {:style (:activity-actions styles)}
      [ui/icon-button {:aria-label "Delete"
                       :on-click   #(do (.stopPropagation %)
                                        (on-delete idx))}
       [ic/delete {:style (:action-icon styles)}]]]]))

(defn- add-scene
  ([storage]
   (add-scene storage {}))
  ([storage scene-data]
   (swap! storage update :activities
          (fn [list]
            (concat [scene-data] list)))
   0))

(defn- edit-scene
  [storage scene-index scene-data]
  (swap! storage update :activities
         (fn [list] (map-indexed (fn [idx item] (if (= scene-index idx) scene-data item)) list))))

(defn- delete-scene
  [storage scene-index]
  (swap! storage update :activities
         (fn [list]
           (keep-indexed (fn [idx item] (if (not= scene-index idx) item)) list))))

(defn- get-scene-by-index
  [scenes searched-index]
  (some (fn [[index scene]]
          (and (= index searched-index)
               scene))
        scenes))

(defn lesson-scenes
  [{:keys [data scenes-list]}]
  (r/with-let [current-scene-index (r/atom nil)]
              (let [items (keep-indexed vector (:activities @data))
                    handle-delete #(delete-scene data %)
                    handle-edit #(reset! current-scene-index %)
                    handle-save #(do (edit-scene data @current-scene-index %)
                                     (reset! current-scene-index nil))
                    handle-add #(handle-edit (add-scene data))
                    handle-close #(do (when % (delete-scene data @current-scene-index))
                                      (reset! current-scene-index nil))]
                [ui/grid {:container true
                          :spacing   32}
                 [ui/grid {:item true :xs 12}
                  [ui/list {:dense true}
                   [list-header {:on-add handle-add}]
                   (for [[item-idx item] (keep-indexed vector (:activities @data))]
                     ^{:key item-idx}
                     [scene-list-item {:idx       item-idx
                                       :scene     item
                                       :on-edit   handle-edit
                                       :on-delete handle-delete}])]
                  (when (-> @current-scene-index boolean)
                    [edit-scene-modal {:scene-data  (get-scene-by-index items @current-scene-index)
                                       :scenes-list scenes-list
                                       :open?       (-> @current-scene-index boolean)
                                       :on-close    handle-close
                                       :on-save     handle-save}])]])))

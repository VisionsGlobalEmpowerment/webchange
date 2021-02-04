(ns webchange.editor-v2.course-dashboard.views-scenes
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.card.views :refer [list-card] :as card]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:skill-list {:color (get-in-theme [:palette :text :primary])}})

(defn- get-scenes-options
  [scenes-list]
  (->> scenes-list
       (map (fn [scene-id]
              {:value scene-id
               :text  (s/replace scene-id #"-" " ")}))
       (sort-by :text)))

(defn- scene-info-data
  [{:keys [scene-id]}]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        styles (get-styles)]
    [:div
     [ui/typography {:variant "title"} "Skills:"]
     [:ul {:style (:skill-list styles)}
      (for [{:keys [id name abbr]} (:skills scene-data)]
        ^{:key id}
        [:li [ui/typography (str "(" abbr ") " name)]])]]))

(defn- scene-info-window
  [{:keys [scene on-close]}]
  [ui/dialog
   {:open     (some? scene)
    :on-close on-close}
   [ui/dialog-title
    "Scene Info"]
   [ui/dialog-content
    [scene-info-data {:scene-id (:value scene)}]]
   [ui/dialog-actions
    [ui/button {:on-click on-close}
     "Close"]]])

(defn scenes-list
  [{:keys [title]}]
  (r/with-let [current-scene-info (r/atom nil)
               handle-open-info #(reset! current-scene-info %)
               handle-close-info #(reset! current-scene-info nil)]
    (let [course @(re-frame/subscribe [::subs/current-course])
          scenes @(re-frame/subscribe [::subs/course-scenes])
          scenes-options (get-scenes-options scenes)
          list-styles (card/get-styles)]
      [list-card {:title       title
                  :full-height true}
       [ui/list {:style (:list-full-height list-styles)}
        (for [scene scenes-options]
          ^{:key (:value scene)}
          [ui/list-item
           [ui/list-item-text {:primary (:text scene)}]
           [ui/list-item-secondary-action
            [ui/icon-button {:aria-label "Info"
                             :on-click   #(handle-open-info scene)}
             [ic/info {:style (:action-icon list-styles)}]]
            [ui/icon-button {:aria-label "Edit"
                             :on-click   #(redirect-to :course-editor-v2-scene :id course :scene-id (:value scene))}
             [ic/edit {:style (:action-icon list-styles)}]]]])]
       [scene-info-window {:scene    @current-scene-info
                           :on-close handle-close-info}]])))

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
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.editor-v2.course-dashboard.state :as state]))

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
  [{:keys [scene-id data]}]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-info @(re-frame/subscribe [::subs/scene-info scene-id])
        styles (get-styles)]
    [:div
     [ui/typography {:variant "title"} "Skills:"]
     [:ul {:style (:skill-list styles)}
      (for [{:keys [id name abbr]} (:skills scene-data)]
        ^{:key id}
        [:li [ui/typography (str "(" abbr ") " name)]])]
     [ui/typography {:variant "title"} "Name:"]
     [ui/text-field {:label         "Name"
                     :full-width    true
                     :default-value (:name scene-info)
                     :variant       "outlined"
                     :on-change     #(swap! data assoc :name (-> % .-target .-value))}]
     [ui/typography {:variant "title"} "Archived:"]
     [ui/checkbox {:label     "Archived"
                   :variant   "outlined"
                   :default-value (:archived scene-info)
                   :on-change #(swap! data assoc :archived (-> % .-target .-checked))}]]))

(defn- scene-info-window
  [{:keys [scene-id on-close]}]
  (let [data (atom {})
        save #(do (re-frame/dispatch [::state/save-scene-info {:scene-id scene-id :data @data}])
                 (on-close))]
    [ui/dialog
     {:open     (some? scene-id)
      :on-close on-close}
     [ui/dialog-title
      "Scene Info"]
     [ui/dialog-content
      [scene-info-data {:scene-id scene-id :data data}]]
     [ui/dialog-actions
      [ui/button {:on-click save}
       "Save"]
      [ui/button {:on-click on-close}
       "Cancel"]]]))

(defn- scene-pick-name-window [{:keys [show on-ok on-cancel name]}]
  [ui/dialog
   {:open show}
   [ui/dialog-title "Choose name"]
   [ui/text-field {:placeholder "New Activity"
                   :on-click    #(.stopPropagation %)
                   :on-change   #(reset! name (->> % .-target .-value))}]
   [ui/dialog-actions
    [ui/button {:on-click on-ok} "Ok"]
    [ui/button {:on-click on-cancel} "Cancel"]]])

(defn scenes-list
  [{:keys [title]}]
  (r/with-let [current-scene-info (r/atom nil)
               handle-open-info #(reset! current-scene-info %)
               handle-close-info #(reset! current-scene-info nil)

               show-name-picker (r/atom false)
               new-activity-name (r/atom nil)]
    (let [course @(re-frame/subscribe [::subs/current-course])
          scene-list @(re-frame/subscribe [::subs/scene-list-ordered])
          list-styles (card/get-styles)]
      [list-card {:title       title
                  :full-height true
                  :on-add-click #(reset! show-name-picker true)}
       [ui/list {:style (:list-full-height list-styles)}
        (for [scene scene-list]
          ^{:key (:scene-id scene)}
          [ui/list-item
           [ui/list-item-text {:primary (:name scene)}]
           [ui/list-item-secondary-action
            [ui/icon-button {:aria-label "Info"
                             :on-click   #(handle-open-info (:scene-id scene))}
             [ic/info {:style (:action-icon list-styles)}]]
            (if (:is-placeholder scene)
              [ui/icon-button {:on-click #(redirect-to :wizard-configured :course-slug course :scene-slug (-> scene :scene-id name))}
               [ic/warning]]
              [ui/icon-button {:aria-label "Edit"
                               :on-click   #(redirect-to :course-editor-scene :id course :scene-id (-> scene :scene-id name))}
               [ic/edit {:style (:action-icon list-styles)}]])]])]
       [scene-info-window {:scene-id    @current-scene-info
                           :on-close handle-close-info}]
       [scene-pick-name-window {:show @show-name-picker
                                :on-ok #(do
                                          (reset! show-name-picker false)
                                          (if (empty? @new-activity-name)
                                            (print "error")
                                            (re-frame/dispatch [::state/create-new-activity @new-activity-name course])))
                                :on-cancel #(reset! show-name-picker false)
                                :name new-activity-name}]])))

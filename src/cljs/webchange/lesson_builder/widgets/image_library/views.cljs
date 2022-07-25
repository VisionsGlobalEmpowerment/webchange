(ns webchange.lesson-builder.widgets.image-library.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.image-library.state :as state]
    [webchange.ui.index :as ui]))

(defn- images-list-item
  [{:keys [on-click thumbnail-path] :as props}]
  (let [image-props (dissoc props :on-click)
        handle-click #(when (fn? on-click) (on-click image-props))]
    [ui/image {:src        thumbnail-path
               :class-name "widget--image-library--list-item"
               :lazy?      true
               :on-click   handle-click}]))

(defn- images-list
  [{:keys [data on-click]}]
  [:div {:class-name "widget--image-library--list"}
   (for [{:keys [id] :as image-data} data]
     ^{:key id}
     [images-list-item (assoc image-data :on-click on-click)])])

(defn image-library
  [{:keys [open? show-search] :or {open? true show-search false}}]
  (r/create-class
    {:display-name "section-block"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-did-update
     (fn [this [_ {old-type :type}]]
       (let [{new-type :type} (r/props this)]
         (when (not= new-type old-type)
           (re-frame/dispatch [::state/update-filter {:type new-type}]))))

     :reagent-render
     (fn [{:keys [class-name on-click]}]
       (let [assets @(re-frame/subscribe [::state/assets])
             loading? @(re-frame/subscribe [::state/assets-loading?])]
         [:div {:class-name (ui/get-class-name {"widget--image-library" true
                                                class-name              (some? class-name)})}
          (when show-search
            [ui/input {:placeholder "search"
                       :icon        "search"
                       :class-name  "search"
                       :on-change #(re-frame/dispatch [::state/update-filter {:query %}])}])
          (if-not loading?
            [images-list {:data     assets
                          :on-click on-click}]
            [ui/loading-overlay])]))}))

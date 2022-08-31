(ns webchange.lesson-builder.widgets.select-image.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.select-image.state :as state]
    [webchange.lesson-builder.widgets.image-library.views :refer [image-library]]
    [webchange.ui.index :as ui]))

(def last-key (atom 1))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn select-image
  [{:keys [class-name-image label value key hide-preview?] :as props}]
  (r/with-let [key (or key (swap! last-key inc))
               _ (re-frame/dispatch [::state/init key props])
               file-input (atom nil)]
    (let [handle-upload #(re-frame/dispatch [::state/upload key %])
          uploading? @(re-frame/subscribe [::state/uploading? key])]
      [:div.select-image
       (when label
         [:h3.select-image-header
          label])
       [:div.options
        (when-not hide-preview?
          ^{:key value}
          [ui/image {:src        value
                     :class-name class-name-image}])
        [:input {:type      "file"
                 :accept    ["gif" "jpg" "jpeg" "png"]
                 :on-change #(-> % change-event->file handle-upload)
                 :ref       #(reset! file-input %)}]
        [:div.actions
         [ui/button {:on-click #(re-frame/dispatch [::state/show-choose-image key])
                     :color    "blue-1"}
          "Open Library"]
         [ui/button {:on-click #(.click @file-input)}
          (if uploading? "Uploading..." "Upload New")]]]]
      #_(when (:cover-image errors)
          [ui/input-error (:cover-image errors)]))))

(defn choose-image-overlay
  []
  (let [handle-click #(re-frame/dispatch [::state/select-image {:image %}])]
    [:div.widget--choose-image-overlay
     [:h1 "Choose Image"]
     [image-library {:type        "etc"
                     :show-search true
                     :on-click    handle-click}]]))

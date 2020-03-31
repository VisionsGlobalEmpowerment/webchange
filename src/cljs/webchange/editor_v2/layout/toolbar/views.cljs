(ns webchange.editor-v2.layout.toolbar.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.layout.logo.views :refer [logo]]
    [webchange.routes :refer [redirect-to]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:toolbar                    {:background-color (get-in-theme [:palette :background :darken])}
   :breadcrumbs-wrapper        {:display     "flex"
                                :align-items "center"}
   :breadcrumbs-icon           {:font-size "1.5rem"
                                :margin    "0 5px"}
   :breadcrumbs-text           {:display "inline-block"}
   :breadcrumbs-text-clickable {:display "inline-block"
                                :cursor  "pointer"}})

(defn- breadcrumbs-view
  [breadcrumbs]
  (let [styles (get-styles)]
    [:div {:style (:breadcrumbs-wrapper styles)}
     (for [[index {:keys [text on-click]}] (map-indexed (fn [index data] [index data]) breadcrumbs)]
       ^{:key text}
       (let [clickable? (-> on-click nil? not)
             last-item? (= index (dec (count breadcrumbs)))]
         [:span {:style (:breadcrumbs-wrapper styles)}
          [ui/typography {:variant  (if last-item? "h2" "h5")
                          :style    (if clickable? (:breadcrumbs-text-clickable styles) (:breadcrumbs-text styles))
                          :on-click #(when clickable? (on-click))}
           text]
          (when-not last-item?
            [ic/chevron-right {:style (:breadcrumbs-icon styles)}])]))]))

(defn toolbar
  [{:keys [title breadcrumbs]}]
  (let [styles (get-styles)]
    [ui/app-bar {:position "fixed"}
     [ui/toolbar {:style (:toolbar styles)}
      [logo]
      (if-not (nil? breadcrumbs)
        [breadcrumbs-view breadcrumbs]
        [ui/typography {:variant "h2"}
         title])]]))

(ns webchange.editor-v2.wizard.activity-template.views-lookup-image
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def lookup-validation-map {:root [(fn [value] (when-not (some? value) "Required field"))]})

(defn- image-option-block
  [{:keys [name src value selected? image-size on-click]
    :or   {image-size 6}}]
  (js/console.log "image-size" image-size)
  [ui/grid {:item  true :xs image-size
            :style {:text-align "center"}}
   [ui/typography {:variant       "h6"
                   :align         "center"
                   :gutter-bottom true}
    name]
   [:img {:src      src
          :on-click #(on-click value)
          :style    (cond-> {:max-width     "100%"
                             :max-height    "300px"
                             :border-radius "8px"
                             :border        "solid 2px "
                             :border-color  (if selected? "#fff" "#757575")
                             :padding       "2px"})}]])

(defn- image-options-list
  [{:keys [options current-value image-size on-click]}]
  [ui/grid {:item true :xs 12}
   [ui/grid {:container   true
             :spacing     16
             :justify     "center"
             :align-items "center"}
    (for [{:keys [value] :as option} options]
      ^{:key value}
      [image-option-block (merge option
                                 {:selected?  (= value current-value)
                                  :image-size image-size
                                  :on-click   on-click})])]])

(defn lookup-image-option
  [{:keys [key option data validator]}]
  (r/with-let [current-value (r/atom nil)
               lookup-image-data (connect-data data [key] nil)
               {:keys [error-message]} (v/init lookup-image-data lookup-validation-map validator)
               handle-option-click (fn [value]
                                     (reset! lookup-image-data value)
                                     (reset! current-value value))]
    [ui/grid {:container true
              :spacing   16}
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "h6"
                      :style   {:display      "inline-block"
                                :margin-right "16px"}}
       (:label option)]
      [error-message {:field-name :root}]]
     [ui/grid {:item true :xs 12}
      [image-options-list {:options       (:options option)
                           :image-size    (:image-size option)
                           :current-value @lookup-image-data
                           :on-click      handle-option-click}]]]))

(ns webchange.lesson-builder.widgets.object-form.image-form.views
  (:require
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- change-image-component
  [{:keys [on-change]}]
  (let [handle-change (fn [{:keys [url]}]
                        (on-change {:src url}))]
    [select-image {:hide-preview? true
                   :on-change     handle-change}]))

(defn- flip-component
  [{:keys [data on-change]}]
  (let [handle-click (fn []
                       (let [scale (-> (get data :scale {:x 1 :y 1})
                                       (update :x #(* -1 %)))]
                         (on-change {:scale scale})))]
    [:div.flip-field
     [ui/input-label
      "Flip"]
     [ui/button {:icon     "flip"
                 :color    "blue-1"
                 :on-click handle-click}]]))

(defn- image-size-component
  [{:keys [data on-change]}]
  (let [value (get data :image-size)
        handle-click #(on-change {:image-size %})]
    [:div.image-fill-field
     [ui/input-label
      "Image Fill"]
     [:div.image-fill-filed-controls
      [ui/button {:title    "Cover"
                  :icon     "cover"
                  :color    (if (= value "cover") "blue-1" "grey-3")
                  :on-click #(handle-click "cover")}]
      [ui/button {:title    "Contain"
                  :icon     "contain"
                  :color    (if (= value "contain") "blue-1" "grey-3")
                  :on-click #(handle-click "contain")}]]]))

(defn- scale-component
  [{:keys [data on-change]}]
  (let [value (get-in data [:scale :x] 1)
        handle-change (fn [value]
                        (let [flip (-> (get-in data [:scale :x])
                                       (< 0))
                              scale {:x (if flip (- value) value)
                                     :y value}]
                          (on-change {:scale scale})))]
    [:div.scale-field
     [ui/input-label
      "Scale"]
     [ui/input {:value     value
                :type      "float"
                :step      0.05
                :on-change handle-change}]]))

(defn- visible-component
  [{:keys [data on-change]}]
  (let [value (get data :visible true)
        handle-change #(on-change {:visible %})]
    [:div.visible-field
     [ui/input-label
      "Visible"]
     [ui/switch {:checked?  value
                 :on-change handle-change}]]))

(defn image-form
  [{:keys [class-name data on-change]}]
  (let [{:keys [editable? src]} data
        options (get editable? :edit-form {:scale      true
                                           :flip       true
                                           :image-size true
                                           :visible    true})

        handle-change #(when (fn? on-change) (on-change %))
        component-props {:data      data
                         :on-change handle-change}]
    [:div {:class-name (ui/get-class-name {"image-form-fields" true
                                           class-name          (some? class-name)})}
     [ui/image {:src        src
                :class-name "image-preview"}]
     [:h2 "Edit Image"]
     [:div.form-fields-group
      (when (:flip options) [flip-component component-props])
      (when (:scale options) [scale-component component-props])
      (when (:image-size options) [image-size-component component-props])
      (when (:visible options) [visible-component component-props])]
     [:h2 "Change image"]
     [change-image-component component-props]]))

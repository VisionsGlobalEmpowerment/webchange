(ns webchange.lesson-builder.widgets.object-form.image-form.views
  (:require
    [webchange.lesson-builder.widgets.object-form.common.views :refer [flip-component scale-component]]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- apply-to-all-component
  [{:keys [on-apply-to-all]}]
  (let [handle-click #(when (fn? on-apply-to-all) (on-apply-to-all))]
    [ui/button {:on-click handle-click
                :shape    "rounded"}
     "Apply to all"]))

(defn- change-image-component
  [{:keys [on-change]}]
  (let [handle-change (fn [{:keys [url]}]
                        (on-change {:src url}))]
    [select-image {:hide-preview? true
                   :on-change     handle-change}]))

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
  [{:keys [class-name data on-change] :as props}]
  (let [{:keys [editable? src]} data
        options (get editable? :edit-form {:scale      true
                                           :flip       true
                                           :image-size false
                                           :visible    false})

        handle-change #(when (fn? on-change) (on-change %))
        component-props (merge props
                               {:on-change handle-change})]
    [:div {:class-name (ui/get-class-name {"image-form" true
                                           class-name   (some? class-name)})}
     [ui/image {:src        src
                :class-name "image-preview"}]
     [:h2 "Edit Image"]
     [:div.form-fields-group
      (when (:flip options) [flip-component component-props])
      (when (:scale options) [scale-component component-props])
      (when (:image-size options) [image-size-component component-props])
      (when (:visible options) [visible-component component-props])
      (when (:apply-to-all options) [apply-to-all-component component-props])]
     [:h2 "Change image"]
     [change-image-component component-props]]))

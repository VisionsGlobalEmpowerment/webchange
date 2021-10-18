(ns webchange.editor-v2.components.character-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.character-form.data :refer [character->skeleton single-skin-character?]]
    [webchange.editor-v2.components.character-form.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.right-menu.views-menu-section :refer [menu-section]]))

(defn- form-list-item
  [{:keys [background image name on-click selected? show-label?] :as props}]
  (let [handle-click #(on-click props)]
    [:div {:class-name (get-class-name {"form-list-item" true
                                        "active"         selected?})
           :title      name
           :on-click   handle-click}
     [:div {:class-name "img-wrapper"
            :style      {:background-color background}}
      (when (some? image)
        [:img {:src image}])]
     (when show-label?
       [:label name])]))

(defn- form-list
  [{:keys [class-name items on-item-click show-label? value]
    :or   {show-label? false}}]
  [:div {:class-name (get-class-name {"form-list" true
                                      class-name  (some? class-name)})}
   (for [item items]
     ^{:key (:value item)}
     [form-list-item (merge item
                            {:on-click    on-item-click
                             :show-label? show-label?
                             :selected?   (= value (:value item))})])])

(defn- character-form
  [{:keys [on-change menu-section? show-label? value]}]
  (let [characters @(re-frame/subscribe [::state/available-characters])
        handle-option-click (fn [{:keys [defaults value]}]
                              (-> {:character        value
                                   :change-skeleton? true}
                                  (merge defaults)
                                  (on-change)))
        form-list-props {:value         value
                         :items         characters
                         :on-item-click handle-option-click
                         :show-label?   show-label?
                         :class-name    "characters-form"}]
    (if menu-section?
      (let [{:keys [image color]} @(re-frame/subscribe [::state/current-character value])]
        [menu-section {:title-text             (clojure.string/capitalize (or value ""))
                       :title-image            image
                       :title-image-background color
                       :expanded?              true}
         [form-list form-list-props]])
      [form-list form-list-props])))

(defn- skin-form
  [{:keys [value on-change]}]
  (let [skins @(re-frame/subscribe [::state/available-skins])
        {:keys [color]} @(re-frame/subscribe [::state/current-skin value])
        handle-option-click (fn [{:keys [value]}]
                              (on-change {:skin value}))]
    [menu-section {:title-text             "Select Skin"
                   :title-image-background color}
     [form-list {:value         value
                 :items         skins
                 :on-item-click handle-option-click
                 :class-name    "skins-form"}]]))

(defn- head-form
  [{:keys [value character skin on-change]}]
  (let [heads @(re-frame/subscribe [::state/available-heads character skin])
        {:keys [image color]} @(re-frame/subscribe [::state/current-head value character skin])
        handle-option-click (fn [{:keys [value]}]
                              (on-change {:head value}))]
    [menu-section {:title-text             "Select Head"
                   :title-image            image
                   :title-image-background color}
     [form-list {:value         value
                 :items         heads
                 :on-item-click handle-option-click
                 :class-name    "heads-form"}]]))

(defn- clothes-form
  [{:keys [value character skin on-change]}]
  (let [clothes @(re-frame/subscribe [::state/available-clothes character skin])
        {:keys [image color]} @(re-frame/subscribe [::state/current-clothes value character skin])
        handle-option-click (fn [{:keys [value]}]
                              (on-change {:clothes value}))]
    [menu-section {:title-text             "Select Clothes"
                   :title-image            image
                   :title-image-background color}
     [form-list {:value         value
                 :items         clothes
                 :on-item-click handle-option-click
                 :class-name    "clothes-form"}]]))

(defn- form-data->skin-params
  [{:keys [change-skeleton? character clothes head skin]
    :or   {change-skeleton? false}}]
  (let [character-str (clojure.string/capitalize character)]
    (-> (cond
          (some #{character} ["boy" "girl"])
          {:name        "child"
           :skin-params {:body    (str "BODY/ChildTon-" skin)
                         :head    (str "HEAD/Head-" character-str "-" head "-Ton-" skin)
                         :clothes (str "CLOTHES/" character-str "-" clothes "-Clothes-" skin)}}

          (some #{character} ["man" "woman"])
          {:name        "adult"
           :skin-params {:body    (str "BODY/AdultTon-" skin)
                         :head    (str "HEAD/" character-str "Head-" head "-Ton-" skin)
                         :clothes (str "CLOTHES/" character-str "-" clothes "-Clothes-" skin)}}

          (single-skin-character? character)
          {:name        (character->skeleton character)
           :skin-params skin})
        (assoc :change-skeleton? change-skeleton?))))

(defn form
  [{:keys [character class-name clothes head skin on-change only-characters? show-character-label?]
    :or   {only-characters?      false
           show-character-label? false}}]
  (let [combined-skin? @(re-frame/subscribe [::state/combined-skin? character])
        handle-change (fn [params]
                        (-> {:character character
                             :clothes   clothes
                             :head      head
                             :skin      skin}
                            (merge params)
                            (form-data->skin-params)
                            (on-change)))
        show-extra-params? (and (not only-characters?)
                                (some? character)
                                combined-skin?)]
    [:div {:class-name (get-class-name {class-name (some? class-name)})}
     [character-form {:value         character
                      :on-change     handle-change
                      :show-label?   show-character-label?
                      :menu-section? (not only-characters?)}]

     (when show-extra-params?
       [skin-form {:value     skin
                   :on-change handle-change}])
     (when show-extra-params?
       [head-form {:value     head
                   :character character
                   :skin      skin
                   :on-change handle-change}])
     (when show-extra-params?
       [clothes-form {:value     clothes
                      :character character
                      :skin      skin
                      :on-change handle-change}])]))

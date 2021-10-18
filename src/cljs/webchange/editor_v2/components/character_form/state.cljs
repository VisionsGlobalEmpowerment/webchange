(ns webchange.editor-v2.components.character-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.character-form.data :as data]))

(re-frame/reg-sub
  ::combined-skin?
  (fn [_ [_ character]]
    (some (fn [{:keys [value combined-skin?]}]
            (and (= value character) combined-skin?))
          data/characters)))

(re-frame/reg-sub
  ::available-characters
  (fn []
    (->> data/characters
         (map (fn [{:keys [value] :as character}]
                (assoc character :image (data/get-preview {:entity    "character"
                                                           :character value})))))))

(re-frame/reg-sub
  ::current-character
  (fn []
    (re-frame/subscribe [::available-characters]))
  (fn [available-characters [_ current-value]]
    (some (fn [{:keys [background image value]}]
            (and (= value current-value)
                 {:image image
                  :color background}))
          available-characters)))

(re-frame/reg-sub
  ::available-skins
  (fn []
    (->> data/skins
         (map (fn [{:keys [color] :as data}]
                (assoc data :background color))))))

(re-frame/reg-sub
  ::current-skin
  (fn []
    (re-frame/subscribe [::available-skins]))
  (fn [available-skins [_ current-value]]
    {:color (some (fn [{:keys [color value]}]
                    (and (= value current-value)
                         color))
                  available-skins)}))

(re-frame/reg-sub
  ::available-heads
  (fn [_ [_ character skin]]
    (let [character-data (some (fn [{:keys [value] :as data}]
                                 (and (= value character) data))
                               data/characters)]
      (->> (:heads character-data)
           (map (fn [head-value]
                  (some (fn [{:keys [value] :as data}]
                          (and (= value head-value) data))
                        data/heads)))
           (map (fn [{:keys [value] :as head-data}]
                  (-> head-data
                      (assoc :image (data/get-preview {:entity    "head"
                                                       :character character
                                                       :skin      skin
                                                       :head      value}))
                      (assoc :background (data/get-background-color {:character character
                                                                     :skin      skin})))))))))

(re-frame/reg-sub
  ::current-head
  (fn [[_ _ character skin]]
    (re-frame/subscribe [::available-heads character skin]))
  (fn [available-heads [_ current-value character skin]]
    {:image (some (fn [{:keys [image value]}]
                    (and (= value current-value)
                         image))
                  available-heads)
     :color (data/get-background-color {:character character
                                        :skin      skin})}))

(re-frame/reg-sub
  ::available-clothes
  (fn [_ [_ character skin]]
    (let [character-data (some (fn [{:keys [value] :as data}]
                                 (and (= value character) data))
                               data/characters)]
      (->> (:clothes character-data)
           (map (fn [clothes-value]
                  (some (fn [{:keys [value] :as data}]
                          (and (= value clothes-value) data))
                        data/clothes)))
           (map (fn [{:keys [value] :as cloth-data}]
                  (-> cloth-data
                      (assoc :image (data/get-preview {:entity    "clothes"
                                                       :character character
                                                       :skin      skin
                                                       :clothes   value}))
                      (assoc :background (data/get-background-color {:character character
                                                                     :skin      skin})))))))))

(re-frame/reg-sub
  ::current-clothes
  (fn [[_ _ character skin]]
    (re-frame/subscribe [::available-clothes character skin]))
  (fn [available-clothes [_ current-value character skin]]
    {:image (some (fn [{:keys [image value]}]
                    (and (= value current-value)
                         image))
                  available-clothes)
     :color (data/get-background-color {:character character
                                        :skin      skin})}))

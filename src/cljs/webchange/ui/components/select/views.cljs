(ns webchange.ui.components.select.views
  (:require
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.components.input-error.views :refer [input-error]]
    [webchange.ui.components.input-label.views :refer [input-label]]
    [webchange.ui.components.select.utils :refer [empty-value? fix-options parse-value]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- get-selected-options
  [event]
  (->> (.. event -target -options)
       (.apply js/Array nil)
       (filter (fn [option] (.-selected option)))
       (map (fn [option] (.-value option)))))

(defn- apply-max-values-number
  [max-values-number values]
  (if (number? max-values-number)
    (take max-values-number values)
    values))

(defn select
  [{:keys [class-name
           disabled?
           error
           id
           label
           max-values-number
           multiple?
           on-arrow-down-click
           on-arrow-up-click
           on-change
           options
           placeholder
           required?
           type
           value
           variant
           width
           with-arrow?]
    :or   {disabled?           false
           multiple?           false
           options             []
           on-change           #()
           on-arrow-down-click #()
           on-arrow-up-click   #()
           placeholder         ""
           required?           false
           type                "str"
           with-arrow?         true}
    :as   props}]
  "Props:
   :options - items list
       .text
       .value
       .disabled?
       .title
   :variant - 'outlined' or none."
  (let [handle-change (fn [event]
                        (let [selected-options (->> (get-selected-options event)
                                                    (map #(parse-value % type))
                                                    (doall)
                                                    (apply-max-values-number max-values-number))]
                          (-> (if multiple?
                                selected-options
                                (first selected-options))
                              (on-change))))
        options (fix-options options props)
        with-arrow? (and with-arrow? (not multiple?) (not disabled?))
        has-label? (some? label)
        id (or id (when has-label? (random-uuid)))]
    [:div {:style      (if (some? width) {:width width} {})
           :class-name (get-class-name (cond-> (-> {"bbs--select"        true
                                                    "bbs--input-wrapper" true
                                                    "with-arrow"         with-arrow?}
                                                   (assoc class-name (some? class-name)))
                                               (some? variant) (assoc (str "variant-" variant) true)))}
     [:div {:class-name "bbs--select-wrapper--header"}
      (when has-label?
        [input-label {:for       id
                      :required? required?}
         label])
      (when (some? error)
        [input-error error])]
     [:select (cond-> {:value     (or value "")
                       :on-change handle-change}
                      multiple? (assoc :multiple true)
                      disabled? (assoc :disabled true))
      (for [{:keys [text value disabled? title]} options]
        ^{:key value}
        [:option (cond-> {:value    value
                          :disabled disabled?}
                         (some? title) (assoc :title title))
         text])]
     (when with-arrow?
       [system-icon {:icon       "drop"
                     :class-name "expand-icon"}])]))

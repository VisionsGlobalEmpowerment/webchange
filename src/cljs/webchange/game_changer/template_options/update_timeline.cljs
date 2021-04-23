(ns webchange.game-changer.template-options.update-timeline
  (:require
    [webchange.editor-v2.wizard.activity-template.views :refer [options-form]]
    [webchange.utils.list :refer [find-item-position replace-at-position]]))

(defn get-component
  [options]
  (let [component (fn [{:keys [data]}]
                    [options-form {:options options
                                   :data    data
                                   ;:metadata
                                   ;:validator
                                   }])]
    component))

(defn- get-template-options-groups
  [data]
  (let [template-options (get-in data [:template :options])
        options-groups (get-in data [:template :options-groups])]
    (when (some? template-options)
      (if (some? options-groups)
        (map (fn [{:keys [title options]}]
               {:title   title
                :options (->> options
                              (map keyword)
                              (map (fn [option-key]
                                     [option-key (get template-options option-key)])))})
             options-groups)
        [{:title   "Template Options"
          :options template-options}]))))

(defn- get-template-steps
  [data]
  (let [options-groups (get-template-options-groups data)]
    (when (some? options-groups)
      (map (fn [{:keys [title options]}]
             {:title     title
              :component (get-component options)})
           options-groups))))

(defn update-timeline
  [steps data]
  (let [replace-idx (find-item-position steps :replace-with-options?)]
    (if (some? replace-idx)
      (let [template-steps (get-template-steps data)]
        (if (some? template-steps)
          (replace-at-position steps replace-idx template-steps {:insert-list? true})
          steps))
      steps)))

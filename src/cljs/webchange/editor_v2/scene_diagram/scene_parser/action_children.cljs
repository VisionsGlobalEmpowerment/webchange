(ns webchange.editor-v2.scene-diagram.scene-parser.action-children
  (:require
    [webchange.logger :as logger]))

;; action

(defn get-template
  [action-data]
  (some
    (fn [from-key]
      (some
        (fn [from-data]
          (and (and (string? (:template from-data))
                    (= "id" (:action-property from-data)))
               from-data))
        (get action-data from-key)))
    [:from-var :from-params]))

(defn get-action-link-from-var
  [action-data]
  (some
    (fn [from-key]
      (some
        (fn [from-data]
          (and (= "id" (:action-property from-data))
               from-data))
        (get action-data from-key)))
    [:from-var :from-params]))

(defn get-possible-values
  [link-data]
  (let [possible-values (:possible-values link-data)]
    (if-not (nil? possible-values)
      (->> possible-values
           (map (fn [value] (cond
                              (number? value) (str value)
                              (keyword? value) (clojure.core/name value)
                              :else value))))
      (do (logger/warn ":possible-values are not defined")
          []))))

(defn template->values
  [template]
  (let [possible-values (get-possible-values template)
        template (:template template)]
    (->> possible-values
         (map (fn [value] (clojure.string/replace template #"%" value)))
         (map (fn [value] [(keyword value)])))))

(defn link->values
  [link]
  (let [possible-values (get-possible-values link)]
    (->> possible-values
         (map (fn [value] [(keyword value)])))))

(defn- get-action-action-children
  [{:keys [action-data]}]
  (let [template (get-template action-data)
        link-from-var (get-action-link-from-var action-data)]
    (cond
      (-> action-data :id string?) [[(-> action-data :id keyword)]]
      (-> template nil? not) (template->values template)
      (-> link-from-var nil? not) (link->values link-from-var)
      :else [])))

;; case

(defn- get-case-action-children
  [{:keys [action-path action-data]}]
  (map (fn [[option-name]]
         (conj action-path :options option-name))
       (-> action-data :options)))

;; provider

(defn- get-provider-action-children
  [{:keys [action-path action-data]}]
  (reduce (fn [result handler]
            (if (contains? action-data handler)
              (let [on-end (handler action-data)]
                (if (string? on-end)
                  (conj result [(keyword on-end)])
                  (conj result (conj action-path handler))))
              result))
          []
          [:on-end]))

;; sequence

(defn- get-sequence-action-children
  [{:keys [action-data]}]
  (->> (:data action-data)
       (map (fn [value] [(keyword value)]))))

(defn- get-sequence-data-action-children
  [{:keys [action-path action-data]}]
  (->> (:data action-data)
       (map-indexed (fn [idx _]
                      (conj action-path :data idx)))))

;; test

(defn- get-test-handler-name
  [action-path action-data event-name]
  (let [handler (->> action-data event-name)]
    (if (string? handler)
      [(keyword handler)]
      (->> (keyword event-name)
           (conj action-path)))))

(defn- get-test-action-children
  [{:keys [action-path action-data]}]
  (let [success-child (when (contains? action-data :success)
                        (get-test-handler-name action-path action-data :success))
        fail-child (when (contains? action-data :fail)
                     (get-test-handler-name action-path action-data :fail))]
    (->> [success-child fail-child]
         (remove nil?))))

;; ---

(defmulti get-action-children
          (fn [{:keys [action-data]}]
            (:type action-data)))

(defmethod get-action-children "action" [params] (get-action-action-children params))
(defmethod get-action-children "case" [params] (get-case-action-children params))
(defmethod get-action-children "lesson-var-provider" [params] (get-provider-action-children params))
(defmethod get-action-children "parallel" [params] (get-sequence-data-action-children params))
(defmethod get-action-children "sequence" [params] (get-sequence-action-children params))
(defmethod get-action-children "sequence-data" [params] (get-sequence-data-action-children params))
(defmethod get-action-children "test-var-scalar" [params] (get-test-action-children params))
(defmethod get-action-children "test-transitions-collide" [params] (get-test-action-children params))
(defmethod get-action-children "test-var-list" [params] (get-test-action-children params))
(defmethod get-action-children "test-value" [params] (get-test-action-children params))
(defmethod get-action-children "vars-var-provider" [params] (get-provider-action-children params))
(defmethod get-action-children :default [_] [])

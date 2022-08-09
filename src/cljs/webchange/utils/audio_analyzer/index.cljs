(ns webchange.utils.audio-analyzer.index
  (:require
    [webchange.utils.audio-analyzer.region-data :as rg]
    [webchange.utils.audio-analyzer.region-to-animation-sequence :as ras]
    [webchange.utils.audio-analyzer.talk-data :refer [get-chunks-for-text]]
    [webchange.utils.numbers :refer [to-precision]]))

;; Conditions

(defn- animation-sequence-data?
  [animation-sequence-data]
  (let [animation-sequence-item? (fn [{:keys [start end anim]}]
                                   (and (number? start)
                                        (number? end)
                                        (some #{anim} ["talk"])))]
    (every? animation-sequence-item? animation-sequence-data)))

(defn- audio-script?
  [audio-script-data]
  (let [audio-script-item? (fn [{:keys [start end]}]
                             (and (number? start)
                                  (number? end)))]
    (every? audio-script-item? audio-script-data)))

(defn- region-data?
  [{:keys [start end duration] :as region-data}]
  (or (nil? region-data)
      (and (number? start)
           (number? end)
           (number? duration))))

(defn- restriction?
  [{:keys [start end] :as restriction}]
  (or (nil? restriction)
      (and (number? start)
           (number? end))))

(defn- text-animation-data?
  [text-animation-data]
  (let [text-animation-item? (fn [{:keys [start end duration at chunk]}]
                               (and (number? start)
                                    (number? end)
                                    (number? duration)
                                    (number? at)
                                    (number? chunk)))]
    (every? text-animation-item? text-animation-data)))

(defn- prepare-region
  [{:keys [start end]}]
  (let [precision 2
        start (to-precision start precision)
        end (to-precision end precision)]
    {:start    start
     :end      end
     :duration (-> (- end start) (to-precision precision))}))

;; Methods

(defn get-available-regions
  [text audio-script]
  (->> (rg/get-start-end-options-for-text text audio-script)
       (map (fn [region-data]
              (merge region-data
                     (prepare-region region-data))))))

(defn get-region-data
  ([text audio-script]
   (get-region-data text audio-script nil))
  ([text audio-script restriction]
   {:pre  [(string? text)
           (audio-script? audio-script)
           (restriction? restriction)]
    :post [(region-data? %)]}
   (let [region-data (-> (rg/get-region-data text audio-script) (prepare-region))]
     (if (and (some? region-data)
              (some? restriction))
       (let [start (max (:start region-data) (:start restriction))
             end (min (:end region-data) (:end restriction))]
         (merge region-data
                {:start    start
                 :end      end
                 :duration (- end start)}))
       region-data))))

(defn region-data->animation-sequence-data
  [region-data audio-script]
  {:pre  [(region-data? region-data)
          (audio-script? audio-script)]
   :post [(animation-sequence-data? %)]}
  (ras/region-data->animation-sequence-data region-data audio-script))

(defn region->text-animation
  [region-data text audio-script]
  {:pre  [(region-data? region-data)
          (string? text)
          (audio-script? audio-script)]
   :post [(text-animation-data? %)]}
  (get-chunks-for-text text audio-script region-data))

(ns webchange.question.common.layout-markup
  (:require
    [webchange.question.common.params :as params]
    [webchange.question.utils :as utils]))

(defn- get-layout-params
  []
  (let [{template-width :width template-height :height} params/template-size
        content-margin-horizontal 328
        content-margin-vertical 56
        content-width (->> (* content-margin-horizontal 2) (- template-width))
        content-height (->> (* content-margin-vertical 2) (- template-height))

        elements-gap 64

        image-width 400
        image-height 400
        text-height 200
        check-button-size 128]
    {:image-width               image-width
     :image-height              image-height
     :text-height               text-height
     :check-button-size         check-button-size

     :content-width             content-width
     :content-height            content-height
     :content-margin-horizontal content-margin-horizontal
     :content-margin-vertical   content-margin-vertical
     :elements-gap              elements-gap}))

(defn- get-options-layout--image
  [{container-width :width container-height :height} {:keys [options-number]}]
  (let [item-sides-ratio 1.3958
        item-margin-ratio 0.2
        item-max-width 384
        item-max-height 536

        item-width-calculated (->> (+ options-number
                                      (* options-number item-margin-ratio)
                                      (- item-margin-ratio))
                                   (/ container-width)
                                   (min item-max-width))
        item-height-calculated (* item-width-calculated item-sides-ratio)
        item-height-fixed (min item-height-calculated item-max-height container-height)

        [item-width item-height] (if (< item-height-fixed item-height-calculated)
                                   [(/ item-height-fixed item-sides-ratio) item-height-fixed]
                                   [item-width-calculated item-height-calculated])
        item-margin (* item-width item-margin-ratio)

        list-margin-x (->> (+ (* item-width options-number)
                              (* item-margin (dec options-number)))
                           (- container-width)
                           (* 0.5))
        list-margin-y (->> (- container-height item-height)
                           (* 0.5))]
    (->> (range options-number)
         (map (fn [idx]
                [(inc idx)
                 {:x      (->> (+ item-width item-margin) (* idx) (+ list-margin-x))
                  :y      list-margin-y
                  :width  item-width
                  :height item-height}]))
         (into {}))))

(defn- get-options-layout--text
  [{container-width :width container-height :height} {:keys [options-number]}]
  (let [item-margin-ratio 0.2
        item-max-width 10000
        item-max-height 10000
        item-margin-horizontal-min 40
        item-margin-vertical-min 40

        columns-number (quot options-number 2)
        rows-number (-> (/ options-number columns-number) Math/ceil utils/round)

        item-width-calculated (->> (+ columns-number
                                      (* columns-number item-margin-ratio)
                                      (- item-margin-ratio))
                                   (/ container-width)
                                   (min item-max-width))
        item-height-calculated (->> (+ rows-number
                                       (* rows-number item-margin-ratio)
                                       (- item-margin-ratio))
                                    (/ container-height)
                                    (min item-max-height))
        item-margin-horizontal (max (* item-width-calculated item-margin-ratio)
                                    item-margin-horizontal-min)
        item-margin-vertical (max (* item-height-calculated item-margin-ratio)
                                  item-margin-vertical-min)
        item-margin (min item-margin-horizontal item-margin-vertical)

        item-width (-> (->> (dec columns-number)
                            (* item-margin)
                            (- container-width))
                       (/ columns-number)
                       utils/round)
        item-height (-> (->> (dec rows-number)
                             (* item-margin)
                             (- container-height))
                        (/ rows-number)
                        utils/round)]
    (->> (range options-number)
         (map (fn [idx]
                (let [column (quot idx rows-number)
                      row (mod idx rows-number)]
                  [(inc idx)
                   {:x      (->> (+ item-width item-margin) (* column))
                    :y      (->> (+ item-height item-margin) (* row))
                    :width  item-width
                    :height item-height}])))
         (into {}))))

(defn- get-options-layout--mark
  [{container-width :width container-height :height} {:keys [options-number]}]
  (let [item-sides-ratio 1
        item-margin-ratio 0.2
        item-max-width 1000
        item-max-height 1000

        item-width-calculated (->> (+ options-number
                                      (* options-number item-margin-ratio)
                                      (- item-margin-ratio))
                                   (/ container-width)
                                   (min item-max-width))
        item-height-calculated (* item-width-calculated item-sides-ratio)
        item-height-fixed (min item-height-calculated item-max-height container-height)

        [item-width item-height] (if (< item-height-fixed item-height-calculated)
                                   [(/ item-height-fixed item-sides-ratio) item-height-fixed]
                                   [item-width-calculated item-height-calculated])
        item-margin (* item-width item-margin-ratio)

        list-margin-x (->> (+ (* item-width options-number)
                              (* item-margin (dec options-number)))
                           (- container-width)
                           (* 0.5))
        list-margin-y (->> (- container-height item-height)
                           (* 0.5))]
    (->> (range options-number)
         (map (fn [idx]
                [(inc idx)
                 {:x      (->> (+ item-width item-margin) (* idx) (+ list-margin-x))
                  :y      list-margin-y
                  :width  item-width
                  :height item-height}]))
         (into {}))))

(defn- get-question-layout
  ([form-data layout-params]
   (get-question-layout form-data layout-params {}))
  ([form-data
    {:keys [content-margin-horizontal content-margin-vertical image-height text-height check-button-size elements-gap]}
    {:keys [header-height]}]
   (let [has-header? (or (utils/task-has-image? form-data)
                         (utils/task-has-text? form-data))
         has-footer? true

         content-x content-margin-horizontal
         content-y content-margin-vertical
         content-width (->> (* content-margin-horizontal 2) (- params/template-width))
         content-height (->> (* content-margin-vertical 2) (- params/template-height))

         add-header (fn [layout]
                      (let [header (cond
                                     (utils/task-has-image? form-data) {:x      content-x
                                                                        :y      content-y
                                                                        :width  content-width
                                                                        :height (if (some? header-height)
                                                                                  header-height
                                                                                  image-height)}
                                     (utils/task-has-text? form-data) {:x      content-x
                                                                       :y      content-y
                                                                       :width  content-width
                                                                       :height (if (some? header-height)
                                                                                 header-height
                                                                                 text-height)})
                            shift (+ (:height header) elements-gap)]
                        (-> layout
                            (assoc :header header)
                            (update-in [:body :y] + shift)
                            (update-in [:body :height] - shift))))
         add-footer (fn [layout]
                      (let [footer {:x      content-x
                                    :y      (- (+ content-y content-height) check-button-size)
                                    :width  content-width
                                    :height check-button-size}
                            shift (+ (:height footer) elements-gap)]
                        (-> layout
                            (assoc :footer footer)
                            (update-in [:body :height] - shift))))]
     (cond-> {:body {:x      content-x
                     :y      content-y
                     :width  content-width
                     :height content-height}}
             has-header? (add-header)
             has-footer? (add-footer)))))

(defn- add-check-button
  [data layout {:keys [check-button-size]}]
  (assoc data :check-button {:x      (-> (- (get-in layout [:footer :width])
                                            check-button-size)
                                         (/ 2)
                                         (+ (get-in layout [:footer :x]))
                                         (utils/round))
                             :y      (get-in layout [:footer :y])
                             :width  check-button-size
                             :height check-button-size}))

(defn- get-task-layout--text
  [form-data layout-params]
  (let [layout (get-question-layout form-data layout-params)]
    (-> {:text              (:header layout)
         :options-container (:body layout)}
        (add-check-button layout layout-params))))

(defn- get-task-layout--image
  [{:keys [options-number question-type] :as form-data} {:keys [elements-gap image-width text-height] :as layout-params}]
  (if (and (= question-type "multiple-choice-text")
           (= options-number 3))
    (let [layout (get-question-layout form-data layout-params {:header-height text-height})]
      (-> {:image             (-> (:body layout)
                                  (update :x + (- (:width (:body layout)) image-width))
                                  (assoc :width image-width))
           :options-container (-> (:body layout)
                                  (update :width - image-width elements-gap))}
          (add-check-button layout layout-params)))
    (let [layout (get-question-layout form-data layout-params)]
      (-> {:image             (:header layout)
           :options-container (:body layout)}
          (add-check-button layout layout-params)))))

(defn- get-task-layout--voice-over
  [form-data layout-params]
  (let [layout (get-question-layout form-data layout-params)]
    (-> {:options-container (:body layout)}
        (add-check-button layout layout-params))))

(defn- get-task-layout--text-image
  [form-data {:keys [elements-gap image-width] :as layout-params}]
  (let [layout (get-question-layout form-data layout-params)]
    (-> {:image             {:x      (get-in layout [:header :x])
                             :y      (get-in layout [:header :y])
                             :width  image-width
                             :height (get-in layout [:header :height])}
         :text              {:x      (+ (get-in layout [:header :x])
                                        image-width
                                        elements-gap)
                             :y      (get-in layout [:header :y])
                             :width  (- (get-in layout [:header :width])
                                        image-width
                                        elements-gap)
                             :height (get-in layout [:header :height])}
         :options-container (:body layout)}
        (add-check-button layout layout-params))))

(defn get-layout-coordinates
  [{:keys [task-type question-type] :as form-data}]
  "Get question main elements position and size.

  - :options-number - a number of available answer options,
  - :task-type - one of the next values: 'text', 'image', 'text-image' or 'voice-over',
  - :question-type - one of the next values: 'multiple-choice-image', 'multiple-choice-text' or 'thumbs-up-n-down';

   Returns map of dimensions for question element if it presents or nil if absent:
   - :image - 'dimensions' of task image,
   - :text - 'dimensions' of task text,
   - :options-container - 'dimensions' all options block,
   - :options-items:
       - 1 - 'dimensions' of the first option,
       - 2 - 'dimensions' of the second option,
       ..etc,
   - :check-button - 'dimensions' of check button for multiple correct answers;

   Where 'dimensions' is a map:
   - :x - number,
   - :y - number,
   - :width - number,
   - :height - number."
  (let [layout-params (get-layout-params)]
    (case question-type
      "multiple-choice-image" (case task-type
                                "text" (let [{:keys [options-container] :as layout} (get-task-layout--text form-data layout-params)]
                                         (merge layout
                                                {:options-items (get-options-layout--image options-container form-data)}))
                                "image" (let [{:keys [options-container] :as layout} (get-task-layout--image form-data layout-params)]
                                          (merge layout
                                                 {:options-items (get-options-layout--image options-container form-data)}))
                                "text-image" (let [{:keys [options-container] :as layout} (get-task-layout--text-image form-data layout-params)]
                                               (merge layout
                                                      {:options-items (get-options-layout--image options-container form-data)}))
                                "voice-over" (let [{:keys [options-container] :as layout} (get-task-layout--voice-over form-data layout-params)]
                                               (merge layout
                                                      {:options-items (get-options-layout--image options-container form-data)}))
                                nil)
      "multiple-choice-text" (case task-type
                               "text" (let [{:keys [options-container] :as layout} (get-task-layout--text form-data layout-params)]
                                        (merge layout
                                               {:options-items (get-options-layout--text options-container form-data)}))
                               "image" (let [{:keys [options-container] :as layout} (get-task-layout--image form-data layout-params)]
                                         (merge layout
                                                {:options-items (get-options-layout--text options-container form-data)}))
                               "text-image" (let [{:keys [options-container] :as layout} (get-task-layout--text-image form-data layout-params)]
                                              (merge layout
                                                     {:options-items (get-options-layout--text options-container form-data)}))
                               "voice-over" (let [{:keys [options-container] :as layout} (get-task-layout--voice-over form-data layout-params)]
                                              (merge layout
                                                     {:options-items (get-options-layout--text options-container form-data)}))
                               nil)
      "thumbs-up-n-down" (case task-type
                           "text" (let [{:keys [options-container] :as layout} (get-task-layout--text form-data layout-params)]
                                    (merge layout
                                           {:options-items (get-options-layout--mark options-container form-data)}))
                           "image" (let [{:keys [options-container] :as layout} (get-task-layout--image form-data layout-params)]
                                     (merge layout
                                            {:options-items (get-options-layout--mark options-container form-data)}))
                           "text-image" (let [{:keys [options-container] :as layout} (get-task-layout--text-image form-data layout-params)]
                                          (merge layout
                                                 {:options-items (get-options-layout--mark options-container form-data)}))
                           "voice-over" (let [{:keys [options-container] :as layout} (get-task-layout--voice-over form-data layout-params)]
                                          (merge layout
                                                 {:options-items (get-options-layout--mark options-container form-data)}))
                           nil)
      nil)))

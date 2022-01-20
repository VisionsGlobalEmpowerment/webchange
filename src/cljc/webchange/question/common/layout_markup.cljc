(ns webchange.question.common.layout-markup
  (:require
    [webchange.question.common.params :as params]
    [webchange.question.utils :as utils]))

(defn- get-layout-params
  []
  (let [{template-width :width template-height :height} params/template-size
        content-margin-horizontal 256
        content-margin-vertical 64
        content-width (->> (* content-margin-horizontal 2) (- template-width))
        content-height (->> (* content-margin-vertical 2) (- template-height))]
    {:image-width               256
     :image-height              256
     :image-width-big           512
     :image-height-big          512
     :text-height               80
     :check-button-size         128

     :elements-gap              56
     :elements-gap-big          128
     :footer-margin             128
     :mark-options-list-height  200

     :content-width             content-width
     :content-height            content-height
     :content-margin-horizontal content-margin-horizontal
     :content-margin-vertical   content-margin-vertical}))

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
    {:keys [content-margin-horizontal content-margin-vertical image-height text-height check-button-size elements-gap footer-margin]}
    {:keys [header-height with-header? with-footer?]
     :or   {with-header? true
            with-footer? true}}]
   (let [has-header? (and with-header?
                          (or (utils/task-has-image? form-data)
                              (utils/task-has-text? form-data)))
         has-footer? with-footer?

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
                            shift (+ (:height footer) footer-margin)]
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
  [{:keys [options-number question-type] :as form-data} {:keys [elements-gap-big image-width image-width-big text-height] :as layout-params}]
  (if (and (= question-type "multiple-choice-text")
           (some #{options-number} [2 3]))
    (let [layout (get-question-layout form-data layout-params {:header-height text-height})]
      (-> {:image             (-> (:body layout)
                                  (update :x + (- (:width (:body layout)) image-width-big))
                                  (assoc :width image-width-big))
           :options-container (-> (:body layout)
                                  (update :width - image-width-big elements-gap-big))}
          (add-check-button layout layout-params)))
    (let [layout (get-question-layout form-data layout-params)]
      (-> {:image             (-> (:header layout)
                                  (update :x + (/ (- (get-in layout [:header :width]) image-width) 2))
                                  (assoc :width image-width))
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

(defn- centralize
  [w1 w2]
  (-> (- w1 w2) (/ 2) (utils/round)))

(defn- shift-to-center
  [x w1 w2]
  (-> (centralize w1 w2)
      (+ x)))

(defn- get-task-layout--thumbs-up-n-down--text
  [form-data {:keys [elements-gap mark-options-list-height text-height] :as layout-params}]
  (let [{:keys [body] :as layout} (get-question-layout form-data layout-params {:with-header? false})
        total-height (+ text-height elements-gap mark-options-list-height)
        padding-top (/ (- (:height body) total-height) 2)]
    (-> {:text              {:x      (:x body)
                             :y      (+ (:y body) padding-top)
                             :width  (:width body)
                             :height text-height}
         :options-container {:x      (:x body)
                             :y      (+ (:y body) padding-top text-height elements-gap)
                             :width  (:width body)
                             :height mark-options-list-height}}
        (add-check-button layout layout-params))))

(defn- get-task-layout--thumbs-up-n-down--image
  [form-data {:keys [elements-gap-big image-height-big image-width-big mark-options-list-height] :as layout-params}]
  (let [{:keys [body] :as layout} (get-question-layout form-data layout-params {:with-header? false})
        right-side-left (+ image-width-big elements-gap-big)]
    (-> {:image             (-> body
                                (update :y shift-to-center (:height body) image-height-big)
                                (assoc :width image-width-big)
                                (assoc :height image-height-big))
         :options-container (-> body
                                (update :x + right-side-left)
                                (update :y shift-to-center (:height body) mark-options-list-height)
                                (update :width - right-side-left)
                                (assoc :height mark-options-list-height))}
        (add-check-button layout layout-params))))

(defn- get-task-layout--thumbs-up-n-down--text-image
  [form-data {:keys [elements-gap-big image-height-big image-width-big mark-options-list-height text-height] :as layout-params}]
  (let [{:keys [body] :as layout} (get-question-layout form-data layout-params {:with-header? false})
        right-side-height (+ text-height elements-gap-big mark-options-list-height)
        right-side-left (+ image-width-big elements-gap-big)
        right-side-top (centralize (:height body) right-side-height)]
    (-> {:text              (-> body
                                (update :x + right-side-left)
                                (update :y + right-side-top)
                                (update :width - right-side-left)
                                (assoc :height text-height))
         :image             (-> body
                                (update :y shift-to-center (:height body) image-height-big)
                                (assoc :width image-width-big)
                                (assoc :height image-height-big))
         :options-container (-> body
                                (update :x + right-side-left)
                                (update :y + right-side-top text-height elements-gap-big)
                                (update :width - right-side-left)
                                (assoc :height mark-options-list-height))}
        (add-check-button layout layout-params))))

(defn- get-task-layout--thumbs-up-n-down--voice-over
  [form-data {:keys [mark-options-list-height] :as layout-params}]
  (let [{:keys [body] :as layout} (get-question-layout form-data layout-params {:with-header? false})]
    (-> {:options-container (-> body
                                (update :y shift-to-center (:height body) mark-options-list-height)
                                (assoc :height mark-options-list-height))}
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
                           "text" (let [{:keys [options-container] :as layout} (get-task-layout--thumbs-up-n-down--text form-data layout-params)]
                                    (merge layout
                                           {:options-items (get-options-layout--mark options-container form-data)}))
                           "image" (let [{:keys [options-container] :as layout} (get-task-layout--thumbs-up-n-down--image form-data layout-params)]
                                     (merge layout
                                            {:options-items (get-options-layout--mark options-container form-data)}))
                           "text-image" (let [{:keys [options-container] :as layout} (get-task-layout--thumbs-up-n-down--text-image form-data layout-params)]
                                          (merge layout
                                                 {:options-items (get-options-layout--mark options-container form-data)}))
                           "voice-over" (let [{:keys [options-container] :as layout} (get-task-layout--thumbs-up-n-down--voice-over form-data layout-params)]
                                          (merge layout
                                                 {:options-items (get-options-layout--mark options-container form-data)}))
                           nil)
      nil)))

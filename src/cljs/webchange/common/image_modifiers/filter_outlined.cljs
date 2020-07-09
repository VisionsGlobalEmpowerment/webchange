(ns webchange.common.image-modifiers.filter-outlined)

(def default-params {:color     "#ffffff"                   ;; name or hex color
                     :thickness 20                          ;; thickness in pixels
                     :blur      100})                        ;; [0..255]

(defn- create-canvas
  [width height]
  (let [canvas (.createElement js/document "canvas")]
    (aset canvas "width" width)
    (aset canvas "height" height)
    [canvas (.getContext canvas "2d")]))

(defn- get-size
  [image-data]
  [(.-width image-data)
   (.-height image-data)])

(defn- add-shadow
  [image-data color size]
  (let [[width height] (get-size image-data)
        [_ ctx] (create-canvas width height)
        [tmp-canvas tmp-ctx] (create-canvas width height)]
    (.putImageData tmp-ctx image-data 0 0)
    (.save ctx)
    (aset ctx "shadowColor" color)
    (aset ctx "shadowBlur" size)
    (.drawImage ctx tmp-canvas 0 0)
    (.restore ctx)
    ctx))

(defn- threshold-shadow
  [image-data image-data-with-shadow threshold]
  (let [data (.-data image-data)
        threshold-min 0
        threshold-max threshold]
    (doseq [i (range 0 (.-length data) 4)]
      (let [r i
            g (+ i 1)
            b (+ i 2)
            a (+ i 3)]
        (aset data r (aget (.-data image-data-with-shadow) r))
        (aset data g (aget (.-data image-data-with-shadow) g))
        (aset data b (aget (.-data image-data-with-shadow) b))
        (let [current-alpha (aget (.-data image-data-with-shadow) a)]
          (when-not (= current-alpha 0)
            (let [new-value (cond
                              (> current-alpha threshold-max) 255
                              (< current-alpha threshold-min) 0
                              :default (* 255 (/ (- current-alpha threshold-min)
                                                 (- threshold-max threshold-min))))]
              (aset data a new-value))))))))

(defn filter-outlined
  [image-data custom-params]
  (let [{:keys [color thickness blur]} (merge default-params custom-params)
        [width height] (get-size image-data)
        image-data-with-shadow (-> image-data
                                   (add-shadow color thickness)
                                   (.getImageData 0 0 width height))]
    (threshold-shadow image-data image-data-with-shadow blur)))

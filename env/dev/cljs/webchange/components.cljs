(ns webchange.components
  (:require
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]))

(defn redraw
  [object-name data]
  (let [db @re-frame.db/app-db
        current-scene (:current-scene db)
        objects (get-in db [:interpreter :scene :objects])
        object-key (keyword object-name)
        object (-> @objects
                   (get object-key)
                   :object)
        parent (-> object .-parent)
        prepared-data (get-object-data current-scene object-name {object-key data})]
    (.removeChild parent object)
    (swap! objects dissoc object-key)
    (create-component (merge prepared-data {:parent      parent
                                            :object-name object-key}))
    prepared-data))

(comment
  (redraw "spot" {:type       "image",
                                 :x             826,
                                 :y          524,
                                 :filters     [{:name "brightness" :value 0}
                                               {:name "glow" :outer-strength 0 :color 0xffd700}]
                                 :src        "/raw/img/park/slide/line_01.png",})

  {:type    "image",
   :x       1690,
   :y       492,
   :actions {:click {:id "finish-activity", :on "click", :type "action"}},
   :src     "/raw/img/ui/back_button_01.png"}

  :approve-playback-button {:type         "svg-path"
                            :x            135 :y 24
                            :width        128 :height 128
                            :stroke-width 4
                            :fill         true
                            :actions      {:click {:id "approve-playback-click" :on "click" :type "action"}}
                            :data         "M 50.88 27.16 L 33.72 44.36 L 27.12 37.76 C 26.7614 37.3412 26.3202 37.0012 25.8238 36.7612 C 25.3276 36.521 24.7872 36.386 24.2362 36.3648 C 23.6854 36.3436 23.136 36.4364 22.6228 36.6374 C 22.1094 36.8386 21.6432 37.1436 21.2534 37.5334 C 20.8636 37.9232 20.5586 38.3894 20.3574 38.9028 C 20.1564 39.416 20.0636 39.9654 20.0848 40.5162 C 20.1062 41.067 20.241 41.6076 20.4812 42.1038 C 20.7212 42.6002 21.0612 43.0414 21.48 43.4 L 30.88 52.84 C 31.2538 53.2108 31.697 53.504 32.1844 53.703 C 32.6718 53.9022 33.1936 54.003 33.72 54 C 34.7694 53.9956 35.775 53.579 36.52 52.84 L 56.52 32.84 C 56.895 32.4682 57.1926 32.0258 57.3956 31.5384 C 57.5986 31.0508 57.7032 30.528 57.7032 30 C 57.7032 29.472 57.5986 28.9492 57.3956 28.4616 C 57.1926 27.9742 56.895 27.5318 56.52 27.16 C 55.7706 26.415 54.7568 25.9968 53.7 25.9968 C 52.6432 25.9968 51.6294 26.415 50.88 27.16 Z M 40 0 C 32.0888 0 24.3552 2.346 17.7772 6.7412 C 11.1992 11.1365 6.0723 17.3836 3.0448 24.6926 C 0.0173 32.0018 -0.7748 40.0444 0.7686 47.8036 C 2.312 55.5628 6.1216 62.6902 11.7157 68.2842 C 17.3098 73.8784 24.4372 77.688 32.1964 79.2314 C 39.9556 80.7748 47.9984 79.9826 55.3074 76.9552 C 62.6164 73.9276 68.8636 68.8008 73.2588 62.2228 C 77.654 55.6448 80 47.9112 80 40 C 80 34.7472 78.9654 29.5456 76.9552 24.6926 C 74.945 19.8396 71.9986 15.4301 68.2842 11.7157 C 64.57 8.0014 60.1604 5.055 55.3074 3.0448 C 50.4544 1.0346 45.2528 0 40 0 Z M 40 72 C 33.671 72 27.4842 70.1232 22.2218 66.607 C 16.9594 63.0908 12.8579 58.0932 10.4359 52.2458 C 8.0139 46.3986 7.3802 39.9646 8.6149 33.7572 C 9.8496 27.5498 12.8973 21.8478 17.3726 17.3726 C 21.8478 12.8973 27.5498 9.8496 33.7572 8.6149 C 39.9646 7.3801 46.3986 8.0139 52.2458 10.4359 C 58.0932 12.8579 63.0908 16.9594 66.607 22.2218 C 70.1232 27.4842 72 33.671 72 40 C 72 48.487 68.6286 56.6262 62.6274 62.6274 C 56.6262 68.6286 48.487 72 40 72 Z"})

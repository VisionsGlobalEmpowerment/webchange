(ns webchange.templates.library.book
  (:require
    [webchange.templates.core :as core]
    [webchange.utils.text :as text-utils]
    [clojure.string :refer [index-of]]))

(def m {:id          5
        :name        "book"
        :tags        ["Book Creator"]
        :description "Simple book"
        :actions {:add-page {:title "Add page",
                             :options  {:pages {:label "Pages"
                                                   :type  "pages"
                                                   :max   10}}}}
        :options     {:title {:label "Title"
                              :type  "string"}
                      :pages {:label "Pages"
                              :type  "pages"
                              :max   10}}})

(def t {:assets        [{:url "/raw/clipart/book/books_02_single-background.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/back_button_01.png" :size 1 :type "image"}]
        :objects       {:background      {:type "background"
                                          :src  "/raw/clipart/book/books_02_single-background.png"}
                        :next-page-arrow {:type    "image",
                                          :x       1770
                                          :y       500
                                          :width   97,
                                          :height  99,
                                          :actions {:click {:id "next-page", :on "click", :type "action"}},
                                          :scale-x -1,
                                          :src     "/raw/img/ui/back_button_01.png"}
                        :group-0         {:type "group" :children ["title"] :x 370 :y 400}
                        :title           {:type "text" :width 1180 :align "center" :vertical-align "top" :font-family "Lexend Deca" :font-size 120
                                          :text nil :chunks nil}
                        }
        :scene-objects [["background" "next-page-arrow" "group-0"]],
        :actions       {:dialog-1-title {:type               "sequence-data",
                                         :editor-type        "dialog",
                                         :concept-var        "current-word",
                                         :data               [{:type "sequence-data"
                                                               :data [{:type "empty" :duration 0}
                                                                      {:data        [],
                                                                       :type        "text-animation",
                                                                       :audio       nil,
                                                                       :target      "title",
                                                                       :animation   "bounce",
                                                                       :phrase-text nil}]}],
                                         :phrase             "dialog-title",
                                         :phrase-description "Title",
                                         :dialog-track       "1 Title"}

                        :next-page      {:type "sequence-data"
                                         :data [{:type     "set-attribute" :attr-name "visible", :attr-value false
                                                 :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                                {:type "counter", :counter-id "current-page", :counter-action "increase"}
                                                {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                 :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                                {:type     "action"
                                                 :from-var [{:template "dialog-%-page", :var-name "current-page", :action-property "id"}]}]}
                        :intro
                                        {:type "sequence-data",
                                         :data [{:type "start-activity"}
                                                {:type "set-variable", :var-name "current-page", :var-value 0}
                                                {:type "set-variable", :var-name "max-page", :var-value 4}
                                                {:type "empty" :duration 1000}
                                                {:type "action" :id "dialog-1-title"}]}}
        :triggers
                       {:start {:on "start", :action "intro"}}
        :metadata      {
                        :history []
                        :stages [{:name    "Title"
                                  :objects ["background" "next-page-arrow" "group-0"]}]
                        :pages-counter 0}

        })

(defn- group-name
  [idx]
  (str "group-" idx))

(defn- create-page
  [idx {:keys [img text]}]
  (let [group-name (group-name idx)
        image-name (str "image-" idx)
        text-name (str "text-" idx)]
    {(keyword group-name) {:type "group" :children [image-name text-name] :visible false :x 370 :y 130}
     (keyword image-name) {:type "image" :src img :x 590 :y 320 :origin {:type "center-center"} :max-width 1180 :max-height 640}
     (keyword text-name)  {:type "text" :x 0 :y 640 :width 1180 :vertical-align "top" :font-family "Lexend Deca" :font-size 80
                           :text text :chunks (text-utils/text->chunks text)}}))

(defn- create-page-dialog
  [idx {:keys [text]}]
  (let [dialog {:type               "sequence-data",
                :concept-var        "current-word",
                :data               [{:type        "sequence-data"
                                      :editor-type "dialog",
                                      :data        [{:type "empty" :duration 0}
                                                    {:data        [],
                                                     :type        "text-animation",
                                                     :audio       nil,
                                                     :target      (str "text-" idx),
                                                     :animation   "bounce",
                                                     :phrase-text text}]}],
                :phrase             (str "dialog-page-" idx),
                :phrase-description (str "Page " idx),
                :dialog-track       "2 Pages"}
        name (str "dialog-" idx "-page")]
    {(keyword name) dialog}))

(defn- create-page-stage
  [idx]
  (let [group-name (group-name idx)]
    {:name    (str "Page " idx)
     :objects ["background" "next-page-arrow" group-name]}))

(defn- create-asset
  [{img :img}]
  {:url img :size 1 :type "image"})

(defn- add-pages
  [t pages]
  (let [pages-counter (get-in t [:metadata :pages-counter])
        page-container (->> pages
                            (map-indexed (fn [idx p] (create-page (+ pages-counter (inc idx)) p)))
                            (reduce merge))
        dialog-container (->> pages
                              (map-indexed (fn [idx p] (create-page-dialog (+ pages-counter (inc idx)) p)))
                              (reduce merge))
        assets (map create-asset pages)
        group-names (->> (+ pages-counter (count pages)) (range pages-counter) (map inc) (map group-name) (into []))
        stages (->> (+ pages-counter (count pages)) (range pages-counter) (map inc) (map create-page-stage) (into []))]
    (-> t
        (update :assets concat assets)
        (update :assets vec)
        (update :objects merge page-container)
        (update :scene-objects conj group-names)
        (update :actions merge dialog-container)
        (update-in [:metadata :stages] concat stages)
        (update-in [:metadata :stages] vec)
        (update-in [:metadata :pages-counter] + (count pages)
                   ))))

(defn- add-title
  [t title]
  (-> t
      (assoc-in [:objects :title :text] title)
      (assoc-in [:objects :title :chunks] (text-utils/text->chunks title))
      (assoc-in [:actions :dialog-1-title :data 0 :data 1 :phrase-text] title)))

(defn fu
  [old-data args]
  (-> old-data
      (add-pages (:pages args))
      (update-in [:metadata :history] conj {:type :update :args args})))

(defn f
  [args]
  (-> t
      (add-title (:title args))
      (add-pages (:pages args))
      (assoc-in [:metadata :actions] (:actions m))
      (assoc-in [:metadata :history] [{:type :create :args args}])))

(core/register-template
  (:id m) m f fu)

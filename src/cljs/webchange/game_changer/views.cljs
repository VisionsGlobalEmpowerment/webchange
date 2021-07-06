(ns webchange.game-changer.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [button message timeline]]
    [webchange.ui-framework.layout.views :refer [layout]]
    [webchange.game-changer.views-layout :as game-changer]

    [webchange.game-changer.steps.create-activity.index :as create-activity]
    [webchange.game-changer.steps.fill-template.index :as fill-template]
    [webchange.game-changer.steps.select-background.index :as select-background]
    [webchange.game-changer.steps.select-background-music.index :as select-background-music]
    [webchange.game-changer.steps.select-character.index :as select-character]
    [webchange.game-changer.steps.select-template.index :as select-template]
    [webchange.game-changer.steps.welcome-message.index :as welcome-message]

    [webchange.logger.index :as logger]))

(def game-changer-steps [
                         welcome-message/data
                         select-template/data
                         select-character/data
                         create-activity/data
                         fill-template/data
                         select-background/data
                         select-background-music/data])

(defn- not-defined-component
  []
  [message {:type    "warn"
            :message "Component not defined"}])

(defn- get-timeline-items
  [step-idx steps data]
  (->> steps
       (map-indexed vector)
       (map (fn [[idx {:keys [title timeline-label available?]
                       :or   {available? (constantly true)}}]]
              {:title      (or timeline-label title)
               :active?    (= idx step-idx)
               :completed? (< idx step-idx)
               :disabled?  (not (available? {:data data}))}))))

(defn- get-current-step-data
  [data step-idx steps]
  (let [step-data (nth steps step-idx)
        next-enabled-handler (get step-data :passed? (constantly true))
        timeline (get-timeline-items step-idx steps data)]
    {:title         (get step-data :title "")
     :timeline      timeline
     :component     (get step-data :component not-defined-component)
     :handle-next   (get step-data :handle-next)
     :next-enabled? (next-enabled-handler {:data data})
     :next-step-idx (->> (map-indexed vector timeline)
                         (some (fn [[index {:keys [disabled?]}]]
                                 (and (> index step-idx)
                                      (not disabled?)
                                      index))))
     :prev-step-idx (->> (map-indexed vector timeline)
                         (reverse)
                         (some (fn [[index {:keys [disabled?]}]]
                                 (and (< index step-idx)
                                      (not disabled?)
                                      index))))}))

(defn- form
  []
  (r/with-let [current-step (r/atom 0)
               current-data (r/atom {}
                              ;{:template   {:id      32
                              ;              :name    "Interactive Read Aloud"
                              ;              :options [{:key   "characters"
                              ;                         :label "Characters"
                              ;                         :type  "characters"
                              ;                         :max   4}
                              ;                        {:key         "cover-layout"
                              ;                         :label       "Cover layout"
                              ;                         :type        "lookup-image"
                              ;                         :description "Cover layout"
                              ;                         :options     [{:name  "Title at top"
                              ;                                        :value "title-top"
                              ;                                        :src   "/images/templates/cover_layout/title_at_top.svg"}
                              ;                                       {:name  "Title at bottom"
                              ;                                        :value "title-bottom"
                              ;                                        :src   "/images/templates/cover_layout/title_at_bottom.svg"}]}
                              ;                        {:key         "cover-title"
                              ;                         :label       "Title"
                              ;                         :placeholder "Type title here"
                              ;                         :description "Cover title"
                              ;                         :type        "string"}
                              ;                        {:key         "cover-image"
                              ;                         :label       "Cover image"
                              ;                         :description "Cover image"
                              ;                         :type        "image"}
                              ;                        {:key   "authors"
                              ;                         :label "Authors"
                              ;                         :type  "strings-list"
                              ;                         :max   3}
                              ;                        {:key       "illustrators"
                              ;                         :label     "Illustrators"
                              ;                         :type      "strings-list"
                              ;                         :optional? true
                              ;                         :max       3}]}
                              ; :options    {:characters   [{:name     "Adult Cow"
                              ;                              :skeleton :senoravaca
                              ;                              :skin     "vaca"}]
                              ;              :valid?       true
                              ;              :cover-layout "title-top"
                              ;              :cover-title  111
                              ;              :cover-image  {:src "/upload/SNXXDKDHTVCTHFQF.png"}
                              ;              :authors      [3]
                              ;              :illustrators [2]}
                              ; :lang       "English"
                              ; :course     {:name       "Interactive Read Aloud Course"
                              ;              :slug       "interactive-read-aloud-course-english-gzgtgixt"
                              ;              :status     "draft"
                              ;              :updated-at ""
                              ;              :lang       "English"
                              ;              :image-src  "https://webchange.local/images/default-course.jpg"
                              ;              :id         554
                              ;              :url        "/courses/interactive-read-aloud-course-english-gzgtgixt/editor-v2"}
                              ; :activity   {:name        "Interactive Read Aloud Activity"
                              ;              :id          567
                              ;              :scene-slug  "interactive-read-aloud-activity"
                              ;              :course-slug "interactive-read-aloud-course-english-gzgtgixt"}
                              ; :background {:name :layered-background
                              ;              :data {:type       "layered-background"
                              ;                     :surface    {:src "/raw/img/casa/surface_casa.png"}
                              ;                     :background {:src "/raw/img/casa/background_casa.png"}
                              ;                     :decoration {:src "/raw/img/casa/decoration_casa.png"}}}
                              ; :scene-data {:assets   [{:url "/raw/audio/background/Map.mp3", :size 10, :type "audio"}]
                              ;              :actions  {:start-background-music {:type "audio", :id "/raw/audio/background/Map.mp3", :loop true}}
                              ;              :metadata {}
                              ;              :triggers {:start {:on "start" :action "script"}
                              ;                         :music {:on "start" :action "start-background-music"}}}}

                              )
               steps (r/atom game-changer-steps)

               handle-prev-step (fn [step-idx] (reset! current-step step-idx))
               handle-next-step (fn [next-idx handle-next]
                                  (if (fn? handle-next)
                                    (handle-next {:data     current-data
                                                  :callback #(reset! current-step next-idx)})
                                    (reset! current-step next-idx)))]

    ;(print "current-step" @current-step)
    ;(print "current-data" @current-data)

    (logger/trace-folded "Game changer data" @current-data)

    (let [current-step-data (get-current-step-data @current-data @current-step @steps)
          {:keys [component title timeline handle-next next-enabled? next-step-idx prev-step-idx]} current-step-data

          first-step? (= @current-step 0)
          last-step? (= @current-step (dec (count timeline)))

          actions (cond->> [{:id      :next-step
                             :text    (if last-step? "Finish" "Next")
                             :props   {:disabled? (not next-enabled?)}
                             :handler #(handle-next-step next-step-idx handle-next)}]
                           (not first-step?) (concat [{:id      :prev-step
                                                       :text    "Previous"
                                                       :handler #(handle-prev-step prev-step-idx)
                                                       :props   {:variant "outlined"}}]))]
      [game-changer/layout {:title          title
                            :timeline-items timeline
                            :actions        actions}
       [component {:data current-data}]])))

(defn index
  []
  [layout {:show-navigation? false}
   [form]])

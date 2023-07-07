(ns webchange.course.export
  (:require [webchange.course.core :as course]
            [webchange.utils.scene-data :as utils]
            [webchange.utils.animations :as animations]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log])
  (:import [org.apache.poi.xwpf.usermodel XWPFDocument XWPFTable XWPFTableRow XWPFTableCell XWPFRun]
           [org.apache.poi POIXMLDocument]
           [org.openxmlformats.schemas.wordprocessingml.x2006.main CTVMerge CTVMerge$Factory STMerge]))

(def heading-1 "1")
(def heading-2 "2")
(def heading-3 "3")

(defn- new-document
  []
  (-> "./docx/template.docx"
      (io/resource)
      (io/input-stream)
      (XWPFDocument.)))

(defn- store-document
  [^XWPFDocument document path]
  (with-open [o (io/output-stream path)]
    (.write document o)))

(defn append-text
  ([^XWPFDocument document value]
   (append-text document value nil))
  ([^XWPFDocument document value style]
   (let [p (.createParagraph document)
         r (.createRun p)]
     (when style
       (.setStyle p style))
     (doto r
       (.setText value)))))

(defn replace-text
  ([^XWPFDocument document value]
   (replace-text document value nil))
  ([^XWPFDocument document value style]
   (let [p (-> document .getParagraphs last)
         r (.createRun p)]
     (when style
       (.setStyle p style))
     (doto r
       (.setText value)))))

(defn append-dialog-table
  [^XWPFDocument document table-data]
  (let [^XWPFTable table (.createTable document)
        cell-width [(biginteger 1800) (biginteger 3700) (biginteger 3700)]]
    (doseq [[row-idx row-data] (map-indexed vector table-data)]
      (let [^XWPFTableRow row (or (.getRow table row-idx) (.createRow table))]
        (doseq [[cell-idx cell-data] (map-indexed vector row-data)]
          (let [^XWPFTableCell cell (or (.getCell row cell-idx) (.addNewTableCell row))
                ^XWPFRun run (-> cell (.getParagraphs) (first) (.createRun))
                tcPr (-> cell (.getCTTc) (.addNewTcPr))
                cellWidth (-> tcPr (.addNewTcW))
                prev-cell-same? (and (= cell-idx 0) (= cell-data (get-in table-data [(dec row-idx) cell-idx])))
                next-cell-same? (and (= cell-idx 0) (= cell-data (get-in table-data [(inc row-idx) cell-idx])))]
            (cond
              prev-cell-same? (let [vmerge (doto (CTVMerge$Factory/newInstance)
                                             (.setVal STMerge/CONTINUE))]
                                (.setVMerge tcPr vmerge))
              next-cell-same? (let [vmerge (doto (CTVMerge$Factory/newInstance)
                                             (.setVal STMerge/RESTART))]
                                (.setVMerge tcPr vmerge)))
            (.setW cellWidth (get cell-width cell-idx))
            (when-not prev-cell-same?
              (.setText run cell-data))))))))

(defn- text-lines
  [{:keys [objects]}]
  (let [lines (->> objects
                   (filter #(-> % second :type (= "text")))
                   (map (fn [[k v]] {:type :object
                                     :path [:objects k]
                                     :text (:text v)})))]
    lines))

(defn- walk-dialog
  [path {:keys [type data] :as action} text-values]
  (case type
    "parallel" (->> data
                    (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d text-values)))
                    (flatten)
                    (remove nil?))
    "sequence-data" (->> data
                         (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d text-values)))
                         (flatten)
                         (remove nil?))
    "animation-sequence" [{:character (animations/->display-name (:target action))
                           :text (:phrase-text action)}]
    "text-animation" [{:character "Text animation"
                       :text (->> action :target keyword (get text-values))}]
    nil))


(defn- dialog-phrases
  [{:keys [actions objects]}]
  (let [dialogs (->> actions
                     (filter #(-> % second :editor-type (= "dialog"))))
        visible-text-objects (->> objects
                                  (filter #(-> % second :type (= "text")))
                                  (map (fn [[k o]] [k (:text o)]))
                                  (into {}))]
    (->> dialogs
         (map (fn [[key action]] [key {:name (:phrase-description action)
                                       :phrases (remove nil? (walk-dialog [:actions key] action visible-text-objects))}]))
         (into {}))))

(defn- extract-script
  [scene-data]
  (let [dialogs (dialog-phrases scene-data)
        tracks (as-> (get-in scene-data [:metadata :tracks]) t
                     (map (fn [{:keys [title nodes]}]
                            {:title title
                             :dialogs (->> nodes
                                           (filter #(= "dialog" (:type %)))
                                           (map :action-id)
                                           (map keyword)
                                           (map #(get dialogs %)))})
                          t)
                     (concat t [{:title "Other"
                                 :dialogs (->> (utils/collect-untracked-actions scene-data)
                                               (map keyword)
                                               (map #(get dialogs %)))}]))]
    {:objects (text-lines scene-data)
     :tracks tracks}))

(defn generate-translate-script
  [scene-data {:keys [name]}]
  (let [{:keys [tracks]} (extract-script scene-data)
        d (new-document)]
    (replace-text d name heading-1)
    (append-text d "Translator notes" heading-2)
    (append-text d "")
    (append-text d "Translated by:")
    (append-text d "")
    (append-text d "Script sequences" heading-1)
    (doseq [t tracks]
      (append-text d (str "Sequence: " (:title t)) heading-2)      
      (doseq [dialog (:dialogs t)]
        (when  (seq (:phrases dialog))
          (append-text d (str "Dialog: " (:name dialog)) heading-3)
          (append-dialog-table d (->> (map (fn [{:keys [character text]}]
                                             [character text ""]) (:phrases dialog))
                                      (into [])))
          (append-text d ""))))
    (append-text d "Notes from the translator" heading-2)
    d))

(comment
  {:objects []
   :tracks [{:name "Main Script"
             :dialogs [{:name "Introduce letter"
                        :phrases [{:character "senora-vaca"
                                   :text "This is the letter"}]}]}]}

  (let [scene-data (course/get-activity-current-version 1792)
        {:keys [objects tracks]} (extract-script scene-data)]
    objects
    )
  
  (course/get-activity-current-version 2008)
  
  (let [scene-data (course/get-activity-current-version 1792)]
    (dialog-phrases scene-data)
    )

  (let [scene-data (course/get-activity-current-version 1792)]
    (get-in scene-data [:objects :senora-vaca]))

  (let [scene-data (course/get-activity-current-version 1792)]
    (get-in scene-data [:actions :introduce-big-small]))

  (let [scene-data (course/get-activity-current-version 1792)]
    (->> scene-data :metadata :tracks
                                        ;   
         ))

  (let [scene-data (course/get-activity-current-version 1792)]
    (utils/collect-untracked-actions scene-data))

  (let [scene-data (course/get-activity-current-version 1792)
        d (generate-translate-script scene-data {:name "Test Activity Name"})]
    (store-document d "/tmp/test-document.docx"))
  
  (let [scene-data (course/get-activity-current-version 1792)
        {:keys [objects tracks]} (extract-script scene-data)
        d (new-document)]
    (append-text d "Visible text" "Heading1")
    (doseq [o objects]
      (append-text d (:text o)))
    (append-text d "Script sequences" "2")
    (doseq [t tracks]
      (append-text d (str "Sequence: " (:title t)))      
      (doseq [dialog (:dialogs t)]
        (append-text d (str "Dialog: " (:name dialog)))
        (append-dialog-table d (->> (map (fn [{:keys [character text]}]
                                           [character text ""]) (:phrases dialog))
                                    (into [])))))
    (store-document d "/tmp/test-document.docx"))
  
  (let [d (new-document)]
    (append-text d "test document text")
    (append-dialog-table d [["character" "phrase 1" ""]
                            ["vera" "pjhrase 2" ""]
                            ["guide" "phrease 3" ""]])
    (store-document d "/tmp/test-document.docx"))
  
  (docx/transform                     ;"/home/ikhaldeev/tmp/file.docx"
   [{:type        :append-text
     :replacement "Text paragraph."}])

  (let [f (CTVMerge$Factory/newInstance)]
    f)
  STMerge/CONTINUE
  )

(ns webchange.interpreter.renderer.scene.components.text.component
  (:require
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.text.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.text.chunked-text :refer [create-chunked-text]]
    [webchange.interpreter.renderer.scene.components.text.chunked-text-utils :refer [register-chunks]]
    [webchange.interpreter.renderer.scene.components.text.simple-text :refer [create-simple-text]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]))

(def default-props {:x               {}
                    :y               {}
                    :text            {}
                    :font-family     {:default "Liberation Sans" :not-nil true}
                    :font-size       {}
                    :fill            {:default "#000000"}
                    :shadow-color    {}
                    :skew-x          {:default 0}
                    :skew-y          {:default 0}
                    :shadow-distance {:default 5}
                    :shadow-blur     {}
                    :shadow-opacity  {}
                    :scale           {}
                    :align           {:default "left"}
                    :vertical-align  {:default "bottom"}
                    :font-weight     {:default "normal"}
                    :chunks          {}
                    :width           {:default 0}
                    :word-wrap       {:default false}
                    :height          {:default 0}
                    :on-click        {}
                    :ref             {}
                    :filters         {}})

(def component-type "text")

(defn create
  "Create `text` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :scale - image scale. Default: {:x 1 :y 1}.
  :text - text string.
  :font-family - specify font family. Default: Liberation Sans.
  :font-size - font size. Default 12.
  :fill - fill color. Default #000000
  :shadow-color - color of text shadow
  :shadow-distance - distance shadow. Default 5
  :shadow-blur - blur of shadow to make it more smooth.
  :shadow-opacity - opacity of text shadows.
  :align -  horizontally positioning of text element. Default left
  :vertical-align -  vertical positioning of text element. Default bottom.
  :font-weight - sets the weight (or boldness) of the font. Default normal
  :chunks - array of chunks. This is usually margins of words, may be used in text animation component to show what part of text should be animated.
            Every chunk has start and end
     :start - number of symbol where curent chunk starts
     :end - number of symbol where curent chunk starts
  :width - text block width. Default 0
  :height - text block height. Default 0
  :on-click - on click event handler.
  :ref - callback function that must be called with component wrapper.
  "
  [{:keys [parent type object-name chunks on-click ref filters] :as props}]
  (if chunks
    (let [{:keys [text-container chunks]} (create-chunked-text props)
          wrapped-text (wrap type object-name text-container chunks)]
      (apply-filters text-container filters)
      (.addChild parent text-container)

      (register-chunks chunks object-name type)

      (when-not (nil? on-click) (utils/set-handler text-container "click" on-click))

      wrapped-text)
    (let [text (create-simple-text props)
          wrapped-text (wrap type object-name text nil)]

      (apply-filters text filters)
      (.addChild parent text)

      (when-not (nil? ref) (ref wrapped-text))

      wrapped-text)))

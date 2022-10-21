(ns webchange.lesson-builder.layout.index
  (:require
    [webchange.lesson-builder.tools.background-image.state :as background-image-state]
    [webchange.lesson-builder.tools.background-image.views :refer [background-image]]
    [webchange.lesson-builder.tools.background-music.views :refer [background-music]]
    [webchange.lesson-builder.tools.character-add.views :refer [character-add]]
    [webchange.lesson-builder.tools.flipbook-add-page.views :as add-page-views]
    [webchange.lesson-builder.tools.flipbook-add-page.state :as add-page-state]
    [webchange.lesson-builder.tools.image-add.views :refer [image-add]]
    [webchange.lesson-builder.tools.object-form.views :refer [object-form]]
    [webchange.lesson-builder.tools.question-form.object-form.views :refer [question-object-form]]
    [webchange.lesson-builder.tools.question-form.state :as question-form-state]
    [webchange.lesson-builder.tools.question-form.toolbox.views :refer [question-toolbox]]
    [webchange.lesson-builder.tools.question-form.menu.views :refer [question-menu]]
    [webchange.lesson-builder.tools.scene-layers.views :refer [scene-layers]]
    [webchange.lesson-builder.tools.settings.views :refer [settings]]
    [webchange.lesson-builder.tools.template-options.rhyming-sides.views :refer [overlay]]
    [webchange.lesson-builder.tools.template-options.views :refer [template-options]]
    [webchange.lesson-builder.tools.voice-translate.views :refer [audio-editor audio-manager]]
    [webchange.lesson-builder.widgets.design-actions.views :refer [design-actions]]
    [webchange.lesson-builder.widgets.pages.views :refer [activity-pages]]
    [webchange.lesson-builder.widgets.select-image.views :refer [choose-image-overlay]]
    [webchange.lesson-builder.widgets.welcome.views :refer [welcome]]))

(def tools-data
  {:default          {:menu    design-actions
                      :toolbox welcome}
   :add-page         {:menu    add-page-views/layout-form
                      :toolbox add-page-views/select-layout
                      :focus   #{:toolbox :menu}
                      :init    ::add-page-state/init}
   :background-image {:toolbox background-image
                      :focus   #{:stage :toolbox}
                      :init    ::background-image-state/init}
   :background-music {:menu background-music}
   :character-add    {:menu character-add}
   :choose-image     {:menu choose-image-overlay}
   :image-add        {:menu image-add}
   :object-form      {:menu object-form}
   :pages            {:toolbox activity-pages}
   :scene-layers     {:menu scene-layers}
   :settings         {:menu settings}
   :question-form    {:menu    question-menu
                      :toolbox question-toolbox
                      :focus   #{:toolbox :stage :menu}
                      :init    ::question-form-state/init-state
                      :reset   ::question-form-state/reset-state}
   :question-option  {:menu question-object-form}
   :rhyming-side     {:menu overlay}
   :template-options {:menu template-options}
   :voice-translate  {:menu    audio-manager
                      :toolbox audio-editor
                      :focus   #{:menu :script :toolbox}}})

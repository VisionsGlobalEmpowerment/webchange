(ns webchange.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [webchange.core-test]
            [webchange.editor.common.action-form.action-form-test]
            [webchange.editor.common.action-form.animation-sequence-test]
            [webchange.editor.form-elements.controlled-input-test]
            [webchange.editor.form-elements.integer-test]
            [webchange.editor.form-elements.number-test]
            [webchange.editor-v2.diagram.diagram-model.items-factory.utils-test]            
            [webchange.editor-v2.graph-builder.graph.book-test]
            [webchange.editor-v2.graph-builder.graph.cinema-test]
            [webchange.editor-v2.graph-builder.graph.cinema-video-test]
            [webchange.editor-v2.graph-builder.graph.cycling-test]
            [webchange.editor-v2.graph-builder.graph.cycling-letters-test]
            [webchange.editor-v2.graph-builder.graph.hide-n-seek-test]
            [webchange.editor-v2.graph-builder.graph.home-test]
            [webchange.editor-v2.graph-builder.graph.letter-intro-test]
            [webchange.editor-v2.graph-builder.graph.library-test]
            [webchange.editor-v2.graph-builder.graph.magic-hat-test]
            [webchange.editor-v2.graph-builder.graph.park-poem-test]
            [webchange.editor-v2.graph-builder.graph.pinata-test]
            [webchange.editor-v2.graph-builder.graph.running-test]
            [webchange.editor-v2.graph-builder.graph.sandbox-test]
            [webchange.editor-v2.graph-builder.graph.see-saw-test]
            [webchange.editor-v2.graph-builder.graph.slide-test]
            [webchange.editor-v2.graph-builder.graph.swings-test]
            [webchange.editor-v2.graph-builder.graph.volleyball-test]
            [webchange.editor-v2.graph-builder.graph.volleyball-letters-test]
            [webchange.editor-v2.graph-builder.graph.writing-lesson-test]
            [webchange.editor-v2.graph-builder.graph.writing-practice-test]            
            [webchange.editor-v2.graph-builder.filters.phrases-test]
            [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer-test]
            [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer-test]
            [webchange.editor-v2.graph-builder.scene-parser.scene-parser-test]
            [webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data-test]
            [webchange.editor-v2.graph-builder.utils.merge-actions-test]
            [webchange.editor-v2.graph-builder.utils.counter-map-test]
            [webchange.editor-v2.graph-builder.utils.change-node-test]
            [webchange.editor-v2.graph-builder.utils.count-nodes-weights-test]
            [webchange.editor-v2.graph-builder.utils.insert-sub-graph-test]
            [webchange.editor-v2.graph-builder.utils.node-children-test]
            [webchange.editor-v2.graph-builder.utils.node-siblings-test]
            [webchange.editor-v2.graph-builder.utils.remove-sub-graph-test]
            [webchange.editor-v2.graph-builder.utils.root-nodes-test]
            [webchange.editor-v2.translator.translator-form.audio-assets.utils.test]
            [webchange.editor-v2.translator.translator-form.dialog.utils-test]
            [webchange.interpreter.events-test]
            [webchange.interpreter.variables.events-test]
            [webchange.interpreter.utils-test]
            [webchange.common.svg-path.path-to-transitions-test]
            [webchange.common.svg-path.path-splitter-test]))

(doo-tests
  'webchange.core-test
  'webchange.editor.common.action-form.action-form-test
  'webchange.editor.common.action-form.animation-sequence-test
  'webchange.editor.form-elements.controlled-input-test
  'webchange.editor.form-elements.integer-test
  'webchange.editor.form-elements.number-test
  'webchange.editor-v2.diagram.diagram-model.items-factory.utils-test
  'webchange.editor-v2.graph-builder.graph.book-test
  'webchange.editor-v2.graph-builder.graph.cinema-test
  'webchange.editor-v2.graph-builder.graph.cinema-video-test
  'webchange.editor-v2.graph-builder.graph.cycling-test
  'webchange.editor-v2.graph-builder.graph.cycling-letters-test
  'webchange.editor-v2.graph-builder.graph.hide-n-seek-test
  'webchange.editor-v2.graph-builder.graph.home-test
  'webchange.editor-v2.graph-builder.graph.letter-intro-test
  'webchange.editor-v2.graph-builder.graph.library-test
  'webchange.editor-v2.graph-builder.graph.magic-hat-test
  'webchange.editor-v2.graph-builder.graph.park-poem-test
  'webchange.editor-v2.graph-builder.graph.pinata-test
  'webchange.editor-v2.graph-builder.graph.running-test
  'webchange.editor-v2.graph-builder.graph.sandbox-test
  'webchange.editor-v2.graph-builder.graph.see-saw-test
  'webchange.editor-v2.graph-builder.graph.slide-test
  'webchange.editor-v2.graph-builder.graph.swings-test
  'webchange.editor-v2.graph-builder.graph.volleyball-test
  'webchange.editor-v2.graph-builder.graph.volleyball-letters-test
  'webchange.editor-v2.graph-builder.graph.writing-lesson-test
  'webchange.editor-v2.graph-builder.graph.writing-practice-test
  'webchange.editor-v2.graph-builder.filters.phrases-test
  'webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer-test
  'webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer-test
  'webchange.editor-v2.graph-builder.scene-parser.scene-parser-test
  'webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data-test
  'webchange.editor-v2.graph-builder.utils.merge-actions-test
  'webchange.editor-v2.graph-builder.utils.counter-map-test
  'webchange.editor-v2.graph-builder.utils.change-node-test
  'webchange.editor-v2.graph-builder.utils.count-nodes-weights-test
  'webchange.editor-v2.graph-builder.utils.insert-sub-graph-test
  'webchange.editor-v2.graph-builder.utils.node-children-test
  'webchange.editor-v2.graph-builder.utils.node-siblings-test
  'webchange.editor-v2.graph-builder.utils.remove-sub-graph-test
  'webchange.editor-v2.graph-builder.utils.root-nodes-test
  'webchange.editor-v2.translator.translator-form.audio-assets.utils.test
  'webchange.editor-v2.translator.translator-form.dialog.utils-test
  'webchange.interpreter.events-test
  'webchange.interpreter.variables.events-test
  'webchange.interpreter.utils-test
  'webchange.common.svg-path.path-to-transitions-test
  'webchange.common.svg-path.path-splitter-test
  )

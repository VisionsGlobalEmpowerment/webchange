(ns webchange.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [webchange.core-test]
            [webchange.editor-v2.course-table.fields.activities.state-test]
            [webchange.editor-v2.course-table.state.data-test]
            [webchange.editor-v2.course-table.utils.move-selection-test]
            [webchange.editor-v2.creation-progress.validate-action-test]
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
            [webchange.editor-v2.graph-builder.utils.merge-actions-test]
            [webchange.editor-v2.graph-builder.utils.counter-map-test]
            [webchange.editor-v2.graph-builder.utils.change-node-test]
            [webchange.editor-v2.graph-builder.utils.count-nodes-weights-test]
            [webchange.editor-v2.graph-builder.utils.insert-sub-graph-test]
            [webchange.editor-v2.graph-builder.utils.node-children-test]
            [webchange.editor-v2.graph-builder.utils.node-siblings-test]
            [webchange.editor-v2.graph-builder.utils.remove-sub-graph-test]
            [webchange.editor-v2.graph-builder.utils.root-nodes-test]
            [webchange.editor-v2.scene-diagram.scene-parser.action-children-test]
            [webchange.editor-v2.scene-diagram.scene-parser.scene-parser-test]
            [webchange.editor-v2.translator.translator-form.audio-assets.utils.test]
            [webchange.interpreter.events-test]
            [webchange.interpreter.variables.events-test]
            [webchange.interpreter.utils-test]
            [webchange.interpreter.utils.propagate-objects-test]
            [webchange.interpreter.utils.find-exit-test]
            [webchange.common.svg-path.path-to-transitions-test]
            [webchange.common.svg-path.path-splitter-test]
            [webchange.interpreter.lessons.activity-test]
            [webchange.utils.list-test]))

(doo-tests
  'webchange.core-test
  'webchange.editor-v2.course-table.fields.activities.state-test
  'webchange.editor-v2.course-table.state.data-test
  'webchange.editor-v2.course-table.utils.move-selection-test
  'webchange.editor-v2.creation-progress.validate-action-test
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
  'webchange.editor-v2.graph-builder.utils.merge-actions-test
  'webchange.editor-v2.graph-builder.utils.counter-map-test
  'webchange.editor-v2.graph-builder.utils.change-node-test
  'webchange.editor-v2.graph-builder.utils.count-nodes-weights-test
  'webchange.editor-v2.graph-builder.utils.insert-sub-graph-test
  'webchange.editor-v2.graph-builder.utils.node-children-test
  'webchange.editor-v2.graph-builder.utils.node-siblings-test
  'webchange.editor-v2.graph-builder.utils.remove-sub-graph-test
  'webchange.editor-v2.graph-builder.utils.root-nodes-test
  'webchange.editor-v2.scene-diagram.scene-parser.action-children-test
  'webchange.editor-v2.scene-diagram.scene-parser.scene-parser-test
  'webchange.editor-v2.translator.translator-form.audio-assets.utils.test
  'webchange.interpreter.events-test
  'webchange.interpreter.variables.events-test
  'webchange.interpreter.utils-test
  'webchange.interpreter.utils.propagate-objects-test
  'webchange.interpreter.utils.find-exit-test
  'webchange.common.svg-path.path-to-transitions-test
  'webchange.common.svg-path.path-splitter-test
  'webchange.interpreter.lessons.activity-test
  'webchange.utils.list-test
  )

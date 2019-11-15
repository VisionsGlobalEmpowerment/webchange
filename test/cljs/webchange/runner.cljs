(ns webchange.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [webchange.core-test]
            [webchange.editor.common.action-form.action-form-test]
            [webchange.editor.common.action-form.animation-sequence-test]
            [webchange.editor.form-elements.controlled-input-test]
            [webchange.editor.form-elements.integer-test]
            [webchange.editor.form-elements.number-test]
            [webchange.editor-v2.diagram.diagram-items-factory.utils-test]
            [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-test]
            [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-actions-test]
            [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-globals-test]
            [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-objects-test]
            [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.duplicates-replicator-test]
            [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.usages-counter-test]
            [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.utils-test]
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
  'webchange.editor-v2.diagram.diagram-items-factory.utils-test
  'webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-test
  'webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-actions-test
  'webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-globals-test
  'webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-objects-test
  'webchange.editor-v2.diagram.scene-parser.duplicates-replicator.duplicates-replicator-test
  'webchange.editor-v2.diagram.scene-parser.duplicates-replicator.usages-counter-test
  'webchange.editor-v2.diagram.scene-parser.duplicates-replicator.utils-test
  'webchange.interpreter.events-test
  'webchange.interpreter.variables.events-test
  'webchange.interpreter.utils-test
  'webchange.common.svg-path.path-to-transitions-test
  'webchange.common.svg-path.path-splitter-test
  )

{
  "title": "Add flipbook page",
  "selectorAttribute": "data-test-id",
  "steps": [
    {
      "type": "setViewport",
      "width": 1375,
      "height": 944,
      "deviceScaleFactor": 1,
      "isMobile": false,
      "hasTouch": false,
      "isLandscape": false
    },
    {
      "type": "navigate",
      "url": "http://localhost:3000/admin/lesson-builder/1756",
      "assertedEvents": [
        {
          "type": "navigation",
          "url": "http://localhost:3000/admin/lesson-builder/1756",
          "title": "Admin / Lesson builder"
        }
      ]
    },
    {
      "type": "waitForElement",
      "timeout": 15000,
      "selectors": [
        "[data-test-id=flipbook-add-page]"
      ]
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=flipbook-add-page]"
        ]
      ],
      "offsetX": 98,
      "offsetY": 50
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=text-small-at-bottom]"
        ]
      ],
      "offsetX": 84,
      "offsetY": 68
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=page-layout-form-\\:text]"
        ]
      ],
      "offsetY": 20,
      "offsetX": 27
    },
    {
      "type": "change",
      "value": "test",
      "selectors": [
        [
          "[data-test-id=page-layout-form-\\:text]"
        ]
      ],
      "target": "main"
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=open-library]"
        ],
        [
          "aria/Open Library",
          "aria/[role=\"generic\"]"
        ]
      ],
      "offsetX": 37.6171875,
      "offsetY": 17.5
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=\\32 10]"
        ],
        [
          "aria//raw/clipart-thumbs/elements/etc--elements-concepts--sister.png",
          "aria/[role=\"img\"]"
        ]
      ],
      "offsetY": 35.5,
      "offsetX": 16.5
    },
    {
      "type": "click",
      "target": "main",
      "selectors": [
        [
          "[data-test-id=page-layout-form-save]"
        ]
      ],
      "offsetX": 19.5703125,
      "offsetY": 8
    }
  ]
}
